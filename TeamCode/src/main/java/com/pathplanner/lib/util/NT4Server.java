package com.pathplanner.lib.util;

import android.util.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.ConcurrentHashMap;

public class NT4Server {

    // NT4 type strings
    private static final String TYPE_DOUBLE_ARRAY = "float64[]";
    private static final String TYPE_RAW          = "rawbytes";
    private static final String TYPE_STRING       = "string";

    // Topic IDs for our published topics
    private static final int ID_VEL          = 1;
    private static final int ID_CURRENT_POSE = 2;
    private static final int ID_TARGET_POSE  = 3;
    private static final int ID_ACTIVE_PATH  = 4;

    // NT4 binary type codes
    private static final int TYPECODE_DOUBLE_ARRAY = 17;
    private static final int TYPECODE_RAW          = 4;
    private static final int TYPECODE_STRING       = 0;

    private static final int PORT = 5810;

    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private volatile boolean running = false;
    private Thread acceptThread;

    // Timestamp origin
    private final long startNanos = System.nanoTime();

    // Current telemetry state
    private volatile double[] velData      = {0, 0, 0, 0};
    private volatile double[] currentPose  = {0, 0, 0};
    private volatile double[] targetPose   = {0, 0, 0};
    private volatile double[][] activePath = new double[0][];

    // Hot reload callbacks
    private HotReloadCallback pathHotReloadCallback = null;
    private HotReloadCallback autoHotReloadCallback = null;

    // Topic ID → topic name for topics the CLIENT publishes to us
    private final Map<Integer, String> clientTopicIdToName = new ConcurrentHashMap<>();

    public interface HotReloadCallback {
        void onHotReload(String name, JSONObject json);
    }

    public void setPathHotReloadCallback(HotReloadCallback cb) {
        this.pathHotReloadCallback = cb;
    }

    public void setAutoHotReloadCallback(HotReloadCallback cb) {
        this.autoHotReloadCallback = cb;
    }

    // ── Lifecycle ────────────────────────────────────────────────────────────

    public void start() {
        running = true;
        acceptThread = new Thread(this::acceptLoop, "NT4-Accept");
        acceptThread.setDaemon(true);
        acceptThread.start();
    }

    public void stop() {
        running = false;
        try { if (serverSocket != null) serverSocket.close(); } catch (IOException ignored) {}
        for (ClientHandler c : clients) c.close();
        clients.clear();
    }

    // ── Telemetry setters ────────────────────────────────────────────────────

    public void setVelocities(double actual, double commanded, double actualAng, double commandedAng) {
        velData = new double[]{actual, commanded, actualAng, commandedAng};
        broadcast(buildBinaryFrame(ID_VEL, TYPECODE_DOUBLE_ARRAY, encodeDoubleArray(velData)));
    }

    public void setCurrentPose(double x, double y, double headingRad) {
        currentPose = new double[]{x, y, headingRad};
        broadcast(buildBinaryFrame(ID_CURRENT_POSE, TYPECODE_RAW, encodePose(currentPose)));
    }

    public void setTargetPose(double x, double y, double headingRad) {
        targetPose = new double[]{x, y, headingRad};
        broadcast(buildBinaryFrame(ID_TARGET_POSE, TYPECODE_RAW, encodePose(targetPose)));
    }

    public void setActivePath(double[][] poses) {
        activePath = poses;
        broadcast(buildBinaryFrame(ID_ACTIVE_PATH, TYPECODE_RAW, encodePoseArray(poses)));
    }

    // ── Encoding ─────────────────────────────────────────────────────────────

    /** Pose2d struct: x + y + heading, each a little-endian f64 = 24 bytes */
    private byte[] encodePose(double[] pose) {
        ByteBuffer buf = ByteBuffer.allocate(24).order(ByteOrder.LITTLE_ENDIAN);
        buf.putDouble(pose[0]);
        buf.putDouble(pose[1]);
        buf.putDouble(pose[2]);
        return buf.array();
    }

