package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.WebcamSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.Libraries.Prism.Color;
import org.firstinspires.ftc.teamcode.Libraries.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.Libraries.Prism.PrismAnimations;

@TeleOp
public class TestPrismOpMode extends MMOpMode {
  GoBildaPrismDriver prism;

  PrismAnimations.Solid solid = new PrismAnimations.Solid(Color.BLUE);
  PrismAnimations.RainbowSnakes rainbowSnakes = new PrismAnimations.RainbowSnakes();
  public TestPrismOpMode() {
    super(OpModeType.NonCompetition.DEBUG, AllianceColor.BLUE);
  }

  @Override
  public void onInit() {
    prism = hardwareMap.get(GoBildaPrismDriver.class,"prism");
    prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1,rainbowSnakes);

  }

  @Override
  public void onPlayLoop() {
    prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, solid);
  }

  @Override
  public void onEnd() {
    super.onEnd();

    prism.clearAllAnimations();
  }
}
