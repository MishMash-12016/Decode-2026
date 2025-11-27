package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.acmerobotics.dashboard.DashboardCore;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SpindexerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import Ori.Coval.Logging.AutoLog;

@TeleOp
@Config
@AutoLog
public class TestOpMode extends MMOpMode {

    public TestOpMode() {
        super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION, AllianceColor.BLUE);
    }
    @Override
    public void onInit() {
        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(()->false);

        MMDrivetrain.getInstance().update();

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                SpindexerSubsystem.getInstance().getToSetpointCommand(SpindexerSubsystem.FIRSTPOS)
        );
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.B).whenPressed(
                SpindexerSubsystem.getInstance().getToSetpointCommand(SpindexerSubsystem.SCNDPOS)
        );
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                SpindexerSubsystem.getInstance().getToSetpointCommand(SpindexerSubsystem.THIRDPOS)
        );



//
//        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP).whileActiveOnce(
//                SpindexerSubsystem.getInstance().setPowerInstantCommand(0.15)
//        );
//        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whileActiveOnce(
//                new WaitUntilCommand(()->(!(SpindexerSubsystem.getInstance().getZeroSwitch()))).andThen(
//                SpindexerSubsystem.getInstance().setPosition(0))
//        );
//
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whileActiveOnce(
                SpindexerSubsystem.reset()
        );


        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
                ()->SpindexerSubsystem.getInstance().setPose(0));


    }
    int counter = 0;
    boolean tempswitch;
    @Override
    public void onInitLoop() {

    }

    @Override
    public void onPlay() {


    }

    @Override
    public void onPlayLoop() {
        MMDrivetrain.update();
        telemetry.update();

//        new WaitUntilCommand(()->(!(SpindexerSubsystem.getInstance().getZeroSwitch()))).andThen(
//                SpindexerSubsystem.getInstance().setPosition(0)).schedule();

//        if (!SpindexerSubsystem.getInstance().getZeroSwitch()&&tempswitch){counter++;}
//        tempswitch = SpindexerSubsystem.getInstance().getZeroSwitch();
//        telemetry.addData("counter",counter);


        telemetry.addData("Spin pose:", SpindexerSubsystem.getInstance().getPose());
        telemetry.addData("Spin switch:", SpindexerSubsystem.getInstance().getZeroSwitch());
        telemetry.addData("Spin power:", SpindexerSubsystem.getInstance().getPower());

//        telemetry.addData("testReset1:", SpindexerSubsystem.testReset1);
//        telemetry.addData("testReset2:", SpindexerSubsystem.testReset2);
//        telemetry.addData("testReset3:", SpindexerSubsystem.testReset3);


//        telemetry.addData("DriveTrain pose:", MMDrivetrain.getInstance().getPose());
//        telemetry.addData("DriveTrain pose:", MMDrivetrain.getInstance().getFollower().getHeading());



    }

    @Override
    public void onEnd() {

    }
}