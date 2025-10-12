package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SpindexerSubsystem;

public class ShootCommandGroup {
    public static Command ShootAll(){
        return new SequentialCommandGroup(
                ShooterSubsystem.  getInstance().getToAndHoldSetPointCommand(40).withTimeout(700),
                new WaitCommand(1500),
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.THIRDPOS),
                new WaitCommand(800),
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.SCNDPOS),
                new WaitCommand(800),
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.FIRSTPOS),
                new WaitCommand(5000),
                ShooterSubsystem.getInstance().setPowerInstantCommand(0)
        );

    }
}
