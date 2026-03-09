package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.MMSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.Prism.Color;
import org.firstinspires.ftc.teamcode.Libraries.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.Libraries.Prism.PrismAnimations;
import org.firstinspires.ftc.teamcode.MMRobot;

public class PrismSubsystem extends MMSubsystem {
  private static PrismSubsystem instance;

  public static PrismSubsystem getInstance() {
    if (instance == null) {
      instance = new PrismSubsystem();
    }
    return instance;
  }

  private final GoBildaPrismDriver prism;

  public PrismSubsystem() {
    prism = MMRobot.getInstance().currentOpMode.hardwareMap.get(GoBildaPrismDriver.class, "prism");
    prism.setStripLength(36);
    prism.setTargetFPS(60);
    buildArtboards();
  }

  private void buildArtboards() {

    //SOLID RED ARTBOARD
    prism.clearAllAnimations();
    prism.insertAndUpdateAnimation(
        GoBildaPrismDriver.LayerHeight.LAYER_0, new PrismAnimations.Solid(Color.RED,8));
    prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);

    //SOLID GREEN ARTBOARD
    prism.clearAllAnimations();
    prism.insertAndUpdateAnimation(
        GoBildaPrismDriver.LayerHeight.LAYER_0, new PrismAnimations.Solid(Color.GREEN,8));
    prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_1);

    //SOLID BLUE ARTBOARD
    prism.clearAllAnimations();
    prism.insertAndUpdateAnimation(
        GoBildaPrismDriver.LayerHeight.LAYER_0, new PrismAnimations.Solid(Color.BLUE,8));
    prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_2);

    //SOLID YELLOW ARTBOARD
    prism.clearAllAnimations();
    prism.insertAndUpdateAnimation(
        GoBildaPrismDriver.LayerHeight.LAYER_0, new PrismAnimations.Solid(Color.YELLOW,8));
    prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_3);


    //BLINK ARTBOARD
    prism.clearAllAnimations();
    PrismAnimations.Blink blinkRed = new PrismAnimations.Blink(Color.BLUE, Color.ORANGE, 200,200);

    prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, blinkRed);
    prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_4);

    prism.clearAllAnimations();

  }



  public Command red() {
    return new InstantCommand(
            () -> prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0));
  }

  public Command green() {
    return new InstantCommand(
        () -> prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_1));
  }

  public Command blue() {
    return new InstantCommand(
        () -> prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_2));
  }

  public Command yellow() {
    return new InstantCommand(
        () -> prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_3));
  }
  public Command blinkBlueOrange() {
    return new InstantCommand(
            () -> prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_4)
    );
  }
  public Command off() {
    return new InstantCommand(prism::clearAllAnimations,this);
  }
  public Command inSpeed(){
    ShooterSubsystem shooter = ShooterSubsystem.getInstance();
    if (Math.abs(shooter.getSetPoint() - shooter.getVelocity()) < 2)
      return PrismSubsystem.getInstance().green();
    return PrismSubsystem.getInstance().red();
  }


  @Override
  public void resetHub() {}

}
