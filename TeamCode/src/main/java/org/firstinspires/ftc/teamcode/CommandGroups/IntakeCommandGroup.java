package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SpindexerSubsystem;

public class IntakeCommandGroup {
    public static Command FeedIntake(){
        return new SequentialCommandGroup(
                SpindexerSubsystem.getInstance().setPowerInstantCommand(0.4),
                new WaitUntilCommand(()->SpindexerSubsystem.getInstance().getZeroSwitch()),
                SpindexerSubsystem.getInstance().setPowerInstantCommand(0),
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.FIRSTPOS),
                IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                new WaitUntilCommand(()->SpindexerSubsystem.getInstance().getAlphaColor()<SpindexerSubsystem.ALPHA_TOLERANCE),
                IntakeSubsystem.getInstance().setPowerInstantCommand(0),
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.SCNDPOS),
                new WaitCommand(500),
                IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                new WaitUntilCommand(()->SpindexerSubsystem.getInstance().getAlphaColor()<SpindexerSubsystem.ALPHA_TOLERANCE),
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.THIRDPOS),
                IntakeSubsystem.getInstance().setPowerInstantCommand(0)
        );
    }

    public static Command FeedOneIntake(){
        return new SequentialCommandGroup(
                SpindexerSubsystem.getInstance().setPowerInstantCommand(0.4),
                new WaitUntilCommand(()->SpindexerSubsystem.getInstance().getZeroSwitch()),
                SpindexerSubsystem.getInstance().setPowerInstantCommand(0),
                IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                new WaitUntilCommand(()->SpindexerSubsystem.getInstance().getAlphaColor()<SpindexerSubsystem.ALPHA_TOLERANCE),
                IntakeSubsystem.getInstance().setPowerInstantCommand(0),
                SpindexerSubsystem.getInstance().setPowerInstantCommand(0.8),
                new WaitCommand(165)
        );
    }
}
