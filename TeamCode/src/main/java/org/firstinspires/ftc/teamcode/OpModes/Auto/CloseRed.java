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
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

@AutoLog
@Autonomous
public class CloseRed extends MMOpMode {

  private PathChain START_TO_PRE_SHOOT,
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

  private final Pose startPose = new Pose(144 - 21, 122, Math.toRadians(180 - 144));
  private final Pose shoot = new Pose(144 - 48, 95, Math.toRadians(180 - 136));
  private final Pose toFrstIntake = new Pose(144 - 48, 84);
  private final Pose frstIntake = new Pose(144 - 14, 84);
  private final Pose toScndIntake = new Pose(144 - 43, 60);
  private final Pose scndintake = new Pose(144 - 2, 60);
  private final Pose endAuto = new Pose(144 - 30, 72);

  Follower follower;

  public void buildPaths() {
    START_TO_PRE_SHOOT =
        follower
            .pathBuilder()
            .addPath(new BezierLine(startPose, shoot))
            .setLinearHeadingInterpolation(Math.toRadians(180 - 144), Math.toRadians(180 - 136))
            .build();

    PRE_SHOOT_TO_FRST_TURN =
        follower
            .pathBuilder()
            .addPath(new BezierLine(shoot, toFrstIntake))
            .setLinearHeadingInterpolation(Math.toRadians(180 - 136), Math.toRadians(0))
            .build();

    FRST_TURN_TO_FRST_INTAKE =
        follower
            .pathBuilder()
            .addPath(new BezierLine(toFrstIntake, frstIntake))
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .build();

    FRST_INTAKE_TO_SCND_SHOOT =
        follower
            .pathBuilder()
            .addPath(new BezierLine(frstIntake, shoot))
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(180 - 136))
            .build();

    // from here 3+6
    SCND_SHOOT_TO_SCND_TURN =
        follower
            .pathBuilder()
            .addPath(new BezierLine(shoot, toScndIntake))
            .setLinearHeadingInterpolation(Math.toRadians(180 - 136), Math.toRadians(0))
            .build();

    SCND_TURN_TO_SCND_INTAKE =
        follower
            .pathBuilder()
            .addPath(new BezierLine(toScndIntake, scndintake))
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .build();

    /// construction ↓
    SCND_INTAKE_TO_SCND_TURN =
        follower
            .pathBuilder()
            .addPath(new BezierLine(scndintake, toScndIntake))
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .build();

    SCND_TURN_TO_THRD_SHOOT =
        follower
            .pathBuilder()
            .addPath(new BezierLine(toScndIntake, shoot))
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(180 - 136))
            .build();
    /// construction ↑

    THRD_SHOOT_TO_END =
        follower
            .pathBuilder()
            .addPath(new BezierLine(shoot, endAuto))
            .setLinearHeadingInterpolation(Math.toRadians(180 - 136), Math.toRadians(180 - 90))
            .build();
  }

  public CloseRed() {
    super(OpModeType.Competition.AUTO, AllianceColor.RED);
  }

  @Override
  public void onInit() {}

  @Override
  public void onPlay() {
    CommandScheduler.getInstance().reset();
    MMDrivetrain.getInstance().reset();
    MMDrivetrain.getInstance().getFollower().setStartingPose(startPose);
    MMDrivetrain.getInstance().getFollower().setPose(startPose);
    follower = MMDrivetrain.getInstance().getFollower();
    buildPaths();

    /// PATH
    Command autonomousSequence =
        new SequentialCommandGroup(
            new ParallelCommandGroup(
                ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(42),
                new SequentialCommandGroup(

                    // START_TO_PRE_SHOOT:
                    new SequentialCommandGroup(
                            new FollowPathCommand(follower, START_TO_PRE_SHOOT),
                            ShootCommandGroup.ballWithControl(),
                            ShootCommandGroup.ballWithControl().withTimeout(1500),
                            ShootCommandGroup.dumbUpShoot(),
                            new WaitCommand(1000))
                        .withTimeout(6000),
                    IntakeCommandGroup.stopIntake(),

                    // PRE_SHOOT_TO_FRST_TURN:
                    new ParallelCommandGroup(
                            new FollowPathCommand(follower, PRE_SHOOT_TO_FRST_TURN),
                            IntakeCommandGroup.dumbFeed())
                        .withTimeout(2000),
                    new WaitCommand(1000),

                    // TURN_TO_FRST_INTAKE:
                    new SequentialCommandGroup(
                            new FollowPathCommand(follower, FRST_TURN_TO_FRST_INTAKE),
                            new WaitCommand(1000))
                        .withTimeout(2000),

                    // FRST_INTAKE_TO_SHOOT:
                    new SequentialCommandGroup(
                            new FollowPathCommand(follower, FRST_INTAKE_TO_SCND_SHOOT),
                            IntakeCommandGroup.stopIntake(),
                            ShootCommandGroup.ballWithControl(),
                            ShootCommandGroup.ballWithControl().withTimeout(1500),
                            ShootCommandGroup.dumbUpShoot(),
                            new WaitCommand(1000))
                        .withTimeout(6000),
                    IntakeCommandGroup.stopIntake(),

                    /// 3+6

                    // SCND_SHOOT_TO_TURN:
                    new ParallelCommandGroup(
                            new FollowPathCommand(follower, SCND_SHOOT_TO_SCND_TURN),
                            IntakeCommandGroup.dumbFeed())
                        .withTimeout(1600),

                    // TURN_TO_SCND_INTAKE:
                    new FollowPathCommand(follower, SCND_TURN_TO_SCND_INTAKE)
                        .andThen(new WaitCommand(500))
                        .withTimeout(2000),

                    // SCND_INTAKE_TO_SHOOT:
                    new SequentialCommandGroup(
                            new FollowPathCommand(follower, SCND_INTAKE_TO_SCND_TURN),
                            IntakeCommandGroup.stopIntake().withTimeout(2000),
                            new WaitCommand(500),
                            new FollowPathCommand(follower, SCND_TURN_TO_THRD_SHOOT))
                        .withTimeout(5000),
                    new WaitCommand(500),
                    ShootCommandGroup.ballWithControl(),
                    ShootCommandGroup.ballWithControl().withTimeout(1500),
                    ShootCommandGroup.dumbUpShoot().withTimeout(2000),
                    new WaitCommand(1000),
                    new FollowPathCommand(follower, THRD_SHOOT_TO_END).withTimeout(2000))),
            IntakeCommandGroup.stopAll()

            // SHOOT_TO_END:
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
