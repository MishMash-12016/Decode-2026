package org.firstinspires.ftc.teamcode.Subsystems;

import Ori.Coval.Logging.AutoLog;
import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Base.MotorOrCrServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVariables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;

@Config
@AutoLog
public class AccelSubsystem extends MotorOrCrServoSubsystem {

  public static AccelSubsystem instance;

  public static synchronized AccelSubsystem getInstance() {
    if (instance == null) {
      if (MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG
          || MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION) {
        instance = new AccelSubsystemAutoLogged("AccelSubsystem");

      } else {
        instance = new AccelSubsystem("AccelSubsystem");
      }
    }
    return instance;
  }

  public AccelSubsystem(String subsystemName) {
    super(subsystemName);
    MMRobot mmRobot = MMRobot.getInstance();

    withMotor("CHPort3", Direction.REVERSE);
  }


  @Override
  public void reset() {
    instance = null;
  }
}
