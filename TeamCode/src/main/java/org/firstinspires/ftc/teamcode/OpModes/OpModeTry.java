package org.firstinspires.ftc.teamcode.OpModes;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleEncoder;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
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
//    NormalizedColorSensor colorSensor;

    @Override
    public void onInit() {
        left = hardwareMap.get(CRServo.class,"left");
        right = hardwareMap.get(CRServo.class,"right");
//        colorSensor = hardwareMap.get(NormalizedColorSensor.class,"pooky");

    }

    @Override
    public void onPlayLoop() {
        left.setPower(gamepad1.left_stick_x);
        right.setPower(gamepad1.left_stick_x);
        telemetry.addData("left",gamepad1.left_stick_x);
        telemetry.addData("right",gamepad1.right_stick_x);
//        telemetry.addData("encoder",encoder.getCurrentPosition());
//        NormalizedRGBA colors = colorSensor.getNormalizedColors();
//        telemetry.addData("red",colors.red*100);
//        telemetry.addData("green",colors.green*100);
//        telemetry.addData("blue",colors.blue*100);
        telemetry.update();
    }
}
