package com.pathplanner.ftc.nt4;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Base64;

/**
 * Serialises Pose2d (and arrays of Pose2d) into the exact binary layout that
 * WPILib's struct system produces, then base64-encodes the result so it can
 * be embedded as a JSON string inside an NT4 value message.
 *
 * WPILib Pose2d struct (little-endian, 24 bytes):
 *   Translation2d.x  : float64  (8 bytes)
 *   Translation2d.y  : float64  (8 bytes)
 *   Rotation2d.value : float64  (8 bytes, radians)
 */
public final class Pose2dSerializer {

    /** Bytes per Pose2d struct. */
    public static final int POSE_BYTES = 24;

    private Pose2dSerializer() {}

    /**
     * Encode a single Pose2d to a base64 string.
     *
     * @param x        metres
     * @param y        metres
     * @param radians  heading in radians
     * @return base64-encoded struct bytes
     */
    public static String encodeOne(double x, double y, double radians) {
        ByteBuffer buf = ByteBuffer.allocate(POSE_BYTES);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putDouble(x);
        buf.putDouble(y);
        buf.putDouble(radians);
        return Base64.getEncoder().encodeToString(buf.array());
    }

    /**
     * Encode an array of poses to a base64 string (concatenated structs).
     *
     * @param poses flat array: [x0, y0, r0, x1, y1, r1, ...]
     * @return base64-encoded concatenated struct bytes
     */
    public static String encodeArray(double[] poses) {
        if (poses.length % 3 != 0) {
            throw new IllegalArgumentException("poses array length must be a multiple of 3");
        }
        int count = poses.length / 3;
        ByteBuffer buf = ByteBuffer.allocate(POSE_BYTES * count);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < count; i++) {
            buf.putDouble(poses[i * 3]);
            buf.putDouble(poses[i * 3 + 1]);
            buf.putDouble(poses[i * 3 + 2]);
        }
        return Base64.getEncoder().encodeToString(buf.array());
    }
}
