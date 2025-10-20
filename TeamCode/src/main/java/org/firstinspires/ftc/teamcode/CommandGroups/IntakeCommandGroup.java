package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SpindexerSubsystem;
public class IntakeCommandGroup {
    static int waitTimeForBall = 300;
    static int ballDis = 18;

    public static Command FeedIntake(){
        return new SequentialCommandGroup(
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.FIRSTPOS).withTimeout(700),
                IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                new WaitUntilCommand(()->(ballDis > SpindexerSubsystem.getInstance().getDistance())),
                new WaitCommand(waitTimeForBall),
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.SCNDPOS).withTimeout(700),
                new WaitCommand(waitTimeForBall),
                new WaitUntilCommand(()->(ballDis > SpindexerSubsystem.getInstance().getDistance())),
                new WaitCommand(waitTimeForBall),
                SpindexerSubsystem.getInstance().getToAndHoldSetPointCommand(SpindexerSubsystem.THIRDPOS).withTimeout(700),
                new WaitUntilCommand(()->(ballDis > SpindexerSubsystem.getInstance().getDistance())),
                IntakeSubsystem.getInstance().setPowerInstantCommand(0)
        );
    }

//    public static Command FeedOneIntake(){
//        return new SequentialCommandGroup(
//        );
//    }
}
