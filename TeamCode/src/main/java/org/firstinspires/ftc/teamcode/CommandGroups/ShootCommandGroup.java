package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
public class ShootCommandGroup {


  public static Command ShootAll() {
    return new ParallelRaceGroup(
            ShooterSubsystem.getInstance().setPowerRunCommand(1),
            new SequentialCommandGroup(
                    ShooterSubsystem.getInstance().stopInstantCommand()
            )
            );
  }
}
