package org.firstinspires.ftc.teamcode.TeleOp;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;


import org.firstinspires.ftc.teamcode.Camera.CameraCommands;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.Logger.KoalaLog;

@TeleOp
@Config
@AutoLog
public class LamlamOpMode extends MMOpMode {

    public LamlamOpMode() {
        super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION);
    }

    boolean SpecimenIntake = false;


    @Override
    public void onInit() {
        MMDrivetrain.update();
        MMDrivetrain.getInstance().setPose(0, 0, Math.toRadians(0));
        KoalaLog.setup(hardwareMap);
        MMDrivetrain.update();

        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(() -> false);

        //The camera intake:
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                CameraCommands.StrafeToArtifact()
        );

        //Other shit:
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.OPTIONS).whenPressed(
                new InstantCommand(() -> MMDrivetrain.getInstance().resetYaw())
        );

        //TODO: Use only track functions from CameraCommands and not from Camera class directly!!! BEWARE or else might not switch correctly
        CameraCommands.trackPurpleAndGreen().schedule(); //tracks green artifacts
    }

    @Override
    public void onInitLoop() {

    }

    @Override
    public void onPlay() {
    }

    @Override
    public void onPlayLoop() {
        MMDrivetrain.update();
    }

    @Override
    public void onEnd() {

    }
}
