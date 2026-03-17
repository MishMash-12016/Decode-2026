package org.firstinspires.ftc.teamcode.Libraries.MMLib;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import java.util.ArrayList;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.MMSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;

public class MMRobotInner extends Robot {
  public MMOpMode currentOpMode;
  public GamepadEx gamepadEx1;
  public GamepadEx gamepadEx2;

  public ArrayList<MMSubsystem> subsystems = new ArrayList<>();

  private static MMRobot instance;

  public static synchronized MMRobot getInstance() {
    if (instance == null) {
      instance = new MMRobot();
    }
    return instance;
  }

  public MMRobotInner() {}

  /**
   * this initializes your subsystems.
   *
   * <p>if experimenting, then this does nothing.
   *
   * @param type the {@link OpModeType} chosen
   */
  public void initializeSystems(OpModeType type) {
    CommandScheduler.getInstance().cancelAll();
    initBasics();
    //        MMDrivetrain.getInstance().getFollower();
    initSubsystems();

    if (type == OpModeType.Competition.TELEOP) {
      initTele();
    } else if (type == OpModeType.Competition.AUTO) {
      initAuto();
    } else if (type == OpModeType.NonCompetition.DEBUG) {
      initDebug();
    }
  }

  public void initAuto() {}

  public void initTele() {}

  public void initDebug() {}

  public void initSubsystems() {
    for (MMSubsystem subsystem : subsystems) {
      subsystem.resetHub();
    }
  }
  private void initBasics() {
    HardwareMap hardwareMap = MMRobot.getInstance().currentOpMode.hardwareMap;
    gamepadEx1 = new GamepadEx(MMRobot.getInstance().currentOpMode.gamepad1);
    gamepadEx2 = new GamepadEx(MMRobot.getInstance().currentOpMode.gamepad2);
  }
}
