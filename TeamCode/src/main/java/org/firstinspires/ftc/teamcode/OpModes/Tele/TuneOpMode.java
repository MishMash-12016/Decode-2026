package org.firstinspires.ftc.teamcode.OpModes.Tele;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleDigital;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleMotor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.subsystems.BallStopperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;


import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.Logger.KoalaLog;

@TeleOp
@AutoLog
public class TuneOpMode extends MMOpMode {

    public TuneOpMode() {
        super(OpModeType.NonCompetition.DEBUG_SERVOHUB, AllianceColor.RED);
    }
    CuttleDigital sensor;

    CuttleMotor p0,p1,p2,p3;
    CuttleMotor ep0,ep1,ep2,ep3;

//    CRServo left;
//    MotorEx a;
    double pose;

    @Override
    public void onInit() {
        GamepadEx GamepadEx1 = MMRobot.getInstance().gamepadEx1;
        GamepadEx GamepadEx2 = MMRobot.getInstance().gamepadEx2;

/*//        right = hardwareMap.get(CRServo.class,"right");
        p0 = new CuttleMotor(MMRobot.getInstance().controlHub, 0);
        p1 = new CuttleMotor(MMRobot.getInstance().controlHub, 1);
        p2 = new CuttleMotor(MMRobot.getInstance().controlHub, 2);
        p3 = new CuttleMotor(MMRobot.getInstance().controlHub, 3);

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                new InstantCommand(()->p0.setPower(1))).whenInactive(
                new InstantCommand(()->p0.setPower(0))
        );
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.B).whenPressed(
                new InstantCommand(()->p1.setPower(1))).whenInactive(
                new InstantCommand(()->p1.setPower(0))
        );
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                new InstantCommand(()->p2.setPower(1))).whenInactive(
                new InstantCommand(()->p2.setPower(0))
        );
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                new InstantCommand(()->p3.setPower(1))).whenInactive(
                new InstantCommand(()->p3.setPower(0))
        );

        ep0 = new CuttleMotor(MMRobot.getInstance().expansionHub, 0);
        ep1 = new CuttleMotor(MMRobot.getInstance().expansionHub, 1);
        ep2 = new CuttleMotor(MMRobot.getInstance().expansionHub, 2);
        ep3 = new CuttleMotor(MMRobot.getInstance().expansionHub, 3);

        MMRobot.getInstance().gamepadEx2.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                new InstantCommand(()->ep0.setPower(1))).whenInactive(
                new InstantCommand(()->ep0.setPower(0))
        );
        MMRobot.getInstance().gamepadEx2.getGamepadButton(GamepadKeys.Button.B).whenPressed(
                new InstantCommand(()->ep1.setPower(1))).whenInactive(
                new InstantCommand(()->ep1.setPower(0))
        );
        MMRobot.getInstance().gamepadEx2.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                new InstantCommand(()->ep2.setPower(1))).whenInactive(
                new InstantCommand(()->ep2.setPower(0))
        );
        MMRobot.getInstance().gamepadEx2.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                new InstantCommand(()->ep3.setPower(1))).whenInactive(
                new InstantCommand(()->ep3.setPower(0))
        );*/
/*        GamepadEx1.getGamepadButton(GamepadKeys.Button.X).toggleWhenActive(
                IntakeCommandGroup.dumbFeed(), IntakeCommandGroup.stopIntake()
        );

        GamepadEx1.getGamepadButton(GamepadKeys.Button.B).toggleWhenActive(
                ShooterSubsystem.getInstance().setPowerInstantCommand(1),
                ShooterSubsystem.getInstance().stopCommand()
        );

        pose = 0;
        GamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                () -> pose += 0.1
        );
        GamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
                () -> pose -= 0.1
        );
        GamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                () -> pose += 0.05
        );
        GamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                () -> pose -= 0.05
        );*/

    }

    @Override
    public void onPlay() {
        super.onPlay();
    }

    @Override
    public void onPlayLoop() {
        telemetry.update();

        BallStopperSubsystem.getInstance().setPositionCommand(pose).schedule();
        ShooterHoodSubsystem.getInstance().setPositionCommand(pose).schedule();

            telemetry.addData("sensor", sensor.getState());
            telemetry.addData("pose", pose);

        KoalaLog.log("null: ", 0, true);

    }

    @Override
    public void onEnd() {
        super.onEnd();
    }
}
