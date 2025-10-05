package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.subsystems.ActiveIntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SpindexerSubsystem;

public class ShootCommandGroup {
    public static Command ShootFirst(){
        return new SequentialCommandGroup(
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.FIRSTPOS),
                ShooterSubsystem.getInstance().setPowerInstantCommand(1),
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.SCNDPOS),
                ShooterSubsystem.getInstance().setPowerInstantCommand(0)
                );
    }
    public static Command ShootScnd(){
        return new SequentialCommandGroup(
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.SCNDPOS),
                ShooterSubsystem.getInstance().setPowerInstantCommand(1),
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.THIRDPOS),
                ShooterSubsystem.getInstance().setPowerInstantCommand(0)
                );
    }
    public static Command ShootThird(){
        return new SequentialCommandGroup(
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.THIRDPOS),
                ShooterSubsystem.getInstance().setPowerInstantCommand(1),
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.FIRSTPOS),
                ShooterSubsystem.getInstance().setPowerInstantCommand(0)
                );
    }
}
