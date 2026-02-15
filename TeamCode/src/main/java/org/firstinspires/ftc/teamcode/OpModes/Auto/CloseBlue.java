package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.Libraries.pedroPathing.FollowPathCommand;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

import Ori.Coval.Logging.AutoLog;

@AutoLog
@Autonomous
public class CloseBlue extends MMOpMode {

    private PathChain
            FRST_SHOOT,
            INTAKE_1,
            INTAKE_15,
            INTAKE_15_TO_SHOOT,
            SHOOT_TO_OPEN,
            OPEN_INTAKE_2,
            INTAKE_2_TO_SHOOT,
            INTAKE25,
            INTAKE3,
            INTAKE_3_TO_SHOOT;
    private final Pose startPos = new Pose(21.000, 122.000,Math.toRadians(324));
    private final Pose shoot = new Pose(62.000, 76.000,Math.toRadians(310));
    private final Pose endPos = null;
    Follower follower;

    public void buildPaths() {
        FRST_SHOOT =
                follower.pathBuilder().addPath(
                        new BezierLine(
                                startPos,
                        ///       ↓
                                shoot
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(324), Math.toRadians(310))
                .build();

        INTAKE_1 =
                follower.pathBuilder().addPath(
                        new BezierCurve(
                                shoot,
                                new Pose(54.000, 60.000),
                                new Pose(36.000, 60.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(310), Math.toRadians(180))
                .build();

        INTAKE_15 =
                follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(36.000, 60.000),
                        ///       ↓
                                new Pose(10.000, 60.000)
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        INTAKE_15_TO_SHOOT =
                follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(10.000, 60.000),
                        ///       ↓
                                shoot
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(310))
                .build();

        SHOOT_TO_OPEN =
                follower.pathBuilder().addPath(
                        new BezierLine(
                                shoot,
                        ///       ↓
                                new Pose(15.000, 66.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(310), Math.toRadians(180))
                .build();

        OPEN_INTAKE_2 =
                follower.pathBuilder().addPath(
                        new BezierCurve(
                                new Pose(15.000, 66.000),
                                new Pose(13.700, 60.000),
                                new Pose(9.000, 60.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(120))
                .build();

        INTAKE_2_TO_SHOOT =
                follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(9.000, 60.000),
                        ///       ↓
                                shoot
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(120), Math.toRadians(310))
                .build();

        INTAKE25 =
                follower.pathBuilder().addPath(
                        new BezierLine(
                                shoot,
                        ///       ↓
                                new Pose(40.000, 84.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(310), Math.toRadians(180))
                .build();

        INTAKE3 =
                follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(40.000, 84.000),
                        ///       ↓
                                new Pose(15.000, 84.000)
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        INTAKE_3_TO_SHOOT =
                follower.pathBuilder().addPath(
                        new BezierLine(
                                new Pose(15.000, 84.000),
                        ///       ↓
                                new Pose(55.100, 84.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(316))
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
                ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(40).alongWith(
                        new SequentialCommandGroup(
                                new FollowPathCommand(follower, FRST_SHOOT),
                                new FollowPathCommand(follower, INTAKE_1),
                                new FollowPathCommand(follower, INTAKE_15),
                                new FollowPathCommand(follower, INTAKE_15_TO_SHOOT),
                                new FollowPathCommand(follower, SHOOT_TO_OPEN),
                                new FollowPathCommand(follower, OPEN_INTAKE_2),
                                new FollowPathCommand(follower, INTAKE_2_TO_SHOOT),
                                new FollowPathCommand(follower, INTAKE25),
                                new FollowPathCommand(follower, INTAKE3),
                                new FollowPathCommand(follower, INTAKE_3_TO_SHOOT)
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
        telemetry.addData("Shooter: ", ShooterSubsystem.getInstance().getVelocity());
    }

    @Override
    public void onEnd() {
        super.onEnd();
        CommandScheduler.getInstance().reset();
    }
}

