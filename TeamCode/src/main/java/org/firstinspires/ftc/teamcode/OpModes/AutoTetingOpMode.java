package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;

import Ori.Coval.Logging.AutoLog;

@Autonomous
@AutoLog
@Config
public class AutoTetingOpMode extends MMOpMode {

    public AutoTetingOpMode() {
        super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION);
    }
    PathChain paths;

    @Override
    public void onInit() {

                paths = MMDrivetrain.getInstance().getFollower().pathBuilder().addPath(
                        // Line 1
                        new BezierLine(new Pose(56.000, 8.000), new Pose(56.000, 36.000))
                )
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))
                .build();
    }

    @Override
    public void onPlay() {

        super.onPlay();
    }

    @Override
    public void onPlayLoop() {


    }
}
