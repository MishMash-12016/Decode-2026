package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.Libraries.pedroPathing.FollowPathCommand;

import Ori.Coval.Logging.AutoLog;

@AutoLog
@Autonomous
@Config
public class TestAutoOpMode extends MMOpMode {


    // Path chains
    private PathChain path1, path2;

    public TestAutoOpMode() {
        super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION);
    }

    public void buildPaths() {

        path1 = MMDrivetrain.follower.pathBuilder()
                .addPath(new BezierLine(new Pose(56.000, 8.000), new Pose(56.000, 90.000)))
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(135))
                .build();
        path2 = MMDrivetrain.follower.pathBuilder()
                .addPath(new BezierLine(new Pose(56.000, 90.000), new Pose(30.000, 115.000)))
                .setTangentHeadingInterpolation()
                .build();
    }


    @Override
    public void onInit() {
//        super.reset();
        MMDrivetrain.update();
        MMDrivetrain.follower.setPose(new Pose(56.000, 8.000, Math.toRadians(90)));
        buildPaths();

        SequentialCommandGroup autonomousSequence = new SequentialCommandGroup(
                new FollowPathCommand(MMDrivetrain.follower, path1),
                new FollowPathCommand(MMDrivetrain.follower, path2)
                );

        autonomousSequence.schedule();
    }

    @Override
    public void onPlayLoop() {
        MMDrivetrain.update();
    }


}
