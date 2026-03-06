package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import org.firstinspires.ftc.teamcode.subsystems.AccelSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.BallStopperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PrismSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

public class ShootCommandGroup {

  public static Command ballWithControl() {
    ShooterSubsystem shooter = ShooterSubsystem.getInstance();
    return new SequentialCommandGroup(
        new WaitUntilCommand(() -> shooter.getSetPoint() < shooter.getVelocity() + 1),
        upShoot(),
        new WaitUntilCommand(() -> shooter.getSetPoint() > shooter.getVelocity()),
        stopShoot());
  }

  public static Command upShoot() {
    return new SequentialCommandGroup(
        BallStopperSubsystem.getInstance().open(),
        new ParallelCommandGroup(
            BallStopperSubsystem.getInstance().open(),
            IntakeSubsystem.getInstance().setPowerInstantCommand(1),
            AccelSubsystem.getInstance().setPowerInstantCommand(1)),
        new WaitUntilCommand(() -> !IntakeSubsystem.getInstance().getState()),
        new WaitCommand(300),
        stopShoot());
  }

  public static Command twoOneShoot() {
    return BallStopperSubsystem.getInstance().open()
        .andThen(
            new SequentialCommandGroup(AccelSubsystem.getInstance().setPowerInstantCommand(1), upShoot()));
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
