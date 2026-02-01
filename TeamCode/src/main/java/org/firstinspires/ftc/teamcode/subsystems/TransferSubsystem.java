package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Base.MotorOrCrServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog
public class TransferSubsystem extends MotorOrCrServoSubsystem {

//    //beam sensor ↓
//    CuttleDigital beamSensor =  new CuttleDigital(MMRobot.getInstance().controlHub, 0);

    public static TransferSubsystem instance;

    public static synchronized TransferSubsystem getInstance() {
        if (instance == null) {
            if (MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG_SERVOHUB ||
                    MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG ||
                    MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION) {
                instance = new TransferSubsystemAutoLogged("TransferSubsystem");

            } else {
                instance = new TransferSubsystem("TransferSubsystem");
            }
        }
        return instance;
    }

    public TransferSubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        withMotor(mmRobot.expansionHub,2, Direction.FORWARD);

        withZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

//    public boolean getBeamSensor(){
//        return beamSensor.getState();
//    }
}

