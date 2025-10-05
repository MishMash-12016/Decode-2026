package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SpindexerSubsystem;

import Ori.Coval.Logging.AutoLog;

@TeleOp
@Config
@AutoLog
public class TestOpMode extends MMOpMode {

    public TestOpMode() {
        super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION);
    }

    @Override
    public void onInit() {
        /*
        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(() -> MMRobot.getInstance().gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.05);

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.OPTIONS).whenPressed(
                new InstantCommand(() -> MMDrivetrain.getInstance().follower.getPose().getHeading())
        );

         */

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(ShooterSubsystem.getInstance().setPowerInstantCommand(1));
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.B).whenPressed(ShooterSubsystem.getInstance().setPowerInstantCommand(0));
//        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(SpindexerSubsystem.getInstance().getToSetpointCommand());

        SpindexerSubsystem.getInstance().withSetDefaultCommand(
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(
                        () -> MMRobot.getInstance().gamepadEx1.getLeftY()
                )
        );

//        ShooterHoodSubsystem.getInstance().withDefaultCommand(ShooterHoodSubsystem.getInstance().setPositionCommand(
//                () -> Math.abs(MMRobot.getInstance().gamepadEx1.getRightY())));
    }

    @Override
    public void onInitLoop() {
//        FtcDashboard.getInstance().getTelemetry().addData("",MMDrivetrain.getInstance().);
    }

    @Override
    public void onPlay() {

    }

    @Override
    public void onPlayLoop() {
        telemetry.addData("encoder", SpindexerSubsystem.getInstance().getPose());
        telemetry.addData("serosPower", SpindexerSubsystem.getInstance().getPower());
    }

    @Override
    public void onEnd() {

    }
}