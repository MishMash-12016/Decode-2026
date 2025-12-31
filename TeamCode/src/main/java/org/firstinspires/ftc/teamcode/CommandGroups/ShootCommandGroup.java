package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.TimedConditionCommand;
import org.firstinspires.ftc.teamcode.subsystems.FunnelStopperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FunnelSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

public class ShootCommandGroup {
  public int timesBallDetected = 0;

  public static Command ShootAllClose() {
            return new SequentialCommandGroup(
//              MMDrivetrain.getInstance().getFollower().holdPoint(MMDrivetrain.getInstance().getPose()),
              //todo ideal: shooter target -> by pose
              ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(50),
              new WaitUntilCommand(()-> ShooterSubsystem.getInstance().getPose() > 45),
              FunnelSubsystem.getInstance().setPowerInstantCommand(1),
              FunnelStopperSubsystem.getInstance().setPositionCommand(FunnelStopperSubsystem.stopperOpen),
              new TimedConditionCommand(()-> FunnelSubsystem.getInstance().getBeamSensor(),2),
              FunnelStopperSubsystem.getInstance().setPositionCommand(FunnelStopperSubsystem.stopperClose),
              FunnelSubsystem.getInstance().stopCommand(),
              ShooterSubsystem.getInstance().stopCommand()
            );
  }

    public static Command ShootAllFar() {
    return new SequentialCommandGroup(
//            MMDrivetrain.getInstance().getFollower().holdPoint(MMDrivetrain.getInstance().getPose()),
            ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(60),
            //first ball
            //todo ideal: shooter target -> by pose
            new WaitUntilCommand(()-> ShooterSubsystem.getInstance().getPose() > 55),
            FunnelSubsystem.getInstance().setPowerInstantCommand(1),
            FunnelStopperSubsystem.getInstance().setPositionCommand(FunnelStopperSubsystem.stopperOpen),
            new TimedConditionCommand(()-> !FunnelSubsystem.getInstance().getBeamSensor(),0.2),
            new WaitUntilCommand(()-> FunnelSubsystem.getInstance().getBeamSensor()),
            FunnelStopperSubsystem.getInstance().setPositionCommand(FunnelStopperSubsystem.stopperClose),
            //second ball
            new WaitUntilCommand(()-> ShooterSubsystem.getInstance().getPose() > 55),
            FunnelStopperSubsystem.getInstance().setPositionCommand(FunnelStopperSubsystem.stopperOpen),
            new TimedConditionCommand(()-> !FunnelSubsystem.getInstance().getBeamSensor(),0.2),
            new WaitUntilCommand(()-> FunnelSubsystem.getInstance().getBeamSensor()),
            FunnelStopperSubsystem.getInstance().setPositionCommand(FunnelStopperSubsystem.stopperClose),
            //third ball
            new WaitUntilCommand(()-> ShooterSubsystem.getInstance().getPose() > 55),
            FunnelStopperSubsystem.getInstance().setPositionCommand(FunnelStopperSubsystem.stopperOpen),
            new TimedConditionCommand(()-> !FunnelSubsystem.getInstance().getBeamSensor(),0.2),
            new WaitUntilCommand(()-> FunnelSubsystem.getInstance().getBeamSensor()),
            FunnelStopperSubsystem.getInstance().setPositionCommand(FunnelStopperSubsystem.stopperClose),
            FunnelSubsystem.getInstance().stopCommand(),
            ShooterSubsystem.getInstance().stopCommand()
    );
  }
}
