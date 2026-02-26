package org.firstinspires.ftc.teamcode.OpModes.Auto;

import Ori.Coval.Logging.AutoLog;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
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
            FRST_SHOOT,
            INTAKE_05,
            INTAKE_1,
            INTAKE_1_TO_SHOOT,
            INTAKE_15,
            INTAKE_15_TO_INTAKE_2,
            INTAKE_2_TO_SHOOT,
            LEAVE;

    private final Pose startPose = new Pose(55, 8, Math.toRadians(270));

    Follower follower;

    public void buildPaths() {
        FRST_SHOOT = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(55.000, 8.000),

                                new Pose(55.000, 13.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(290))
                .build();

        INTAKE_05 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(55.000, 13.000),

                                new Pose(50.000, 35.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(290), Math.toRadians(180))
                .build();

        INTAKE_1 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(50.000, 35.000),

                                new Pose(12.00, 36.00)
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        INTAKE_1_TO_SHOOT = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(12.00, 36.00),

                                new Pose(55.000, 13.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(292)).setBrakingStart(1.5)
                .build();

        INTAKE_15 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(55.000, 13.000),

                                new Pose(8.000, 30.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(290), Math.toRadians(270))

                .build();

        INTAKE_15_TO_INTAKE_2 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(8.000, 30.000),

                                new Pose(8.000, 11.000)
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(270)).setBrakingStart(1.8)

                .build();

        INTAKE_2_TO_SHOOT = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(8.000, 11.000),

                                new Pose(55.000, 13.00)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(292)).setBrakingStart(1.8)

                .build();

        LEAVE = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(55.000, 13.000),

                                new Pose(45.000, 20.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(292), Math.toRadians(230))

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
                ///things that will happened along the auto   ↓
                new ParallelCommandGroup(
                        ShooterSubsystem.getInstance().speedByLocation(),
                        ShooterHoodSubsystem.getInstance().aimHood(),
                        ///                                   ↑
                        new SequentialCommandGroup(
                                new FollowPathCommand(follower, FRST_SHOOT)
                                        .andThen(new WaitCommand(2000))
                                        .withTimeout(5000)
                                        .andThen(ShootCommandGroup.twoOneShoot())
                                        .andThen(new WaitCommand(1000)),

                                new FollowPathCommand(follower, INTAKE_05)
                                        .andThen(new WaitCommand(1000))
                                        .withTimeout(4000),

                                new FollowPathCommand(follower, INTAKE_1)
                                        .alongWith(IntakeCommandGroup.smartFeed())
                                        .andThen(new WaitCommand(1000))
                                        .withTimeout(3000)
                                        .andThen(IntakeCommandGroup.stopIntake()),

                                new FollowPathCommand(follower, INTAKE_1_TO_SHOOT)
                                        .andThen(new WaitCommand(1000))
                                        .withTimeout(4000)
                                        .andThen(ShootCommandGroup.twoOneShoot())
                                        .andThen(new WaitCommand(2000))
                                        .withTimeout(8000),

                                new FollowPathCommand(follower, INTAKE_15)
                                        .alongWith(IntakeCommandGroup.smartFeed())
                                        .andThen(new WaitCommand(1000))
                                        .withTimeout(4000),

                                new FollowPathCommand(follower, INTAKE_15_TO_INTAKE_2)
                                        .andThen(new WaitCommand(1000))
                                        .withTimeout(2000),

                                new FollowPathCommand(follower, INTAKE_2_TO_SHOOT)
                                        .alongWith(IntakeCommandGroup.stopIntake())
                                        .andThen(new WaitCommand(1500))
                                        .withTimeout(4000)
                                        .andThen(ShootCommandGroup.twoOneShoot())
                                        .andThen(new WaitCommand(2000))
                                        .withTimeout(7000),

                                new FollowPathCommand(follower, LEAVE)
                        )
                ).andThen(IntakeCommandGroup.stopAll()).schedule();
    }

    @Override
    public void onPlayLoop() {
        MMDrivetrain.update();
        telemetry.update();
    }

    @Override
    public void onEnd() {
        CommandScheduler.getInstance().reset();
    }
}
