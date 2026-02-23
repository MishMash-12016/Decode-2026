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

  public double hoodMax = 1.0;
  public double hoodMin = 0.1;
  public ExterpolationMap closeExterpolationMap =
      new ExterpolationMap().put(61.129, 0.27).put(70.529, 0.32).put(88.529, 0.315).put(98.629,0.3);
  public double getCloseInterpolation(double exter){
     return closeExterpolationMap.exterpolate(exter);
  }

public ExterpolationMap farExterpolationMap =
        new ExterpolationMap().put(131.8, 0.53).put(135.4, 0.55).put(128.7, 0.59).put(141.5, 0.52);
  public double getFarInterpolation(double exter){
    return farExterpolationMap.exterpolate(exter);
  }


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
                ? KoalaLog.log("hood/closeInterpolation",getCloseInterpolation(RobotUtils.getDistanceToTarget()),true)
                : KoalaLog.log("hood/farInterpolation",getFarInterpolation(RobotUtils.getDistanceToTarget()),true));
  }


  @Override
  public void setPosition(double position) {
    if(position < hoodMin)
      position = hoodMin;
    if(position > hoodMax)
      position = hoodMax;
    super.setPosition(position);
  }

  @Override
  public Command setPositionCommand(DoubleSupplier position) {
    return new RunCommand(() -> setPosition(position.getAsDouble()), this);
  }

//  @Override
//  public void resetHub() {
//    super.resetHub();
//    instance = null;
//  }
}
