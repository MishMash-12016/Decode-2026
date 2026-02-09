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
    ///sensors

    ///right pos
    public static double stopR = 0.05;
    public static double pushR = 0.6;
    ///mid pos
    public static double stopM = 0.65;
    public static double pushM = 0.1;
    ///left pos
    public static double stopL = 0.65;
    public static double pushL = 0.1;

    CuttleServo right;
    CuttleServo middle;
    CuttleServo left;

    public static RightStopperSubsystem instance;

    public static synchronized RightStopperSubsystem getInstance() {
        if (instance == null) {
            if (MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG_SERVOHUB ||
                    MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG ||
                    MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION) {
                instance = new RightStopperSubsystemAutoLogged("RightStopperSubsystem");

            } else {
                instance = new RightStopperSubsystem("RightStopperSubsystem");
            }
        }
        return instance;
    }

    public RightStopperSubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();


        ///right ↓
        withServo(2,mmRobot.controlHub, Direction.FORWARD,0);

        middle = new CuttleServo(mmRobot.controlHub, 1).setDirection(Direction.FORWARD);
        left = new CuttleServo(mmRobot.controlHub, 0).setDirection(Direction.FORWARD);


    }

    ///STOP

    public Command stopR(){
        return setPositionCommand(stopR);
    }

    ///PUSH
    public Command pushR(){
        return setPositionCommand(pushR);
    }

}