package org.firstinspires.ftc.teamcode.OpModes.Tele;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleDigital;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleMotor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

@TeleOp
public class TuneOpMode extends MMOpMode {

    public TuneOpMode() {
        super(OpModeType.NonCompetition.DEBUG_SERVOHUB, AllianceColor.BLUE);
    }

/*
//       CRServo left;
    CuttleMotor p0,p1,p2,p3;
    */

    @Override
    public void onInit() {
//        right = hardwareMap.get(CRServo.class,"right");
//        p0 = new CuttleMotor(MMRobot.getInstance().controlHub, 0);
//        p1 = new CuttleMotor(MMRobot.getInstance().controlHub, 1);
//        p2 = new CuttleMotor(MMRobot.getInstance().controlHub, 2);
//        p3 = new CuttleMotor(MMRobot.getInstance().controlHub, 3);

//        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whileActiveOnce(
//                new InstantCommand(()->p0.setPower(1)));
//        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.B).whileActiveOnce(
//                new InstantCommand(()->p1.setPower(1)));
//        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whileActiveOnce(
//                new InstantCommand(()->p2.setPower(1)));
//        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whileActiveOnce(
//                new InstantCommand(()->p3.setPower(1)));
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.CIRCLE).whenPressed(
                TurretSubsystem.getInstance().getToAndHoldSetPointCommand(30)
        );
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.CROSS).whenPressed(
                TurretSubsystem.getInstance().getToAndHoldSetPointCommand(40)
        );
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.SQUARE).whenPressed(
                TurretSubsystem.getInstance().getToAndHoldSetPointCommand(200)
        );
    }

    @Override
    public void onPlayLoop() {
        telemetry.addData("run1", null);
    }
}
