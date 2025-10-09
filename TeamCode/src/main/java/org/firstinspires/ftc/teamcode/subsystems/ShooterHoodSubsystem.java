package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Servo.ServoSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog

public class ShooterHoodSubsystem extends ServoSubsystem {

    public static double hoodMax = 0.65;
    public static double hoodMin = 0.0;


    public static double POSITION_TOLERANCE = 0.1;

    public static ShooterHoodSubsystemAutoLogged instance;

    public static synchronized ShooterHoodSubsystemAutoLogged getInstance() {
        if (instance == null) {
            instance = new ShooterHoodSubsystemAutoLogged("ShooterHoodSubsystem");
        }
        return instance;
    }
    public ShooterHoodSubsystem(String subsystemName) {
        super(subsystemName);

        MMRobot mmRobot = MMRobot.getInstance();

        withServo("shooterHoodServo",Direction.FORWARD,0);
    }
}