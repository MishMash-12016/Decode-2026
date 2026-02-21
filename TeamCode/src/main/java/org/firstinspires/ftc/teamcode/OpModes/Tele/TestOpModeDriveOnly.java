package org.firstinspires.ftc.teamcode.OpModes.Tele;

import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.Logger.KoalaLog;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleDigital;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleMotor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.exterpolation.ExterpolationMap;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.RobotUtils;
import org.firstinspires.ftc.teamcode.subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

@TeleOp
@AutoLog
@Config
public class TestOpModeDriveOnly extends MMOpMode {

    public TestOpModeDriveOnly() {
        super(OpModeType.NonCompetition.DEBUG_SERVOHUB, AllianceColor.RED);
    }

    CuttleDigital sensor;

    CuttleMotor p0, p1, p2, p3;
    CuttleMotor ep0, ep1, ep2, ep3;
    Pose startPose = new Pose(8, 10, 0);

    //    CRServo left;
    //    MotorEx a;
    public static double pose;
    public static double pow;
    boolean slow = false;
    ExterpolationMap closeExterpolationMap =
            new ExterpolationMap().put(63.02, 0.155).put(89.4, 0.21).put(105.8, 0.18);

    @Override
    public void onInit() {
        CommandScheduler.getInstance().reset();
        GamepadEx GamepadEx1 = MMRobot.getInstance().gamepadEx1;
        GamepadEx GamepadEx2 = MMRobot.getInstance().gamepadEx2;

        /// DriveTrain
        MMDrivetrain.getInstance().setPose(startPose);
        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(() -> slow);
        new Trigger(() -> gamepad1.left_trigger > 0.1).whenActive(() -> slow = !slow);
        GamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .toggleWhenActive(MMDrivetrain.getInstance().enableDriveAligned(() -> slow));
        GamepadEx1.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .whenPressed(() -> MMDrivetrain.getInstance().resetYaw());

        new Trigger(() -> gamepad1.right_trigger > 0.1)
                .toggleWhenActive(ShootCommandGroup.dumbUpShoot(), ShootCommandGroup.stopShoot());
        new Trigger(() -> gamepad1.left_trigger > 0.1)
                .toggleWhenActive(ShootCommandGroup.superDumbUpShoot(), ShootCommandGroup.stopShoot());

        GamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .toggleWhenActive(IntakeCommandGroup.smartFeed(), IntakeCommandGroup.stopIntake());
    }

    @Override
    public void onPlay() {
        super.onPlay();
        ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(() -> pow).schedule();
    }

    @Override
    public void onPlayLoop() {
        telemetry.update();
        //        telemetry.addData("pose", pose);
        //        KoalaLog.log("pose: ", pose, true);

        KoalaLog.log("shooter/farInterpolation",closeExterpolationMap.exterpolate(RobotUtils.getDistanceToTarget()),true);

    ShooterHoodSubsystem.getInstance().setPosition(pose);
    }

    @Override
    public void onEnd() {
        super.onEnd();
        CommandScheduler.getInstance().reset();
    }
}
