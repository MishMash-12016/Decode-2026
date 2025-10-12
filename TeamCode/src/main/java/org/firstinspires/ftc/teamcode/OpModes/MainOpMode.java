package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SpindexerSubsystem;

import Ori.Coval.Logging.AutoLog;

@TeleOp
@Config
@AutoLog
public class MainOpMode extends MMOpMode {

    public MainOpMode() {
        super(OpModeType.NonCompetition.DEBUG);
    }

    @Override
    public void onInit() {
//        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(()->false);
//        MMDrivetrain.getInstance().update();

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(IntakeCommandGroup.FeedIntake());
//        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(
//                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.FIRSTPOS));
//        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.B).whenPressed(
//                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.SCNDPOS));
//        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(
//                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.THIRDPOS));

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(ShootCommandGroup.ShootAll());

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(SpindexerSubsystem.reset());

    }

    @Override
    public void onPlayLoop() {
        telemetry.addData("spindexer pose:", SpindexerSubsystem.getInstance().getPose());
        telemetry.addData("dis",SpindexerSubsystem.getInstance().getDistance());
        telemetry.update();
    }
}