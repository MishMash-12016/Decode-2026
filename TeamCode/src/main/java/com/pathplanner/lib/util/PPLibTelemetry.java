package com.pathplanner.lib.util;

import com.pathplanner.lib.path.PathPlannerPath;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import Ori.Coval.Logging.Logger.KoalaLog;

/** Utility class for sending data to the PathPlanner app via KoalaLog */
public class PPLibTelemetry {
  private static boolean compMode = false;

  /** Enable competition mode. This will disable hot reload. */
  public static void enableCompetitionMode() {
    compMode = true;
  }

  /**
   * Set the path following actual/target velocities
   *
   * @param actualVel Actual robot velocity in m/s
   * @param commandedVel Target robot velocity in m/s
   * @param actualAngVel Actual angular velocity in rad/s
   * @param commandedAngVel Target angular velocity in rad/s
   */
  public static void setVelocities(
      double actualVel, double commandedVel, double actualAngVel, double commandedAngVel) {
    if (!compMode) {
      KoalaLog.log(
          "PathPlanner/vel",
          new double[]{actualVel, commandedVel, actualAngVel, commandedAngVel},
          true
      );
    }
  }

  /**
   * Set the current robot pose
   *
   * @param pose Current robot pose
   */
  public static void setCurrentPose(Pose2d pose) {
    if (!compMode) {
      KoalaLog.log(
          "PathPlanner/currentPose",
          new double[]{pose.getX(),
          pose.getY(),
          pose.getRotation().getRadians()},
          true
      );
    }
  }

  /**
   * Set the current path being followed
   *
   * @param path The current path
   */
  public static void setCurrentPath(PathPlannerPath path) {
    if (!compMode) {
      List<Pose2d> poses = path.getPathPoses();
      double[] flat = new double[poses.size() * 3];
      for (int i = 0; i < poses.size(); i++) {
        Pose2d p = poses.get(i);
        flat[i * 3]     = p.getX();
        flat[i * 3 + 1] = p.getY();
        flat[i * 3 + 2] = p.getRotation().getRadians();
      }
      KoalaLog.log("PathPlanner/activePath", flat, true);
    }
  }

  /**
   * Set the target robot pose
   *
   * @param targetPose Target robot pose
   */
  public static void setTargetPose(Pose2d targetPose) {
    if (!compMode) {
      KoalaLog.log(
          "PathPlanner/targetPose",
          new double[]{targetPose.getX(),
          targetPose.getY(),
          targetPose.getRotation().getRadians()},
          true
      );
    }
  }
}