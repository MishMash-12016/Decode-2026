package com.pathplanner.ftc.nt4;

/** NT4 topic names and WPILib type strings for PathPlanner telemetry. */
public final class NT4Topics {

    public static final String VEL          = "/PathPlanner/vel";
    public static final String CURRENT_POSE = "/PathPlanner/currentPose";
    public static final String ACTIVE_PATH  = "/PathPlanner/activePath";
    public static final String TARGET_POSE  = "/PathPlanner/targetPose";

    public static final String HOT_RELOAD_PATH = "/PathPlanner/HotReload/hotReloadPath";
    public static final String HOT_RELOAD_AUTO = "/PathPlanner/HotReload/hotReloadAuto";

    /** NT4 type string for double[] (velocities). */
    public static final String TYPE_DOUBLE_ARRAY = "float64[]";

    /** NT4 type string for a single WPILib Pose2d struct (24 bytes, little-endian). */
    public static final String TYPE_STRUCT_POSE2D = "struct:Pose2d";

    /** NT4 type string for an array of Pose2d structs. */
    public static final String TYPE_STRUCT_POSE2D_ARRAY = "struct:Pose2d[]";

    private NT4Topics() {}
}
