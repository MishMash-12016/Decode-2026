package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelDeadlineGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.Libraries.pedroPathing.FollowPathCommand;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

import Ori.Coval.Logging.AutoLog;

@AutoLog
@Autonomous
public class CloseRed extends MMOpMode {

    private PathChain
            START_TO_PRE_SHOOT,
            PRE_SHOOT_TO_FRST_TURN,
            FRST_TURN_TO_FRST_INTAKE,
            FRST_INTAKE_TO_SCND_SHOOT,
            SCND_SHOOT_TO_SCND_TURN,
            SCND_TURN_TO_SCND_INTAKE,
            SCND_INTAKE_TO_THRD_SHOOT;

    private final Pose startPose = new Pose(123, 122, Math.toRadians(36));
    private final Pose shoot = new Pose(84.5, 84);
    private final Pose toFrstIntake = new Pose(100, 84);
    private final Pose frstIntake = new Pose(125, 84);
    private final Pose toScndIntake = new Pose(0, 0);
    private final Pose scndintake = new Pose(0, 0);


    Follower follower;

    public void buildPaths() {
        START_TO_PRE_SHOOT = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shoot))
                .setLinearHeadingInterpolation(Math.toRadians(36), Math.toRadians(43)).build();

        PRE_SHOOT_TO_FRST_TURN = follower.pathBuilder()
                .addPath(new BezierLine(shoot, toFrstIntake))
                .setLinearHeadingInterpolation(Math.toRadians(43), Math.toRadians(0)).build();

        FRST_TURN_TO_FRST_INTAKE = follower.pathBuilder()
                .addPath(new BezierLine(toFrstIntake, frstIntake))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0)).build();

        FRST_INTAKE_TO_SCND_SHOOT = follower.pathBuilder()
                .addPath(new BezierLine(frstIntake, shoot))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(43)).build();
        //scnd from here
        SCND_SHOOT_TO_SCND_TURN = follower.pathBuilder()
                .addPath(new BezierLine(shoot, toScndIntake))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0)).build();

        SCND_TURN_TO_SCND_INTAKE = follower.pathBuilder()
                .addPath(new BezierLine(toScndIntake, scndintake))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0)).build();

        SCND_INTAKE_TO_THRD_SHOOT = follower.pathBuilder()
                .addPath(new BezierLine(scndintake, shoot))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0)).build();
    }

    public CloseRed() {
        super(OpModeType.NonCompetition.DEBUG_SERVOHUB, AllianceColor.RED);
    }

    @Override
    public void onInit() {
        MMDrivetrain.getInstance().getFollower().setStartingPose(startPose);
        follower = MMDrivetrain.getInstance().getFollower();
        buildPaths();

        ParallelCommandGroup autonomousSequence =
                new ParallelCommandGroup(
//                        TurretSubsystem.getInstance().alignToTarget(),
                        TurretSubsystem.getInstance().holdCurrentPoseCommand(),
                        new SequentialCommandGroup(
                                new ParallelDeadlineGroup(
                                        new SequentialCommandGroup(
                                                new FollowPathCommand(follower, START_TO_PRE_SHOOT),
                                                ShootCommandGroup.DumbUpShoot(),
                                                new WaitCommand(4000)
                                        ).withTimeout(7000),
                                        ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(50)
                                ),
                                ShooterSubsystem.getInstance().stopCommand(),
                                new ParallelDeadlineGroup(
                                        new FollowPathCommand(follower, PRE_SHOOT_TO_FRST_TURN),
                                        IntakeCommandGroup.FeedIntake()
                                ).withTimeout(2000),
//                                new WaitCommand(2000),
                                new SequentialCommandGroup(
                                        new FollowPathCommand(follower, FRST_TURN_TO_FRST_INTAKE),
                                        new WaitCommand(100)
                                ).withTimeout(2000),
                                IntakeCommandGroup.StopIntake(),
                                new ParallelDeadlineGroup(
                                        new SequentialCommandGroup(
                                                new FollowPathCommand(follower, FRST_INTAKE_TO_SCND_SHOOT),
                                                new WaitCommand(1000),
                                                ShootCommandGroup.DumbUpShoot(),
                                                new WaitCommand(3000)
                                        ).withTimeout(6000),
                                        ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(50)
                                ),
                                IntakeCommandGroup.StopAll()/*,
//                                scnd intake:
                                new ParallelDeadlineGroup(
                                        new FollowPathCommand(follower, SCND_SHOOT_TO_SCND_TURN),
                                        IntakeCommandGroup.FeedIntake()
                                ).withTimeout(2000),
                                new SequentialCommandGroup(
                                        new FollowPathCommand(follower, SCND_TURN_TO_SCND_INTAKE),
                                        new WaitCommand(100)
                                ).withTimeout(2000),
                                IntakeCommandGroup.StopIntake(),
                                new ParallelDeadlineGroup(
                                        new SequentialCommandGroup(
                                                new FollowPathCommand(follower, SCND_INTAKE_TO_THRD_SHOOT),
                                                new WaitCommand(1000),
                                                ShootCommandGroup.DumbUpShoot(),
                                                new WaitCommand(3000)
                                        ).withTimeout(6000),
                                        ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(50)
                                ),
                                IntakeCommandGroup.StopAll()*/
                        )
                );
        autonomousSequence.schedule();
    }

    @Override
    public void onPlayLoop() {
        MMDrivetrain.update();
        telemetry.update();
    }
}

