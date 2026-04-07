package org.firstinspires.ftc.teamcode.Subsystems;

import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.Logger.KoalaLog;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleDigital;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Base.MotorOrCrServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVariables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;

@Config
@AutoLog
public class IntakeSubsystem extends MotorOrCrServoSubsystem {
    static CuttleDigital frstSensor;
    static CuttleDigital scndSensor;

    // Singleton instance
    public static IntakeSubsystem instance;

    /**
     * Get the singleton instance of ElevatorSubsystem.
     */
    public static synchronized IntakeSubsystem getInstance() {
        if (instance == null) {
            instance = new IntakeSubsystemAutoLogged("IntakeSubsystem");
        }
        return instance;
    }


    public IntakeSubsystem(String subsystemName) {
        super(subsystemName);
        frstSensor = new CuttleDigital(MMRobot.getInstance().controlHub, 4);
        scndSensor = new CuttleDigital(MMRobot.getInstance().controlHub, 6);

        withMotor(MMRobot.getInstance().controlHub, 2, Direction.REVERSE);

    }



    public boolean getFrstState() {
        return KoalaLog.log("frstState", frstSensor.getState(),true);
    }
    public boolean getScndState() {
        return KoalaLog.log("scndState", scndSensor.getState(),true);
    }

    @Override
    public void reset() {
        instance = null;
    }
}