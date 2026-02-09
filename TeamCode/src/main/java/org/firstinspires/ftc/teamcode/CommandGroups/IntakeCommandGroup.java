package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.TimedConditionCommand;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LeftStopperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.MiddleStopperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.RightStopperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;


public class IntakeCommandGroup {


    public static Command stoppersStop() {
        return new ParallelCommandGroup(
                RightStopperSubsystem.getInstance().stop(),
                MiddleStopperSubsystem.getInstance().stop(),
                LeftStopperSubsystem.getInstance().stop()
        );
    }

    public static Command dumbFeed() {
        return new ParallelCommandGroup(
                stoppersStop(),
                IntakeSubsystem.getInstance().setPowerInstantCommand(1)
        );
    }

    public static Command stopIntake() {
        return new ParallelCommandGroup(
                IntakeSubsystem.getInstance().stopCommand()
        );
    }

    public static Command outIntake() {
        return new ParallelCommandGroup(
                IntakeSubsystem.getInstance().setPowerInstantCommand(-1)
        );
    }

    public static Command stopAll() {
        return new ParallelCommandGroup(
                IntakeSubsystem.getInstance().stopCommand(),
                ShooterSubsystem.getInstance().stopCommand()
        );

    }
}

