package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
public class IntakeCommandGroup {
    static int waitTimeForBall = 300;

    public static Command FeedIntake(){
        return new SequentialCommandGroup(
        );
    }

//    public static Command FeedOneIntake(){
//        return new SequentialCommandGroup(
//        );
//    }
}
