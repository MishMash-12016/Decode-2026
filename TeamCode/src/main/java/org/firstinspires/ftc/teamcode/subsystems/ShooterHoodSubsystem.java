package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Servo.ServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.exterpolation.ExterpolationMap;
import org.firstinspires.ftc.teamcode.MMRobot;

import java.util.function.DoubleSupplier;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog

public class ShooterHoodSubsystem extends ServoSubsystem {

    ExterpolationMap exterpolationMap = new ExterpolationMap()
            .put(5,5);

    //TODO: generic values
    public static double hoodUp = 0.09;
    public static double hoodDown = 0.001;


    public static double POSITION_TOLERANCE = 1;

    public static ShooterHoodSubsystemAutoLogged instance;

    public static synchronized ShooterHoodSubsystemAutoLogged getInstance() {
        if (instance == null) {
            instance = new ShooterHoodSubsystemAutoLogged("ShooterHoodSubsystem");
        }
        return instance;
    }
    public ShooterHoodSubsystem(String subsystemName) {
        super(subsystemName);

        MMRobot mmRobot = MMRobot.getInstance();

        withServo("shooterHoodServo",Direction.FORWARD,0);
    }

    public Command aimHoodToShut(DoubleSupplier distance) {
        return setPositionCommand(
                ()-> exterpolationMap.exterpolate(distance.getAsDouble()));
    }
}