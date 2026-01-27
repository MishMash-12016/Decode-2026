package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleDigital;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.WebcamSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.RobotUtils;
import org.firstinspires.ftc.teamcode.subsystems.IndexerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TransferSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.Logger.KoalaLog;

@TeleOp
@Config
@AutoLog
public class TestOpMode extends MMOpMode {
    boolean slow = false;
    Follower follower;
    CuttleDigital sensor0;
    CuttleDigital sensor1;
    public TestOpMode() {
        super(OpModeType.NonCompetition.DEBUG_SERVOHUB, AllianceColor.RED);
    }

    @Override
    public void onInit() {
        sensor0 =  new CuttleDigital(MMRobot.getInstance().expansionHub, 0);
        sensor1 =  new CuttleDigital(MMRobot.getInstance().expansionHub, 1);

        /** Auto things */
        follower = MMDrivetrain.getInstance().getFollower();
        MMDrivetrain.getInstance().enableTeleopDriveDefaultCommand(() -> slow);
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.SHARE).whenPressed(
                () -> slow = !slow
        );
        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).toggleWhenActive(
                IntakeSubsystem.getInstance().setPowerInstantCommand(1)
        );


        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).toggleWhenActive(
                new ParallelCommandGroup(
                        IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                        TransferSubsystem.getInstance().setPowerInstantCommand(0.2),
                        IndexerSubsystem.getInstance().setPowerInstantCommand(-1)
                )
        );

        new Trigger(() -> gamepad1.left_trigger > 0.1).toggleWhenActive(
                new ParallelCommandGroup(
                        IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                        TransferSubsystem.getInstance().setPowerInstantCommand(1),
                        IndexerSubsystem.getInstance().setPowerInstantCommand(1)
                ),IntakeCommandGroup.StopAll()
        );

        MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.A).toggleWhenActive(
                new ParallelCommandGroup(
                        ShooterSubsystem.getInstance().setPowerInstantCommand(1)
                )
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
        telemetry.update();


        telemetry.addData("sensor0: ", sensor0.getState());
        telemetry.addData("sensor1: ", sensor1.getState());
        KoalaLog.log("ShootSpeed: ", ShooterSubsystem.getInstance().getVelocity(), true);

//        KoalaLog.log("    :",  ,true);
//        KoalaLog.log("DriveTrainY:", MMDrivetrain.getInstance().getPose().getY(),true);
//        KoalaLog.log("DriveTrainHeading:", MMDrivetrain.getInstance().getPose().getHeading(),true);

    }

    @Override
    public void onEnd() {
    }
}