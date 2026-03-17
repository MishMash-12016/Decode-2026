package org.firstinspires.ftc.teamcode.Subsystems;

import Ori.Coval.Logging.AutoLog;
import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Servo.ServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;

@Config
@AutoLog
public class BallStopperSubsystem extends ServoSubsystem {

  public static double open = 1;
  public static double close = 0.68;

  public static BallStopperSubsystem instance;

  public static synchronized BallStopperSubsystem getInstance() {
    if (instance == null) {
      if (MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG ||
              MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION) {
        instance = new BallStopperSubsystemAutoLogged("BallStopperSubsystem" );

      } else {
        instance = new BallStopperSubsystem("BallStopperSubsystem" );
      }
    }
    return instance;
  }


  public BallStopperSubsystem(String subsystemName) {
    super(subsystemName);
    MMRobot mmRobot = MMRobot.getInstance();

    ///port 0
    withServo("SHPort0",Direction.FORWARD,0);

  }


  public Command close() {
    return setPositionCommand(close);
  }

  public Command open() {
    return setPositionCommand(open);
  }

  @Override
  public void resetHub() {
    instance = null;
  }
}
