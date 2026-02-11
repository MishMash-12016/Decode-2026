package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.RobotUtils;
import org.firstinspires.ftc.teamcode.subsystems.BallStopperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FunnelSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

public class ShootCommandGroup {

    public static Command ballWithControl() {
        return new SequentialCommandGroup(
                new WaitUntilCommand(
                        () -> ShooterSubsystem.getInstance().getSetPoint() < ShooterSubsystem.getInstance().getVelocity()
                ),
                dumbUpShoot(),
                new WaitUntilCommand(
                        () -> ShooterSubsystem.getInstance().getSetPoint() > ShooterSubsystem.getInstance().getVelocity()
                ),
                stopShoot()
        );
    }

    public static Command dumbUpShoot() {
        return new ParallelCommandGroup(
//                BallStopperSubsystem.getInstance().open(),
                IntakeSubsystem.getInstance().setPowerInstantCommand(1),
                FunnelSubsystem.getInstance().setPowerInstantCommand(1)
        );
    }

    public static Command smartUpShoot(boolean slow) {
        return MMDrivetrain.getInstance().enableDriveAligned(() -> slow).andThen(
                new ParallelCommandGroup(
                        MMDrivetrain.getInstance().enableDriveAligned(() -> slow),
//                        BallStopperSubsystem.getInstance().open(),
                        FunnelSubsystem.getInstance().setPowerInstantCommand(1)
                )
        );
    }

    public static Command stopShoot() {
        return new ParallelCommandGroup(
                FunnelSubsystem.getInstance().stopCommand()
        );
    }

    public static Command closeDumbSpeed() {
        return ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(48);
    }

    //TODO: function by speed and hood angle
    public static Command speedByLocation() {
        return ShooterSubsystem.getInstance().speedByDistance(RobotUtils.getDistanceToTarget());
    }

}

/*
 if ("robot" !work){
        "work"
    } */