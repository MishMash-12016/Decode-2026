package org.firstinspires.ftc.teamcode.OpModes.Tele;

import Ori.Coval.Logging.AutoLog;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleDigital;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleMotor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.WebcamSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

//@TeleOp
//@AutoLog
//@Config
public class TuneOpMode extends MMOpMode {

  public TuneOpMode() {
    super(OpModeType.NonCompetition.DEBUG, AllianceColor.BLUE);
  }

  CuttleDigital sensor;

  CuttleMotor p0, p1, p2, p3;
  CuttleMotor ep0, ep1, ep2, ep3;
  Pose startPose = new Pose(135, 7, Math.toRadians(180));

//      CRServo left;
//      MotorEx a;
  public static double pose = 0.1;
  public static double pow;
  boolean slow = false;


  @Override
  public void onInit() {
    CommandScheduler.getInstance().reset();
    GamepadEx GamepadEx1 = MMRobot.getInstance().gamepadEx1;
    GamepadEx GamepadEx2 = MMRobot.getInstance().gamepadEx2;
    /// DriveTrain
    MMDrivetrain.getInstance().setPose(startPose);
    MMDrivetrain.getInstance().enableBlueDriveDefaultCommand(() -> slow);
    GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(() -> slow = !slow);
    new Trigger(() -> gamepad1.left_trigger > 0.1).toggleWhenActive(
            MMDrivetrain.getInstance().enableBlueAligned(() -> slow));

    new Trigger(() -> gamepad1.right_trigger > 0.1).toggleWhenActive(
            ShootCommandGroup.dumbUpShoot(), ShootCommandGroup.stopShoot());


    GamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
            IntakeCommandGroup.smartFeed()).whenInactive(IntakeCommandGroup.stopIntake());
    GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
            .toggleWhenActive(IntakeCommandGroup.outIntake(), IntakeCommandGroup.stopIntake());
    GamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
            .whenPressed(IntakeCommandGroup.stopAll());
  }

  @Override
  public void onPlay() {
    super.onPlay();
  }

  @Override
  public void onPlayLoop() {
    telemetry.update();

    ShooterHoodSubsystem.getInstance().setPositionCommand(pose).schedule();
    ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(pow).schedule();

    //        telemetry.addData("pose", pose);
    //        KoalaLog.log("pose: ", pose, true);

  }

  @Override
  public void onEnd() {
    super.onEnd();
    CommandScheduler.getInstance().reset();
  }
}
