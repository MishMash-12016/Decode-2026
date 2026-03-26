package com.pathplanner.lib.util;

import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Filesystem;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.*;

/**
 * FTC port of PPLibTelemetry.
 *
 * Starts an NT4 WebSocket server on port 5810 so the PathPlanner
 * app's telemetry panel and hot reload work exactly as in FRC.
 *
 * Usage:
 *   PPLibTelemetry.start();                              // once in init()
 *   PPLibTelemetry.setCurrentPose(x, y, headingRad);    // each loop
 *   PPLibTelemetry.setTargetPose(x, y, headingRad);
 *   PPLibTelemetry.setVelocities(av, cv, aav, cav);
 *   PPLibTelemetry.setCurrentPath(path);
 *   PPLibTelemetry.stop();                               // in stop()
 */
public class PPLibTelemetry {

  private static boolean compMode = false;
  private static final NT4Server server = new NT4Server();
  private static boolean started = false;

  // Registered instances for hot reload
  private static final Map<String, List<PathPlannerPath>> hotReloadPaths = new HashMap<>();
  private static final Map<String, List<PathPlannerAuto>> hotReloadAutos = new HashMap<>();

  // ── Lifecycle ────────────────────────────────────────────────────────────

  /** Call once at the start of your OpMode, before the loop. */
  public static void start() {
    if (!compMode && !started) {
      server.start();
      started = true;

      // Wire up hot reload: path
      server.setPathHotReloadCallback((name, pathJson) -> {
        if (hotReloadPaths.containsKey(name)) {
          for (PathPlannerPath path : hotReloadPaths.get(name)) {
            path.hotReload(pathJson);
          }
        }
        saveDeployFile("pathplanner/paths/" + name + ".path", pathJson.toJSONString());
      });

      // Wire up hot reload: auto
      server.setAutoHotReloadCallback((name, autoJson) -> {
        if (hotReloadAutos.containsKey(name)) {
          for (PathPlannerAuto auto : hotReloadAutos.get(name)) {
            auto.hotReload(autoJson);
          }
        }
        saveDeployFile("pathplanner/autos/" + name + ".auto", autoJson.toJSONString());
      });
    }
  }

  /** Call when your OpMode ends. */
  public static void stop() {
    server.stop();
    started = false;
  }

  /** Enable competition mode — disables all telemetry and hot reload. */
  public static void enableCompetitionMode() {
    compMode = true;
  }

  // ── Telemetry setters ────────────────────────────────────────────────────

  /**
   * @param actualVel       Actual robot velocity (m/s)
   * @param commandedVel    Target velocity (m/s)
   * @param actualAngVel    Actual angular velocity (rad/s)
   * @param commandedAngVel Target angular velocity (rad/s)
   */
  public static void setVelocities(
      double actualVel, double commandedVel,
      double actualAngVel, double commandedAngVel) {
    if (!compMode) {
      server.setVelocities(actualVel, commandedVel, actualAngVel, commandedAngVel);
    }
  }

  public static void setCurrentPose(Pose2d pose2d) {
    if (!compMode) server.setCurrentPose(pose2d.getX(), pose2d.getY(), pose2d.getRotation().getRadians());
  }

  public static void setTargetPose(Pose2d pose2d) {
    if (!compMode) server.setTargetPose(pose2d.getX(), pose2d.getY(), pose2d.getRotation().getRadians());
  }

  /**
   * Set the current path being followed.
   * Extracts poses from the PathPlannerPath automatically.
   *
   * @param path The current PathPlannerPath
   */
  public static void setCurrentPath(PathPlannerPath path) {
    if (!compMode) {
      List<edu.wpi.first.math.geometry.Pose2d> poses = path.getPathPoses();
      double[][] poseArray = new double[poses.size()][3];
      for (int i = 0; i < poses.size(); i++) {
        poseArray[i][0] = poses.get(i).getX();
        poseArray[i][1] = poses.get(i).getY();
        poseArray[i][2] = poses.get(i).getRotation().getRadians();
      }
      server.setActivePath(poseArray);
    }
  }

  // ── Hot reload registration ───────────────────────────────────────────────

  /**
   * Register a path for hot reload.
   * Called internally by PathPlannerPath.fromPathFile().
   */
  public static void registerHotReloadPath(String pathName, PathPlannerPath path) {
    if (!compMode) {
      if (!hotReloadPaths.containsKey(pathName)) {
        hotReloadPaths.put(pathName, new ArrayList<>());
      }
      hotReloadPaths.get(pathName).add(path);
    }
  }

  /**
   * Register an auto for hot reload.
   * Called internally by PathPlannerAuto.
   */
  public static void registerHotReloadAuto(String autoName, PathPlannerAuto auto) {
    if (!compMode) {
      if (!hotReloadAutos.containsKey(autoName)) {
        hotReloadAutos.put(autoName, new ArrayList<>());
      }
      hotReloadAutos.get(autoName).add(auto);
    }
  }

  // ── File I/O ─────────────────────────────────────────────────────────────

  /**
   * Save updated JSON back to the deploy directory so hot reload
   * changes persist across restarts without redeploying.
   */
  private static void saveDeployFile(String relativePath, String content) {
    try {
      File file = new File(
          Filesystem.getDeployDirectory(
              MMRobot.getInstance().currentOpMode.hardwareMap.appContext),
          relativePath);
      try (FileWriter writer = new FileWriter(file)) {
        writer.write(content);
        writer.flush();
      }
    } catch (IOException e) {
      android.util.Log.w("PPLibTelemetry",
          "Hot reload file save failed: " + relativePath +
              " — changes won't persist after restart. Redeploy to make them permanent.");
    }
  }
}