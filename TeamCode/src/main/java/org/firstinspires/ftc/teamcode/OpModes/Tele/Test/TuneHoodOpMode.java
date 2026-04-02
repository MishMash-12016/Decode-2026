package org.firstinspires.ftc.teamcode.OpModes.Tele.Test;

import Ori.Coval.Logging.AutoLog;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVariables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVariables.OpModeType;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterHoodSubsystem;

//@TeleOp(name = "TuneHoodOpMode", group = "tunersOpModes")
@AutoLog
@Config
public class TuneHoodOpMode extends MMOpMode {

    public TuneHoodOpMode() {
        super(OpModeType.NonCompetition.DEBUG, AllianceColor.RED);
    }

    public static double pose = 0.2;

    @Override
    public void onInit() {
        CommandScheduler.getInstance().reset();
    }

    @Override
    public void onPlay() {
        super.onPlay();
    }

    @Override
    public void onPlayLoop() {
        telemetry.update();
        ShooterHoodSubsystem.getInstance().setPositionCommand(pose).schedule();
        telemetry.addLine("The hood is now in " + pose + " it should be aligned straight with the panels.");

    }

    @Override
    public void onEnd() {
        super.onEnd();
        CommandScheduler.getInstance().reset();
    }
}
