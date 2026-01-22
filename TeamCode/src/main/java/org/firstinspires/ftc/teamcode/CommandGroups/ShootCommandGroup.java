package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.Commands.WithFinally;
import org.firstinspires.ftc.teamcode.subsystems.IndexerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TransferSubsystem;

import java.util.function.BooleanSupplier;

public class ShootCommandGroup {

  public static Command upShoot() {
      return new WithFinally(
              new ParallelCommandGroup(
//                      MMDrivetrain.getInstance().HoldPointCommand(),
                      //todo ideal: shooter target -> by pose
                      IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                      TransferSubsystem.getInstance().setPowerInstantCommand(1),
                      IndexerSubsystem.getInstance().setPowerInstantCommand(1)),
              ()-> new ParallelCommandGroup(
                      IntakeSubsystem.getInstance().stopCommand(),
                      TransferSubsystem.getInstance().stopCommand(),
                      IndexerSubsystem.getInstance().stopCommand())
      );
  }
    public static Command upShootThird(BooleanSupplier third) {
        return new WithFinally(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
//                      MMDrivetrain.getInstance().HoldPointCommand(),
                        IntakeSubsystem.getInstance().setPowerInstantCommand(0.4),
                        TransferSubsystem.getInstance().setPowerInstantCommand(1),
                        IndexerSubsystem.getInstance().setPowerInstantCommand(1)),
                        new WaitUntilCommand(third),
                        IntakeSubsystem.getInstance().setPowerInstantCommand(1)),
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
                IntakeSubsystem.getInstance().stopCommand());
    }
    public static Command StartWheelClose() {
        return ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(50);}
    public static Command StartWheelFar() {
        return ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(85);}


}
