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
                ShooterSubsystem.  getInstance().getToAndHoldSetPointCommand(40).withTimeout(600),
                SpindexerSubsystem.getInstance().setPowerInstantCommand(-1),
                new WaitCommand(5000),
                ShooterSubsystem.getInstance().setPowerInstantCommand(0)
        );

    }
}
