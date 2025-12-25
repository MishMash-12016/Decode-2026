package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Servo.ServoSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog

public class FunnelStoperSubsystem extends ServoSubsystem {

    //TODO: generic values
    public static double stoperOpen = 1.0;
    

    public static double stoperClose = 0.0;

    public static double POSITION_TOLERANCE = 0.1;

    public static FunnelStoperSubsystemAutoLogged instance;

    public static synchronized FunnelStoperSubsystemAutoLogged getInstance() {
        if (instance == null) {
            instance = new FunnelStoperSubsystemAutoLogged("ShooterHoodSubsystem");
        }
        return instance;
    }
    public FunnelStoperSubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        withServo("FunnelStoperSubsystem",Direction.FORWARD,0);
    }
}