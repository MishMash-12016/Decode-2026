package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;


import org.firstinspires.ftc.teamcode.subsystems.BallStopperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FunnelSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;


public class IntakeCommandGroup {

    public static Command dumbFeed() {
        return new ParallelCommandGroup(
                FunnelSubsystem.getInstance().setPowerInstantCommand(1)
        );
    }

    public static Command smartFeed() {
        return new SequentialCommandGroup(
        );
    }

    public static Command stopIntake() {
        return new ParallelCommandGroup(
                FunnelSubsystem.getInstance().stopCommand(),
                BallStopperSubsystem.getInstance().close()
        );
    }

    public static Command outIntake() {
        return new ParallelCommandGroup(
                FunnelSubsystem.getInstance().setPowerInstantCommand(-1)
        );
    }

    public static Command stopAll() {
        return new ParallelCommandGroup(
                FunnelSubsystem.getInstance().stopCommand(),
                ShooterSubsystem.getInstance().stopCommand()
        );

    }
}

