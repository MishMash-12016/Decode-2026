package org.firstinspires.ftc.teamcode.OpModes.Tele;

import Ori.Coval.Logging.AutoLog;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleDigital;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleMotor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;

@TeleOp
@AutoLog
@Config
public class MotorTestOpMode extends MMOpMode {

    public MotorTestOpMode() {
        super(OpModeType.NonCompetition.DEBUG_SERVOHUB, AllianceColor.RED);
    }

    CuttleDigital sensor;

    CuttleMotor p0, p1, p2, p3;
    CuttleMotor ep0, ep1, ep2, ep3;
    Pose startPose = new Pose(9, 10, 0);

    //      CRServo left;
//      MotorEx a;
    public static double pose = 0.1;
    public static double pow;
    boolean slow = false;

    @Override
    public void onInit() {
//        right = hardwareMap.get(CRServo.class,"right");
        p0 = new CuttleMotor(MMRobot.getInstance().controlHub, 0);
        p1 = new CuttleMotor(MMRobot.getInstance().controlHub, 1);
        p2 = new CuttleMotor(MMRobot.getInstance().controlHub, 2);
        p3 = new CuttleMotor(MMRobot.getInstance().controlHub, 3);

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whileActiveOnce(
                new InstantCommand(()->p0.setPower(1)));
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.B).whileActiveOnce(
                new InstantCommand(()->p1.setPower(1)));
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whileActiveOnce(
                new InstantCommand(()->p2.setPower(1)));
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whileActiveOnce(
                new InstantCommand(()->p3.setPower(1)));

        ep0 = new CuttleMotor(MMRobot.getInstance().expansionHub, 0);
        ep1 = new CuttleMotor(MMRobot.getInstance().expansionHub, 1);
        ep2 = new CuttleMotor(MMRobot.getInstance().expansionHub, 2);
        ep3 = new CuttleMotor(MMRobot.getInstance().expansionHub, 3);

        MMRobot.getInstance().gamepadEx2.getGamepadButton(GamepadKeys.Button.A).whileActiveOnce(
                new InstantCommand(()->ep0.setPower(1)));
        MMRobot.getInstance().gamepadEx2.getGamepadButton(GamepadKeys.Button.B).whileActiveOnce(
                new InstantCommand(()->ep1.setPower(1)));
        MMRobot.getInstance().gamepadEx2.getGamepadButton(GamepadKeys.Button.X).whileActiveOnce(
                new InstantCommand(()->ep2.setPower(1)));
        MMRobot.getInstance().gamepadEx2.getGamepadButton(GamepadKeys.Button.Y).whileActiveOnce(
                new InstantCommand(()->ep3.setPower(1)));
        
    }

    @Override
    public void onPlay() {
        super.onPlay();
    }

    @Override
    public void onPlayLoop() {
        telemetry.update();
        //        telemetry.addData("pose", pose);
        //        KoalaLog.log("pose: ", pose, true);

    }

    @Override
    public void onEnd() {
        super.onEnd();
        CommandScheduler.getInstance().reset();
    }
}
