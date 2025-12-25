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
import org.firstinspires.ftc.teamcode.subsystems.FunnelSubsystem;


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

    }

    @Override
    public void onEnd() {
    }
}