package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.Commands.WithFinally;
import org.firstinspires.ftc.teamcode.subsystems.FunnelStopperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FunnelSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

public class ShootCommandGroup {
  int timesBallDetected = 0;

  public static Command ShootAllClose() {
            return new SequentialCommandGroup(
              ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(60),
                //first ball
              new WaitUntilCommand(()-> ShooterSubsystem.getInstance().getPose() > 50),
              FunnelStopperSubsystem.getInstance().setPositionCommand(FunnelStopperSubsystem.stopperOpen),
              FunnelSubsystem.getInstance().setPowerInstantCommand(1),
              new WaitUntilCommand(()-> FunnelSubsystem.getInstance().getBeamSensor()),
              FunnelStopperSubsystem.getInstance().setPositionCommand(FunnelStopperSubsystem.stopperClose),
                //second ball
              new WaitUntilCommand(()-> ShooterSubsystem.getInstance().getPose() > 50),
              FunnelStopperSubsystem.getInstance().setPositionCommand(FunnelStopperSubsystem.stopperOpen),
              FunnelSubsystem.getInstance().setPowerInstantCommand(1),
              new WaitUntilCommand(()-> FunnelSubsystem.getInstance().getBeamSensor()),
              FunnelStopperSubsystem.getInstance().setPositionCommand(FunnelStopperSubsystem.stopperClose),
                //third ball
              new WaitUntilCommand(()-> ShooterSubsystem.getInstance().getPose() > 50),
              FunnelStopperSubsystem.getInstance().setPositionCommand(FunnelStopperSubsystem.stopperOpen),
              FunnelSubsystem.getInstance().setPowerInstantCommand(1),
              new WaitUntilCommand(()-> FunnelSubsystem.getInstance().getBeamSensor()),
              FunnelStopperSubsystem.getInstance().setPositionCommand(FunnelStopperSubsystem.stopperClose),
            );
  }
  public static Command ShootAllFar() {
    return new SequentialCommandGroup(
            ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(60),
            new WaitUntilCommand(()-> ShooterSubsystem.getInstance().getPose() > 50),
            FunnelStopperSubsystem.getInstance().setPositionCommand(FunnelStopperSubsystem.stopperOpen),
            FunnelSubsystem.getInstance().setPowerInstantCommand(1),
            new WaitUntilCommand()

    );
  }
}
