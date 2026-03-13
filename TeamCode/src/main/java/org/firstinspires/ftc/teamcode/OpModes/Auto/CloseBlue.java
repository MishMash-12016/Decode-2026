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
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

@AutoLog
@Autonomous
public class CloseBlue extends MMOpMode {
    public PathChain FRST_SHOOT,
    INTAKE_05,
    INTAKE_1,
    INTAKE_1_TO_SHOOT,
    SHOOT_TO_OPEN_05,
    OPEN_05_TO_OPEN_1,
    OPEN_1_TO_OPEN_15,
    OPEN_15_TO_SHOOT,
    INTAKE_15,
    INTAKE_2,
    INTAKE_2_TO_SHOOT;

    private final Pose startPos = new Pose(20.50, 121.00, Math.toRadians(324));
    private final Pose shoot = new Pose(62.000, 76.000, Math.toRadians(310));
    Follower follower;

    public void buildPaths() {
        FRST_SHOOT =  follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(20.500, 121.000),
                                new Pose(62.000, 76.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(324), Math.toRadians(310))
                .build();

        INTAKE_05 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(62.000, 76.000),
                                new Pose(55.000, 59.500)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(310), Math.toRadians(180))
                .build();

        INTAKE_1 =  follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(55.000, 59.500),
                                new Pose(13.000, 59.500)
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        INTAKE_1_TO_SHOOT = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(13.000, 59.500),
                                new Pose(62.000, 76.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(309))
                .build();

        SHOOT_TO_OPEN_05 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(62.000, 76.000),
                                new Pose(28.000, 60.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(310), Math.toRadians(170))
                .build();

        OPEN_05_TO_OPEN_1 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(28.000, 60.000),
                                new Pose(12.500, 60.500)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(170), Math.toRadians(135))
                .build();

        OPEN_1_TO_OPEN_15 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(12.500, 60.500),
                                new Pose(10.000, 57.000)
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(135))
                .build();

        OPEN_15_TO_SHOOT = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(10.000, 57.000),
                                new Pose(62.000, 76.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(310))
                .build();

        INTAKE_15 = follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(62.000, 76.000),
                                new Pose(48.000, 84.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(310), Math.toRadians(180))
                .build();

        INTAKE_2 =  follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(48.000, 84.000),
                                new Pose(20.000, 84.000)
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        INTAKE_2_TO_SHOOT =follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(20.000, 84.000),
                                new Pose(55.000, 103.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(321))
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
                        ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(ShooterSubsystem.farSpeed),
                        ShooterHoodSubsystem.getInstance().aimHood(),
                ///                                           ↑
                        new SequentialCommandGroup(
                                new FollowPathCommand(follower, FRST_SHOOT)
                                        .andThen(new WaitCommand(1000))
                                        .withTimeout(2500)
                                        .andThen(ShootCommandGroup.upShoot())
                                        .andThen(new WaitCommand(1000)),

                                new FollowPathCommand(follower, INTAKE_05)
                                        .andThen(new WaitCommand(1000))
                                        .withTimeout(4000),

                                new FollowPathCommand(follower, INTAKE_1)
                                        .alongWith(IntakeCommandGroup.smartFeed())
                                        .andThen(new WaitCommand(1000))
                                        .withTimeout(2500)
                                        .andThen(IntakeCommandGroup.stopIntake()),

                                new FollowPathCommand(follower, INTAKE_1_TO_SHOOT)
                                        .andThen(new WaitCommand(1000))
                                        .withTimeout(4000)
                                        .andThen(ShootCommandGroup.upShoot())
                                        .andThen(new WaitCommand(2000))
                                        .withTimeout(8000),

                                new FollowPathCommand(follower, SHOOT_TO_OPEN_05)
                                        .alongWith(IntakeCommandGroup.smartFeed())
                                        .andThen(new WaitCommand(2000))
                                        .withTimeout(2500),

                                new FollowPathCommand(follower, OPEN_05_TO_OPEN_1)
                                        .andThen(new WaitCommand(1000))
                                        .withTimeout(2000),

                                new FollowPathCommand(follower, OPEN_1_TO_OPEN_15)
                                        .andThen(new WaitCommand(1000))
                                        .withTimeout(2500)
                                        .andThen(IntakeCommandGroup.stopIntake()),

                                new FollowPathCommand(follower, OPEN_15_TO_SHOOT)
                                        .andThen(new WaitCommand(1000))
                                        .withTimeout(4000)
                                        .andThen(ShootCommandGroup.upShoot())
                                        .andThen(new WaitCommand(2000))
                                        .withTimeout(8000),

                              new FollowPathCommand(follower, INTAKE_15)
                                      .andThen(new WaitCommand(1000))
                                      .withTimeout(4000),
                                new FollowPathCommand(follower, INTAKE_2)
                                        .alongWith(IntakeCommandGroup.smartFeed())
                                        .andThen(new WaitCommand(1000))
                                        .withTimeout(2500)
                                        .andThen(IntakeCommandGroup.stopIntake()),

                                new FollowPathCommand(follower, INTAKE_2_TO_SHOOT)
                                        .andThen(new WaitCommand(1000))
                                        .withTimeout(4000)
                                        .andThen(ShootCommandGroup.upShoot())
                                        .andThen(new WaitCommand(2000))
                                        .withTimeout(8000)
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

