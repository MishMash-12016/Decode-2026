package org.firstinspires.ftc.teamcode.OpModes;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.PPLibTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.Command;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import Ori.Coval.Logging.Logger.KoalaLog;

@Autonomous
public class testop extends MMOpMode {
    Command auto;
    FakeDrive fakeDrive;
    public testop() {
        super(OpModeType.NonCompetition.DEBUG, AllianceColor.BLUE);
    }

    @Override
    public void onInit() {
        fakeDrive = new FakeDrive();
        PathPlannerPath path;
        try {
            path = PathPlannerPath.fromPathFile("path");
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        auto = AutoBuilder.followPath(path);
    }

    @Override
    public void onPlay() {
        super.onPlay();
        auto.schedule();
    }

    @Override
    public void onPlayLoop() {

    }

    @Override
    public void onEnd() {
        super.onEnd();
    }
}
