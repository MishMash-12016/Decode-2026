package org.firstinspires.ftc.teamcode.OpModes.Tele.Main;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVariables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVariables.OpModeType;
import org.firstinspires.ftc.teamcode.OpModes.Tele.MainTeleOp;


@TeleOp(name = "BLUE TELEOP")

public class BlueTeleOp extends MainTeleOp {

  public BlueTeleOp() {
    super(OpModeType.Competition.TELEOP, AllianceColor.BLUE);
  }

  @Override
  public void onInit() {
    super.onInit();
  }

  @Override
  public void onPlayLoop() {
    super.onPlayLoop();
  }
}