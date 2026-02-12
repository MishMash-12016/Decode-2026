package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleDigital;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Servo.ServoSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog

public class BallStopperSubsystem extends ServoSubsystem {

    static CuttleDigital sensor;

    public static double open = 0.8;
    public static double close = 0.55;

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

        sensor = new CuttleDigital(MMRobot.getInstance().controlHub, 6);

        withServo(0,mmRobot.servoHub,Direction.FORWARD,0);
    }

    public boolean getState(){
        return sensor.getState();
    }
    public Command close(){
        return setPositionCommand(close);
    }
    public Command open(){
        return setPositionCommand(open);
    }

    public void reset(){
        instance = null;
    }
}