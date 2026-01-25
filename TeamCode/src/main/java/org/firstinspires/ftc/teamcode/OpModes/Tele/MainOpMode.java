package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

import Ori.Coval.Logging.AutoLog;

@TeleOp
@Config
@AutoLog
public class MainOpMode extends MMOpMode {
    //    boolean slow = false;
    Follower follower;
    public MainOpMode() {
        super(OpModeType.Competition.TELEOP, AllianceColor.RED);
    }

    @Override
    public void onInit() {
        /** Auto things */
        follower = MMDrivetrain.getInstance().getFollower();
        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(
                () -> gamepad1.left_stick_button||gamepad1.right_stick_button
        );
        TurretSubsystem.getInstance().alignToTarget().schedule();

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).toggleWhenActive(
                IntakeCommandGroup.FeedIntake(), IntakeCommandGroup.StopIntake()
        );
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).toggleWhenActive(
                new SequentialCommandGroup(
                        IntakeCommandGroup.OutIntake(),
                        new WaitCommand(800),
                        IntakeCommandGroup.StopIntake()
                )
        );

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).toggleWhenActive(
                ShootCommandGroup.StartWheelClose(), ShooterSubsystem.getInstance().stopCommand()
        );
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.B).whenPressed(
                ShooterSubsystem.getInstance().stopCommand()
        );

        new Trigger(() -> gamepad1.right_trigger > 0.1).toggleWhenActive(
                ShootCommandGroup.DumbUpShoot(), ShootCommandGroup.StopShoot()
        );

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                IntakeCommandGroup.StopAll()
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

        telemetry.addData("can shoot?: ", ShooterSubsystem.getInstance().getVelocity()>47);
    }

    @Override
    public void onEnd() {
    }
}