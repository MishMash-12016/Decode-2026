package org.firstinspires.ftc.teamcode.Subsystems;

import Ori.Coval.Logging.Logger.KoalaLog;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
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
    super("prismSubsystem");
    prism = MMRobot.getInstance().currentOpMode.hardwareMap.get(GoBildaPrismDriver.class, "prism");
    prism.setStripLength(36);
    prism.setTargetFPS(60);
    buildArtboards();
  }

  private void buildArtboards() {

    // SOLID RED ARTBOARD
    prism.clearAllAnimations();
    prism.insertAndUpdateAnimation(
        GoBildaPrismDriver.LayerHeight.LAYER_0, new PrismAnimations.Solid(Color.RED, 15));
    prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);

    // SOLID GREEN ARTBOARD
    prism.clearAllAnimations();
    prism.insertAndUpdateAnimation(
        GoBildaPrismDriver.LayerHeight.LAYER_0, new PrismAnimations.Solid(Color.GREEN, 8));
    prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_1);

    // SOLID BLUE ARTBOARD
    prism.clearAllAnimations();
    prism.insertAndUpdateAnimation(
        GoBildaPrismDriver.LayerHeight.LAYER_0, new PrismAnimations.Solid(Color.BLUE, 10));
    prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_2);

    // BLINK GREEN ARTBOARD
    prism.clearAllAnimations();
    prism.insertAndUpdateAnimation(
        GoBildaPrismDriver.LayerHeight.LAYER_0,
        new PrismAnimations.Blink(Color.GREEN, Color.TRANSPARENT, 400, 800));
    prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_4);

    // BLANK ARTBOARD
    prism.clearAllAnimations();
    prism.insertAndUpdateAnimation(
        GoBildaPrismDriver.LayerHeight.LAYER_0, new PrismAnimations.Solid(Color.TRANSPARENT, 0));
    prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_5);

    prism.clearAllAnimations();
  }

  public Command green() {
    return new InstantCommand(
        () -> prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_1));
  }

  public Command blue() {
    return new InstantCommand(
        () -> prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_2));
  }

  public Command off() {
    return new InstantCommand(
        () -> prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_5));
  }

  private void blinkGreen() {
    prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_4);
  }

  private void red() {
    prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);
  }

  public Command isReady() {
    return new RunCommand(
        () -> {
          double shooterError = Math.abs(ShooterSubsystem.getInstance().getError());
          double headingError = Math.abs(MMDrivetrain.getInstance().headingPID.getError());
          double headingTol = MMDrivetrain.headingTolerance;

          if (KoalaLog.log(
              "yitzhak is gay: ", Math.abs(shooterError) < 2 && headingError < headingTol, true))
            blinkGreen();
          else red();
        },
        this);
  }

  @Override
  public void reset() {instance = null;}
}
