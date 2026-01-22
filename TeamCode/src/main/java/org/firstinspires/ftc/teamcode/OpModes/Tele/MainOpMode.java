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
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
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
        MMDrivetrain.update();

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(IntakeCommandGroup.FeedIntake());
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(ShootCommandGroup.upShoot());

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.B).whileActiveOnce(
                ShooterSubsystem.getInstance().setPowerRunCommand(1))
                .whenInactive(ShooterSubsystem.getInstance().setPowerInstantCommand(0));
    }

    @Override
    public void onPlay() {
        super.onPlay();
    }

    @Override
    public void onPlayLoop() {
        telemetry.update();
    }
}