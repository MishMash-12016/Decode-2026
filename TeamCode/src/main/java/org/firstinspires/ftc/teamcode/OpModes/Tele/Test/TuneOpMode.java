package org.firstinspires.ftc.teamcode.OpModes.Tele.Test;

import Ori.Coval.Logging.AutoLog;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.Subsystems.AccelSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.BallStopperSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;

@TeleOp(name = "TuneOpMode", group = "tunersOpModes")
@AutoLog
@Config
public class TuneOpMode extends MMOpMode {

  public TuneOpMode() {
    super(OpModeType.NonCompetition.DEBUG, AllianceColor.BLUE);
  }

  Pose startPose = new Pose(135, 7, Math.toRadians(180));
  public static double hoodPose = 0.2;
  public static double ballStopperPose = 0.2;
  public static double shootSpeed = 0;
  public static double shootPow = 0;
  public static double intakePow = 0;
  public static double accelPow = 0;
  boolean slow = false;


  @Override
  public void onInit() {
    CommandScheduler.getInstance().reset();
    GamepadEx GamepadEx1 = MMRobot.getInstance().gamepadEx1;
    GamepadEx GamepadEx2 = MMRobot.getInstance().gamepadEx2;
    MMDrivetrain.getInstance().setPose(startPose);
    MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(() -> slow,allianceColor);
    GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(() -> slow = !slow);
    GamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
            .toggleWhenActive(MMDrivetrain.getInstance().enableDriveAligned(() -> slow,allianceColor));
    GamepadEx1.getGamepadButton(GamepadKeys.Button.SHARE)
            .whenPressed(() -> MMDrivetrain.getInstance().resetYaw(allianceColor));
  }

  @Override
  public void onPlay() {
    super.onPlay();
  }

  @Override
  public void onPlayLoop() {
    telemetry.update();
    telemetry.addLine("");
      /// Servos
      ShooterHoodSubsystem.getInstance().setPositionCommand(hoodPose).schedule();
      BallStopperSubsystem.getInstance().setPositionCommand(ballStopperPose).schedule();
      /// Motors
      IntakeSubsystem.getInstance().setPowerInstantCommand(intakePow).schedule();
      AccelSubsystem.getInstance().setPowerInstantCommand(accelPow).schedule();
      if (shootPow != 0) ShooterSubsystem.getInstance().setPowerInstantCommand(shootPow).schedule();
      else ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(shootSpeed).schedule();
  }

  @Override
  public void onEnd() {
    super.onEnd();
    CommandScheduler.getInstance().reset();
  }
}
