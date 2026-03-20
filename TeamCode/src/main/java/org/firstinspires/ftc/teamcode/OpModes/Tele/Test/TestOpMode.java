package org.firstinspires.ftc.teamcode.OpModes.Tele.Test;

import Ori.Coval.Logging.AutoLog;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Commands.LamlamCommands;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.OpModes.Tele.MainTeleOp;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;

@TeleOp
@Config
@AutoLog
public class TestOpMode extends MainTeleOp {

  public TestOpMode() {
    super(OpModeType.NonCompetition.DEBUG, AllianceColor.BLUE);
  }

  @Override
  public void onInit() {
    super.onInit();

    MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(
            new InstantCommand(LamlamCommands::goToArtifact)
    );
    MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
            LamlamCommands.strafeToArtifact()
    );

    new Trigger(() -> gamepad1.right_trigger > 0.1)
            .whenActive(
                    ShootCommandGroup.twoOneShoot()
                            .alongWith(new InstantCommand(() -> aligned = false))
                            .alongWith(new WaitCommand(2500).andThen(
                                    new InstantCommand(()-> a = false)))
            );
  }

  @Override
  public void onPlayLoop() {
    super.onPlayLoop();
    //    KoalaLog.log("   : ", ,true);
    ShooterSubsystem.getInstance().periodic();

    telemetry.addData("farSpeed: ", farSpeed);
  }
}
