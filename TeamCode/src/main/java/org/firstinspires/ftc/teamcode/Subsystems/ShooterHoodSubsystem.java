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

  ExterpolationMap closeExter = new ExterpolationMap()
              .put(62.39,0.29)
              .put(68.06,0.26)
              .put(74.99,0.55);

  ExterpolationMap midExter = new ExterpolationMap()
              .put(75.469,0.395)
              .put(83.5,0.4    )
              .put(92.87,0.398 )
              .put(99.84,0.38  )
              .put(112.19,0.34 )
              .put(117.7,0.26  );

  ExterpolationMap farExter = new ExterpolationMap()
              .put(129.08,0.5  )
              .put(144.21,0.425)
              .put(148.71,0.4  );

  InterpLUT farInter = new InterpLUT()
          .add(129.08,0.5  )
          .add(144.21,0.425)
          .add(148.71,0.4  )
          .createLUT();

  public double getInterpulaton(double dis){
    if(dis < 75) return closeExter.exterpolate(dis);
    if (dis < 120) return midExter.exterpolate(dis);
    return MMUtils.isInRange(dis,130, 148)?
            farInter.get(dis) : farExter.exterpolate(dis);
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
