package com.pathplanner.ftc.nt4;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

/**
 * Minimal NT4 server that speaks enough of the NetworkTables 4 WebSocket
 * protocol for PathPlanner's desktop app to connect, subscribe, and receive
 * live telemetry from an FTC robot.
 *
 * <h2>Protocol summary</h2>
 * NT4 uses a JSON-text frame for control messages and MessagePack binary
 * frames for high-frequency value updates.  PathPlanner's client speaks the
 * <em>text-only</em> subset, so this implementation does the same: every
 * value update is sent as a JSON text frame rather than MessagePack.
 *
 * <h2>Topic lifecycle</h2>
 * <ol>
 *   <li>Server sends {@code announce} for every known topic as soon as a
 *       client connects.</li>
 *   <li>Client sends {@code subscribe} to express interest.</li>
 *   <li>Server sends {@code value} frames whenever the robot updates a
 *       topic.</li>
 * </ol>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * NT4Server server = new NT4Server(5810);
 * server.start();
 * // ...
 * server.publishDoubleArray("/PathPlanner/vel", new double[]{1.0, 1.2, 0.0, 0.0});
 * }</pre>
 */
@SuppressWarnings("unchecked")
public class NT4Server extends WebSocketServer {

    private static final Logger LOG = Logger.getLogger(NT4Server.class.getName());

    // ── NT4 WebSocket sub-protocol required by the spec ───────────────────
    private static final String NT4_SUBPROTOCOL = "v4.1.networktables.first.wpi.edu";

    // ── Internal state ─────────────────────────────────────────────────────
    /** topic name → topic metadata */
    private final Map<String, TopicInfo> topics = new ConcurrentHashMap<>();

    /** topic name → last known value (raw JSON value object) */
    private final Map<String, Object>    lastValues = new ConcurrentHashMap<>();

    /** All currently connected clients */
    private final List<WebSocket> clients = new CopyOnWriteArrayList<>();

    /** Monotonically-increasing timestamp (µs since server start) */
    private final long startNanos = System.nanoTime();

    /** UID generator for topic IDs */
    private final AtomicInteger nextTopicId = new AtomicInteger(1);

    // ── Hot-reload callbacks ───────────────────────────────────────────────
    /** Called when the PathPlanner app pushes a path hot-reload message. */
    private BiConsumer<String, String> hotReloadPathCallback;

    /** Called when the PathPlanner app pushes an auto hot-reload message. */
    private BiConsumer<String, String> hotReloadAutoCallback;

    // ──────────────────────────────────────────────────────────────────────
    //  Constructor
    // ──────────────────────────────────────────────────────────────────────

    public NT4Server(int port) {
        super(new InetSocketAddress(port));
        setReuseAddr(true);

        // Pre-register all PathPlanner topics
        registerTopic(NT4Topics.VEL,          NT4Topics.TYPE_DOUBLE_ARRAY);
        registerTopic(NT4Topics.CURRENT_POSE,  NT4Topics.TYPE_STRUCT_POSE2D);
        registerTopic(NT4Topics.ACTIVE_PATH,   NT4Topics.TYPE_STRUCT_POSE2D_ARRAY);
        registerTopic(NT4Topics.TARGET_POSE,   NT4Topics.TYPE_STRUCT_POSE2D);

        // Inbound topics (app → robot) – registered so we can receive them
        registerTopic(NT4Topics.HOT_RELOAD_PATH, "string");
        registerTopic(NT4Topics.HOT_RELOAD_AUTO, "string");
    }

    // ──────────────────────────────────────────────────────────────────────
    //  WebSocketServer callbacks
    // ──────────────────────────────────────────────────────────────────────

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        LOG.info("PathPlanner client connected: " + conn.getRemoteSocketAddress());
        clients.add(conn);

        // Announce every known topic to the new client
        for (TopicInfo topic : topics.values()) {
            sendAnnounce(conn, topic);
        }

