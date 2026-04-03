package org.firstinspires.ftc.teamcode.OpModes.Tele.Test;

import Ori.Coval.Logging.AutoLog;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;


import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVariables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVariables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.Subsystems.Clawsubsystem;

@AutoLog
@Config
public class Clawtest extends MMOpMode {

    public Clawtest() {
        super(OpModeType.NonCompetition.DEBUG, AllianceColor.RED);
    }

    @Override
    public void onInit() {
        

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                Clawsubsystem.getInstance().Clawopen()
        );

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                Clawsubsystem.getInstance().Clawclose()
        );

    }

    @Override
    public void onPlay() {

    }

    @Override
    public void onPlayLoop() {


    }

    @Override
    public void onEnd() {

    }
}
