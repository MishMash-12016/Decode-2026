package org.firstinspires.ftc.teamcode.Subsystems;

import Ori.Coval.Logging.AutoLog;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.util.InterpLUT;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Servo.ServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.MMUtils;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.exterpolation.ExterpolationMap;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.RobotUtils;

@Config
@AutoLog
public class ShooterHoodSubsystem extends ServoSubsystem {

  public double hoodMax = 0.9;
  public double hoodMin = 0.15;

  InterpLUT closeInter = new InterpLUT()
          .add(68.425 ,0.1 )
          .add(70.305 ,0.15)
          .add(76.125 ,0.18)
          .add(83.873 ,0.2 )
          .add(90.074 ,0.25)
          .add(98.247 ,0.18)
          .add(106.226,0.22)
          .add(114.831,0.16)
          .add(120   ,0.157)
          .add(125   ,0.151)
          .add(131   ,0.144)
          .createLUT();
  InterpLUT farInter = new InterpLUT()
          .add(125    ,0.49 )
          .add(129.466,0.472)
          .add(136.025,0.472)
          .add(139.237,0.41 )
          .add(143.153,0.36 )
          .add(150.36 ,0.36 )
          .add(153.131,0.35 )
          .add(156.668,0.3  )
          .createLUT();

  public double getInterpulaton(double dis){
    return (dis < 128) ?
            closeInter.get(MMUtils.clamp(dis, 69,130)) :
            farInter.get(MMUtils.clamp(dis, 130,156));
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
    ///port 1
    withServo("SHPort1",Direction.FORWARD,0);
  }


  public Command aimHood() {
    return setPositionCommand(
        () -> getInterpulaton(RobotUtils.getDistanceToTarget())
    );
  }

  @Override
  public void setPosition(double position) {
    super.setPosition(MMUtils.clamp(position, hoodMin,hoodMax));
  }

  @Override
  public void resetHub() {
    instance = null;
  }
}
