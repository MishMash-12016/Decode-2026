package com.pathplanner.ftc.nt4;

/**
 * NT4 topic names and type strings matching what PathPlanner's desktop app
 * subscribes to. These must exactly match the WPILib NT4 topic names used
 * in the original PPLibTelemetry.java.
 */
public final class NT4Topics {

    // ── Topic paths ────────────────────────────────────────────────────────
    public static final String VEL          = "/PathPlanner/vel";
    public static final String CURRENT_POSE = "/PathPlanner/currentPose";
    public static final String ACTIVE_PATH  = "/PathPlanner/activePath";
    public static final String TARGET_POSE  = "/PathPlanner/targetPose";

    // Hot-reload (inbound from app → robot, read-only from the server side)
    public static final String HOT_RELOAD_PATH = "/PathPlanner/HotReload/hotReloadPath";
    public static final String HOT_RELOAD_AUTO = "/PathPlanner/HotReload/hotReloadAuto";

    // ── NT4 type strings ───────────────────────────────────────────────────
    /** double[] – [actualVel, commandedVel, actualAngVel, commandedAngVel] */
    public static final String TYPE_DOUBLE_ARRAY = "float64[]";

    /**
     * WPILib struct-encoded Pose2d serialised as raw bytes, then base64'd
     * inside the JSON value field so it travels cleanly over the text-mode
     * NT4 WebSocket framing that PathPlanner uses.
     *
     * Pose2d struct layout (little-endian):
     *   double x        (8 bytes)
     *   double y        (8 bytes)
     *   double rotation (8 bytes)  ← radians
     * Total: 24 bytes
     */
    public static final String TYPE_STRUCT_POSE2D       = "struct:Pose2d";
    public static final String TYPE_STRUCT_POSE2D_ARRAY = "struct:Pose2d[]";

    // NT4 message type IDs (from the NT4 spec)
    public static final int MSG_PUBLISH     = 0;  // server → client: "this topic exists"
    public static final int MSG_UNPUBLISH   = 1;
    public static final int MSG_SET_FLAGS   = 2;
    public static final int MSG_ANNOUNCE    = 3;  // server → client topic announcement
    public static final int MSG_UNANNOUNCE  = 4;
    public static final int MSG_PROPERTIES  = 5;
    public static final int MSG_SUBSCRIBE   = 6;  // client → server
    public static final int MSG_UNSUBSCRIBE = 7;

    private NT4Topics() {}
}
