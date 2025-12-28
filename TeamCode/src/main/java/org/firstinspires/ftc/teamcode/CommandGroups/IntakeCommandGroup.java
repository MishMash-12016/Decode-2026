package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.Commands.WithFinally;
import org.firstinspires.ftc.teamcode.subsystems.FunnelStopperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FunnelSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
public class IntakeCommandGroup {
    static int waitTimeForBall = 300;

    public static Command FeedIntake(){
        return new WithFinally(
             new SequentialCommandGroup(
                FunnelStopperSubsystem.getInstance().setPositionCommand(FunnelStopperSubsystem.stopperClose),
                new ParallelCommandGroup(
                        IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                        FunnelSubsystem.getInstance().setPowerInstantCommand(0.3)
                )
            ),
            ()->{IntakeSubsystem.getInstance().stop();
                FunnelSubsystem.getInstance().stop();}
        );
    }

}
