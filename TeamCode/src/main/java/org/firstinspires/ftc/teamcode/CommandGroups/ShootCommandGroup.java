package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import org.firstinspires.ftc.teamcode.Subsystems.AccelSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.BallStopperSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;

public class ShootCommandGroup {

  public static Command ballWithControl() {
    ShooterSubsystem shooter = ShooterSubsystem.getInstance();
    return new SequentialCommandGroup(
        new WaitUntilCommand(() -> shooter.getSetPoint() < shooter.getVelocity() + 1),
        smartUpShoot(),
        new WaitUntilCommand(() -> shooter.getSetPoint() > shooter.getVelocity()),
        stopShoot());
  }

  public static Command upShoot() {
        return new ParallelCommandGroup(
            BallStopperSubsystem.getInstance().open(),
            IntakeSubsystem.getInstance().setPowerInstantCommand(1),
            AccelSubsystem.getInstance().setPowerInstantCommand(1)
        );
  }

  public static Command smartUpShoot() {
    return new SequentialCommandGroup(
        BallStopperSubsystem.getInstance().open(),
        new WaitCommand(200),
        upShoot(),
        new WaitUntilCommand(() -> !IntakeSubsystem.getInstance().getScndState()),
        new WaitCommand(150),
        stopShoot());
  }

  public static Command twoOneShoot() {
    return BallStopperSubsystem.getInstance().open()
        .andThen(
            new SequentialCommandGroup(AccelSubsystem.getInstance().setPowerInstantCommand(1), smartUpShoot()));
  }

  public static Command stopShoot() {
    return new ParallelCommandGroup(
        AccelSubsystem.getInstance().stopCommand(),
        IntakeSubsystem.getInstance().stopCommand());
  }
}

/*
if ("robot" !work){
       "work"
   } */
