package org.firstinspires.ftc.teamcode.OpModes.Tele;

import Ori.Coval.Logging.AutoLog;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
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
import org.firstinspires.ftc.teamcode.subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

//@TeleOp
@AutoLog
@Config
public class ExtOpMode extends MMOpMode {

    public ExtOpMode() {
        super(OpModeType.NonCompetition.DEBUG, AllianceColor.BLUE);
    }

    Pose startPose = new Pose(135, 7, Math.toRadians(180));
    public static double hoodPose = 0.13;
    public static double shootSpeed = 0;
    boolean slow = false;


    @Override
    public void onInit() {
        CommandScheduler.getInstance().reset();
        GamepadEx GamepadEx1 = MMRobot.getInstance().gamepadEx1;
        MMDrivetrain.getInstance().setPose(startPose);
        MMDrivetrain.getInstance().enableBlueDriveDefaultCommand(() -> slow);
        GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(() -> slow = !slow);
        GamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .toggleWhenActive(MMDrivetrain.getInstance().enableBlueAligned(() -> slow));
        GamepadEx1.getGamepadButton(GamepadKeys.Button.SHARE)
                .whenPressed(() -> MMDrivetrain.getInstance().resetYaw());

        new Trigger(() -> gamepad1.right_trigger > 0.1)
                .whenActive(
                        new SequentialCommandGroup(
                                BallStopperSubsystem.getInstance().open(),
                                new ParallelCommandGroup(
                                        BallStopperSubsystem.getInstance().open(),
                                        IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                                        AccelSubsystem.getInstance().setPowerInstantCommand(1)))
                ).whenInactive(ShootCommandGroup.stopShoot());

        GamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                IntakeCommandGroup.smartFeed())
        .whenInactive(IntakeCommandGroup.stopIntake()
        );

        GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(IntakeCommandGroup.outIntake())
                .whenInactive(IntakeCommandGroup.stopIntake());
    }

    @Override
    public void onPlay() {
        super.onPlay();
    }

    @Override
    public void onPlayLoop() {
        telemetry.update();
        telemetry.addLine("");
        ///
        ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(shootSpeed).schedule();
        ShooterHoodSubsystem.getInstance().setPositionCommand(hoodPose).schedule();
    }

    @Override
    public void onEnd() {
        super.onEnd();
        CommandScheduler.getInstance().reset();
    }
}
