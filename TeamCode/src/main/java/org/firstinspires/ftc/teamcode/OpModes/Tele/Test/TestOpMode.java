package org.firstinspires.ftc.teamcode.OpModes.Tele.Test;

import Ori.Coval.Logging.AutoLog;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Commands.LamlamCommands;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.OpModes.Tele.MainTeleOp;


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
    MMRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
            LamlamCommands.StrafeToArtifact()
    );
  }

  @Override
  public void onPlayLoop() {
    super.onPlayLoop();
//    KoalaLog.log("   : ", ,true);

    telemetry.addData("farSpeed: ", farSpeed);
  }
}
