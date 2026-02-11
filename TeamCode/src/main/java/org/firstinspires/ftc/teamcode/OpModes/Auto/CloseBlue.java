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

    /// constrction
            SCND_INTAKE_TO_SCND_TURN,
            SCND_TURN_TO_THRD_SHOOT,
    /// constrction
            THRD_SHOOT_TO_END;

    private final Pose startPose = new Pose(21, 122, Math.toRadians(144));
    private final Pose shoot = new Pose(48, 95, Math.toRadians(136));
    private final Pose toFrstIntake = new Pose(48, 84);
    private final Pose frstIntake = new Pose(14, 84);
    private final Pose toScndIntake = new Pose(43, 60);
    private final Pose scndintake = new Pose(8, 60);
    private final Pose endAuto = new Pose(30, 72);

    Follower follower;

    public void buildPaths() {
        START_TO_PRE_SHOOT = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shoot))
                .setLinearHeadingInterpolation(Math.toRadians(144), Math.toRadians(136)).build();

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
                        new SequentialCommandGroup(
                                new ParallelCommandGroup(
                                        ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(40),
                                        new SequentialCommandGroup(

                                                //START_TO_PRE_SHOOT:
                                                new SequentialCommandGroup(
                                                        new FollowPathCommand(follower, START_TO_PRE_SHOOT),
                                                        ShootCommandGroup.ballWithControl(),
                                                        ShootCommandGroup.ballWithControl()
                                                                .withTimeout(1500),
                                                        ShootCommandGroup.dumbUpShoot(),
                                                        new WaitCommand(1000)
                                                ).withTimeout(6000),
                                                IntakeCommandGroup.stopIntake(),



                                                //PRE_SHOOT_TO_FRST_TURN:
                                                new ParallelCommandGroup(
                                                        new FollowPathCommand(follower, PRE_SHOOT_TO_FRST_TURN),
                                                        IntakeCommandGroup.dumbFeed()
                                                ).withTimeout(2000),
                                                new WaitCommand(1000),

                                                //TURN_TO_FRST_INTAKE:
                                                new SequentialCommandGroup(
                                                        new FollowPathCommand(follower, FRST_TURN_TO_FRST_INTAKE),
                                                        new WaitCommand(1000)
                                                ).withTimeout(2000),

                                                //FRST_INTAKE_TO_SHOOT:
                                                new SequentialCommandGroup(
                                                        new FollowPathCommand(follower, FRST_INTAKE_TO_SCND_SHOOT),
                                                        IntakeCommandGroup.stopIntake(),
                                                        ShootCommandGroup.ballWithControl(),
                                                        ShootCommandGroup.ballWithControl()
                                                                .withTimeout(1500),
                                                        ShootCommandGroup.dumbUpShoot(),
                                                        new WaitCommand(1000)
                                                ).withTimeout(6000),
                                                IntakeCommandGroup.stopIntake(),

                                                /// 3+6

                                                //SCND_SHOOT_TO_TURN:
                                                new ParallelCommandGroup(
                                                        new FollowPathCommand(follower, SCND_SHOOT_TO_SCND_TURN),
                                                        IntakeCommandGroup.dumbFeed()
                                                ).withTimeout(1600),

                                                //TURN_TO_SCND_INTAKE:
                                                new FollowPathCommand(follower, SCND_TURN_TO_SCND_INTAKE)
                                                        .andThen(new WaitCommand(500))
                                                        .withTimeout(2000),

                                                //SCND_INTAKE_TO_SHOOT:
                                                new SequentialCommandGroup(
                                                        new FollowPathCommand(follower, SCND_INTAKE_TO_SCND_TURN),
                                                        IntakeCommandGroup.stopIntake()
                                                        .withTimeout(2000),
                                                        new WaitCommand(500),
                                                        new FollowPathCommand(follower, SCND_TURN_TO_THRD_SHOOT)
                                                ).withTimeout(5000),
                                                new WaitCommand(500),
                                                ShootCommandGroup.ballWithControl(),
                                                ShootCommandGroup.ballWithControl()
                                                        .withTimeout(1500),
                                                ShootCommandGroup.dumbUpShoot()
                                                        .withTimeout(2000),
                                                new WaitCommand(1000),
                                                new FollowPathCommand(follower, THRD_SHOOT_TO_END)
                                                        .withTimeout(2000)

                                        )
                                ),
                                IntakeCommandGroup.stopAll()

                                //SHOOT_TO_END:
//                                new FollowPathCommand(follower, THRD_SHOOT_TO_END)
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
/*

public PathChain Path1;
public PathChain Path2;
public PathChain Path3;
public PathChain Path4;
public PathChain Path5;
public PathChain Path6;
public PathChain Path7;
public PathChain Path8;

    public Paths(Follower follower) {
      Path1 = follower.pathBuilder().addPath(
          new BezierLine(
            new Pose(21.000, 122.000),

            new Pose(48.000, 95.000)
          )
        ).setLinearHeadingInterpolation(Math.toRadians(144), Math.toRadians(136))

        .build();

Path2 = follower.pathBuilder().addPath(
          new BezierCurve(
            new Pose(48.000, 95.000),
            new Pose(47.000, 86.000),
new Pose(43.000, 83.000),
            new Pose(15.000, 84.000)
          )
        ).setConstantHeadingInterpolation(Math.toRadians(180))

        .build();

Path3 = follower.pathBuilder().addPath(
          new BezierLine(
            new Pose(15.000, 84.000),

            new Pose(48.000, 95.000)
          )
        ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(136))

        .build();

Path4 = follower.pathBuilder().addPath(
          new BezierCurve(
            new Pose(48.000, 95.000),
            new Pose(47.000, 60.000),
new Pose(47.000, 60.000),
            new Pose(15.000, 60.000)
          )
        ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

        .build();

Path5 = follower.pathBuilder().addPath(
          new BezierCurve(
            new Pose(15.000, 60.000),
            new Pose(25.000, 65.000),
            new Pose(14.000, 70.000)
          )
        ).setConstantHeadingInterpolation(Math.toRadians(180))

        .build();

Path6 = follower.pathBuilder().addPath(
          new BezierLine(
            new Pose(14.000, 70.000),

            new Pose(48.000, 95.000)
          )
        ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(136))

        .build();

Path7 = follower.pathBuilder().addPath(
          new BezierCurve(
            new Pose(48.000, 95.000),
            new Pose(47.000, 32.000),
new Pose(48.000, 36.000),
            new Pose(16.000, 36.000)
          )
        ).setConstantHeadingInterpolation(Math.toRadians(180))

        .build();

Path8 = follower.pathBuilder().addPath(
          new BezierLine(
            new Pose(16.000, 36.000),

            new Pose(48.000, 95.000)
          )
        ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(136))

        .build();
    }
  }
    */
