package org.firstinspires.ftc.teamcode.OpModes.Auto;

import Ori.Coval.Logging.AutoLog;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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
public class FarRed extends MMOpMode {

    private final Pose startPose = new Pose(89.000, 8.000, Math.toRadians(-90));
    Follower follower;
    private PathChain
            FRST_SHOOT,
            INTAKE_05,
            INTAKE_1,
            INTAKE_1_TO_SHOOT,
            INTAKE_15,
            INTAKE_15_TO_INTAKE_2,
            INTAKE_2_TO_SHOOT,
            INTAKE_25,
            INTAKE_3,
            INTAKE_3_TO_SHOOT,
            LEAVE;

    public void buildPaths() {
        FRST_SHOOT = follower.pathBuilder().addPath(
                        new BezierLine(
                                startPose,

                                new Pose(89.000, 15.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-90), Math.toRadians(-109))
                .build();

        INTAKE_05 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(89.000, 15.000),

                                new Pose(94.000, 35.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-109), Math.toRadians(0))
                .build();

        INTAKE_1 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(94.000, 35.000),

                                new Pose(134.000, 35.000)
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        INTAKE_1_TO_SHOOT = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(134.000, 35.000),

                                new Pose(89.000, 15.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-111))
                .build();

        INTAKE_15 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(89.000, 15.000),

                                new Pose(136.000, 13.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-111), Math.toRadians(-10))
                .build();

        INTAKE_15_TO_INTAKE_2 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(136.000, 13.000),
                                new Pose(124.000, 8.000),
                                new Pose(136.000, 5.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-10), Math.toRadians(0))
                .build();

        INTAKE_2_TO_SHOOT = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(136.000, 5.000),

                                new Pose(89.000, 15.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-111))
                .build();

        INTAKE_25 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(89.000, 15.000),

                                new Pose(136.000, 35.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-111), Math.toRadians(60))
                .build();

        INTAKE_3 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(136.000, 35.000),
                                new Pose(114.000, 25.000),
                                new Pose(136.000, 20.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(60), Math.toRadians(10))
                .build();

        INTAKE_3_TO_SHOOT = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(136.000, 20.000),
                                new Pose(89.000, 15.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(10), Math.toRadians(-111))

                .build();

        LEAVE = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(89.000, 15.000),
                                new Pose(92.000, 19.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-111), Math.toRadians(-50))
                .build();
    }

    public FarRed() {
        super(OpModeType.Competition.AUTO, AllianceColor.RED);
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
                ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(53),
                ShooterHoodSubsystem.getInstance().aimHood(),
                ///                                   ↑
                new SequentialCommandGroup(
                        new FollowPathCommand(follower, FRST_SHOOT)
                                .andThen(new WaitCommand(2000))
                                .withTimeout(3500)
                                .andThen(ShootCommandGroup.upShoot())
                                .andThen(new WaitCommand(800)),

                        new FollowPathCommand(follower, INTAKE_05)
                                .andThen(new WaitCommand(500))
                                .withTimeout(2000),

                        new FollowPathCommand(follower, INTAKE_1)
                                .alongWith(IntakeCommandGroup.smartFeed())
                                .andThen(new WaitCommand(500))
                                .withTimeout(2000),

                        new FollowPathCommand(follower, INTAKE_1_TO_SHOOT)
                                .andThen(new WaitCommand(500))
                                .withTimeout(2500)
                                .andThen(ShootCommandGroup.upShoot())
                                .andThen(new WaitCommand(800))
                                .withTimeout(5000),

                        new FollowPathCommand(follower, INTAKE_15)
                                .alongWith(IntakeCommandGroup.smartFeed())
                                .andThen(new WaitCommand(750))
                                .withTimeout(2500),

                        new FollowPathCommand(follower, INTAKE_15_TO_INTAKE_2)
                                .andThen(new WaitCommand(500))
                                .withTimeout(1500),

                        new FollowPathCommand(follower, INTAKE_2_TO_SHOOT)
                                .andThen(new WaitCommand(500))
                                .withTimeout(3000)
                                .andThen(ShootCommandGroup.upShoot())
                                .andThen(new WaitCommand(800))
                                .withTimeout(5000),

                        new FollowPathCommand(follower, INTAKE_25)
                                .alongWith(IntakeCommandGroup.smartFeed())
                                .andThen(new WaitCommand(500))
                                .withTimeout(2000),

                        new FollowPathCommand(follower, INTAKE_3)
                                .andThen(new WaitCommand(500))
                                .withTimeout(2000),


                        new FollowPathCommand(follower, INTAKE_3_TO_SHOOT)
                                .andThen(new WaitCommand(500))
                                .withTimeout(2500)
                                .andThen(ShootCommandGroup.upShoot())
                                .andThen(new WaitCommand(800))
                                .withTimeout(5000),

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