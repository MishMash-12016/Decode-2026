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
 * NT4 WebSocket server built on NanoWSD.
 *
 * Wire protocol:
 *   Text  frames → JSON array of {"method":..., "params":...} objects
 *   Binary frames → MessagePack: [topicId, timestampµs, typeCode, value]
 *
 * ─── IMPORTANT: no serve() override ─────────────────────────────────────────
 * NanoWSD's serve() already writes Sec-WebSocket-Protocol in the 101 response
 * by echoing the client's requested value.  Adding it again in a serve()
 * override produces DUPLICATE headers.  Dart's dart:io HTTP parser calls
 * headers.value('sec-websocket-protocol'), which throws StateError when it finds
 * more than one value for the same header name.  That exception propagates out of
 * _mainWebsocket!.ready, the NT4Client catch-block schedules a 1-second retry,
 * and PathPlanner never shows as connected.
 *
 * The NT4 client requests two subprotocols:
 *   "networktables.first.wpi.edu, v4.1.networktables.first.wpi.edu"
 * NanoWSD echoes the full comma-separated string back.  Dart compares that against
 * the exact string 'v4.1.networktables.first.wpi.edu' → not equal → v4.0 fallback.
 * v4.0 mode is fully functional for PathPlanner (telemetry + hot reload both work).
 * ─────────────────────────────────────────────────────────────────────────────
 */
@SuppressWarnings("unchecked")
public class NT4Server extends NanoWSD {

    private static final String TAG = "NT4Server";

    // NT4 msgpack type codes — must match NT4TypeStr.typeMap in the Dart client:
    //   boolean → 0 | double → 1 | int → 2 | float → 3
    //   string  → 4 | raw    → 5 | … | double[] → 17
    private static final int MTYPE_DOUBLE_ARRAY = 17;  // double[]   – velocities
    private static final int MTYPE_RAW          = 5;   // raw bytes  – struct:Pose2d
    private static final int MTYPE_STRING       = 4;   // string     – hot-reload JSON

    private final Map<String, TopicInfo>  topics     = new ConcurrentHashMap<>();
    private final Map<String, byte[]>     lastFrames = new ConcurrentHashMap<>();
    private final List<NT4WebSocket>      clients    = new CopyOnWriteArrayList<>();
    private final long                    startNanos = System.nanoTime();
    private final AtomicInteger           nextId     = new AtomicInteger(1);

    // pubuid → topic name: populated when client sends a "publish" text frame so
    // that inbound hot-reload binary frames can be routed by ID, not by guessing.
    private final Map<Integer, String> clientPubUidToTopicName = new ConcurrentHashMap<>();

    private BiConsumer<String, String> hotReloadPathCallback;
    private BiConsumer<String, String> hotReloadAutoCallback;

    // ── Constructor ───────────────────────────────────────────────────────

    public NT4Server(int port) {
        super(port);

        // Type strings that PathPlanner's Dart client understands:
        //   "double[]" → velocities decoded as List<double>
        //   "raw"      → struct bytes decoded via Pose2d.fromBytes(Uint8List)
        //   "string"   → hot-reload JSON payload
        registerTopic(NT4Topics.VEL,             "double[]", MTYPE_DOUBLE_ARRAY);
        registerTopic(NT4Topics.CURRENT_POSE,    "raw",      MTYPE_RAW);
        registerTopic(NT4Topics.ACTIVE_PATH,     "raw",      MTYPE_RAW);
        registerTopic(NT4Topics.TARGET_POSE,     "raw",      MTYPE_RAW);
        registerTopic(NT4Topics.HOT_RELOAD_PATH, "string",   MTYPE_STRING);
        registerTopic(NT4Topics.HOT_RELOAD_AUTO, "string",   MTYPE_STRING);
    }

    // ── openWebSocket ─────────────────────────────────────────────────────

