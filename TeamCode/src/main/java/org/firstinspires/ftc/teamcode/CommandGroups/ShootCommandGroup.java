package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.RobotUtils;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LeftStopperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.MiddleStopperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.RightStopperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

public class ShootCommandGroup {


    public static Command stoppersPush() {
        return new ParallelCommandGroup(
                RightStopperSubsystem.getInstance().push(),
                MiddleStopperSubsystem.getInstance().push(),
                LeftStopperSubsystem.getInstance().push()
        );
    }

    public static Command ballWithControl() {
        return new SequentialCommandGroup(
                new WaitUntilCommand(
                        () -> ShooterSubsystem.getInstance().getSetPoint() < ShooterSubsystem.getInstance().getVelocity()
                ),
                shootAll()
        );
    }

    public static Command shootAll() {
        return new ParallelCommandGroup(
            IntakeSubsystem.getInstance().setPowerInstantCommand(1),
            stoppersPush()
        );
    }


    public static Command stopShoot() {
        return IntakeSubsystem.getInstance().stopCommand();
    }
/*
    public static Command smartUpShoot(boolean slow) {
        return MMDrivetrain.getInstance().enableDriveAligned(() -> slow);
    }
*/

    public static Command closeDumbSpeed() {
        return ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(48);
    }

    //TODO: function by speed and hood angle
    public static Command speedByLocation() {
        return ShooterSubsystem.getInstance().speedByDistance(RobotUtils.getDistanceToTarget());
    }

}