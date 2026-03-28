package com.pathplanner.lib.util;

import com.pathplanner.ftc.nt4.NT4Server;
import com.pathplanner.ftc.nt4.NT4Topics;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.math.geometry.Pose2d;

import java.util.List;
import java.util.logging.Logger;

/**
 * FTC-compatible drop-in replacement for PathPlanner's {@code PPLibTelemetry}.
 *
 * Method signatures are identical to the WPILib version — {@link Pose2d} and
 * {@link PathPlannerPath} are accepted directly. Internally the poses are
 * unpacked and serialised into the NT4 struct binary format, then broadcast
 * over a lightweight WebSocket server that PathPlanner's desktop app can
 * connect to.
 *
 * Setup:
 * <pre>{@code
 * @Override
 * public void runOpMode() {
 *     PPLibTelemetry.startServer();
 *     // ... your code, identical to FRC usage
 * }
 * }</pre>
 *
 * In PathPlanner → Settings → Telemetry set the server address to
 * {@code ws://192.168.43.1:5810} (Control Hub AP address).
 */
public class PPLibTelemetry {

  private static final Logger LOG = Logger.getLogger(PPLibTelemetry.class.getName());

  /** Default NT4 port – matches what PathPlanner's app expects. */
  public static final int DEFAULT_PORT = 5810;

  private static NT4Server server;
  private static boolean   compMode = false;

  // ── Server lifecycle (FTC-only additions) ─────────────────────────────

  /** Start the NT4 mock server on the default port (5810). */
  public static synchronized void startServer() {
    startServer(DEFAULT_PORT);
  }

  /**
   * Start the NT4 mock server on a custom port.
   *
   * @param port TCP port to listen on (PathPlanner app expects 5810)
   */
  public static synchronized void startServer(int port) {
    if (server != null) {
      LOG.warning("NT4 server already running, ignoring startServer() call");
      return;
    }
    server = new NT4Server(port);
    server.start();
    LOG.info("PathPlanner NT4 telemetry server started on port " + port);
  }

  /** Stop the NT4 mock server. Call this in your OpMode's stop() method. */
  public static synchronized void stopServer() {
    if (server == null) return;
    try {
      server.stop(1000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      server = null;
    }
  }

  /** @return true if the NT4 server is currently running */
  public static boolean isRunning() {
    return server != null;
  }

  // ── Competition mode (identical to WPILib version) ────────────────────

  /** Enable competition mode – disables all telemetry publishing. */
  public static void enableCompetitionMode() {
    compMode = true;
  }

  // ── Telemetry setters (identical signatures to the WPILib version) ────

  /**
   * Set the path following actual/target velocities.
   *
   * @param actualVel        Actual robot velocity in m/s
   * @param commandedVel     Target robot velocity in m/s
   * @param actualAngVel     Actual angular velocity in rad/s
   * @param commandedAngVel  Target angular velocity in rad/s
   */
  public static void setVelocities(
      double actualVel, double commandedVel,
      double actualAngVel, double commandedAngVel) {
    if (compMode || server == null) return;
    server.publishDoubleArray(
        NT4Topics.VEL,
        new double[]{actualVel, commandedVel, actualAngVel, commandedAngVel});
  }

  /**
   * Set the current robot pose.
   *
   * @param pose Current robot pose
   */
  public static void setCurrentPose(Pose2d pose) {
    if (compMode || server == null) return;
    server.publishPose2d(
        NT4Topics.CURRENT_POSE,
        pose.getX(),
        pose.getY(),
        pose.getRotation().getRadians());
  }

  /**
   * Set the current path being followed.
   *
   * @param path The current path
   */
  public static void setCurrentPath(PathPlannerPath path) {
    if (compMode || server == null) return;
    List<Pose2d> poses = path.getPathPoses();
    double[] flat = new double[poses.size() * 3];
    for (int i = 0; i < poses.size(); i++) {
      Pose2d p = poses.get(i);
      flat[i * 3]     = p.getX();
      flat[i * 3 + 1] = p.getY();
      flat[i * 3 + 2] = p.getRotation().getRadians();
    }
    server.publishPose2dArray(NT4Topics.ACTIVE_PATH, flat);
  }

  /**
   * Set the target robot pose.
   *
   * @param targetPose Target robot pose
   */
  public static void setTargetPose(Pose2d targetPose) {
    if (compMode || server == null) return;
    server.publishPose2d(
        NT4Topics.TARGET_POSE,
        targetPose.getX(),
        targetPose.getY(),
        targetPose.getRotation().getRadians());
  }

  // ── Hot-reload support (identical to WPILib version minus file I/O) ───

  /**
   * Register a callback for path hot-reload events from the PathPlanner app.
   *
   * @param callback (pathName, jsonString) → void
   */
  public static void setHotReloadPathCallback(java.util.function.BiConsumer<String, String> callback) {
    if (server != null) server.setHotReloadPathCallback(callback);
  }

  /**
   * Register a callback for auto hot-reload events from the PathPlanner app.
   *
   * @param callback (autoName, jsonString) → void
   */
  public static void setHotReloadAutoCallback(java.util.function.BiConsumer<String, String> callback) {
    if (server != null) server.setHotReloadAutoCallback(callback);
  }

  private PPLibTelemetry() {}
}