    @Override
    protected NanoWSD.WebSocket openWebSocket(IHTTPSession handshake) {
        String path = handshake.getUri();
        // /nt/elastic is the NT4 v4.1 RTT latency socket.  In v4.0 fallback mode
        // (which is what NanoWSD's subprotocol echo causes) the Dart client never
        // opens this socket.  The RTTWebSocket handler is kept for correctness.
        if (path != null && path.startsWith("/nt/elastic")) {
            return new RTTWebSocket(handshake);
        }
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
            p.packInt(MTYPE_DOUBLE_ARRAY);
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
            buf.putDouble(poses[i]); buf.putDouble(poses[i + 1]); buf.putDouble(poses[i + 2]);
        }
        return buf.array();
    }

    private void dispatchHotReload(String topicName, String jsonValue) {
        if (NT4Topics.HOT_RELOAD_PATH.equals(topicName) && hotReloadPathCallback != null) {
            hotReloadPathCallback.accept(topicName, jsonValue);
        } else if (NT4Topics.HOT_RELOAD_AUTO.equals(topicName) && hotReloadAutoCallback != null) {
            hotReloadAutoCallback.accept(topicName, jsonValue);
        }
    }

    // ── RTT WebSocket (/nt/elastic) ───────────────────────────────────────

    private static class RTTWebSocket extends NanoWSD.WebSocket {
        RTTWebSocket(IHTTPSession handshake) { super(handshake); }

        @Override protected void onOpen() { Log.d("NT4Server", "RTT socket connected"); }

        @Override
        protected void onMessage(WebSocketFrame msg) {
            // Echo timestamp frames back so the client can compute round-trip latency.
            if (msg.getOpCode() == WebSocketFrame.OpCode.Binary) {
                try { send(msg.getBinaryPayload()); } catch (IOException ignored) {}
            }
        }

        @Override protected void onClose(WebSocketFrame.CloseCode c, String r, boolean b) {}
        @Override protected void onPong(WebSocketFrame f) {}
        @Override protected void onException(IOException e) {}
    }

    // ── Main NT4 WebSocket (/nt/<clientName>) ─────────────────────────────

    private class NT4WebSocket extends NanoWSD.WebSocket {

        NT4WebSocket(IHTTPSession handshake) { super(handshake); }

        @Override
        protected void onOpen() {
            clients.add(this);
            Log.i(TAG, "PathPlanner connected: " + getHandshakeRequest().getRemoteIpAddress());

            // 1. Announce all topics (client needs IDs + types before binary frames).
            for (TopicInfo t : topics.values()) {
                try { send(buildAnnounce(t)); } catch (IOException ignored) {}
            }

            // 2. Replay last cached values so PathPlanner shows current state immediately.
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
                handleBinaryFrame(msg.getBinaryPayload());
                return;
            }
            handleTextFrame(msg.getTextPayload());
        }

        @Override protected void onPong(WebSocketFrame pong) {}

        @Override
        protected void onException(IOException e) {
            Log.w(TAG, "WebSocket exception: " + e.getMessage());
        }

        private void handleTextFrame(String text) {
            try {
                Object parsed = new JSONParser().parse(text);
                if (!(parsed instanceof JSONArray)) return;
                for (Object obj : (JSONArray) parsed) {
                    if (!(obj instanceof JSONObject)) continue;
                    JSONObject m      = (JSONObject) obj;
                    String     method = String.valueOf(m.get("method"));
                    JSONObject params = (JSONObject) m.get("params");
                    if (params == null) continue;

                    if ("publish".equals(method)) {
                        // Record the client's chosen pubuid so inbound binary frames
                        // can be routed to the correct hot-reload callback by ID.
                        String name      = String.valueOf(params.get("name"));
                        Object pubuidObj = params.get("pubuid");
                        if (pubuidObj instanceof Number) {
                            int pubuid = ((Number) pubuidObj).intValue();
                            clientPubUidToTopicName.put(pubuid, name);
                            Log.d(TAG, "Client publishing: " + name + " (pubuid=" + pubuid + ")");
                        }
                    }
                    // "subscribe" frames are noted but require no action — we broadcast all topics.
                }
            } catch (ParseException ignored) {}
        }

        private void handleBinaryFrame(byte[] payload) {
            try {
                org.msgpack.core.MessageUnpacker u = MessagePack.newDefaultUnpacker(payload);
                if (u.unpackArrayHeader() < 4) return;

                int pubuid    = u.unpackInt();
                u.unpackLong();              // timestamp (unused server-side)
                int typeCode  = u.unpackInt();

                if (typeCode == MTYPE_STRING) {
                    String value     = u.unpackString();
                    String topicName = clientPubUidToTopicName.get(pubuid);

                    if (topicName != null) {
                        dispatchHotReload(topicName, value);
                    } else {
                        // pubuid not yet mapped — fall back to content detection.
                        if (value.contains("\"path\"")) {
                            dispatchHotReload(NT4Topics.HOT_RELOAD_PATH, value);
                        } else if (value.contains("\"auto\"")) {
                            dispatchHotReload(NT4Topics.HOT_RELOAD_AUTO, value);
                        }
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "Inbound binary frame error: " + e.getMessage());
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