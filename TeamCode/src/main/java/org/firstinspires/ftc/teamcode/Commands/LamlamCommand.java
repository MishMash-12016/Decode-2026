package org.firstinspires.ftc.teamcode.Commands;

import com.acmerobotics.dashboard.config.Config;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.Path;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.pedroPathing.FollowPathCommand;
import org.firstinspires.ftc.teamcode.Subsystems.CameraSubsystem;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;


import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.Logger.KoalaLog;

@Config
@AutoLog
public class LamlamCommand {

    public static double offsetX = 0; //mm
    public static double offsetY = 0; //mm
    public static boolean noResult = true;


    public static SequentialCommandGroup trackGreen(){
        return new SequentialCommandGroup(
                new InstantCommand(() -> CameraSubsystem.getInstance().trackGreen()),
                makeSurePipelineSwitched()
        );
    }

    public static SequentialCommandGroup trackPurple(){
        return new SequentialCommandGroup(
                new InstantCommand(() -> CameraSubsystem.getInstance().trackPurple()),
                makeSurePipelineSwitched()
        );
    }

    public static SequentialCommandGroup trackPurpleAndGreen(){
        return new SequentialCommandGroup(
                new InstantCommand(() -> CameraSubsystem.getInstance().trackPurpleAndGreen()),
                makeSurePipelineSwitched()
        );
    }

    private static SequentialCommandGroup makeSurePipelineSwitched(){
        return new SequentialCommandGroup(
                new InstantCommand(() -> CameraSubsystem.getInstance().switchToDetector()),
                new WaitUntilCommand(() -> CameraSubsystem.getInstance().getPipelineIndex() == CameraSubsystem.getInstance().currentPipeline)
        );
    }

    public static boolean isResult(){
        LLResult last = CameraSubsystem.getInstance().getLatestResult();
        if (last == null || last.getDetectorResults().isEmpty())
            return false;

        noResult = false;
        return true;
    }
    public static Command GoToArtifact() {
                LLResult last = CameraSubsystem.getInstance().getLatestResult();
        if (last == null || last.getDetectorResults().isEmpty()) {
            KoalaLog.log("last result is null", "", true);;
            return new InstantCommand();
        }

                double distanceX = -(CameraSubsystem.distanceX + offsetX) / 25.4; // mm to inch
                double distanceY = (CameraSubsystem.distanceY + offsetY) / 25.4; // mm to inch

                distanceY += 2;

                Pose currentPose = MMDrivetrain.getInstance().getFollower().getPose();
                double currentHeading = currentPose.getHeading();

                double dxField = distanceY * Math.cos(currentHeading) - distanceX * Math.sin(currentHeading);
                double dyField = distanceY * Math.sin(currentHeading) + distanceX * Math.cos(currentHeading);

                //  Camera distance from robot's center in inches, TODO: change that for the robot!!!
                // Positive x is robot forward, positive y is robot left.
                final double camX_in = 0.0;  // set the offset
                final double camY_in = 0.0;  // set the offset

                // rotate camera offset into field frame and add it so the goal is relative to robot center
                double dCamXField =  camX_in * Math.cos(currentHeading) - camY_in * Math.sin(currentHeading);
                double dCamYField =  camX_in * Math.sin(currentHeading) + camY_in * Math.cos(currentHeading);

                dxField += dCamXField;
                dyField += dCamYField;

                double endX = currentPose.getX() + dxField;
                double endY = currentPose.getY() + dyField;

                Path path = new Path(new BezierLine(
                        new Pose(currentPose.getX(),
                                currentPose.getY()),
                        new Pose(endX,
                                endY)));

                double targetHeading = Math.atan2(dyField, dxField);
                path.setLinearHeadingInterpolation(currentHeading, targetHeading);


                // Schedule the actual followPath command now that we have live data
                return new FollowPathCommand(MMDrivetrain.getInstance().getFollower(), path);

            }

    public static Command AutoGoToArtifact() {
        CameraSubsystem.getInstance().getLatestResult();

        double distanceX = -(CameraSubsystem.distanceX + offsetX) / 25.4; // mm to inch
        double distanceY = (CameraSubsystem.distanceY + offsetY) / 25.4; // mm to inch

        distanceY += 2;

        Pose currentPose = MMDrivetrain.getInstance().getFollower().getPose();
        double currentHeading = currentPose.getHeading();

        double dxField = distanceY * Math.cos(currentHeading) - distanceX * Math.sin(currentHeading);
        double dyField = distanceY * Math.sin(currentHeading) + distanceX * Math.cos(currentHeading);

        //  Camera distance from robot's center in inches, TODO: change that for the robot!!!
        // Positive x is robot forward, positive y is robot left.
        final double camX_in = 0.0;  // set the offset
        final double camY_in = 0.0;  // set the offset

        // rotate camera offset into field frame and add it so the goal is relative to robot center
        double dCamXField =  camX_in * Math.cos(currentHeading) - camY_in * Math.sin(currentHeading);
        double dCamYField =  camX_in * Math.sin(currentHeading) + camY_in * Math.cos(currentHeading);

        dxField += dCamXField;
        dyField += dCamYField;

        double endX = currentPose.getX() + dxField;
        double endY = currentPose.getY() + dyField;

        Path path = new Path(new BezierLine(
                new Pose(currentPose.getX(),
                        currentPose.getY()),
                new Pose(endX,
                        endY)));

        double targetHeading = Math.atan2(dyField, dxField);
        path.setLinearHeadingInterpolation(currentHeading, targetHeading);


        // Schedule the actual followPath command now that we have live data
        return new FollowPathCommand(MMDrivetrain.getInstance().getFollower(), path);

    }

}
