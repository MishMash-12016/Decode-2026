package org.firstinspires.ftc.teamcode.OpModes;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleEncoder;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

@TeleOp
public class TuneOpMode extends MMOpMode {

    public TuneOpMode() {
        super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION);
    }

    //    CRServo left;
//    CRServo right;
    @Override
    public void onInit() {
//        left = hardwareMap.get(CRServo.class,"left");
//        right = hardwareMap.get(CRServo.class,"right");
    }

    @Override
    public void onPlayLoop() {
//        left.setPower(gamepad1.left_stick_x/2);
//        right.setPower(gamepad1.left_stick_x/2);
//        telemetry.addData("left",gamepad1.left_stick_x);
//        telemetry.addData("right",gamepad1.right_stick_x);
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whileActiveOnce(
                ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(50)
        ).whenInactive(ShooterSubsystem.getInstance().setPowerInstantCommand(0));

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.B).whenPressed(
                new InstantCommand(
                        () -> ShooterSubsystem.instance = null
                )
        );

        ShooterHoodSubsystem.getInstance().setPosition(
                MMRobot.getInstance().gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)
        );
        telemetry.addData("hood:", ShooterHoodSubsystem.getInstance().getPosition());
        telemetry.update();
    }
}
