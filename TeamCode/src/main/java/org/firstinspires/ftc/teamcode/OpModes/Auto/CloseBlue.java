package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.CommandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.Libraries.pedroPathing.FollowPathCommand;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

import Ori.Coval.Logging.AutoLog;

@AutoLog
@Autonomous
public class CloseBlue extends MMOpMode {

    private PathChain
            FROM_START_TO_SHOOT,
            FROM_SHOOT_TO_TURN,
            FROM_TURN_TO_INTAKE;

    private final Pose startPose = new Pose(18, 117,Math.toRadians(136));
    private final Pose shoot = new Pose(52, 85,Math.toRadians(136));
    private final Pose turn = new Pose(50, 85,Math.toRadians(180));
    private final Pose intake = new Pose(15, 85,Math.toRadians(180));

    public void buildPaths() {
        FROM_START_TO_SHOOT = MMDrivetrain.follower.pathBuilder()
                .addPath(new BezierLine(startPose, shoot))
                .setLinearHeadingInterpolation(Math.toRadians(136), Math.toRadians(136)).build();

        FROM_SHOOT_TO_TURN = MMDrivetrain.follower.pathBuilder()
                .addPath(new BezierLine(shoot, turn))
                .setLinearHeadingInterpolation(Math.toRadians(136), Math.toRadians(180)).build();

        FROM_TURN_TO_INTAKE = MMDrivetrain.follower.pathBuilder()
                .addPath(new BezierLine(turn, intake))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180)).build();

    }

    public CloseBlue() {
        super(OpModeType.NonCompetition.DEBUG_SERVOHUB, AllianceColor.BLUE);
    }

    @Override
    public void onInit() {
        MMDrivetrain.getInstance().setPose(startPose);
        Follower follower = MMDrivetrain.follower;
        buildPaths();

        TurretSubsystem.getInstance().setPositionCommand(0).schedule();

        SequentialCommandGroup autonomousSequence = new SequentialCommandGroup(
                TurretSubsystem.getInstance().alignToTarget(),
                ShooterSubsystem.getInstance().getToAndHoldSetPointCommand(45),
                new FollowPathCommand(follower, FROM_START_TO_SHOOT),
                ShootCommandGroup.upShoot(),
                new WaitCommand(3000),
                ShooterSubsystem.getInstance().stopCommand(),
                new FollowPathCommand(follower, FROM_SHOOT_TO_TURN),
                IntakeCommandGroup.FeedIntake(),
                new FollowPathCommand(follower, FROM_TURN_TO_INTAKE),
                IntakeCommandGroup.StopIntake());
        autonomousSequence.schedule();
    }

    @Override
    public void onPlayLoop() {
        MMDrivetrain.update();
        telemetry.update();
    }
}
