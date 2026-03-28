package com.pathplanner.ftc.nt4;

import android.util.Log;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoWSD;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/**
 * NT4 WebSocket server built on NanoWSD (already available as a transitive
 * dependency via FTC Dashboard — no extra dependency needed).
 *
 * Wire protocol:
 *   Text  frames → JSON array of {"method":..., "params":...} objects
 *                  Used for: announce (server→client), subscribe (client→server)
 *   Binary frames → MessagePack array: [topicId, timestampµs, typeCode, value]
 *                  Used for all value updates (both directions)
 *
 * Subprotocol "v4.1.networktables.first.wpi.edu" is echoed back in the HTTP
 * upgrade response — PathPlanner's client closes immediately without it.
 */
@SuppressWarnings("unchecked")
public class NT4Server extends NanoWSD {

    private static final String TAG = "NT4Server";
    private static final String NT4_SUBPROTOCOL = "v4.1.networktables.first.wpi.edu";

    // MessagePack NT4 type codes
    private static final int MTYPE_FLOAT64_ARRAY = 17;
    private static final int MTYPE_RAW           = 4;   // raw bytes (struct)
    private static final int MTYPE_STRING        = 6;

    private final Map<String, TopicInfo>  topics     = new ConcurrentHashMap<>();
    private final Map<String, byte[]>     lastFrames = new ConcurrentHashMap<>();
    private final List<NT4WebSocket>      clients    = new CopyOnWriteArrayList<>();
    private final long                    startNanos = System.nanoTime();
    private final AtomicInteger           nextId     = new AtomicInteger(1);

    private BiConsumer<String, String> hotReloadPathCallback;
    private BiConsumer<String, String> hotReloadAutoCallback;

    // ── Constructor ───────────────────────────────────────────────────────

    public NT4Server(int port) {
        super(port);

        registerTopic(NT4Topics.VEL,            NT4Topics.TYPE_DOUBLE_ARRAY,       MTYPE_FLOAT64_ARRAY);
        registerTopic(NT4Topics.CURRENT_POSE,   NT4Topics.TYPE_STRUCT_POSE2D,      MTYPE_RAW);
        registerTopic(NT4Topics.ACTIVE_PATH,    NT4Topics.TYPE_STRUCT_POSE2D_ARRAY, MTYPE_RAW);
        registerTopic(NT4Topics.TARGET_POSE,    NT4Topics.TYPE_STRUCT_POSE2D,      MTYPE_RAW);
        registerTopic(NT4Topics.HOT_RELOAD_PATH, "string",                         MTYPE_STRING);
        registerTopic(NT4Topics.HOT_RELOAD_AUTO, "string",                         MTYPE_STRING);
    }

    // ── NanoWSD override — subprotocol negotiation ────────────────────────

    @Override
    public Response serve(IHTTPSession session) {
        // Echo back the NT4 subprotocol header if the client requests it.
        // NanoWSD's serve() builds the 101 Switching Protocols response;
        // we intercept to add the Sec-WebSocket-Protocol header.
        String requested = session.getHeaders().get("sec-websocket-protocol");
        Response r = super.serve(session);
        if (requested != null && requested.contains(NT4_SUBPROTOCOL)) {
            r.addHeader("Sec-WebSocket-Protocol", NT4_SUBPROTOCOL);
        }
        return r;
    }

    @Override
    protected NanoWSD.WebSocket openWebSocket(IHTTPSession handshake) {
        return new NT4WebSocket(handshake);
    }

    // ── Public publish API ────────────────────────────────────────────────

    public void publishDoubleArray(String topicName, double[] values) {
        TopicInfo t = topics.get(topicName);
        if (t == null) return;
        try (MessageBufferPacker p = MessagePack.newDefaultBufferPacker()) {
            p.packArrayHeader(4);
            p.packInt(t.id);
            p.packLong(nowMicros());
            p.packInt(MTYPE_FLOAT64_ARRAY);
            p.packArrayHeader(values.length);
            for (double v : values) p.packDouble(v);
            p.flush();
            broadcast(topicName, p.toByteArray());
        } catch (IOException e) {
            Log.w(TAG, "publishDoubleArray failed: " + e.getMessage());
        }
    }

    public void publishPose2d(String topicName, double x, double y, double radians) {
        TopicInfo t = topics.get(topicName);
        if (t == null) return;
        broadcast(topicName, buildStructFrame(t.id, encodePose2d(x, y, radians)));
    }

    public void publishPose2dArray(String topicName, double[] poses) {
        TopicInfo t = topics.get(topicName);
        if (t == null) return;
        broadcast(topicName, buildStructFrame(t.id, encodePose2dArray(poses)));
    }

    public void setHotReloadPathCallback(BiConsumer<String, String> cb) { hotReloadPathCallback = cb; }
    public void setHotReloadAutoCallback(BiConsumer<String, String> cb) { hotReloadAutoCallback = cb; }

    // ── Internal helpers ──────────────────────────────────────────────────

    private void registerTopic(String name, String type, int msgpackType) {
        topics.put(name, new TopicInfo(nextId.getAndIncrement(), name, type, msgpackType));
    }

    private long nowMicros() {
        return (System.nanoTime() - startNanos) / 1_000L;
    }

    private void broadcast(String topicName, byte[] frame) {
        lastFrames.put(topicName, frame);
        for (NT4WebSocket ws : clients) {
            if (ws.isOpen()) {
                try { ws.send(frame); } catch (IOException ignored) {}
            }
        }
    }

