package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Servo.ServoSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog

public class BallStopperSubsystem extends ServoSubsystem {

    //TODO: generic values
    public static double stopperOpen = 1.0;
    public static double stopperClose = 0.0;

    public static double POSITION_TOLERANCE = 0.1;


    public static BallStopperSubsystemAutoLogged instance;

    public static synchronized BallStopperSubsystemAutoLogged getInstance() {
        if (instance == null) {
            instance = new BallStopperSubsystemAutoLogged("BallStopperSubsystem");
        }
        return instance;
    }

    public BallStopperSubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        withServo(0,mmRobot.controlHub,Direction.FORWARD,0);
        withServo(1,mmRobot.controlHub,Direction.FORWARD,0);
        withServo(2,mmRobot.controlHub,Direction.FORWARD,0);
    }
    public Command close(){
        return setPositionCommand(stopperClose);
    }
    public Command open(){
        return setPositionCommand(stopperOpen);
    }
}