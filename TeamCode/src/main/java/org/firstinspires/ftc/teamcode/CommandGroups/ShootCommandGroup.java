package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SpindexerSubsystem;

public class ShootCommandGroup {

  static double SpindexerTempStartPose = 0;

  public static Command ShootAll() {
    return new ParallelRaceGroup(
            ShooterSubsystem.getInstance().setPowerRunCommand(1),
            new SequentialCommandGroup(
                    new InstantCommand(() -> SpindexerTempStartPose = SpindexerSubsystem.getInstance().getRawPose()),
                    new WaitCommand(3000),
                    SpindexerSubsystem.getInstance().setPowerRunCommand(-1)
            ).interruptOn(() -> SpindexerTempStartPose -
                    SpindexerSubsystem.getInstance().getRawPose() > 360))
            .andThen(
                    SpindexerSubsystem.getInstance().stopInstantCommand(),
                    ShooterSubsystem.getInstance().stopInstantCommand()
            );
  }
}
