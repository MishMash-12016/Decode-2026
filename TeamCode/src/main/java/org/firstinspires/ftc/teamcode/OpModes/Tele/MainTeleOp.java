package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.MMUtils;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.Subsystems.PrismSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;

public class MainTeleOp extends MMOpMode {
    protected boolean slow = false;
    protected boolean aligned = false;
    protected boolean farSpeed = false;
    protected boolean a = false;
    protected Pose startPose;
    private Pose blueStartPose = new Pose(135, 8, Math.toRadians(180));

    public MainTeleOp(OpModeType opModeType, AllianceColor allianceColor) {
        super(opModeType, allianceColor);
        if (MMDrivetrain.isInstanceNull()) {
            startPose = (allianceColor == AllianceColor.BLUE) ?
                    blueStartPose : MMUtils.mirrorPedroPose(blueStartPose);
        }
    }

    @Override
    public void onInit() {
        GamepadEx GamepadEx1 = MMRobot.getInstance().gamepadEx1;
        GamepadEx GamepadEx2 = MMRobot.getInstance().gamepadEx2;
        MMDrivetrain drivetrain = MMDrivetrain.getInstance();
        ShooterSubsystem shooter = ShooterSubsystem.getInstance();
        /// happens along the teleop
        shooter.rest().schedule();
        ShooterHoodSubsystem.getInstance().aimHood().schedule();
        ///     ↑

        /// DriveTrain
        drivetrain.enableTeleopDriveDefaultCommand(() -> slow, allianceColor);
        GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(() -> slow = !slow);
        GamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(()-> aligned = !aligned);
        new Trigger(() -> aligned).whileActiveOnce(drivetrain.enableDriveAligned(() -> slow, allianceColor));
        if(opModeType != OpModeType.Competition.TELEOP)
            GamepadEx1.getGamepadButton(GamepadKeys.Button.SHARE).whenPressed(()->drivetrain.resetYaw(allianceColor));
        if (startPose != null) {
            drivetrain.getFollower().setStartingPose(startPose);
            drivetrain.setPose(startPose);
        }
        ///     ↑

        GamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(IntakeCommandGroup.smartFeed())
                .whenInactive(IntakeCommandGroup.stopIntake());


        /// Shooter
        GamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(new InstantCommand(()->a = !a));

        new Trigger(() -> a)
                .whileActiveOnce(
                    shooter.getToAndHoldSetPointCommand(ShooterSubsystem.farSpeed)
                .alongWith(
                        PrismSubsystem.getInstance().isReady()))
                .whenInactive(shooter.rest());

        new Trigger(() -> gamepad1.right_trigger > 0.1)
                .whenActive(
                        ShootCommandGroup.smartUpShoot()
                                .alongWith(new InstantCommand(() -> aligned = false))
                                .alongWith(new WaitCommand(2500).andThen(
                                        new InstantCommand(()-> a = false)))
                );
        ///     ↑


        /// Intake
        GamepadEx2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(IntakeCommandGroup.smartFeed())
                .whenInactive(IntakeCommandGroup.stopIntake());

        GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(IntakeCommandGroup.outIntake())
                .whenInactive(IntakeCommandGroup.stopIntake());
    ///     ↑

    }

    @Override
    public void onPlayLoop() {
    }

    @Override
    public void onEnd() {
        super.onEnd();
        PrismSubsystem.getInstance().off().schedule();
    }
}
