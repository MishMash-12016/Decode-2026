package org.firstinspires.ftc.teamcode.OpModes.Auto;

import Ori.Coval.Logging.AutoLog;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Commands.LamlamCommands;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVariables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVariables.OpModeType;
import org.firstinspires.ftc.teamcode.Libraries.pedroPathing.FollowPathCommand;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;

@AutoLog
@Autonomous
public class FarBlue extends MMOpMode {

    private final Pose startPose = new Pose(55.000, 8.000, Math.toRadians(270));
    Follower follower;
    private PathChain
            FRST_SHOOT,
            INTAKE_05,
            INTAKE_1,
            INTAKE_1_TO_SHOOT,
            INTAKE_15,
            INTAKE_15_TO_INTAKE_2,
            INTAKE_2_TO_SHOOT,
            DETECT_05,
            DETECT_1,
            DETECT_TO_SHOOT,
            INTAKE_3,
            INTAKE_3_TO_SHOOT,
            LEAVE;

    public void buildPaths() {
        FRST_SHOOT = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(55.000, 11.000),

                                new Pose(55.000, 15.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(290))

                .build();

        INTAKE_05 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(55.000, 15.000),

                                new Pose(50.000, 35.500)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(290), Math.toRadians(180))

                .build();

        INTAKE_1 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(50.000, 35.500),

                                new Pose(10.000, 35.500)
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(180))

                .build();

        INTAKE_1_TO_SHOOT = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(10.000, 35.500),

                                new Pose(55.000, 15.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(290))

                .build();

        INTAKE_15 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(55.000, 15.000),

                                new Pose(8.000, 14.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(290), Math.toRadians(190))

                .build();

        INTAKE_15_TO_INTAKE_2 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(8.000, 14.000),
                                new Pose(20.000, 8.000),
                                new Pose(8.000, 5.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(190), Math.toRadians(180))

                .build();

        INTAKE_2_TO_SHOOT = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(8.000, 5.000),

                                new Pose(55.000, 15.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(290))

                .build();

        DETECT_05 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(55.000, 15.000),

                                new Pose(32.000, 25.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(290), Math.toRadians(220))

                .build();

        DETECT_1 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(32.000, 25.000),
                                new Pose(30.000, 30.000),
                                new Pose(32.000, 39.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(220), Math.toRadians(150))

                .build();

        DETECT_TO_SHOOT = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(25.000, 30.000),
                                new Pose(55.000, 15.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(290))

                .build();

        INTAKE_3 = follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(32.000, 39.000),
                                new Pose(8.000, 35.000),
                                new Pose(7.000, 7.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(150), Math.toRadians(270))

                .build();

        INTAKE_3_TO_SHOOT = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(7.000, 7.000),

                                new Pose(55.000, 15.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(290))

                .build();

        LEAVE = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(55.000, 15.000),

                                new Pose(52.000, 19.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(290), Math.toRadians(230))

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
        MMDrivetrain.getInstance().reset();
        follower = MMDrivetrain.getInstance().getFollower();
        CommandScheduler.getInstance().reset();
        follower.setStartingPose(startPose);
        follower.setPose(startPose);
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
                                .andThen(ShootCommandGroup.smartUpShoot())
                                .andThen(new WaitCommand(800)),

                        new FollowPathCommand(follower, INTAKE_05)
                                .andThen(new WaitCommand(500))
                                .withTimeout(2000),

                        new FollowPathCommand(follower, INTAKE_1)
                                .alongWith(IntakeCommandGroup.autoFeed())
                                .andThen(new WaitCommand(500))
                                .withTimeout(2000),

                        new FollowPathCommand(follower, INTAKE_1_TO_SHOOT)
                                .andThen(new WaitCommand(500))
                                .withTimeout(2500)
                                .andThen(ShootCommandGroup.smartUpShoot())
                                .andThen(new WaitCommand(800))
                                .withTimeout(5000),

                        new FollowPathCommand(follower, INTAKE_15)
                                .alongWith(IntakeCommandGroup.autoFeed())
                                .andThen(new WaitCommand(750))
                                .withTimeout(2500),

                        new FollowPathCommand(follower, INTAKE_15_TO_INTAKE_2)
                                .andThen(new WaitCommand(500))
                                .withTimeout(1500),

                        new FollowPathCommand(follower, INTAKE_2_TO_SHOOT)
                                .andThen(new WaitCommand(500))
                                .withTimeout(3000)
                                .andThen(ShootCommandGroup.smartUpShoot())
                                .andThen(new WaitCommand(800))
                                .withTimeout(4000),

                        new ParallelRaceGroup(
                            new SequentialCommandGroup(
                                new FollowPathCommand(follower, DETECT_05),
                                new FollowPathCommand(follower, DETECT_1, 0.7)
                            ).interruptOn(LamlamCommands::isResult)
                             .andThen(
                                 new ConditionalCommand(

                                         new FollowPathCommand(follower, INTAKE_3)
                                                 .andThen(new WaitCommand(500))
                                                 .withTimeout(3000)
                                                 .andThen(new FollowPathCommand(follower, INTAKE_3_TO_SHOOT)),

                                         LamlamCommands.goToArtifact()
                                             .andThen(new FollowPathCommand(follower, DETECT_TO_SHOOT)),

                                         () -> LamlamCommands.noResult
                                 )
                             ),
                             IntakeCommandGroup.autoFeed()
                        ),
                        ShootCommandGroup.smartUpShoot(),

                        new FollowPathCommand(follower, LEAVE)
                )
        ).andThen(IntakeCommandGroup.stopAll()).schedule();
    }

    @Override
    public void onPlayLoop() {
    }

    @Override
    public void onEnd() {
    }
}