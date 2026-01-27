package org.firstinspires.ftc.teamcode.OpModes.Auto;

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
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

import Ori.Coval.Logging.AutoLog;

@AutoLog
@Autonomous
public class CloseBlue extends MMOpMode {

    private PathChain
            START_TO_PRE_SHOOT,
            PRE_SHOOT_TO_FRST_TURN,
            FRST_TURN_TO_FRST_INTAKE,
            FRST_INTAKE_TO_SCND_SHOOT,
            SCND_SHOOT_TO_SCND_TURN,
            SCND_TURN_TO_SCND_INTAKE,

    ///constrction
    SCND_INTAKE_TO_SCND_TURN,
            SCND_TURN_TO_THRD_SHOOT,
    ///constrction
    THRD_SHOOT_TO_END;

    private final Pose startPose = new Pose(21, 122, Math.toRadians(144));
    private final Pose shoot = new Pose(59.5, 84, Math.toRadians(136));
    private final Pose toFrstIntake = new Pose(59.5, 84);
    private final Pose frstIntake = new Pose(14, 84);
    private final Pose toScndIntake = new Pose(43, 58);
    private final Pose scndintake = new Pose(10, 58);
    private final Pose endAuto = new Pose(30, 72);

    Follower follower;

    public void buildPaths() {
        START_TO_PRE_SHOOT = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shoot))
                .setLinearHeadingInterpolation(Math.toRadians(143), Math.toRadians(136)).build();

        PRE_SHOOT_TO_FRST_TURN = follower.pathBuilder()
                .addPath(new BezierLine(shoot, toFrstIntake))
                .setLinearHeadingInterpolation(Math.toRadians(136), Math.toRadians(180)).build();

        FRST_TURN_TO_FRST_INTAKE = follower.pathBuilder()
                .addPath(new BezierLine(toFrstIntake, frstIntake))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180)).build();

        FRST_INTAKE_TO_SCND_SHOOT = follower.pathBuilder()
                .addPath(new BezierLine(frstIntake, shoot))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(136)).build();
        //from here 3+6
        SCND_SHOOT_TO_SCND_TURN = follower.pathBuilder()
                .addPath(new BezierLine(shoot, toScndIntake))
                .setLinearHeadingInterpolation(Math.toRadians(136), Math.toRadians(180)).build();

        SCND_TURN_TO_SCND_INTAKE = follower.pathBuilder()
                .addPath(new BezierLine(toScndIntake, scndintake))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180)).build();

///construction ↓
        SCND_INTAKE_TO_SCND_TURN = follower.pathBuilder()
                .addPath(new BezierLine(scndintake, toScndIntake))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180)).build();

        SCND_TURN_TO_THRD_SHOOT = follower.pathBuilder()
                .addPath(new BezierLine(toScndIntake, shoot))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(136)).build();
///construction ↑

        THRD_SHOOT_TO_END = follower.pathBuilder()
                .addPath(new BezierLine(shoot, endAuto))
                .setLinearHeadingInterpolation(Math.toRadians(136), Math.toRadians(90)).build();
    }



    public CloseBlue() {
        super(OpModeType.Competition.AUTO, AllianceColor.BLUE);
    }

    @Override
    public void onInit() {
        CommandScheduler.getInstance().reset();
        MMDrivetrain.getInstance().reset();
        MMDrivetrain.getInstance().getFollower().setStartingPose(startPose);
        MMDrivetrain.getInstance().getFollower().setPose(startPose);
        follower = MMDrivetrain.getInstance().getFollower();
        buildPaths();

        ///PATH
        Command autonomousSequence =
                TurretSubsystem.getInstance().holdCurrentPoseCommand().alongWith(
                        new SequentialCommandGroup(
                                new ParallelCommandGroup(
                                        ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(60),
                                        new SequentialCommandGroup(

                                                //START_TO_PRE_SHOOT:
                                                new SequentialCommandGroup(
                                                        new FollowPathCommand(follower, START_TO_PRE_SHOOT),
                                                        ShootCommandGroup.SmartUpShoot(),
                                                        new WaitCommand(1000)
                                                ).withTimeout(5000),

                                                //PRE_SHOOT_TO_FRST_TURN:
                                                new ParallelCommandGroup(
                                                        new FollowPathCommand(follower, PRE_SHOOT_TO_FRST_TURN),
                                                        IntakeCommandGroup.FeedIntake()
                                                ).withTimeout(1000),
                                                new WaitCommand(500),

                                                //TURN_TO_FRST_INTAKE:
                                                new SequentialCommandGroup(
                                                        new FollowPathCommand(follower, FRST_TURN_TO_FRST_INTAKE),
                                                        new WaitCommand(500)
                                                ).withTimeout(1500),
                                                IntakeCommandGroup.StopIntake(),

                                                //FRST_INTAKE_TO_SHOOT:
                                                new SequentialCommandGroup(
                                                        new FollowPathCommand(follower, FRST_INTAKE_TO_SCND_SHOOT),
                                                        new WaitCommand(1000),
                                                        ShootCommandGroup.SmartUpShoot(),
                                                        new WaitCommand(500)
                                                ),
                                                IntakeCommandGroup.StopIntake(),

                                                /// 3+6

                                                //SHOOT_TO_TURN:
                                                new ParallelCommandGroup(
                                                        new FollowPathCommand(follower, SCND_SHOOT_TO_SCND_TURN),
                                                        IntakeCommandGroup.FeedIntake()
                                                ).withTimeout(1600),

                                                //TURN_TO_SCND_INTAKE:
                                                new SequentialCommandGroup(
                                                        new FollowPathCommand(follower, SCND_TURN_TO_SCND_INTAKE),
                                                        new WaitCommand(100)
                                                ).withTimeout(1600),
                                                IntakeCommandGroup.StopIntake(),


                                                new SequentialCommandGroup(
                                                        new FollowPathCommand(follower,SCND_INTAKE_TO_SCND_TURN),
                                                        new WaitCommand(500),
                                                        new FollowPathCommand(follower,SCND_TURN_TO_THRD_SHOOT)
                                                ),

                                                new WaitCommand(1000)
                                        )
                                ),
                                IntakeCommandGroup.StopAll(),

                                //SHOOT_TO_END:
                                new FollowPathCommand(follower, THRD_SHOOT_TO_END)
                        )
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