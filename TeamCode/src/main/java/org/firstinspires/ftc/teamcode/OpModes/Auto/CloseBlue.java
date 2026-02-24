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
public class CloseBlue extends MMOpMode {

    private PathChain
            FRST_SHOOT,
            INTAKE_05,
            INTAKE_1,
            INTAKE_1_TO_SHOOT,
            SHOOT_TO_OPEN,
            OPEN_TO_SHOOT,
            INTAKE_25,
            INTAKE_3,
            INTAKE_3_TO_SHOOT,
            LEAVE;
    private final Pose startPos = new Pose(21.000, 122.000, Math.toRadians(324));
    private final Pose shoot = new Pose(62.000, 76.000, Math.toRadians(310));
    private final Pose endPos = null;
    Follower follower;

    public void buildPaths() {
        FRST_SHOOT = follower.pathBuilder().addPath(
                                new BezierLine(
                                        startPos,
                                ///       ↓
                                        shoot
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(324), Math.toRadians(310))
                        .build();

        INTAKE_05 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(62.000, 76.000),
                        ///       ↓
                                new Pose(46.000, 60.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(310), Math.toRadians(180))
                .build();

        INTAKE_1 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(46.000, 60.000),
                        ///       ↓
                                new Pose(10.000, 60.000)
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        INTAKE_1_TO_SHOOT = follower.pathBuilder().addPath(
                     new BezierLine(
                             new Pose(10.000, 60.000),
                     ///       ↓
                             shoot
                     )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(310))
                .build();

        SHOOT_TO_OPEN = follower.pathBuilder().addPath(
                        new BezierLine(
                                shoot,
                        ///       ↓
                                new Pose(10.000, 60.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(310), Math.toRadians(140))
                .build();

        OPEN_TO_SHOOT = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(10.000, 60.000),
                        ///       ↓
                                new Pose(62.000, 76.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(140), Math.toRadians(310))
                .build();

        INTAKE_25 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        shoot,
                                ///       ↓
                                        new Pose(40.000, 84.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(310), Math.toRadians(180))
                        .build();

        INTAKE_3 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(40.000, 84.000),
                                ///       ↓
                                        new Pose(15.000, 84.000)
                                )
                        ).setConstantHeadingInterpolation(Math.toRadians(180))
                        .build();

        INTAKE_3_TO_SHOOT = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(15.000, 84.000),
                                ///       ↓
                                        new Pose(55.100, 84.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(316))
                        .build();

        LEAVE = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(55.000, 84.000),
                        ///       ↓
                                new Pose(25.000, 70.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(316), Math.toRadians(90))
                .build();

    }

    public CloseBlue() {
        super(OpModeType.Competition.AUTO, AllianceColor.BLUE);
    }

    @Override
    public void onInit() {
        CommandScheduler.getInstance().reset();
        MMDrivetrain.getInstance().reset();
        MMDrivetrain.getInstance().getFollower().setStartingPose(startPos);
    }

    @Override
    public void onPlay() {
        CommandScheduler.getInstance().reset();
        MMDrivetrain.getInstance().reset();
        MMDrivetrain.getInstance().getFollower().setStartingPose(startPos);
        MMDrivetrain.getInstance().getFollower().setPose(startPos);
        follower = MMDrivetrain.getInstance().getFollower();
        buildPaths();

        ///TRAJECTORY
        Command autonomousSequence =
                ///things that will happened along the auto   ↓
                new ParallelCommandGroup(
                        ShooterSubsystem.getInstance().speedByLocation(),
                        ShooterHoodSubsystem.getInstance().aimHood(),
                ///                                           ↑
                        new SequentialCommandGroup(
                                new FollowPathCommand(follower, FRST_SHOOT)
                                        .andThen(new WaitCommand(0))
                                        .andThen(ShootCommandGroup.twoOneShoot())
                                        .andThen(new WaitCommand(0)),

                                new FollowPathCommand(follower, INTAKE_05)
                                        .andThen(IntakeCommandGroup.smartFeed()),

                                new FollowPathCommand(follower, INTAKE_1)
                                        .andThen(IntakeCommandGroup.stopIntake()),

                                new FollowPathCommand(follower, INTAKE_1_TO_SHOOT)
                                        .andThen(new WaitCommand(0))
                                        .andThen(ShootCommandGroup.twoOneShoot())
                                        .andThen(new WaitCommand(0)),

                                new FollowPathCommand(follower, SHOOT_TO_OPEN)
                                        .alongWith(IntakeCommandGroup.smartFeed())
                                        .andThen(new WaitCommand(0))
                                        .andThen(IntakeCommandGroup.stopIntake()),

                                new FollowPathCommand(follower, OPEN_TO_SHOOT)
                                        .andThen(new WaitCommand(0))
                                        .andThen(ShootCommandGroup.twoOneShoot())
                                        .andThen(new WaitCommand(0)),

                                new FollowPathCommand(follower, INTAKE_25)
                                        .alongWith(IntakeCommandGroup.smartFeed()),

                                new FollowPathCommand(follower, INTAKE_3)
                                        .andThen(IntakeCommandGroup.stopIntake()),

                                new FollowPathCommand(follower, INTAKE_3_TO_SHOOT)
                                        .andThen(new WaitCommand(0))
                                        .andThen(ShootCommandGroup.twoOneShoot())
                                        .andThen(new WaitCommand(0))
                        )
                ).andThen(
                    IntakeCommandGroup.stopAll()
                );


        autonomousSequence.schedule();
    }

    @Override
    public void onPlayLoop() {
        MMDrivetrain.update();
        telemetry.update();
    }

    @Override
    public void onEnd() {
        super.onEnd();
        CommandScheduler.getInstance().reset();
    }
}

