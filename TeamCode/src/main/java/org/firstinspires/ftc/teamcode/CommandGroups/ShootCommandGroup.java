package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.TimedConditionCommand;
import org.firstinspires.ftc.teamcode.subsystems.IndexerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TransferSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

public class ShootCommandGroup {
  public int timesBallDetected = 0;

  public static Command ShootAllClose() {
            return new SequentialCommandGroup(
//              MMDrivetrain.getInstance().getFollower().holdPoint(MMDrivetrain.getInstance().getPose()),
              //todo ideal: shooter target -> by pose
              ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(50),
              new WaitUntilCommand(()-> ShooterSubsystem.getInstance().getPose() > 45),
              TransferSubsystem.getInstance().setPowerInstantCommand(1),
              IndexerSubsystem.getInstance().setPowerInstantCommand(1),
              IntakeSubsystem.getInstance().setPowerInstantCommand(1)
            );
  }

    public static Command StopShoot() {
        return new SequentialCommandGroup(
                TransferSubsystem.getInstance().setPowerInstantCommand(0),
                IndexerSubsystem.getInstance().setPowerInstantCommand(0),
                IntakeSubsystem.getInstance().setPowerInstantCommand(0)
        );
    }

//    public static Command ShootAllFar() {
//    return new SequentialCommandGroup(
////            MMDrivetrain.getInstance().getFollower().holdPoint(MMDrivetrain.getInstance().getPose()),
//            ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(60),
//            //first ball
//            //todo ideal: shooter target -> by pose
//            new WaitUntilCommand(()-> ShooterSubsystem.getInstance().getPose() > 55),
//            TransferSubsystem.getInstance().setPowerInstantCommand(1),
//            IndexerSubsystem.getInstance().setPositionCommand(IndexerSubsystem.stopperOpen),
//            new TimedConditionCommand(()-> !TransferSubsystem.getInstance().getBeamSensor(),0.2),
//            new WaitUntilCommand(()-> TransferSubsystem.getInstance().getBeamSensor()),
//            IndexerSubsystem.getInstance().setPositionCommand(IndexerSubsystem.stopperClose),
//            //second ball
//            new WaitUntilCommand(()-> ShooterSubsystem.getInstance().getPose() > 55),
//            IndexerSubsystem.getInstance().setPositionCommand(IndexerSubsystem.stopperOpen),
//            new TimedConditionCommand(()-> !TransferSubsystem.getInstance().getBeamSensor(),0.2),
//            new WaitUntilCommand(()-> TransferSubsystem.getInstance().getBeamSensor()),
//            IndexerSubsystem.getInstance().setPositionCommand(IndexerSubsystem.stopperClose),
//            //third ball
//            new WaitUntilCommand(()-> ShooterSubsystem.getInstance().getPose() > 55),
//            IndexerSubsystem.getInstance().setPositionCommand(IndexerSubsystem.stopperOpen),
//            new TimedConditionCommand(()-> !TransferSubsystem.getInstance().getBeamSensor(),0.2),
//            new WaitUntilCommand(()-> TransferSubsystem.getInstance().getBeamSensor()),
//            IndexerSubsystem.getInstance().setPositionCommand(IndexerSubsystem.stopperClose),
//            TransferSubsystem.getInstance().stopCommand(),
//            ShooterSubsystem.getInstance().stopCommand()
//    );
//  }
}
