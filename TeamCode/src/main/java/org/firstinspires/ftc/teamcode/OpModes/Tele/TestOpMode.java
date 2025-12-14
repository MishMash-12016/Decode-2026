package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.acmerobotics.dashboard.DashboardCore;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SpindexerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import Ori.Coval.Logging.AutoLog;

@TeleOp
@Config
@AutoLog
public class TestOpMode extends MMOpMode {

    public TestOpMode() {
        super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION, AllianceColor.BLUE);
    }
    @Override
    public void onInit() {
//        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(()->false);
//        MMDrivetrain.update();

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                SpindexerSubsystem.getInstance().getToSetpointCommand(SpindexerSubsystem.FIRSTPOS)
        );
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.B).whenPressed(
                SpindexerSubsystem.getInstance().getToSetpointCommand(SpindexerSubsystem.SCNDPOS)
        );
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                SpindexerSubsystem.getInstance().getToSetpointCommand(SpindexerSubsystem.THIRDPOS)
        );

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whileActiveOnce(
                SpindexerSubsystem.reset()
        );

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
                SpindexerSubsystem.getInstance().setPowerInstantCommand(0));


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
    }

    @Override
    public void onEnd() {
    }
}