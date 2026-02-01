package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Base.MotorOrCrServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog

public class IndexerSubsystem extends MotorOrCrServoSubsystem {


    public static IndexerSubsystem instance;

    public static synchronized IndexerSubsystem getInstance() {
        if (instance == null) {
            if (MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG_SERVOHUB ||
                    MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG ||
                    MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION) {
                instance = new IndexerSubsystemAutoLogged("IndexerSubsystem");

            } else {
                instance = new IndexerSubsystem("IndexerSubsystem");
            }
        }
        return instance;
    }

    public IndexerSubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        withCrServo(mmRobot.servoHub, 2, Direction.REVERSE);
    }

}