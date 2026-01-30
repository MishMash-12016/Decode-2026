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

public class ShootCommandGroup {

    public static Command BallWithControl() {
        return new SequentialCommandGroup(
                new WaitUntilCommand(
                        ()-> ShooterSubsystem.getInstance().getSetPoint() < ShooterSubsystem.getInstance().getVelocity()
                ),
                DumbUpShoot(),
                new WaitUntilCommand(
                        ()-> ShooterSubsystem.getInstance().getSetPoint() > ShooterSubsystem.getInstance().getVelocity()
                ),
                StopShoot()
        );
    }

    public static Command DumbUpShoot() {
                return new ParallelCommandGroup(
//                      MMDrivetrain.getInstance().HoldPointCommand(),
                        //todo ideal: shooter target -> by pose
                        IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                        TransferSubsystem.getInstance().setPowerInstantCommand(1),
                        IndexerSubsystem.getInstance().setPowerInstantCommand(1)
        );
    }


    public static Command SmartUpShoot() {
                return new SequentialCommandGroup(
                        /*new WaitUntilCommand(
                                ()-> ShooterSubsystem.getInstance().getSetPoint() < ShooterSubsystem.getInstance().getVelocity() + 3
                        ),
                        DumbUpShoot(),
                        new WaitUntilCommand(
                                ()-> ShooterSubsystem.getInstance().getSetPoint() > ShooterSubsystem.getInstance().getVelocity()
                        ),
                        StopShoot(),
                        new WaitUntilCommand(
                                ()-> ShooterSubsystem.getInstance().getSetPoint() < ShooterSubsystem.getInstance().getVelocity() + 3
                        ),
                        DumbUpShoot(),
                        new WaitUntilCommand(
                                ()-> ShooterSubsystem.getInstance().getSetPoint() > ShooterSubsystem.getInstance().getVelocity()
                        ),
                        StopShoot(),
                        new WaitUntilCommand(
                                ()-> ShooterSubsystem.getInstance().getSetPoint() < ShooterSubsystem.getInstance().getVelocity() + 3
                        ),
                        DumbUpShoot(),
                        new WaitUntilCommand(
                                ()-> ShooterSubsystem.getInstance().getSetPoint() > ShooterSubsystem.getInstance().getVelocity()
                        ),
                        StopShoot()*/
                        BallWithControl(),
                        BallWithControl(),
                        DumbUpShoot(),
                        new WaitUntilCommand(
                                ()-> ShooterSubsystem.getInstance().getSetPoint() > ShooterSubsystem.getInstance().getVelocity()
                        )
                );
    }


    public static Command StopShoot() {
        return new ParallelCommandGroup(
                TransferSubsystem.getInstance().stopCommand(),
                IndexerSubsystem.getInstance().stopCommand(),
                IntakeSubsystem.getInstance().stopCommand());
    }

    public static Command StartWheelClose() {
        return ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(43);
    }

    public static Command StartWheelFar() {
        return ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(48);
    }


}
