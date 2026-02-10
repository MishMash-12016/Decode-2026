package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Servo.ServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.exterpolation.ExterpolationMap;
import org.firstinspires.ftc.teamcode.MMRobot;

import java.util.function.DoubleSupplier;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog

public class ShooterHoodSubsystem extends ServoSubsystem {

    public static double hoodMax = 0.65;
    public static double hoodMin = 0.0;
    ExterpolationMap exterpolationMap = new ExterpolationMap()
            .put(5,5);

    //TODO: generic values
    public static double hoodUp = 0.09;
    public static double hoodDown = 0.001;


    public static double POSITION_TOLERANCE = 0.1;

    public static ShooterHoodSubsystem instance;

    public static synchronized ShooterHoodSubsystem getInstance() {
        if (instance == null) {
            if (MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG_SERVOHUB ||
                    MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG ||
                    MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION) {
                instance = new ShooterHoodSubsystemAutoLogged("ShooterHoodSubsystem");

            } else {
                instance = new ShooterHoodSubsystem("ShooterHoodSubsystem");
            }
        }
        return instance;
    }
    public ShooterHoodSubsystem(String subsystemName) {
        super(subsystemName);


        MMRobot mmRobot = MMRobot.getInstance();

        withServo(1,mmRobot.servoHub,Direction.FORWARD,0);
    }

    public Command aimHoodToShoot(DoubleSupplier distance) {
        return setPositionCommand(
                ()-> exterpolationMap.exterpolate(distance.getAsDouble()));
    }
}