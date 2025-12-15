package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;

import org.firstinspires.ftc.teamcode.subsystems.SpindexerSubsystem;


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

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.OPTIONS).whenPressed(
                SpindexerSubsystem.reset());

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                IntakeCommandGroup.FeedIntake());
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
                ShootCommandGroup.ShootAll());


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

        telemetry.addData("Spin pose:", SpindexerSubsystem.getInstance().getPose());
        telemetry.addData("Spin switch:", SpindexerSubsystem.getInstance().getZeroSwitch());
        telemetry.addData("Spin power:", SpindexerSubsystem.getInstance().getPower());
        telemetry.addData("ts:", null);
    }

    @Override
    public void onEnd() {
    }
}