package org.firstinspires.ftc.teamcode.TeleOp;

import Ori.Coval.Logging.AutoLog;
import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.WaitCommand;
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
import org.firstinspires.ftc.teamcode.subsystems.PrismSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

//@TeleOp
@Config
@AutoLog
public class LamlamOpMode extends MMOpMode {
    boolean slow = false;
    boolean alignd = false;
    boolean inShootMode = false;
    boolean a = false;
    Pose startPose = new Pose(135, 7, Math.toRadians(180));

    public LamlamOpMode() {
        super(OpModeType.NonCompetition.DEBUG, AllianceColor.BLUE);
    }

    @Override
    public void onInit() {
        GamepadEx GamepadEx1 = MMRobot.getInstance().gamepadEx1;
        GamepadEx GamepadEx2 = MMRobot.getInstance().gamepadEx2;
        ShooterSubsystem.getInstance().rest().schedule();
        ShooterHoodSubsystem.getInstance().aimHood().schedule();
        /// DriveTrain
        MMDrivetrain.getInstance().enableBlueDriveDefaultCommand(() -> slow);
        MMDrivetrain.getInstance().setPose(startPose);
        GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(() -> slow = !slow);
        GamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(()-> alignd = !alignd);
        GamepadEx1.getGamepadButton(GamepadKeys.Button.SHARE)
                .whenPressed(() -> MMDrivetrain.getInstance().resetYaw());
        /// ↑

        GamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                        IntakeCommandGroup.smartFeed()
                                .alongWith(new InstantCommand(() -> inShootMode = false)))
                .whenInactive(IntakeCommandGroup.stopIntake()
                        .alongWith(new InstantCommand(() -> inShootMode = true)));

        GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(IntakeCommandGroup.outIntake())
                .whenInactive(IntakeCommandGroup.stopIntake());

        /// Shooter
        GamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(new InstantCommand(()->a = !a)
                        .alongWith(new InstantCommand(() -> inShootMode = true)));
        ///   ↑

        new Trigger(() -> gamepad1.right_trigger > 0.1)
                .whenActive(
                        ShootCommandGroup.upShoot()
                                .alongWith(new InstantCommand(() -> inShootMode = false))
                                .alongWith(new InstantCommand(() -> alignd = false))
                                .alongWith(new WaitCommand(2000)
                                        .andThen(new InstantCommand(()->a = false))
                                )
                );

        new Trigger(() -> alignd).whileActiveOnce(
                MMDrivetrain.getInstance().enableBlueAligned(() -> slow)
        );

        new Trigger(() -> a).whileActiveOnce(
                ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(ShooterSubsystem.farSpeed)
        ).whenInactive(ShooterSubsystem.getInstance().rest());

        new Trigger(() -> inShootMode).whileActiveOnce(
                ShooterSubsystem.getInstance().inSpeed()
        );
    }

    @Override
    public void onInitLoop() {}

    @Override
    public void onPlay() {}

    @Override
    public void onPlayLoop() {
        telemetry.update();
        MMDrivetrain.update();

        telemetry.addData("", inShootMode);
//    if(gamepad2.a)throw new NullPointerException("im pointing to your moms ass");
    }

    @Override
    public void onEnd() {
        super.onEnd();
        PrismSubsystem.getInstance().off().schedule();
        CommandScheduler.getInstance().reset();
    }
}
