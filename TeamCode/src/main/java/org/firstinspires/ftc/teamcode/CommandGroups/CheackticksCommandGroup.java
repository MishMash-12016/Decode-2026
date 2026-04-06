package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.Commands.TimedConditionCommand;
import org.firstinspires.ftc.teamcode.Subsystems.AccelSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Movemotorsubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.PrismSubsystem;

public class CheackticksCommandGroup {

    public static Command Runmotor() {
        return new SequentialCommandGroup(
                Movemotorsubsystem.getInstance().setPowerInstantCommand(1),
                new WaitCommand(3000),
                Movemotorsubsystem.getInstance().setPowerInstantCommand(0)
        );
    }

}
