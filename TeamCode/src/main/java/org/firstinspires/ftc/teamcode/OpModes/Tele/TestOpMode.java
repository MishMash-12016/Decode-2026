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
    boolean slow = false;
    Follower follower;

    public TestOpMode() {
        super(OpModeType.NonCompetition.DEBUG_SERVOHUB, AllianceColor.RED);
    }

    @Override
    public void onInit() {
        /** Auto things */
        MMDrivetrain.getInstance().getFollower().setStartingPose(new Pose(8,8,0));
        follower = MMDrivetrain.getInstance().getFollower();
        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(() -> slow);
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.SHARE).whenPressed(
                () -> slow = !slow
        );

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).toggleWhenActive(
                IntakeCommandGroup.FeedIntake(), IntakeCommandGroup.StopIntake()
        );
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
                new SequentialCommandGroup(
                        IntakeCommandGroup.OutIntake(),
                        new WaitCommand(700),
                        IntakeCommandGroup.StopIntake()
                )
        );

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).toggleWhenActive(
                ShootCommandGroup.StartWheelClose(), ShooterSubsystem.getInstance().stopCommand()
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
        MMRobot.getInstance().gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(
                ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(100)
        );

        //TODO; needs to be tested

        MMRobot.getInstance().gamepadEx2.getGamepadButton(GamepadKeys.Button.OPTIONS).whenPressed(
                ()->MMDrivetrain.getInstance().resetYaw()
        );
        MMRobot.getInstance().gamepadEx2.getGamepadButton(GamepadKeys.Button.OPTIONS).whenPressed(
                MMDrivetrain.getInstance().turnCommand(Math.toRadians(RobotUtils.getAngleToTarget()),true)
        );

        new Trigger(() -> gamepad1.right_trigger > 0.1).toggleWhenActive(
                ShootCommandGroup.SmartUpShoot(), ShootCommandGroup.StopShoot()
        );


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
        telemetry.update();

        telemetry.addData("ShootSpeed: ", ShooterSubsystem.getInstance().getVelocity());
        KoalaLog.log("ShootSpeed: ", ShooterSubsystem.getInstance().getVelocity(), true);

//        KoalaLog.log("    :",  ,true);
//        KoalaLog.log("DriveTrainY:", MMDrivetrain.getInstance().getPose().getY(),true);
//        KoalaLog.log("DriveTrainHeading:", MMDrivetrain.getInstance().getPose().getHeading(),true);

    }

    @Override
    public void onEnd() {
    }
}