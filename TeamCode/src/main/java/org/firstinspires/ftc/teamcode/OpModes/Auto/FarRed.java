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
public class FarRed extends MMOpMode {

  private PathChain
          FRST_SHOOT,
          INTAKE_05,
          INTAKE_1,
          INTAKE_1_TO_SHOOT;

  private final Pose startPose = new Pose(144 - 55, 8, Math.toRadians(180 - 270));

  Follower follower;

  public void buildPaths() {
    FRST_SHOOT = follower.pathBuilder().addPath(
                    new BezierLine(
                            new Pose(144 - 55.000, 8.000),

                            new Pose(144 - 55.000, 18.000)
                    )
            ).setLinearHeadingInterpolation(Math.toRadians(180 - 270), Math.toRadians(180 - 290))
            .build();

    INTAKE_05 = follower.pathBuilder().addPath(
                    new BezierLine(
                            new Pose(144 - 55.000, 18.000),

                            new Pose(144 - 50.000, 35.000)
                    )
            ).setLinearHeadingInterpolation(Math.toRadians(180 - 290), Math.toRadians(180 - 180))
            .build();

    INTAKE_1 = follower.pathBuilder().addPath(
                    new BezierLine(
                            new Pose(144 - 50.000, 35.000),

                            new Pose(144 - 12.00, 36.00)
                    )
            ).setConstantHeadingInterpolation(Math.toRadians(180 - 180))
            .build();

    INTAKE_1_TO_SHOOT = follower.pathBuilder().addPath(
                    new BezierLine(
                            new Pose(144 - 12.00, 36.00),

                            new Pose(144 - 55.000, 18.000)
                    )
            ).setLinearHeadingInterpolation(Math.toRadians(180 - 180), Math.toRadians(180 - 290)).setBrakingStart(1.5)
            .build();
  }


  public FarRed() {
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
                                    .withTimeout(8000)
                    )
            ).andThen(IntakeCommandGroup.stopAll());
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