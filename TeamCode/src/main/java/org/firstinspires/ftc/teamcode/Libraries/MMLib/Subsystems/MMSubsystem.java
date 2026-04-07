package org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import org.firstinspires.ftc.teamcode.MMRobot;

public abstract class MMSubsystem extends SubsystemBase {

  public final String subsystemName;
  public MMSubsystem(String subsystemName) {
    super();
    MMRobot.getInstance().subsystems.add(this);
    this.subsystemName = subsystemName;
  }

  public void reset(){};
}
