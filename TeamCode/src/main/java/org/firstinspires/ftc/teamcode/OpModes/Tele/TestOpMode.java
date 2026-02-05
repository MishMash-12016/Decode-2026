package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.acmerobotics.dashboard.config.Config;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;


import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.MMUtils;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;


import Ori.Coval.Logging.AutoLog;

@TeleOp
@Config
@AutoLog
public class TestOpMode extends MMOpMode {
    boolean slow = false;
    boolean Shoot = false;
    Pose startPose = new Pose(9,7,0);
    public TestOpMode() {
        super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION, AllianceColor.RED);
    }


    @Override
    public void onInit() {
        MMDrivetrain.getInstance().setPose(startPose);
        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(() -> slow);
        GamepadEx GamepadEx1 = MMRobot.getInstance().gamepadEx1;
        GamepadEx1.getGamepadButton(GamepadKeys.Button.SHARE).whenPressed(
                () -> slow = !slow
        );

        GamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).toggleWhenActive(
                MMDrivetrain.getInstance().enableDriveAligned(()->slow)
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


        telemetry.addData("drive pose: ", MMDrivetrain.getInstance().getPose());
    }

    @Override
    public void onEnd() {
        CommandScheduler.getInstance().reset();
    }
}