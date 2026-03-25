package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.Commands.TimedConditionCommand;
import org.firstinspires.ftc.teamcode.Subsystems.AccelSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.BallStopperSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.PrismSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;

public class IntakeCommandGroup {

     private static Command dumbFeed() {
        return new ParallelCommandGroup(
                BallStopperSubsystem.getInstance().close(),
                IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                AccelSubsystem.getInstance().setPowerInstantCommand(0.9)
        );
    }

        public static Command smartFeed() {
            return new SequentialCommandGroup(
                    dumbFeed().alongWith(PrismSubsystem.getInstance().off()),
                    new TimedConditionCommand(()->IntakeSubsystem.getInstance().getScndState(),500),
                    AccelSubsystem.getInstance().stopCommand(),
                    new WaitCommand(200),
                    new WaitUntilCommand(()->IntakeSubsystem.getInstance().getFrstState()),
                    PrismSubsystem.getInstance().blue());
        }

    public static Command autoFeed() {
        return new SequentialCommandGroup(
                dumbFeed().alongWith(PrismSubsystem.getInstance().off()),
                new TimedConditionCommand(()->IntakeSubsystem.getInstance().getFrstState(),750),
                AccelSubsystem.getInstance().stopCommand(),
                new WaitCommand(300),
                new WaitUntilCommand(()->IntakeSubsystem.getInstance().getFrstState()),
                PrismSubsystem.getInstance().blue(),
                new WaitCommand(500),
                stopIntake()
        );

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

