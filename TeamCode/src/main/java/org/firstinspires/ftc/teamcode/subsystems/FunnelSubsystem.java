package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Base.MotorOrCrServoSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog
public class FunnelSubsystem extends MotorOrCrServoSubsystem {

    //touch sensor ↓


    //TODO: not tuned

    public static double VelTol = 5;
    public static double RESOLUTION = 28;


    public static FunnelSubsystemAutoLogged instance;

    public static synchronized FunnelSubsystemAutoLogged getInstance() {
        if (instance == null) {
            instance = new FunnelSubsystemAutoLogged("FunnelSubsystem");
        }
        return instance;
    }

    public FunnelSubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        //TODO: motor type? port?
        withCrServo(mmRobot.expansionHub,0, Direction.FORWARD);
        withCrServo(mmRobot.expansionHub,0, Direction.FORWARD);

        withZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public boolean (){
        return touchSensor.getState();
    }
}

