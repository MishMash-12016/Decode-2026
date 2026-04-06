package org.firstinspires.ftc.teamcode.OpModes.Tele.Test;

import Ori.Coval.Logging.AutoLog;
import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.CommandGroups.CheackticksCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVariables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVariables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.Subsystems.Movemotorsubsystem;

@AutoLog
@Config
public class CheackticksTele extends MMOpMode {

    public CheackticksTele() {
        super(OpModeType.NonCompetition.DEBUG, AllianceColor.RED);
    }

    @Override
    public void onInit() {
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                CheackticksCommandGroup.Runmotor()
        );
    }

    @Override
    public void onPlay() {
    }

    @Override
    public void onPlayLoop() {
        // Calculate ticks from pose (rotations * resolution)
        double ticks = Movemotorsubsystem.getInstance().getPose() * Movemotorsubsystem.RESOLUTION;
        
        // Use normal telemetry to show the ticks
        telemetry.addData("Motor Encoder Ticks", ticks);
        telemetry.update();
    }

    @Override
    public void onEnd() {
    }
}
