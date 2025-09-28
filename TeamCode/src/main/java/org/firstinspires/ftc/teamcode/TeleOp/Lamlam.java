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
import org.firstinspires.ftc.teamcode.subsystems.Camera;

import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.Logger.KoalaLog;

@TeleOp
@Config
@AutoLog
public class Lamlam extends MMOpMode {

    public Lamlam() {
        super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION);
    }

    boolean SpecimenIntake = false;


    @Override
    public void onInit() {
        Camera.getInstance().trackGreen();
        MMDrivetrain.getInstance().setPose(0, 0, Math.toRadians(0));
        KoalaLog.setup(hardwareMap);

        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(() -> MMRobot.getInstance().gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.05);

        //The camera intake:
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                CameraCommands.StrafeToArtifact()
        );

        //Other shit:
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.OPTIONS).whenPressed(
                new InstantCommand(() -> MMDrivetrain.getInstance().resetYaw())
        );
    }

    @Override
    public void onInitLoop() {

    }

    @Override
    public void onPlay() {
    }

    @Override
    public void onPlayLoop() {

    }

    @Override
    public void onEnd() {

    }
}
