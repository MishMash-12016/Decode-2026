package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.Logger.KoalaLog;

@TeleOp
@Config
@AutoLog
public class TestOpModeDriveOnly extends MMOpMode {

    public TestOpModeDriveOnly() {
        super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION);
    }

    @Override
    public void onInit() {
        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(() -> MMRobot.getInstance().gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.05);

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.OPTIONS).whenPressed(
                new InstantCommand(() -> MMDrivetrain.getInstance().resetYaw())
        );
    }

    @Override
    public void onInitLoop() {
//        FtcDashboard.getInstance().getTelemetry().addData("",MMDrivetrain.getInstance().);
    }

    @Override
    public void onPlay() {
    }
    @Override
    public void onPlayLoop() {
        FtcDashboard.getInstance().getTelemetry().addData("x", (int)MMDrivetrain.getInstance().getPose().getX());
        FtcDashboard.getInstance().getTelemetry().addData("y", (int)MMDrivetrain.getInstance().getPose().getY());
        FtcDashboard.getInstance().getTelemetry().addData("yaw", (int)Math.toDegrees(MMDrivetrain.getInstance().getPose().getHeading()));
    }
    @Override
    public void onEnd() {
    }
}