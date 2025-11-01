package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.seattlesolvers.solverslib.command.InstantCommand;
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
//        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(()->false);
        MMDrivetrain.getInstance().update();

//        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whileActiveOnce().whenInactive(
//                );
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenActive(
                new InstantCommand(()-> MMDrivetrain.getInstance().resetYaw()));
    }

    @Override
    public void onInitLoop() {

    }

    @Override
    public void onPlay() {


    }

    @Override
    public void onPlayLoop() {
        MMDrivetrain.getInstance().update();
        telemetry.update();

        TurretSubsystem.getInstance().alignToTarget();

        telemetry.addData("Turret pose:", TurretSubsystem.getInstance().getPose());
        telemetry.addData("DriveTrain pose:", MMDrivetrain.getInstance().getPose());



    }

    @Override
    public void onEnd() {

    }
}