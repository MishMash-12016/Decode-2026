package org.firstinspires.ftc.teamcode.subsystems;

import Ori.Coval.Logging.AutoLog;
import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleDigital;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Base.MotorOrCrServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;

@Config
@AutoLog
public class IntakeSubsystem extends MotorOrCrServoSubsystem {
    static CuttleDigital sensor;

    // Singleton instance
    public static IntakeSubsystem instance;

    /**
     * Get the singleton instance of ElevatorSubsystem.
     */
    public static synchronized IntakeSubsystem getInstance() {
        if (instance == null) {
            if (MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG ||
                    MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION) {
                instance = new IntakeSubsystemAutoLogged("IntakeSubsystem");

            } else {
                instance = new IntakeSubsystem("IntakeSubsystem");
            }
        }
        return instance;
    }


    public IntakeSubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        sensor = new CuttleDigital(MMRobot.getInstance().controlHub, 4);

        withMotor(mmRobot.controlHub, 2,Direction.FORWARD);

    }


    public boolean getState() {
        return sensor.getState();
    }

    @Override
    public void resetHub() {
        instance = null;
    }
}