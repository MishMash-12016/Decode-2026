package org.firstinspires.ftc.teamcode.Subsystems;

import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.Logger.KoalaLog;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Base.MotorOrCrServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;

@Config
@AutoLog
public class IntakeSubsystem extends MotorOrCrServoSubsystem {
    static DigitalChannel frstSensor;
    static DigitalChannel scndSensor;

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
        HardwareMap hardwareMap = MMRobot.getInstance().currentOpMode.hardwareMap;

        frstSensor = hardwareMap.get(DigitalChannel.class, "CHDPort4");
        scndSensor = hardwareMap.get(DigitalChannel.class, "CHDPort6");

        withMotor("CHPort2",Direction.REVERSE);

    }



    public boolean getFrstState() {
        return KoalaLog.log("frstState", frstSensor.getState(),true);
    }
    public boolean getScndState() {
        return KoalaLog.log("scndState", scndSensor.getState(),true);
    }

    @Override
    public void resetHub() {
        instance = null;
    }
}