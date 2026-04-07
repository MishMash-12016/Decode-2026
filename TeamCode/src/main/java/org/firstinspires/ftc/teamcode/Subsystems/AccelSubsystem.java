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
      instance = new AccelSubsystemAutoLogged("AccelSubsystem");
    }
    return instance;
  }

  public AccelSubsystem(String subsystemName) {
    super(subsystemName);
    withMotor(MMRobot.getInstance().controlHub, 1, Direction.REVERSE);
  }


  @Override
  public void reset() {
    instance = null;
  }
}
