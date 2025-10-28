package org.firstinspires.ftc.teamcode.OpModes.Tele;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

@TeleOp
public class TuneOpMode extends MMOpMode {

    public TuneOpMode() {
        super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION, AllianceColor.BLUE);
    }
    CRServo left;
    CRServo right;

    @Override
    public void onInit() {
        left = hardwareMap.get(CRServo.class,"left");
        right = hardwareMap.get(CRServo.class,"right");
    }

    @Override
    public void onPlayLoop() {

        left.setPower(gamepad1.left_stick_x);
        right.setPower(gamepad1.left_stick_x);

        telemetry.addData("hood:", ShooterHoodSubsystem.getInstance().getPosition());
        telemetry.update();
    }
}
