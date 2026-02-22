package org.firstinspires.ftc.teamcode.OpModes.Tele;

import Ori.Coval.Logging.AutoLog;
import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
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
import org.firstinspires.ftc.teamcode.subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

@TeleOp
@AutoLog
public class MainOpMode extends MMOpMode {
    boolean slow = false;
    boolean Shoot = false;
    Pose startPose = new Pose(8, 10, 0);

    public MainOpMode() {
        super(OpModeType.Competition.TELEOP, AllianceColor.RED);
    }

    @Override
    public void onInit() {
        GamepadEx GamepadEx1 = MMRobot.getInstance().gamepadEx1;
        GamepadEx GamepadEx2 = MMRobot.getInstance().gamepadEx2;
        /// DriveTrain
        MMDrivetrain.getInstance().setPose(startPose);
        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(() -> slow);
        new Trigger(() -> gamepad1.left_trigger > 0.1).whenActive(() -> slow = !slow);
        GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP)

                .toggleWhenActive(MMDrivetrain.getInstance().enableDriveAligned(() -> slow));
        GamepadEx1.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .whenPressed(() -> MMDrivetrain.getInstance().resetYaw());
        /// ↑

        GamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .toggleWhenActive(IntakeCommandGroup.smartFeed(), IntakeCommandGroup.stopIntake());
        GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .toggleWhenActive(IntakeCommandGroup.outIntake(), IntakeCommandGroup.stopIntake());

        /// Shooter
        GamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(ShooterSubsystem.getInstance().speedByLocation());
        GamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(ShooterSubsystem.getInstance().stopCommand());
        /// ↑

        new Trigger(() -> gamepad1.right_trigger > 0.1)
                .toggleWhenActive(ShootCommandGroup.superDumbUpShoot(), ShootCommandGroup.stopShoot());

        GamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(IntakeCommandGroup.stopAll());
    }

    @Override
    public void onPlayLoop() {
        telemetry.update();
        MMDrivetrain.update();
        ShooterHoodSubsystem.getInstance().aimHood().schedule();

        if(MMDrivetrain.getInstance().headingPid.getError() < 3
                && ShooterSubsystem.getInstance().getSetPoint() - ShooterSubsystem.getInstance().getVelocity() < 2)
            gamepad1.rumble(100);

    }

    @Override
    public void onEnd() {
        super.onEnd();
        CommandScheduler.getInstance().reset();
    }
}
