package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ConditionalCommand;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Servo.ServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.exterpolation.ExterpolationMap;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.RobotUtils;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog

public class ShooterHoodSubsystem extends ServoSubsystem {

    public static double hoodMax = 1.0;
    public static double hoodMin = 0.05;
    ExterpolationMap closeExterpolationMap = new ExterpolationMap()
            .put(63.02, 0.155)
            .put(89.4, 0.21)
            .put(105.8, 0.18);
    ExterpolationMap farExterpolationMap = new ExterpolationMap()
            .put(134.38, 0.68);

    public static ShooterHoodSubsystem instance;

    public static synchronized ShooterHoodSubsystem getInstance() {
        if (instance == null) {
            if (MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG_SERVOHUB ||
                    MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG ||
                    MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION) {
                instance = new ShooterHoodSubsystemAutoLogged("ShooterHoodSubsystem" );

            } else {
                instance = new ShooterHoodSubsystem("ShooterHoodSubsystem" );
            }
        }
        return instance;
    }

    public ShooterHoodSubsystem(String subsystemName) {
        super(subsystemName);

        MMRobot mmRobot = MMRobot.getInstance();

        withServo(1, mmRobot.servoHub, Direction.FORWARD, 0);
    }

    public Command aimHoodToShootClose() {
        return setPositionCommand(
                () -> closeExterpolationMap.exterpolate(RobotUtils.getDistanceToTarget()));
    }

    public Command aimHoodToShootFar() {
        return setPositionCommand(
                () -> farExterpolationMap.exterpolate(RobotUtils.getDistanceToTarget()));
    }
    
    public Command aimHood() {
      return setPositionCommand(
          () ->
              RobotUtils.getDistanceToTarget() > 110
                  ? closeExterpolationMap.exterpolate(RobotUtils.getDistanceToTarget())
                  : farExterpolationMap.exterpolate(RobotUtils.getDistanceToTarget()));
    }
}