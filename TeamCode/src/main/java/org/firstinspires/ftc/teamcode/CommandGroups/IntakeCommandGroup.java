package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.Commands.WithFinally;
import org.firstinspires.ftc.teamcode.subsystems.IndexerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TransferSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
public class IntakeCommandGroup {

    public static Command FeedIntake() {
        return new ParallelCommandGroup(
                IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                TransferSubsystem.getInstance().setPowerInstantCommand(0.7),
                IndexerSubsystem.getInstance().setPowerInstantCommand(-1));
    }
    public static Command StopIntake() {
        return new ParallelCommandGroup(
                IntakeSubsystem.getInstance().stopCommand(),
                TransferSubsystem.getInstance().stopCommand(),
                IndexerSubsystem.getInstance().stopCommand());
    }

    public static Command OutIntake() {
        return new ParallelCommandGroup(
                IntakeSubsystem.getInstance().setPowerInstantCommand(-1),
                TransferSubsystem.getInstance().setPowerInstantCommand(-1),
                IndexerSubsystem.getInstance().setPowerInstantCommand(-1));
    }

    public static Command StopAll() {
        return new ParallelCommandGroup(
                TransferSubsystem.getInstance().stopCommand(),
                IndexerSubsystem.getInstance().stopCommand(),
                IntakeSubsystem.getInstance().stopCommand(),
                ShooterSubsystem.getInstance().stopCommand());
    }
}

