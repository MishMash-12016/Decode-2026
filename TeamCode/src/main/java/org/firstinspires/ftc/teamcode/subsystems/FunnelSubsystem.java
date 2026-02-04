package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Base.MotorOrCrServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog
public class FunnelSubsystem extends MotorOrCrServoSubsystem {

    public static FunnelSubsystem instance;

    public static synchronized FunnelSubsystem getInstance() {
        if (instance == null) {
            if (MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG_SERVOHUB ||
                    MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG ||
                    MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION) {
                instance = new FunnelSubsystemAutoLogged("FunnelSubsystem");

            } else {
                instance = new FunnelSubsystem("FunnelSubsystem");
            }
        }
        return instance;
    }

    public FunnelSubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        withMotor(mmRobot.expansionHub,3, Direction.REVERSE);
        withMotor(mmRobot.expansionHub,4, Direction.REVERSE);

//        withZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}