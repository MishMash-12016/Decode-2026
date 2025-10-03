package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.subsystems.ActiveIntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SpindexerSubsystem;

public class IntakeCommandGroup {
    public static Command intakeCommandGroup(){
        return new SequentialCommandGroup(
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.FIRSTPOS),
                ActiveIntakeSubsystem.getInstance().setPowerInstantCommand(1),
                new WaitCommand(1),//until ballin == T
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.SCNDPOS),
                ActiveIntakeSubsystem.getInstance().setPowerInstantCommand(1),
                new WaitCommand(1),//until ballin == T
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.THIRDPOS)
        );
    }
}
