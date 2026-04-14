package org.firstinspires.ftc.teamcode.Subsystems;

import Ori.Coval.Logging.AutoLog;
import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Servo.ServoSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;

@Config
@AutoLog
public class BallStopperSubsystem extends ServoSubsystem {

  public static double open = 1;
  public static double close = 0.68;

  public static BallStopperSubsystem instance;

  public static synchronized BallStopperSubsystem getInstance() {
    if (instance == null) {
      instance = new BallStopperSubsystemAutoLogged("BallStopperSubsystem");
    }
    return instance;
  }

  public BallStopperSubsystem(String subsystemName) {
    super(subsystemName);

    /// port 0
    withServo("ballStopper",Direction.FORWARD,0);
  }

  public Command closeCommand() {
    return setPositionCommand(close);
  }

  public Command openCommand() {
    return setPositionCommand(open);
  }

  @Override
  public void reset() {
    instance = null;
  }
}
