package org.firstinspires.ftc.teamcode.OpModes.Auto;

import Ori.Coval.Logging.AutoLog;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.Libraries.pedroPathing.FollowPathCommand;
import org.firstinspires.ftc.teamcode.subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

@AutoLog
@Autonomous
public class FarBlue extends MMOpMode {

    private PathChain
            Path1,
            Path2,
            Path3,
            Path4;

    private final Pose startPose = new Pose(55, 8, Math.toRadians(270));

    Follower follower;

    public void buildPaths() {
        Path1 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(55.000, 8.000),

                                new Pose(55.000, 18.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(290))
                .build();

        Path2 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(55.000, 18.000),

                                new Pose(50.000, 35.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(290), Math.toRadians(180))
                .build();

        Path3 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(50.000, 35.000),

                                new Pose(12.00, 36.00)
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        Path4 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(12.00, 36.00),

                                new Pose(55.000, 18.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(290)).setBrakingStart(1.5)
                .build();
    }


    public FarBlue() {
        super(OpModeType.Competition.AUTO, AllianceColor.BLUE);
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onPlay() {
        CommandScheduler.getInstance().reset();
        MMDrivetrain.getInstance().reset();
        MMDrivetrain.getInstance().getFollower().setStartingPose(startPose);
        MMDrivetrain.getInstance().getFollower().setPose(startPose);
        follower = MMDrivetrain.getInstance().getFollower();
        buildPaths();

        ///PATH
        Command autonomousSequence =
                ///things that will happened along the auto   ↓
                new ParallelCommandGroup(
                        ShooterSubsystem.getInstance().speedByLocation(),
                        ShooterHoodSubsystem.getInstance().aimHood(),
                        ///                                   ↑
                        new SequentialCommandGroup(
                                new FollowPathCommand(follower, Path1)
                                        .andThen(new WaitCommand(2000))
                                        .withTimeout(5000)
                                        .andThen(ShootCommandGroup.twoOneShoot())
                                        .andThen(new WaitCommand(1000)),

                                new FollowPathCommand(follower, Path2)
                                        .andThen(new WaitCommand(1000))
                                        .withTimeout(4000),

                                new FollowPathCommand(follower, Path3)
                                        .alongWith(IntakeCommandGroup.smartFeed())
                                        .andThen(new WaitCommand(1000))
                                        .withTimeout(3000)
                                        .andThen(IntakeCommandGroup.stopIntake()),

                                new FollowPathCommand(follower, Path4)
                                        .andThen(new WaitCommand(1000))
                                        .withTimeout(4000)
                                        .andThen(ShootCommandGroup.twoOneShoot())
                                        .andThen(new WaitCommand(2000))
                                        .withTimeout(8000)
                        )
                ).andThen(IntakeCommandGroup.stopAll());
        autonomousSequence.schedule();
    }

    @Override
    public void onPlayLoop() {
        MMDrivetrain.update();
        telemetry.update();
        telemetry.addData("Shooter: ", ShooterSubsystem.getInstance().getVelocity());
    }

    @Override
    public void onEnd() {
        super.onEnd();
        CommandScheduler.getInstance().reset();
    }
}