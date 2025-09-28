package org.firstinspires.ftc.teamcode.Camera;

import com.acmerobotics.dashboard.config.Config;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.Path;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.pedroPathing.FollowPathCommand;
import org.firstinspires.ftc.teamcode.subsystems.Camera;
import com.pedropathing.geometry.Pose;


import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.Logger.KoalaLog;

@Config
@AutoLog
public class CameraCommands {

    public static double plusXdis = 0;
    public static double plusYdis = 0;

    public static CommandBase StrafeToArtifact() {
        return new CommandBase() {
            CommandBase strafeCommand = new InstantCommand();

            @Override
            public void initialize() {
                LLResult last = Camera.getInstance().GetPreviousDetectorResult();
                if (last == null) {
                    KoalaLog.log("last result is null", "", true);
                    return;
                }

                double dx = Camera.dx + plusXdis;
                double dy = Camera.dy + plusYdis;

                MMDrivetrain dt = MMDrivetrain.getInstance();
                dt.follower.updatePose();
                dt.follower.update();

                Pose p = dt.follower.getPose();
                double h = p.getHeading();

                double dxf = dx * Math.cos(h + Math.PI/2.0) + dy * Math.cos(h);
                double dyf = dx * Math.sin(h + Math.PI/2.0) + dy * Math.sin(h);

                double endX = p.getX() + dxf;
                double endY = p.getY() + dyf;

                Path path = new Path(new BezierLine(
                        new Pose(p.getX(), p.getY()),
                        new Pose(endX, endY)));
                path.setConstantHeadingInterpolation(h);

                // Schedule the actual followPath command now that we have live data
                strafeCommand = new FollowPathCommand(MMDrivetrain.getInstance().getFollower(), path);
                strafeCommand.schedule();
            }

            @Override
            public boolean isFinished() {
                return strafeCommand.isFinished();
            }
        };
    }

}
