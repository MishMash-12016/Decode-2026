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
    prism.setStripLength(60);
    prism.setTargetFPS(60);
    buildArtboards();
  }

  private void buildArtboards() {

    // ---------- ARTBOARD 0 : SOLID RED ----------
    prism.clearAllAnimations();
    prism.insertAndUpdateAnimation(
        GoBildaPrismDriver.LayerHeight.LAYER_0, new PrismAnimations.Solid(Color.RED));
    prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);

    // ---------- ARTBOARD 1 : SOLID GREEN ----------
    prism.clearAllAnimations();
    prism.insertAndUpdateAnimation(
        GoBildaPrismDriver.LayerHeight.LAYER_0, new PrismAnimations.Solid(Color.GREEN));
    prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_1);

    // ---------- ARTBOARD 2 : SOLID BLUE ----------
    prism.clearAllAnimations();
    prism.insertAndUpdateAnimation(
        GoBildaPrismDriver.LayerHeight.LAYER_0, new PrismAnimations.Solid(Color.BLUE));
    prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_2);

    // ---------- ARTBOARD 3 : SOLID YELLOW ----------
    prism.clearAllAnimations();
    prism.insertAndUpdateAnimation(
        GoBildaPrismDriver.LayerHeight.LAYER_0, new PrismAnimations.Solid(Color.YELLOW));
    prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_3);


    // ---------- ARTBOARD 4 : BLINK RED ----------
    prism.clearAllAnimations();
    PrismAnimations.Blink blinkRed =
            new PrismAnimations.Blink(Color.RED, Color.ORANGE, 1000, 500);

    prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, blinkRed);
    prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_4);

    prism.clearAllAnimations();

  }



  public Command redCommand() {
    return new InstantCommand(
            () -> prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0));
  }

  public Command greenCommand() {
    return new InstantCommand(
        () -> prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_1));
  }

  public Command blueCommand() {
    return new InstantCommand(
        () -> prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_2));
  }

  public Command yellowCommand() {
    return new InstantCommand(
        () -> prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_3));
  }
  public Command blinkRed() {
    return new InstantCommand(
            () -> prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_4)
    );
  }
  public Command off() {
    return new InstantCommand(prism::clearAllAnimations);
  }


  @Override
  public void resetHub() {}

}
