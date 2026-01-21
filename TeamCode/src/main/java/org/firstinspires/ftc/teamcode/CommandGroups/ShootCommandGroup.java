package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.Commands.WithFinally;
import org.firstinspires.ftc.teamcode.subsystems.IndexerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TransferSubsystem;

public class ShootCommandGroup {
  public int timesBallDetected = 0;

  public static Command PrepShoot() {
      return new WithFinally(
              new ParallelCommandGroup(
//                      MMDrivetrain.getInstance().HoldPointCommand(),
                      //todo ideal: shooter target -> by pose
                      IntakeSubsystem.getInstance().setPowerInstantCommand(-0.5),
                      TransferSubsystem.getInstance().setPowerInstantCommand(1),
                      IndexerSubsystem.getInstance().setPowerInstantCommand(1)),
              ()-> new ParallelCommandGroup(
                      IntakeSubsystem.getInstance().stopCommand(),
                      TransferSubsystem.getInstance().stopCommand(),
                      IndexerSubsystem.getInstance().stopCommand())
      );
  }

    public static Command StopShoot() {
        return new ParallelCommandGroup(
                TransferSubsystem.getInstance().stopCommand(),
                IndexerSubsystem.getInstance().stopCommand(),
                IntakeSubsystem.getInstance().stopCommand()
        );
    }
//    public static Command StartWheelFar() {
//        return ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(70);
//    }
    public static Command StartWheelClose() {
        return ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(50);
    }

}
