package org.firstinspires.ftc.teamcode.OpModes.Tele.Test;

import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.Logger.KoalaLog;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.OpModes.Tele.MainTeleOp;
import org.firstinspires.ftc.teamcode.RobotUtils;


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
  }

  @Override
  public void onPlayLoop() {
    super.onPlayLoop();
    KoalaLog.log("tuneTarget/x:", RobotUtils.getTargetPose().getX(),true);
    KoalaLog.log("tuneTarget/y:", RobotUtils.getTargetPose().getY(),true);
    telemetry.addData("inShootMode:", inShootMode);
  }
}
