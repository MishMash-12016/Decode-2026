package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

import java.util.function.BooleanSupplier;

import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.Logger.KoalaLog;

@TeleOp
@Config
@AutoLog
public class TestOpMode extends MMOpMode {
    boolean slow = false;
    public TestOpMode() {
        super(OpModeType.NonCompetition.DEBUG_SERVOHUB, AllianceColor.BLUE);
    }
    @Override
    public void onInit() {
        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(()-> slow);
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.SHARE).whenPressed(
                ()-> slow = !slow
        );

        MMDrivetrain.getInstance().resetYaw();
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.OPTIONS).whenPressed(
                ()->MMDrivetrain.getInstance().resetYaw());
//        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(
//                TurretSubsystem.getInstance().alignToTarget());
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).toggleWhenActive(
                IntakeCommandGroup.FeedIntake(), IntakeCommandGroup.StopIntake());
        //todo needs to be checked
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).toggleWhenActive(
                ShootCommandGroup.upShootThird(()->gamepad1.dpad_left), ShootCommandGroup.StopShoot());

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).toggleWhenActive(
                ShootCommandGroup.StartWheelClose(), ShooterSubsystem.getInstance().stopCommand());
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).toggleWhenActive(
                ShootCommandGroup.StartWheelFar(), ShooterSubsystem.getInstance().stopCommand());

        new Trigger(()-> gamepad1.right_trigger > 0.1).toggleWhenActive(
                new SequentialCommandGroup(
                        IntakeCommandGroup.OutIntake(),
                        new WaitCommand(1000),
                        IntakeCommandGroup.StopIntake()));

 /**
  * temp stuff:
  */

        MMRobot.getInstance().gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(
                ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(60));
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP).toggleWhenActive(
                ShootCommandGroup.upShoot(), ShootCommandGroup.StopShoot());
        MMRobot.getInstance().gamepadEx2.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(48));
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.B).whenPressed(ShooterSubsystem.getInstance().stopCommand());
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(IntakeCommandGroup.StopAll());

    }
    @Override
    public void onInitLoop() {
    }
    @Override
    public void onPlay() {
    }

    @Override
    public void onPlayLoop() {
        telemetry.addData("ShootSpeed: ",ShooterSubsystem.getInstance().getVelocity());

        telemetry.addData("X: ",MMDrivetrain.getInstance().getPose().getX());
        telemetry.addData("Y: ",MMDrivetrain.getInstance().getPose().getY());
        KoalaLog.log("ShootSpeed: ", ShooterSubsystem.getInstance().getVelocity(),true);

//        KoalaLog.log("DriveTrainX:", MMDrivetrain.getInstance().getPose().getX(),true);
//        KoalaLog.log("DriveTrainY:", MMDrivetrain.getInstance().getPose().getY(),true);
//        KoalaLog.log("DriveTrainHeading:", MMDrivetrain.getInstance().getPose().getHeading(),true);

    }

    @Override
    public void onEnd() {
    }
}