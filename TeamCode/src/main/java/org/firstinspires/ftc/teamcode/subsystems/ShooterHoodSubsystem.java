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
      new ExterpolationMap()
              .put(50.94, 0.21)
              .put(59.82, 0.23)
              .put(70.51, 0.26)
              .put(77.54,0.26)
              .put(87.12,0.25)
              .put(95.08,0.23)
              .put(97.38, 0.15);
  public double getCloseInterpolation(double exter){
     return closeExterpolationMap.exterpolate(exter);
  }

  public ExterpolationMap midExterpolationMap =
      new ExterpolationMap()
              .put(103.7, 0.3)
              .put(114.3, 0.28)
              .put(120.65, 0.25);
  public double getMidInterpolation(double exter){
     return midExterpolationMap.exterpolate(exter);
  }

public ExterpolationMap farExterpolationMap =
        new ExterpolationMap()
                .put(122.34, 0.44)
                .put(125.09, 0.42)
                .put(128.36, 0.43)
                .put(145.38,0.327);
  public double getFarInterpolation(double exter){
    return farExterpolationMap.exterpolate(exter);
  }


  public static ShooterHoodSubsystem instance;

  public static synchronized ShooterHoodSubsystem getInstance() {
    if (instance == null) {
      if (MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG
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

    withServo("shooterHood",Direction.FORWARD,0);
  }


  public Command aimHood() {
    return setPositionCommand(
        () ->hoodDis(RobotUtils.getDistanceToTarget())
    );
  }
  public double hoodDis(double dis){
    if(dis < 101) return getCloseInterpolation(dis);
    else if (dis < 121.5) return getCloseInterpolation(dis);
    return getFarInterpolation(dis);
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

  @Override
  public void resetHub() {
    instance = null;
  }
}
