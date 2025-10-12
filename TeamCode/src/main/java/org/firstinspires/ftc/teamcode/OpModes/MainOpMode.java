package org.firstinspires.ftc.teamcode.OpModes;

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
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SpindexerSubsystem;

import Ori.Coval.Logging.AutoLog;

@TeleOp
@Config
@AutoLog
public class MainOpMode extends MMOpMode {

    public MainOpMode() {
        super(OpModeType.NonCompetition.DEBUG, AllianceColor.BLUE);
    }

    @Override
    public void onInit() {
        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(()->false);
        MMDrivetrain.getInstance().update();

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(IntakeCommandGroup.FeedIntake());
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(ShootCommandGroup.ShootAll());
    }

    @Override
    public void onPlayLoop() {
        telemetry.update();
    }
}