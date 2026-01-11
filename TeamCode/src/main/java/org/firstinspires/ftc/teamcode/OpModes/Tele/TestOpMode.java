package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.subsystems.IndexerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TransferSubsystem;

import Ori.Coval.Logging.AutoLog;
@TeleOp
@Config
@AutoLog
public class TestOpMode extends MMOpMode {

    public TestOpMode() {
        super(OpModeType.NonCompetition.DEBUG, AllianceColor.BLUE);
    }
    @Override
    public void onInit() {
        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(()->false);
        MMDrivetrain.update();

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).toggleWhenActive(
                IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                IntakeSubsystem.getInstance().stopCommand());

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.B).toggleWhenActive(
                TransferSubsystem.getInstance().setPowerInstantCommand(1),
                TransferSubsystem.getInstance().stopCommand());

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.X).toggleWhenActive(
                TransferSubsystem.getInstance().setPowerInstantCommand(0.2),
                TransferSubsystem.getInstance().stopCommand());

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).toggleWhenActive(
                IndexerSubsystem.getInstance().setPowerInstantCommand(1),
                IndexerSubsystem.getInstance().stopCommand());

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).toggleWhenActive(
                IndexerSubsystem.getInstance().setPowerInstantCommand(-0.8),
                IndexerSubsystem.getInstance().stopCommand());

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).toggleWhenActive(
                ShooterSubsystem.getInstance().getToSetpointCommand(50),
                ShooterSubsystem.getInstance().stopCommand());


    }
    @Override
    public void onInitLoop() {
    }

    @Override
    public void onPlay() {


    }

    @Override
    public void onPlayLoop() {
        MMDrivetrain.update();
        telemetry.update();

    }

    @Override
    public void onEnd() {
    }
}