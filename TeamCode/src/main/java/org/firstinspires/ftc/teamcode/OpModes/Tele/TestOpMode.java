package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
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
import org.firstinspires.ftc.teamcode.subsystems.IndexerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TransferSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

import Ori.Coval.Logging.AutoLog;
@TeleOp
@Config
@AutoLog
public class TestOpMode extends MMOpMode {
    int c = 0;
    public TestOpMode() {
        super(OpModeType.NonCompetition.DEBUG_SERVOHUB, AllianceColor.BLUE);
    }
    @Override
    public void onInit() {
        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(()-> gamepad1.left_stick_button);
        MMDrivetrain.getInstance().resetYaw();

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                TurretSubsystem.getInstance().alignToTarget()
        );

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).toggleWhenActive(
                IntakeCommandGroup.FeedIntake(),
                IntakeCommandGroup.StopIntake());

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).toggleWhenActive(
                ShootCommandGroup.PrepShoot(),
                ShootCommandGroup.StopShoot());

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).toggleWhenActive(
                ShootCommandGroup.StartWheel(),
                ShooterSubsystem.getInstance().stopCommand());


        new Trigger(()-> gamepad1.right_trigger > 0.01).whenActive(
                new SequentialCommandGroup(
                        IntakeCommandGroup.OutIntake(),
                        new WaitCommand(2000),
                        IntakeCommandGroup.StopIntake()));

    }
    @Override
    public void onInitLoop() {
    }

    @Override
    public void onPlay() {


    }

    @Override
    public void onPlayLoop() {
        telemetry.addData("able Shoot: ",46 < ShooterSubsystem.getInstance().getVelocity());
        telemetry.addData("ShootSpeed: ",ShooterSubsystem.getInstance().getVelocity());

    }

    @Override
    public void onEnd() {
    }
}