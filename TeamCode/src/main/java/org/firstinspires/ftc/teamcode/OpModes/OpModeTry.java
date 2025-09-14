package org.firstinspires.ftc.teamcode.OpModes;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;

import java.security.spec.EncodedKeySpec;

@TeleOp
public class OpModeTry extends MMOpMode {

    public OpModeTry() {
        super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION);
    }

    CRServo left;
    CRServo right;
    DcMotorEx encoder;
    @Override
    public void onInit() {
        left = hardwareMap.get(CRServo.class,"left");
        right = hardwareMap.get(CRServo.class,"right");
        encoder = hardwareMap.get(DcMotorEx.class,"encoder");
    }

    @Override
    public void onPlayLoop() {
        left.setPower(gamepad1.left_stick_x);
        right.setPower(gamepad1.right_stick_x);
        telemetry.addData("left",gamepad1.left_stick_x);
        telemetry.addData("right",gamepad1.right_stick_x);
        telemetry.addData("encoder",encoder.getCurrentPosition());
        telemetry.update();
    }
}