    /** Array of Pose2d structs */
    private byte[] encodePoseArray(double[][] poses) {
        ByteBuffer buf = ByteBuffer.allocate(poses.length * 24).order(ByteOrder.LITTLE_ENDIAN);
        for (double[] p : poses) {
            buf.putDouble(p[0]);
            buf.putDouble(p[1]);
            buf.putDouble(p[2]);
        }
        return buf.array();
    }

    /** double[] as little-endian IEEE 754 */
    private byte[] encodeDoubleArray(double[] arr) {
        ByteBuffer buf = ByteBuffer.allocate(arr.length * 8).order(ByteOrder.LITTLE_ENDIAN);
        for (double d : arr) buf.putDouble(d);
        return buf.array();
    }

    // ── NT4 message builders ─────────────────────────────────────────────────

    private long nowMicros() {
        return (System.nanoTime() - startNanos) / 1000L;
    }

    /** Binary WebSocket frame: MessagePack [topicId, timestamp, typeCode, value] */
    private byte[] buildBinaryFrame(int topicId, int typeCode, byte[] value) {
        try {
            MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
            packer.packArrayHeader(4);
            packer.packInt(topicId);
            packer.packLong(nowMicros());
            packer.packInt(typeCode);
            packer.packBinaryHeader(value.length);
            packer.writePayload(value);
            packer.close();
            return wrapWebSocketFrame(packer.toByteArray(), true);
        } catch (IOException e) {
            return new byte[0];
        }
    }

    /** Announce our 4 published topics + subscribe to the 2 hot reload topics */
    private String buildAnnounceAndSubscribeJson() {
        String announces = "[" +
            announce("/PathPlanner/vel",         TYPE_DOUBLE_ARRAY, ID_VEL)          + "," +
            announce("/PathPlanner/currentPose", TYPE_RAW,          ID_CURRENT_POSE) + "," +
            announce("/PathPlanner/targetPose",  TYPE_RAW,          ID_TARGET_POSE)  + "," +
            announce("/PathPlanner/activePath",  TYPE_RAW,          ID_ACTIVE_PATH)  +
            "]";

        String subscribes = "[" +
            subscribe("/PathPlanner/HotReload/hotReloadPath", 100) + "," +
            subscribe("/PathPlanner/HotReload/hotReloadAuto", 101) +
            "]";

        return announces + "\n" + subscribes;
    }

    private String announce(String name, String type, int id) {
        return "{\"method\":\"announce\",\"params\":{" +
            "\"name\":\"" + name + "\"," +
            "\"id\":" + id + "," +
            "\"type\":\"" + type + "\"," +
            "\"properties\":{}}}";
    }

    private String subscribe(String topicName, int subUid) {
        return "{\"method\":\"subscribe\",\"params\":{" +
            "\"topics\":[\"" + topicName + "\"]," +
            "\"subuid\":" + subUid + "," +
            "\"options\":{}}}";
    }

    // ── WebSocket helpers ────────────────────────────────────────────────────

    /** Wrap payload into a WebSocket frame (server → client, no masking) */
    static byte[] wrapWebSocketFrame(byte[] payload, boolean binary) {
        int opcode = binary ? 0x02 : 0x01;
        int len = payload.length;
        byte[] header;
        if (len < 126) {
            header = new byte[]{(byte)(0x80 | opcode), (byte) len};
        } else if (len < 65536) {
            header = new byte[]{(byte)(0x80 | opcode), (byte)126,
                (byte)(len >> 8), (byte)(len & 0xFF)};
        } else {
            header = new byte[]{(byte)(0x80 | opcode), (byte)127,
                0,0,0,0,
                (byte)(len >> 24),(byte)(len >> 16),(byte)(len >> 8),(byte)(len & 0xFF)};
        }
        byte[] frame = new byte[header.length + len];
        System.arraycopy(header, 0, frame, 0, header.length);
        System.arraycopy(payload, 0, frame, header.length, len);
        return frame;
    }

