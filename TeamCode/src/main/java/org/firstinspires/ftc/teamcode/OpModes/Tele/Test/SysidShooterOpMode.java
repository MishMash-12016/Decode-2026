package org.firstinspires.ftc.teamcode.OpModes.Tele.Test;

import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import edu.wpi.first.sysid.SysIdRoutine;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;

//@TeleOp
public class SysidShooterOpMode extends MMOpMode {

  public SysidShooterOpMode() {
    super(OpModeType.NonCompetition.DEBUG, AllianceColor.BLUE);
  }

  @Override
  public void onInit() {
    GamepadEx gamepadEx1 = MMRobot.getInstance().gamepadEx1;

    gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
            .whileActiveOnce(ShooterSubsystem.getInstance()
                    .sysidQuasistatic(SysIdRoutine.Direction.kForward));

    gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
            .whileActiveOnce(ShooterSubsystem.getInstance()
                    .sysidQuasistatic(SysIdRoutine.Direction.kReverse));

    gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
            .whileActiveOnce(ShooterSubsystem.getInstance()
                    .sysidDynamic(SysIdRoutine.Direction.kForward));

    gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
            .whileActiveOnce(ShooterSubsystem.getInstance()
                    .sysidDynamic(SysIdRoutine.Direction.kReverse));
  }

  @Override
  public void onPlayLoop() {
  }
}
