package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
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
        boolean slow = false;
        boolean Shoot = false;
    public MainOpMode() {
        super(OpModeType.Competition.TELEOP, AllianceColor.RED);
    }


    @Override
    public void onInit() {
        GamepadEx GamepadEx1 = MMRobot.getInstance().gamepadEx1;
        /** Auto things */
        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(() -> slow);
        GamepadEx1.getGamepadButton(GamepadKeys.Button.SHARE).whenPressed(
                () -> slow = !slow
        );


        GamepadEx1.getGamepadButton(GamepadKeys.Button.OPTIONS).whenPressed(
                ()->MMDrivetrain.getInstance().resetYaw()
        );

        GamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).toggleWhenActive(
                IntakeCommandGroup.dumbFeed(), IntakeCommandGroup.stopIntake()
        );
        GamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).toggleWhenActive(
                new SequentialCommandGroup(
                        IntakeCommandGroup.outIntake(),
                        new WaitCommand(800),
                        IntakeCommandGroup.stopIntake()
                )
        );

        GamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                ShootCommandGroup.closeDumbSpeed()
        );GamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                ShootCommandGroup.speedByLocation()
        );GamepadEx1.getGamepadButton(GamepadKeys.Button.B).whenPressed(
                ShooterSubsystem.getInstance().stopCommand()
        );


        new Trigger(() -> gamepad1.left_trigger > 0.1).toggleWhenActive(
                ShootCommandGroup.smartUpShoot(slow),
                ShootCommandGroup.stopShoot()
        );
        new Trigger(() -> gamepad1.right_trigger > 0.1).toggleWhenActive(
                ShootCommandGroup.dumbUpShoot(), ShootCommandGroup.stopShoot()
        );

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                IntakeCommandGroup.stopAll()
        );
    }

    @Override
    public void onInitLoop() {
    }
    @Override
    public void onPlay() {
    }
    @Override
    public void onPlayLoop() {
        telemetry.update();
        MMDrivetrain.update();

        if(ShooterSubsystem.getInstance().getVelocity()>ShooterSubsystem.getInstance().getSetPoint()
                && ShooterSubsystem.getInstance().getSetPoint() != 0) {
            gamepad1.rumble(100);
        }

        telemetry.addData("shooter speed: ", ShooterSubsystem.getInstance().getVelocity());
    }

    @Override
    public void onEnd() {
    }
}