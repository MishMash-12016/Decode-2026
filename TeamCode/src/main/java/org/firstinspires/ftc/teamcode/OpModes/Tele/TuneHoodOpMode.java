package org.firstinspires.ftc.teamcode.OpModes.Tele;

import Ori.Coval.Logging.AutoLog;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleDigital;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleMotor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

@TeleOp
@AutoLog
@Config
public class TuneHoodOpMode extends MMOpMode {

    public TuneHoodOpMode() {
        super(OpModeType.NonCompetition.DEBUG, AllianceColor.RED);
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
        CommandScheduler.getInstance().reset();
        GamepadEx GamepadEx1 = MMRobot.getInstance().gamepadEx1;
        GamepadEx GamepadEx2 = MMRobot.getInstance().gamepadEx2;

        ShooterHoodSubsystem.getInstance().setPositionCommand(pose).schedule();

        ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(pow).schedule();

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


        telemetry.addData("The hood is now in " + pose + " it should be aligned straight with the panels.", null);

    }

    @Override
    public void onEnd() {
        super.onEnd();
        CommandScheduler.getInstance().reset();
    }
}
