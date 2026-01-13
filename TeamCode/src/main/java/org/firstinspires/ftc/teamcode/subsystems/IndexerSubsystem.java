package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Base.MotorOrCrServoSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog

public class IndexerSubsystem extends MotorOrCrServoSubsystem {

    //TODO: generic values

    public static IndexerSubsystemAutoLogged instance;

    public static synchronized IndexerSubsystemAutoLogged getInstance() {
        if (instance == null) {
            instance = new IndexerSubsystemAutoLogged("IndexerSubsystem");
        }
        return instance;
    }

    public IndexerSubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        //todo: change to right hub&port
        withCrServo(mmRobot.servoHub,2,Direction.REVERSE);
    }


}