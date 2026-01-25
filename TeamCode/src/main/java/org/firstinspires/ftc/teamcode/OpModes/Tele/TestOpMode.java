package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.WebcamSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.RobotUtils;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.Logger.KoalaLog;

@TeleOp
@Config
@AutoLog
public class TestOpMode extends MMOpMode {
//    boolean slow = false;
    Follower follower;

    public TestOpMode() {
        super(OpModeType.NonCompetition.DEBUG_SERVOHUB, AllianceColor.RED);
    }

    @Override
    public void onInit() {
        /** Auto things */
        MMDrivetrain.getInstance().getFollower().setStartingPose(new Pose(8,8,0));
        follower = MMDrivetrain.getInstance().getFollower();
        WebcamSubsystem.getInstance();
        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(() -> /*slow*/
        gamepad1.left_stick_button||gamepad1.right_stick_button);
//        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.SHARE).whenPressed(
//                () -> slow = !slow
//        );

        MMDrivetrain.getInstance().turnCommand(Math.toRadians(RobotUtils.getAngleToTarget()),true);

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

        new Trigger(() -> gamepad1.right_trigger > 0.1).toggleWhenActive(
                ShootCommandGroup.UpShoot(), ShootCommandGroup.StopShoot()
        );

        /**
         * temp stuff:
         */
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.B).whenPressed(
                ShooterSubsystem.getInstance().stopCommand()
        );
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                IntakeCommandGroup.StopAll()
        );
        MMRobot.getInstance().gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(100));
        TurretSubsystem.getInstance().holdCurrentPoseCommand().schedule();
    }

    @Override
    public void onInitLoop() {
    }

    @Override
    public void onPlay() {
        TurretSubsystem.getInstance().alignToTarget().schedule();
    }

    @Override
    public void onPlayLoop() {
        telemetry.addData("ShootSpeed: ", ShooterSubsystem.getInstance().getVelocity());

        telemetry.addData("X: ", MMDrivetrain.getInstance().getPose().getX());
        telemetry.addData("Y: ", MMDrivetrain.getInstance().getPose().getY());
        KoalaLog.log("ShootSpeed: ", ShooterSubsystem.getInstance().getVelocity(), true);

//        KoalaLog.log("DriveTrainX:", MMDrivetrain.getInstance().getPose().getX(),true);
//        KoalaLog.log("DriveTrainY:", MMDrivetrain.getInstance().getPose().getY(),true);
//        KoalaLog.log("DriveTrainHeading:", MMDrivetrain.getInstance().getPose().getHeading(),true);

    }

    @Override
    public void onEnd() {
    }
}