package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Servo.ServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;

import Ori.Coval.Logging.AutoLog;

@AutoLog
public class ShooterHoodSubsystem extends ServoSubsystem {
    public static double hoodUp = 1.0;
    public static double hoodDown = 0.0;
    private static ShooterHoodSubsystem instance;


    public static synchronized ShooterHoodSubsystem getInstance() {
        if (instance == null) {
            instance = new ShooterHoodSubsystemAutoLogged();
        }
        return instance;
    }
    public ShooterHoodSubsystem() {
        super("ShooterHoodSubsystem");
        withServo("ShooterHoodSubsystem", Direction.FORWARD,0.0);
    }
}
