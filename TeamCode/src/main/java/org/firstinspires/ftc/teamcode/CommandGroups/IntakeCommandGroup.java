package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import org.firstinspires.ftc.teamcode.subsystems.AccelSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.BallStopperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PrismSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

public class IntakeCommandGroup {

    public static Command dumbFeed() {
        return new ParallelCommandGroup(
                BallStopperSubsystem.getInstance().close(),
                IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                AccelSubsystem.getInstance().setPowerInstantCommand(0.5)
        );
    }

    public static Command smartFeed() {
        return new SequentialCommandGroup(
                PrismSubsystem.getInstance().blueCommand().alongWith(
                dumbFeed()),
                new WaitUntilCommand(()->BallStopperSubsystem.getInstance().getState()),
//                new TimedConditionCommand(()->/*sensor*/false,2),
                PrismSubsystem.getInstance().yellowCommand(),
                AccelSubsystem.getInstance().stopCommand()
                );
    }

    public static Command stopIntake() {
        return new ParallelCommandGroup(
                IntakeSubsystem.getInstance().stopCommand(),
                AccelSubsystem.getInstance().stopCommand()
//                BallStopperSubsystem.getInstance().close()
        );
    }

    public static Command outIntake() {
        return new ParallelCommandGroup(
                IntakeSubsystem.getInstance().setPowerInstantCommand(-1),
                AccelSubsystem.getInstance().setPowerInstantCommand(-1)
        );
    }

    public static Command stopAll() {
        return new ParallelCommandGroup(
                AccelSubsystem.getInstance().stopCommand(),
                IntakeSubsystem.getInstance().stopCommand(),
                ShooterSubsystem.getInstance().rest()
        );

    }
}

