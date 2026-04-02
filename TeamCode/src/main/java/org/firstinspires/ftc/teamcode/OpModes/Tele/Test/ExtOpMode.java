package org.firstinspires.ftc.teamcode.OpModes.Tele.Test;

import Ori.Coval.Logging.AutoLog;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVariables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVariables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.Subsystems.AccelSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.BallStopperSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;


@TeleOp(name = "ExtOpMode", group = "tunersOpModes")
@Config
@AutoLog
public class ExtOpMode extends MMOpMode {
    boolean slow = false;
    boolean aligned = false;
    public ExtOpMode() {
        super(OpModeType.NonCompetition.DEBUG, AllianceColor.BLUE);
    }
    public static double hoodPose = 0.4;
    public static double shootSpeed = 0;
    public static double shootTimes = 0;
    private Pose startPose = new Pose(135, 8, Math.toRadians(180));

    @Override
    public void onInit() {
        GamepadEx GamepadEx1 = MMRobot.getInstance().gamepadEx1;
        GamepadEx GamepadEx2 = MMRobot.getInstance().gamepadEx2;
        MMDrivetrain drivetrain = MMDrivetrain.getInstance();

        /// DriveTrain
        drivetrain.enableTeleopDriveDefaultCommand(() -> slow, allianceColor);
        GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(() -> slow = !slow);
        GamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(()-> aligned = !aligned);
        new Trigger(() -> aligned).whileActiveOnce(drivetrain.enableDriveAligned(() -> slow, allianceColor));
        GamepadEx1.getGamepadButton(GamepadKeys.Button.SHARE).whenPressed(()->drivetrain.resetYaw(allianceColor));
        drivetrain.getFollower().setStartingPose(startPose);
        drivetrain.setPose(startPose);



        /// Shooter
        new Trigger(() -> gamepad1.right_trigger > 0.1)
            .whenActive(new SequentialCommandGroup(
                    BallStopperSubsystem.getInstance().open(),
                    new WaitCommand(200),
                    new ParallelCommandGroup(
                            BallStopperSubsystem.getInstance().open(),
                            IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                            AccelSubsystem.getInstance().setPowerInstantCommand(1)
                    )
            ).alongWith(new InstantCommand(()->shootTimes++))).whenInactive(ShootCommandGroup.stopShoot());
        ///     ↑

        /// Intake
        GamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(IntakeCommandGroup.smartFeed())
                .whenInactive(IntakeCommandGroup.stopIntake());

        GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(IntakeCommandGroup.outIntake())
                .whenInactive(IntakeCommandGroup.stopIntake());
        ///     ↑
    }

    @Override
    public void onPlayLoop() {
        telemetry.update();
        telemetry.addLine("");
    ///
        if (gamepad1.a)
            ShooterSubsystem.getInstance().stopCommand().schedule();
        else
            ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(shootSpeed).schedule();
        ShooterHoodSubsystem.getInstance().setPositionCommand(hoodPose).schedule();
        telemetry.addData("shootTimes: ", shootTimes);

        if(gamepad2.a) throw new NullPointerException("yoyo youre gay its on gampad 2");
    }
}
