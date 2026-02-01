package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.MMUtils;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.RobotUtils;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

import Ori.Coval.Logging.AutoLog;

@TeleOp
@Config
@AutoLog
public class TestOpMode extends MMOpMode {
    boolean slow = false;
    boolean Shoot = false;
    Pose startPose = new Pose(57,55,0);
    public TestOpMode() {
        super(OpModeType.NonCompetition.DEBUG_SERVOHUB, AllianceColor.RED);
    }


    @Override
    public void onInit() {
        MMDrivetrain.getInstance().setPose(startPose);
        GamepadEx GamepadEx1 = MMRobot.getInstance().gamepadEx1;

/*        GamepadEx1.getGamepadButton(GamepadKeys.Button.OPTIONS).whenPressed(
                ()->MMDrivetrain.getInstance().resetYaw()
        );

        GamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).toggleWhenActive(
                IntakeCommandGroup.FeedIntake(), IntakeCommandGroup.StopIntake()
        );

        GamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).toggleWhenActive(
                        IntakeCommandGroup.OutIntake(), IntakeCommandGroup.StopIntake()
        );

        GamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                ShootCommandGroup.StartWheelClose()
        );GamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                ShootCommandGroup.StartWheelFar()
        );GamepadEx1.getGamepadButton(GamepadKeys.Button.B).whenPressed(
                ShooterSubsystem.getInstance().stopCommand()
        );


        new Trigger(() -> gamepad1.left_trigger > 0.1).toggleWhenActive(
                ShootCommandGroup.SmartUpShoot(), ShootCommandGroup.StopShoot()
        );
        new Trigger(() -> gamepad1.right_trigger > 0.1).toggleWhenActive(
                ShootCommandGroup.DumbUpShoot(), ShootCommandGroup.StopShoot()
        );

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                IntakeCommandGroup.StopAll()
        );*/

   GamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(
           ()->MMDrivetrain.getInstance().getFollower().followPath(
                MMDrivetrain.getInstance().getFollower().pathBuilder()
                        .setLinearHeadingInterpolation(Math.toRadians(MMDrivetrain.getInstance().getFollower().getHeading()),
                                RobotUtils.getAngleToTarget().getRadians()).build()
        )
   );
        GamepadEx1.getGamepadButton(GamepadKeys.Button.B).whenPressed(
                ()->CommandScheduler.getInstance().reset()
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

        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(() -> false);

        telemetry.addData("driver pose: ", MMDrivetrain.getInstance().getPose());
        telemetry.addData("driver target: ", MMDrivetrain.getInstance().getAScopeTargetPose());
    }

    @Override
    public void onEnd() {
    }
}