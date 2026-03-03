package org.firstinspires.ftc.teamcode.OpModes.Tele;

import Ori.Coval.Logging.Logger.KoalaLog;

import com.pedropathing.geometry.Pose;
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

//@TeleOp(name="BlueOpMode", group="CompOpMods")
//@Config
//@AutoLog
public class MainBlueOpMode extends MMOpMode {
    boolean slow = false;
    boolean Shoot = false;
    boolean aligned = false;
    Pose startPose = new Pose(135, 7, Math.toRadians(180));

    public MainBlueOpMode() {
        super(OpModeType.Competition.TELEOP, AllianceColor.BLUE);
    }

    @Override
    public void onInit() {
        GamepadEx GamepadEx1 = MMRobot.getInstance().gamepadEx1;
        GamepadEx GamepadEx2 = MMRobot.getInstance().gamepadEx2;
        /// DriveTrain
        MMDrivetrain.getInstance().setPose(startPose);
        MMDrivetrain.getInstance().enableBlueDriveDefaultCommand(() -> slow);
        GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(() -> slow = !slow);
        new Trigger(() -> gamepad1.left_trigger > 0.1).toggleWhenActive(
                MMDrivetrain.getInstance().enableBlueAligned(() -> slow));
        GamepadEx1.getGamepadButton(GamepadKeys.Button.SHARE)
                .whenPressed(() -> MMDrivetrain.getInstance().resetYaw());
        /// ↑
        //        WebcamSubsystem.getInstance();

        GamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                IntakeCommandGroup.smartFeed()).whenInactive(IntakeCommandGroup.stopIntake());
        GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .toggleWhenActive(IntakeCommandGroup.outIntake(), IntakeCommandGroup.stopIntake());

        /// Shooter
        GamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(ShooterSubsystem.getInstance().speedByLocation());
        GamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(ShooterSubsystem.getInstance().rest());
        ///   ↑

        new Trigger(() -> gamepad1.right_trigger > 0.1).toggleWhenActive(ShootCommandGroup.upShoot(), ShootCommandGroup.stopShoot());

        GamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(IntakeCommandGroup.stopAll());
    }

    @Override
    public void onInitLoop() {}

    @Override
    public void onPlay() {}

    @Override
    public void onPlayLoop() {
        telemetry.update();
        MMDrivetrain.update();
        ShooterHoodSubsystem.getInstance().aimHood().schedule();

        KoalaLog.log("",0,true);
        telemetry.addData("run: ", 2);
    }

    @Override
    public void onEnd() {
        super.onEnd();
        CommandScheduler.getInstance().reset();
    }
}