        // Send the last known value for each topic so the client has
        // something to display immediately
        for (Map.Entry<String, Object> entry : lastValues.entrySet()) {
            TopicInfo topic = topics.get(entry.getKey());
            if (topic != null) {
                sendValue(conn, topic, entry.getValue());
            }
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        LOG.info("PathPlanner client disconnected: " + conn.getRemoteSocketAddress());
        clients.remove(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        // PathPlanner may send subscribe or hot-reload messages as JSON arrays
        try {
            Object parsed = new JSONParser().parse(message);
            if (!(parsed instanceof JSONArray)) return;
            JSONArray frames = (JSONArray) parsed;
            for (Object frameObj : frames) {
                if (!(frameObj instanceof JSONArray)) continue;
                JSONArray frame = (JSONArray) frameObj;
                if (frame.size() < 2) continue;

                String type = String.valueOf(frame.get(0));
                if (!("publish".equals(type))) continue;

                // Client publishing a value (hot reload case)
                // frame: ["publish", topicName, timestamp, value]
                if (frame.size() >= 4) {
                    String topicName = String.valueOf(frame.get(1));
                    Object value     = frame.get(3);
                    handleInboundValue(topicName, value);
                }
            }
        } catch (ParseException e) {
            // Ignore malformed frames
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        // PathPlanner uses text-mode NT4; binary MessagePack frames are not
        // expected from the client side.  Ignore silently.
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        LOG.warning("NT4 server error: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        LOG.info("NT4 mock server listening on port " + getPort());
    }

    // ──────────────────────────────────────────────────────────────────────
    //  Public publish API
    // ──────────────────────────────────────────────────────────────────────

    /**
     * Publish a double array value (e.g. velocities).
     *
     * @param topicName NT4 topic path
     * @param values    the array values
     */
    public void publishDoubleArray(String topicName, double[] values) {
        JSONArray arr = new JSONArray();
        for (double v : values) arr.add(v);
        publish(topicName, arr);
    }

    /**
     * Publish a single Pose2d encoded as a base64 struct string.
     *
     * @param topicName NT4 topic path
     * @param x         metres
     * @param y         metres
     * @param radians   heading
     */
    public void publishPose2d(String topicName, double x, double y, double radians) {
        publish(topicName, Pose2dSerializer.encodeOne(x, y, radians));
    }

    /**
     * Publish an array of Pose2d values encoded as a base64 struct string.
     *
     * @param topicName NT4 topic path
     * @param poses     flat array [x0,y0,r0, x1,y1,r1, ...]
     */
    public void publishPose2dArray(String topicName, double[] poses) {
        publish(topicName, Pose2dSerializer.encodeArray(poses));
    }

    // ──────────────────────────────────────────────────────────────────────
    //  Hot-reload callbacks
    // ──────────────────────────────────────────────────────────────────────

    public void setHotReloadPathCallback(BiConsumer<String, String> cb) {
        this.hotReloadPathCallback = cb;
    }

    public void setHotReloadAutoCallback(BiConsumer<String, String> cb) {
        this.hotReloadAutoCallback = cb;
    }

    // ──────────────────────────────────────────────────────────────────────
    //  Internal helpers
    // ──────────────────────────────────────────────────────────────────────

    private void registerTopic(String name, String type) {
        topics.put(name, new TopicInfo(nextTopicId.getAndIncrement(), name, type));
    }

    private long nowMicros() {
        return (System.nanoTime() - startNanos) / 1_000L;
    }

    /**
     * Core publish – stores the value and broadcasts to all connected clients.
     */
    private void publish(String topicName, Object jsonValue) {
        TopicInfo topic = topics.get(topicName);
        if (topic == null) return;

        lastValues.put(topicName, jsonValue);

        for (WebSocket client : clients) {
            if (client.isOpen()) {
                sendValue(client, topic, jsonValue);
            }
        }
    }

    /**
     * Send an NT4 {@code announce} control frame to one client.
     *
     * NT4 announce frame (JSON array):
     * {@code [3, topicId, topicName, typeString, {}]}
     */
    private void sendAnnounce(WebSocket conn, TopicInfo topic) {
        JSONArray frame = new JSONArray();
        frame.add(NT4Topics.MSG_ANNOUNCE);   // message type = 3
        frame.add(topic.id);
        frame.add(topic.name);
        frame.add(topic.type);
        frame.add(new JSONObject());         // empty properties

        // NT4 control messages are sent as a JSON array-of-frames
        JSONArray wrapper = new JSONArray();
        wrapper.add(frame);
        conn.send(wrapper.toJSONString());
    }

    /**
     * Send an NT4 value frame to one client.
     *
     * NT4 value frame (JSON array):
     * {@code [topicId, timestamp_µs, value]}
     *
     * Multiple value frames are batched inside an outer JSON array.
     */
    private void sendValue(WebSocket conn, TopicInfo topic, Object jsonValue) {
        JSONArray frame = new JSONArray();
        frame.add(topic.id);
        frame.add(nowMicros());
        frame.add(jsonValue);

        JSONArray wrapper = new JSONArray();
        wrapper.add(frame);
        conn.send(wrapper.toJSONString());
    }

    private void handleInboundValue(String topicName, Object value) {
        if (NT4Topics.HOT_RELOAD_PATH.equals(topicName) && hotReloadPathCallback != null) {
            hotReloadPathCallback.accept(topicName, String.valueOf(value));
        } else if (NT4Topics.HOT_RELOAD_AUTO.equals(topicName) && hotReloadAutoCallback != null) {
            hotReloadAutoCallback.accept(topicName, String.valueOf(value));
        }
    }

    // ──────────────────────────────────────────────────────────────────────
    //  Internal data class
    // ──────────────────────────────────────────────────────────────────────

    private static final class TopicInfo {
        final int    id;
        final String name;
        final String type;

        TopicInfo(int id, String name, String type) {
            this.id   = id;
            this.name = name;
            this.type = type;
        }
    }
}