    /** NT4 announce text frame: [{"method":"announce","params":{...}}] */
    private static String buildAnnounce(TopicInfo t) {
        JSONObject params = new JSONObject();
        params.put("name",       t.name);
        params.put("id",         t.id);
        params.put("type",       t.type);
        params.put("pubuid",     0);
        params.put("properties", new JSONObject());

        JSONObject msg = new JSONObject();
        msg.put("method", "announce");
        msg.put("params", params);

        JSONArray arr = new JSONArray();
        arr.add(msg);
        return arr.toJSONString();
    }

    private byte[] buildStructFrame(int topicId, byte[] structBytes) {
        try (MessageBufferPacker p = MessagePack.newDefaultBufferPacker()) {
            p.packArrayHeader(4);
            p.packInt(topicId);
            p.packLong(nowMicros());
            p.packInt(MTYPE_RAW);
            p.packBinaryHeader(structBytes.length);
            p.writePayload(structBytes);
            p.flush();
            return p.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    private static byte[] encodePose2d(double x, double y, double radians) {
        ByteBuffer buf = ByteBuffer.allocate(24);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putDouble(x); buf.putDouble(y); buf.putDouble(radians);
        return buf.array();
    }

    private static byte[] encodePose2dArray(double[] poses) {
        if (poses.length % 3 != 0) throw new IllegalArgumentException("poses.length % 3 != 0");
        ByteBuffer buf = ByteBuffer.allocate(24 * (poses.length / 3));
        buf.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < poses.length; i += 3) {
            buf.putDouble(poses[i]); buf.putDouble(poses[i+1]); buf.putDouble(poses[i+2]);
        }
        return buf.array();
    }

    private void handleInbound(String topicName, String value) {
        if (NT4Topics.HOT_RELOAD_PATH.equals(topicName) && hotReloadPathCallback != null) {
            hotReloadPathCallback.accept(topicName, value);
        } else if (NT4Topics.HOT_RELOAD_AUTO.equals(topicName) && hotReloadAutoCallback != null) {
            hotReloadAutoCallback.accept(topicName, value);
        }
    }

    // ── Inner WebSocket class ─────────────────────────────────────────────

    private class NT4WebSocket extends NanoWSD.WebSocket {

        NT4WebSocket(IHTTPSession handshake) {
            super(handshake);
        }

        @Override
        protected void onOpen() {
            clients.add(this);
            Log.i(TAG, "PathPlanner connected: " + getHandshakeRequest().getRemoteIpAddress());

            // Announce all topics
            for (TopicInfo t : topics.values()) {
                try { send(buildAnnounce(t)); } catch (IOException ignored) {}
            }

            // Replay last known values
            for (Map.Entry<String, byte[]> e : lastFrames.entrySet()) {
                try { send(e.getValue()); } catch (IOException ignored) {}
            }
        }

        @Override
        protected void onClose(WebSocketFrame.CloseCode code, String reason, boolean byRemote) {
            clients.remove(this);
            Log.i(TAG, "PathPlanner disconnected");
        }

        @Override
        protected void onMessage(WebSocketFrame msg) {
            if (msg.getOpCode() == WebSocketFrame.OpCode.Binary) {
                // Binary msgpack frame from client — hot reload path
                try {
                    org.msgpack.core.MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(msg.getBinaryPayload());
                    int len = unpacker.unpackArrayHeader(); // should be 4
                    if (len < 4) return;
                    unpacker.unpackInt();   // topicId (client-assigned, ignore)
                    unpacker.unpackLong();  // timestamp
                    unpacker.unpackInt();   // type code
                    String value = unpacker.unpackString();
                    // Determine which hot-reload topic by checking registered client publish topics
                    // (we'll match by checking if value looks like a hot-reload JSON payload)
                    handleInboundBinary(value);
                } catch (Exception ignored) {}
                return;
            }

            // Text frame: JSON array of {method, params}
            try {
                Object parsed = new JSONParser().parse(msg.getTextPayload());
                if (!(parsed instanceof JSONArray)) return;
                for (Object obj : (JSONArray) parsed) {
                    if (!(obj instanceof JSONObject)) continue;
                    JSONObject m      = (JSONObject) obj;
                    String     method = String.valueOf(m.get("method"));
                    JSONObject params = (JSONObject) m.get("params");
                    if (params == null) continue;

                    if ("publish".equals(method)) {
                        String name  = String.valueOf(params.get("name"));
                        Object value = params.get("value");
                        handleInbound(name, value == null ? "" : String.valueOf(value));
                    }
                }
            } catch (ParseException ignored) {}
        }

        @Override
        protected void onPong(WebSocketFrame pong) {}

        @Override
        protected void onException(IOException e) {
            Log.w(TAG, "WebSocket exception: " + e.getMessage());
        }

        private void handleInboundBinary(String value) {
            // Try to detect which hot-reload topic this belongs to by JSON structure
            if (value.contains("\"path\"")) {
                handleInbound(NT4Topics.HOT_RELOAD_PATH, value);
            } else if (value.contains("\"auto\"")) {
                handleInbound(NT4Topics.HOT_RELOAD_AUTO, value);
            }
        }
    }

    // ── Topic metadata ────────────────────────────────────────────────────

    private static final class TopicInfo {
        final int    id;
        final String name;
        final String type;
        final int    msgpackType;

        TopicInfo(int id, String name, String type, int msgpackType) {
            this.id = id; this.name = name; this.type = type; this.msgpackType = msgpackType;
        }
    }
}
