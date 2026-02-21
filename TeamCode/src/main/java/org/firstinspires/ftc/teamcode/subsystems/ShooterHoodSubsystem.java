package org.firstinspires.ftc.teamcode.subsystems;

import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.Logger.KoalaLog;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Servo.ServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.exterpolation.ExterpolationMap;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.RobotUtils;

import java.util.function.DoubleSupplier;

@Config
@AutoLog
public class ShooterHoodSubsystem extends ServoSubsystem {

  public static double hoodMax = 1.0;
  public static double hoodMin = 0.05;
  ExterpolationMap closeExterpolationMap =
      new ExterpolationMap().put(63.02, 0.155).put(89.4, 0.21).put(105.8, 0.18);
/*  public double getCloseInterpolation(double exter){
    if(closeExterpolationMap.exterpolate(exter) > 1)
      return 1.0;
    if(closeExterpolationMap.exterpolate(exter) < 0.05)
      return 0.05;
    return closeExterpolationMap.exterpolate(exter);
  }*/

  ExterpolationMap farExterpolationMap = new ExterpolationMap().put(134.38, 0.68);
/*
  public double getFarInterpolation(double exter){
    if(farExterpolationMap.exterpolate(exter) > 1)
      return 1.0;
    if(farExterpolationMap.exterpolate(exter) < 0)
      return 0.0;
    return farExterpolationMap.exterpolate(exter);
  }
*/


  public static ShooterHoodSubsystem instance;

  public static synchronized ShooterHoodSubsystem getInstance() {
    if (instance == null) {
      if (MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG_SERVOHUB
          || MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG
          || MMRobot.getInstance().currentOpMode.opModeType
              == OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION) {
        instance = new ShooterHoodSubsystemAutoLogged("ShooterHoodSubsystem");

      } else {
        instance = new ShooterHoodSubsystem("ShooterHoodSubsystem");
      }
    }
    return instance;
  }

  public ShooterHoodSubsystem(String subsystemName) {
    super(subsystemName);
//    setDefaultCommand(aimHood());

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

/*  public Command aimHood() {
    return setPositionCommand(
        () ->
            RobotUtils.getDistanceToTarget() < 110
                ? closeExterpolationMap.exterpolate(RobotUtils.getDistanceToTarget())
                : farExterpolationMap.exterpolate(RobotUtils.getDistanceToTarget()));
  }*/

  public Command aimHood() {
    return setPositionCommand(
        () ->
            RobotUtils.getDistanceToTarget() < 110
                ? KoalaLog.log("shooter/closeInterpolation",closeExterpolationMap.exterpolate(RobotUtils.getDistanceToTarget()),true)
                : KoalaLog.log("shooter/farInterpolation",farExterpolationMap.exterpolate(RobotUtils.getDistanceToTarget()),true));
  }


  @Override
  public void setPosition(double position) {
    if(position < 0.05)
      position = 0.05;
    if(position > 1.0)
      position = 1.0;
    super.setPosition(position);
  }

  @Override
  public Command setPositionCommand(DoubleSupplier position) {
    return new RunCommand(() -> setPosition(position.getAsDouble()), this);
  }

  @Override
  public void resetHub() {
    super.resetHub();
    instance = null;
  }
}
