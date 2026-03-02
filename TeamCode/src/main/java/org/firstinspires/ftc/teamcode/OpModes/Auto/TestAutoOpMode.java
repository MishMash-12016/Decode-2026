package org.firstinspires.ftc.teamcode.OpModes.Auto;

import Ori.Coval.Logging.AutoLog;
import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.Libraries.pedroPathing.FollowPathCommand;

//@AutoLog
// @Autonomous
public class TestAutoOpMode extends MMOpMode {

  // Path chains
  private PathChain path1, path2;

  public TestAutoOpMode() {
    super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION, AllianceColor.BLUE);
  }

  public void buildPaths() {

    path1 =
        follower
            .pathBuilder()
            .addPath(new BezierLine(new Pose(56.000, 8.000), new Pose(56.000, 90.000)))
            .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(135))
            .build();
    path2 =
        follower
            .pathBuilder()
            .addPath(new BezierLine(new Pose(56.000, 90.000), new Pose(30.000, 115.000)))
            .setTangentHeadingInterpolation()
            .build();
  }

  Follower follower;

  @Override
  public void onInit() {
    //        super.reset();
    MMDrivetrain.update();
    follower.setPose(new Pose(56.000, 8.000, Math.toRadians(90)));
    buildPaths();

    SequentialCommandGroup autonomousSequence =
        new SequentialCommandGroup(
            new FollowPathCommand(follower, path1), new FollowPathCommand(follower, path2));

    autonomousSequence.schedule();
  }

  @Override
  public void onPlayLoop() {
    MMDrivetrain.update();
  }
}
