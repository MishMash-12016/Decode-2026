package org.firstinspires.ftc.teamcode.OpModes.Tele.Comp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.OpModes.Tele.MainTeleOp;


@TeleOp
public class RedTeleOp extends MainTeleOp {

  public RedTeleOp() {
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