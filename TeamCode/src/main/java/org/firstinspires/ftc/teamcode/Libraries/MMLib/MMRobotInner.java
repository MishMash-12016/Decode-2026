package org.firstinspires.ftc.teamcode.Libraries.MMLib;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import java.util.ArrayList;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleRevHub;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.MMSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVariables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;

public class MMRobotInner extends Robot {
  public MMOpMode currentOpMode;
  public CuttleRevHub controlHub;
  public CuttleRevHub expansionHub;
  public String controlHubName = "Control Hub";
  public String expansionHubName = "Expansion Hub";

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
    CommandScheduler.getInstance().reset();
    initBasics();
    initSubsystems();

    if (type == OpModeType.Competition.TELEOP) {
      initTele();
    } else if (type == OpModeType.Competition.AUTO) {
      initAuto();
    } else if (type == OpModeType.NonCompetition.DEBUG) {
      initDebug();
    }
  }

  public void setControlHubName(String controlHubName) {
    this.controlHubName = controlHubName;
  }

  public void setExpansionHubName(String expansionHubName) {
    this.expansionHubName = expansionHubName;
  }

  public void initAuto() {}

  public void initTele() {}

  public void initDebug() {}

  public void initSubsystems() {
    for (MMSubsystem subsystem : subsystems) {
      subsystem.reset();
    }
  }

  private void initBasics() {
    HardwareMap hardwareMap = MMRobot.getInstance().currentOpMode.hardwareMap;
    gamepadEx1 = new GamepadEx(currentOpMode.gamepad1);
    gamepadEx2 = new GamepadEx(currentOpMode.gamepad2);

    controlHub = new CuttleRevHub(hardwareMap, controlHubName);
    if (MMRobot.getInstance().currentOpMode.opModeType
        != OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION) {
      expansionHub = new CuttleRevHub(hardwareMap, expansionHubName);
    }
  }
}
