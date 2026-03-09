package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.TimedConditionCommand;
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
                AccelSubsystem.getInstance().setPowerInstantCommand(0.9)
        );
    }

    public static Command smartFeed() {
        return new SequentialCommandGroup(
                dumbFeed().alongWith(
                PrismSubsystem.getInstance().yellow()),
                new TimedConditionCommand(()->IntakeSubsystem.getInstance().getFrstState(),300),
                new WaitCommand(100),
                new WaitUntilCommand(()->IntakeSubsystem.getInstance().getFrstState()),
                PrismSubsystem.getInstance().blue());
    }


    public static Command stopIntake() {
    return new ParallelCommandGroup(
        IntakeSubsystem.getInstance().stopCommand(),
        AccelSubsystem.getInstance().stopCommand()
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
                ShooterSubsystem.getInstance().rest(),
                PrismSubsystem.getInstance().off()
        );

    }
}

