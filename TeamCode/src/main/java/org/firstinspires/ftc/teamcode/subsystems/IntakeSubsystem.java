package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Base.MotorOrCrServoSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog
public class IntakeSubsystem extends MotorOrCrServoSubsystem {

    public static IntakeSubsystemAutoLogged instance;

    public static synchronized IntakeSubsystemAutoLogged getInstance() {
        if (instance == null) {
            instance = new IntakeSubsystemAutoLogged("IntakeSubsystem");
        }
        return instance;
    }

    public IntakeSubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        //todo: change to right hub&port
        withMotor(mmRobot.expansionHub,2, Direction.REVERSE);

//        withZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}