package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.CommandGroups.ShootCommandGroup;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.WebcamSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.Libraries.pedroPathing.FollowPathCommand;
import org.firstinspires.ftc.teamcode.RobotConstants;

import Ori.Coval.Logging.AutoLog;

@AutoLog
@Autonomous
public class RedFar extends MMOpMode {

    private PathChain
            FROM_START_TO_FIRST_INTAKE,
            FROM_FIRST_INTAKE_TO_SCORE,
            FROM_SCORE_TO_SECOND_INTAKE,
            FROM_SECOND_INTAKE_TO_SCORE;

    private final Pose startPose = new Pose(87, 8,Math.toRadians(-90));
    private final Pose firstIntake = new Pose(132, 35,Math.toRadians(-90));
    private final Pose scorePose = new Pose(85, 12,Math.toRadians(50));
    private final Pose secondIntake = new Pose(132, 35,Math.toRadians(-90));

    Follower follower;
    public void buildPaths() {
        FROM_START_TO_FIRST_INTAKE = follower.pathBuilder()
                .addPath(new BezierLine(startPose, firstIntake))
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(135))
                .build();
        FROM_FIRST_INTAKE_TO_SCORE = follower.pathBuilder()
                .addPath(new BezierLine(firstIntake, scorePose))
                .setTangentHeadingInterpolation().build();
        FROM_SCORE_TO_SECOND_INTAKE = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, secondIntake))
                .setTangentHeadingInterpolation().build();
        FROM_SECOND_INTAKE_TO_SCORE = follower.pathBuilder()
                .addPath(new BezierLine(secondIntake, scorePose))
                .setTangentHeadingInterpolation().build();
    }

    public RedFar() {
        super(OpModeType.NonCompetition.DEBUG_SERVOHUB, AllianceColor.RED);
    }

    @Override
    public void onInit() {
        MMDrivetrain.getInstance().setPose(startPose);
        follower = MMDrivetrain.getInstance().getFollower();
        buildPaths();

        SequentialCommandGroup autonomousSequence = new SequentialCommandGroup(
                // shoot
                // intake on
                new FollowPathCommand(follower, FROM_START_TO_FIRST_INTAKE),
                // intake off
                new FollowPathCommand(follower, FROM_FIRST_INTAKE_TO_SCORE),
                //shoot
                //intake on
                new FollowPathCommand(follower, FROM_SCORE_TO_SECOND_INTAKE),
                // intake off
                new FollowPathCommand(follower, FROM_SECOND_INTAKE_TO_SCORE)
                //shoot
        );
        autonomousSequence.schedule();
    }

    @Override
    public void onPlayLoop() {
        MMDrivetrain.update();
        telemetry.update();
    }
}
