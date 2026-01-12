package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Base.MotorOrCrServoSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog
public class TransferSubsystem extends MotorOrCrServoSubsystem {

//    //beam sensor ↓
//    CuttleDigital beamSensor =  new CuttleDigital(MMRobot.getInstance().controlHub, 0);

    public static TransferSubsystemAutoLogged instance;

    public static synchronized TransferSubsystemAutoLogged getInstance() {
        if (instance == null) {
            instance = new TransferSubsystemAutoLogged("TransferSubsystem");
        }
        return instance;
    }

    public TransferSubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        //todo: change to right hub&port
        withMotor(mmRobot.expansionHub,3, Direction.FORWARD);

        withZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

//    public boolean getBeamSensor(){
//        return beamSensor.getState();
//    }
}

