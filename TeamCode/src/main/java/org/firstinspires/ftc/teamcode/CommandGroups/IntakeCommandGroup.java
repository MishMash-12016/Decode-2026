package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.TimedConditionCommand;
import org.firstinspires.ftc.teamcode.subsystems.BallStopperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FunnelSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;


public class IntakeCommandGroup {

    public static Command dumbFeed() {
        return new ParallelCommandGroup(
//                BallStopperSubsystem.getInstance().close(),
                IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                FunnelSubsystem.getInstance().setPowerInstantCommand(0.4)
        );
    }

    public static Command smartFeed() {
        return new SequentialCommandGroup(
                dumbFeed(),
                new WaitUntilCommand(()->/*sensor*/false),
                FunnelSubsystem.getInstance().setPowerInstantCommand(0.1),
                new TimedConditionCommand(()->/*sensor*/false,2),
                stopIntake()
                );
    }

    public static Command stopIntake() {
        return new ParallelCommandGroup(
                IntakeSubsystem.getInstance().stopCommand(),
                FunnelSubsystem.getInstance().stopCommand()
//                BallStopperSubsystem.getInstance().close()
        );
    }

    public static Command outIntake() {
        return new ParallelCommandGroup(
                IntakeSubsystem.getInstance().setPowerInstantCommand(-1),
                FunnelSubsystem.getInstance().setPowerInstantCommand(-1)
        );
    }

    public static Command stopAll() {
        return new ParallelCommandGroup(
                FunnelSubsystem.getInstance().stopCommand(),
                IntakeSubsystem.getInstance().stopCommand(),
                ShooterSubsystem.getInstance().stopCommand()
        );

    }
}

