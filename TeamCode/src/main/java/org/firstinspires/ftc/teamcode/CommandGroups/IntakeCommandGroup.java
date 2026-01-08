package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.Commands.WithFinally;
import org.firstinspires.ftc.teamcode.subsystems.IndexerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TransferSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
public class IntakeCommandGroup {
    static int waitTimeForBall = 300;

    public static Command FeedIntake(){
        return new WithFinally(
             new SequentialCommandGroup(
                IndexerSubsystem.getInstance().setPowerInstantCommand(-1),
                new ParallelCommandGroup(
                        IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                        TransferSubsystem.getInstance().setPowerInstantCommand(0.2)
                )
            ),
            ()->{   IntakeSubsystem.getInstance().stop();
                    TransferSubsystem.getInstance().stop();
                    IndexerSubsystem.getInstance().stop();}
        );
    }

}
