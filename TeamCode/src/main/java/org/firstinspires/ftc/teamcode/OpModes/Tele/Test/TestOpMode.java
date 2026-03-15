package org.firstinspires.ftc.teamcode.OpModes.Tele.Test;

import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.Logger.KoalaLog;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.OpModes.Tele.MainTeleOp;


@TeleOp(name = "Test OpMode", group = "TestOpModes")
@Config
@AutoLog
public class TestOpMode extends MainTeleOp {

  public TestOpMode() {
    super(OpModeType.NonCompetition.DEBUG, AllianceColor.BLUE);
  }

  @Override
  public void onInit() {
    super.onInit();
  }

  @Override
  public void onPlayLoop() {
    super.onPlayLoop();
//    KoalaLog.log("   : ", ,true);

    telemetry.addData("farSpeed: ", farSpeed);
  }
}
