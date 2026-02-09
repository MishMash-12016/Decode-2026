package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleServo;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Servo.ServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog

public class LeftStopperSubsystem extends ServoSubsystem {
    ///sensor
    public static double stop = 0.7;
    public static double push = 0.2;

    public static LeftStopperSubsystem instance;

    public static synchronized LeftStopperSubsystem getInstance() {
        if (instance == null) {
            if (MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG_SERVOHUB ||
                    MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG ||
                    MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION) {
                instance = new LeftStopperSubsystemAutoLogged("LeftStopperSubsystem");

            } else {
                instance = new LeftStopperSubsystem("LeftStopperSubsystem");
            }
        }
        return instance;
    }

    public LeftStopperSubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        withServo(0,mmRobot.controlHub, Direction.FORWARD,0);
    }

    public Command stop(){
        return setPositionCommand(stop);
    }

    public Command push(){
        return setPositionCommand(push);
    }

}
