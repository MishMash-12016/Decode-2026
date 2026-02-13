package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.acmerobotics.dashboard.config.Config;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;


import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.subsystems.AccelSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.BallStopperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;


import Ori.Coval.Logging.AutoLog;

@TeleOp
@Config
@AutoLog
public class TestOpMode extends MMOpMode {
    boolean slow = false;
    boolean Shoot = false;
    double pow;
    Pose startPose = new Pose(9,7,0);
    public TestOpMode() {
        super(OpModeType.NonCompetition.DEBUG_SERVOHUB, AllianceColor.RED);
    }


    @Override
    public void onInit() {
        GamepadEx GamepadEx1 = MMRobot.getInstance().gamepadEx1;
        GamepadEx GamepadEx2 = MMRobot.getInstance().gamepadEx2;
        ///DriveTrain
        MMDrivetrain.getInstance().setPose(startPose);
        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(() -> slow);
        GamepadEx1.getGamepadButton(GamepadKeys.Button.SHARE).whenPressed(
                () -> slow = !slow
        );
        GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP).toggleWhenActive(
                MMDrivetrain.getInstance().enableDriveAligned(()->slow)
        );
        GamepadEx1.getGamepadButton(GamepadKeys.Button.OPTIONS).whenPressed(
                ()->MMDrivetrain.getInstance().resetYaw()
        );
        /// ↑

        GamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).toggleWhenActive(
                IntakeCommandGroup.smartFeed(), IntakeCommandGroup.stopIntake()
        );
        GamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).toggleWhenActive(
                IntakeCommandGroup.outIntake(), IntakeCommandGroup.stopIntake()
        );

        ///Shooter
        GamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                ShootCommandGroup.closeDumbSpeed()
        );GamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(65)
        );GamepadEx1.getGamepadButton(GamepadKeys.Button.B).whenPressed(
                ShooterSubsystem.getInstance().stopCommand()
        );
        /// ↑

        new Trigger(() -> gamepad1.left_trigger > 0.1).toggleWhenActive(
                ShootCommandGroup.smartUpShoot(slow),
                ShootCommandGroup.stopShoot()
        );
        new Trigger(() -> gamepad1.right_trigger > 0.1).toggleWhenActive(
                ShootCommandGroup.dumbUpShoot(), ShootCommandGroup.stopShoot()
        );
                GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                IntakeCommandGroup.stopAll()
        );

        ///testing
        GamepadEx2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                () -> pow += 0.1
        );
        GamepadEx2.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
                () -> pow -= 0.1
        );
        new Trigger(()->gamepad2.right_trigger > 0.01).whenActive(
                AccelSubsystem.getInstance().setPowerInstantCommand(0)
        );








    }

    @Override
    public void onInitLoop() {
    }
    @Override
    public void onPlay() {
    }
    @Override
    public void onPlayLoop() {
        telemetry.update();
        MMDrivetrain.update();

        telemetry.addData("ball_Sensor: ", BallStopperSubsystem.getInstance().getState());
        telemetry.addData("power: ", pow);
    }

    @Override
    public void onEnd() {
        super.onEnd();
        BallStopperSubsystem.getInstance().reset();
    }
}