package com.pathplanner.lib.util;

import com.pathplanner.ftc.nt4.NT4Server;
import com.pathplanner.ftc.nt4.NT4Topics;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.math.geometry.Pose2d;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * FTC-compatible drop-in replacement for PathPlanner's PPLibTelemetry.
 *
 * Identical public API to the WPILib version. Internally spins up a lightweight
 * NT4 WebSocket server so PathPlanner's desktop app can connect over WiFi.
 *
 * Setup — one line in your OpMode:
 *   PPLibTelemetry.startServer();
 *
 * Connect PathPlanner to: ws://192.168.43.1:5810
 */
public class PPLibTelemetry {

  private static final Logger LOG = Logger.getLogger(PPLibTelemetry.class.getName());

  public static final int DEFAULT_PORT = 5810;

  private static NT4Server server;
  private static boolean compMode = false;

  // Hot reload registries — mirrors the WPILib version's Maps
  private static final Map<String, List<PathPlannerPath>> hotReloadPaths = new HashMap<>();
  private static final Map<String, List<PathPlannerAuto>> hotReloadAutos = new HashMap<>();

  // ── Server lifecycle ──────────────────────────────────────────────────

  public static synchronized void startServer() {
    startServer(DEFAULT_PORT);
  }

  public static synchronized void startServer(int port) {
    if (server != null) {
      LOG.warning("NT4 server already running");
      return;
    }
    server = new NT4Server(port);

    // Wire inbound hot-reload messages from the app → our handlers
    server.setHotReloadPathCallback((topicName, jsonStr) -> handlePathHotReload(jsonStr));
    server.setHotReloadAutoCallback((topicName, jsonStr) -> handleAutoHotReload(jsonStr));

    try {
      server.start(-1, false); // -1 = no read timeout; connections stay open indefinitely
      LOG.info("PathPlanner NT4 telemetry server started on port " + port);
    } catch (java.io.IOException e) {
      LOG.warning("Failed to start NT4 server: " + e.getMessage());
      server = null;
    }
  }

  public static synchronized void stopServer() {
    if (server == null) return;
    try {
      server.stop();
    } finally {
      server = null;
    }
    hotReloadPaths.clear();
    hotReloadAutos.clear();
  }

  public static boolean isRunning() {
    return server != null;
  }

  // ── Competition mode ──────────────────────────────────────────────────

  public static void enableCompetitionMode() {
    compMode = true;
  }

  // ── Telemetry setters (identical signatures to the WPILib version) ────

  /**
   * Set the path following actual/target velocities.
   *
   * @param actualVel       Actual robot velocity in m/s
   * @param commandedVel    Target robot velocity in m/s
   * @param actualAngVel    Actual angular velocity in rad/s
   * @param commandedAngVel Target angular velocity in rad/s
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

  // ── Hot reload registration (called by PathPlannerPath / PathPlannerAuto) ─

  /**
   * Register a path for hot reload. Called internally by PathPlannerPath.fromPathFile().
   *
   * @param pathName Name of the path
   * @param path     Reference to the path object
   */
  public static void registerHotReloadPath(String pathName, PathPlannerPath path) {
    if (compMode) return;
    if (!hotReloadPaths.containsKey(pathName)) {
      hotReloadPaths.put(pathName, new ArrayList<>());
    }
    hotReloadPaths.get(pathName).add(path);
  }

  /**
   * Register an auto for hot reload. Called internally by PathPlannerAuto.
   *
   * @param autoName Name of the auto
   * @param auto     Reference to the auto object
   */
  public static void registerHotReloadAuto(String autoName, PathPlannerAuto auto) {
    if (compMode) return;
    if (!hotReloadAutos.containsKey(autoName)) {
      hotReloadAutos.put(autoName, new ArrayList<>());
    }
    hotReloadAutos.get(autoName).add(auto);
  }

  // ── Hot reload handlers (called when app pushes an update) ────────────

  private static void handlePathHotReload(String jsonStr) {
    if (compMode) return;
    try {
      JSONObject json     = (JSONObject) new JSONParser().parse(jsonStr);
      String     name     = (String) json.get("name");
      JSONObject pathJson = (JSONObject) json.get("path");

      List<PathPlannerPath> paths = hotReloadPaths.get(name);
      if (paths != null) {
        for (PathPlannerPath p : paths) {
          p.hotReload(pathJson);
        }
      }
      // FTC has no Filesystem.getDeployDirectory() file write — hot reload
      // applies in-memory only. Re-deploy to persist changes.
    } catch (Exception e) {
      LOG.warning("Hot reload path parse failed: " + e.getMessage());
    }
  }

  private static void handleAutoHotReload(String jsonStr) {
    if (compMode) return;
    try {
      JSONObject json     = (JSONObject) new JSONParser().parse(jsonStr);
      String     name     = (String) json.get("name");
      JSONObject autoJson = (JSONObject) json.get("auto");

      List<PathPlannerAuto> autos = hotReloadAutos.get(name);
      if (autos != null) {
        for (PathPlannerAuto a : autos) {
          a.hotReload(autoJson);
        }
      }
    } catch (Exception e) {
      LOG.warning("Hot reload auto parse failed: " + e.getMessage());
    }
  }

  private PPLibTelemetry() {}
}