    /** Unmask a client → server WebSocket payload */
    static byte[] unmask(byte[] data, int offset, int length, byte[] mask) {
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = (byte)(data[offset + i] ^ mask[i % 4]);
        }
        return result;
    }

    /** HTTP → WebSocket upgrade handshake */
    static void doHandshake(InputStream in, OutputStream out) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        String key = null;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            if (line.startsWith("Sec-WebSocket-Key:")) {
                key = line.substring(line.indexOf(':') + 1).trim();
            }
        }
        if (key == null) throw new IOException("No WebSocket key found");

        String accept = Base64.encodeToString(
            MessageDigest.getInstance("SHA-1").digest(
                (key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")),
            Base64.NO_WRAP);

        String response =
            "HTTP/1.1 101 Switching Protocols\r\n" +
                "Upgrade: websocket\r\n" +
                "Connection: Upgrade\r\n" +
                "Sec-WebSocket-Protocol: networktables.first.wpi.edu\r\n" +
                "Sec-WebSocket-Accept: " + accept + "\r\n\r\n";
        out.write(response.getBytes("UTF-8"));
        out.flush();
    }

    // ── Accept loop ──────────────────────────────────────────────────────────

    private void acceptLoop() {
        try {
            serverSocket = new ServerSocket(PORT);
            serverSocket.setReuseAddress(true);
            while (running) {
                try {
                    Socket socket = serverSocket.accept();
                    ClientHandler handler = new ClientHandler(socket);
                    clients.add(handler);
                    handler.start();
                } catch (IOException ignored) {}
            }
        } catch (IOException e) {
            if (running) e.printStackTrace();
        }
    }

    private void broadcast(byte[] frame) {
        for (ClientHandler c : clients) c.send(frame);
    }

    // ── Client handler ───────────────────────────────────────────────────────

    private class ClientHandler {
        private final Socket socket;
        private OutputStream out;
        private volatile boolean open = false;
        private final BlockingQueue<byte[]> sendQueue = new LinkedBlockingQueue<>();

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        void start() {
            Thread t = new Thread(this::run, "NT4-Client");
            t.setDaemon(true);
            t.start();
        }

        void send(byte[] frame) {
            if (open) sendQueue.offer(frame);
        }

        void close() {
            open = false;
            try { socket.close(); } catch (IOException ignored) {}
        }

        private void run() {
            try {
                InputStream in = socket.getInputStream();
                out = socket.getOutputStream();

                // 1. WebSocket handshake
                doHandshake(in, out);
                open = true;

                // 2. Start sender thread
                Thread sender = new Thread(() -> {
                    while (open) {
                        try {
                            byte[] frame = sendQueue.poll(100, TimeUnit.MILLISECONDS);
                            if (frame != null) out.write(frame);
                        } catch (Exception e) { break; }
                    }
                }, "NT4-Sender");
                sender.setDaemon(true);
                sender.start();

                // 3. Send announces + subscribe to hot reload topics
                byte[] announceFrame = wrapWebSocketFrame(
                    buildAnnounceAndSubscribeJson().getBytes("UTF-8"), false);
                send(announceFrame);

                // 4. Send current values so panel isn't blank on connect
                send(buildBinaryFrame(ID_VEL,          TYPECODE_DOUBLE_ARRAY, encodeDoubleArray(velData)));
                send(buildBinaryFrame(ID_CURRENT_POSE, TYPECODE_RAW,          encodePose(currentPose)));
                send(buildBinaryFrame(ID_TARGET_POSE,  TYPECODE_RAW,          encodePose(targetPose)));
                send(buildBinaryFrame(ID_ACTIVE_PATH,  TYPECODE_RAW,          encodePoseArray(activePath)));

                // 5. Read loop — parse incoming frames for hot reload
                while (open) {
                    int b0 = in.read();
                    int b1 = in.read();
                    if (b0 < 0 || b1 < 0) break;

                    int opcode     = b0 & 0x0F;
                    boolean masked = (b1 & 0x80) != 0;
                    int payloadLen = b1 & 0x7F;

                    if (payloadLen == 126) {
                        byte[] ext = new byte[2];
                        in.read(ext);
                        payloadLen = ((ext[0] & 0xFF) << 8) | (ext[1] & 0xFF);
                    } else if (payloadLen == 127) {
                        byte[] ext = new byte[8];
                        in.read(ext);
                        payloadLen = (int)(((ext[4]&0xFF)<<24)|((ext[5]&0xFF)<<16)|
                            ((ext[6]&0xFF)<<8)|(ext[7]&0xFF));
                    }

                    byte[] maskBytes = new byte[4];
                    if (masked) in.read(maskBytes);

                    byte[] payload = new byte[payloadLen];
                    int totalRead = 0;
                    while (totalRead < payloadLen) {
                        int n = in.read(payload, totalRead, payloadLen - totalRead);
                        if (n < 0) break;
                        totalRead += n;
                    }

                    if (masked) payload = unmask(payload, 0, payloadLen, maskBytes);

                    if (opcode == 1) {
                        // Text frame — JSON control messages (publish announcements)
                        handleTextFrame(new String(payload, "UTF-8"));
                    } else if (opcode == 2) {
                        // Binary frame — MessagePack data (hot reload payloads)
                        handleBinaryFrame(payload);
                    } else if (opcode == 8) {
                        // Close frame
                        break;
                    }
                    // opcode 9 = ping, ignore
                }

            } catch (Exception e) {
                // client disconnected
            } finally {
                open = false;
                clients.remove(this);
            }
        }

        /**
         * Parse JSON control frames from the client.
         * We look for "publish" messages to learn which topic ID maps to
         * the hot reload topic names.
         */
        private void handleTextFrame(String json) {
            try {
                // Find all publish method blocks and extract name → id
                String[] parts = json.split("\\{\"method\":\"publish\"");
                for (int i = 1; i < parts.length; i++) {
                    String chunk = parts[i];
                    String name = extractJsonString(chunk, "name");
                    int id      = extractJsonInt(chunk, "id");
                    if (name != null && id >= 0) {
                        clientTopicIdToName.put(id, name);
                    }
                }
            } catch (Exception ignored) {}
        }

        /**
         * Parse binary (MessagePack) frames from the client.
         * NT4 format: [topicId, timestampMicros, typeCode, value]
         * Hot reload values are JSON strings.
         */
        private void handleBinaryFrame(byte[] data) {
            try {
                MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(data);
                unpacker.unpackArrayHeader(); // 4 elements
                int topicId    = unpacker.unpackInt();
                long timestamp = unpacker.unpackLong(); // unused but must consume
                int typeCode   = unpacker.unpackInt();  // unused but must consume
                String value   = unpacker.unpackString();
                unpacker.close();

                String topicName = clientTopicIdToName.get(topicId);
                if (topicName == null) return;

                JSONObject json = (JSONObject) new JSONParser().parse(value);

                if (topicName.equals("/PathPlanner/HotReload/hotReloadPath")
                    && pathHotReloadCallback != null) {
                    String name       = (String) json.get("name");
                    JSONObject pathJson = (JSONObject) json.get("path");
                    pathHotReloadCallback.onHotReload(name, pathJson);

                } else if (topicName.equals("/PathPlanner/HotReload/hotReloadAuto")
                    && autoHotReloadCallback != null) {
                    String name       = (String) json.get("name");
                    JSONObject autoJson = (JSONObject) json.get("auto");
                    autoHotReloadCallback.onHotReload(name, autoJson);
                }

            } catch (Exception ignored) {}
        }

        // ── Minimal JSON field extractors ────────────────────────────────────

        private String extractJsonString(String json, String key) {
            String search = "\"" + key + "\":\"";
            int start = json.indexOf(search);
            if (start < 0) return null;
            start += search.length();
            int end = json.indexOf("\"", start);
            return end < 0 ? null : json.substring(start, end);
        }

        private int extractJsonInt(String json, String key) {
            String search = "\"" + key + "\":";
            int start = json.indexOf(search);
            if (start < 0) return -1;
            start += search.length();
            int end = start;
            while (end < json.length() &&
                (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) end++;
            try { return Integer.parseInt(json.substring(start, end)); }
            catch (NumberFormatException e) { return -1; }
        }
    }
}