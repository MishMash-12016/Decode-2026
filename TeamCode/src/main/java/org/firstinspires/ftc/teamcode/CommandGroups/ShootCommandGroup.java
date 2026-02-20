package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.RobotUtils;
import org.firstinspires.ftc.teamcode.subsystems.AccelSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.BallStopperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterHoodSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
public class ShootCommandGroup {

    public static Command ballWithControl() {
        ShooterSubsystem shooter = ShooterSubsystem.getInstance();
        return new SequentialCommandGroup(
                new WaitUntilCommand(
                        () -> shooter.getSetPoint() < shooter.getVelocity() + 1
                ),
                dumbUpShoot(),
                new WaitUntilCommand(
                        () -> shooter.getSetPoint() > shooter.getVelocity()
                ),
                stopShoot()
        );
    }

    public static Command dumbUpShoot() {
        return BallStopperSubsystem.getInstance().open()
                .andThen(new ParallelCommandGroup(
                        BallStopperSubsystem.getInstance().open(),
                        IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                        AccelSubsystem.getInstance().setPowerInstantCommand(1)
                ));
    }


    public static Command superDumbUpShoot() {
        return BallStopperSubsystem.getInstance().open()
                .andThen(new SequentialCommandGroup(
                                AccelSubsystem.getInstance().setPowerInstantCommand(1),
                                dumbUpShoot()
                        )
                );
    }

    public static Command smartUpShoot(boolean slow) {
        return MMDrivetrain.getInstance().enableDriveAligned(() -> slow).andThen(
                new ParallelCommandGroup(
                        MMDrivetrain.getInstance().enableDriveAligned(() -> slow),
                        BallStopperSubsystem.getInstance().open(),
                        AccelSubsystem.getInstance().setPowerInstantCommand(1)
                )
        );
    }

    public static Command stopShoot() {
        return new ParallelCommandGroup(
                AccelSubsystem.getInstance().stopCommand(),
                IntakeSubsystem.getInstance().stopCommand()
        );
    }

/*    public static Command closeSpeed() {
        return ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(45);
    }

    public static Command farSpeed() {
        return ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(72);
    }*/

    //TODO: function by speed and hood angle
/*    public static Command speedByLocation() {
        return null;
    }*/
}

/*
 if ("robot" !work){
        "work"
    } */