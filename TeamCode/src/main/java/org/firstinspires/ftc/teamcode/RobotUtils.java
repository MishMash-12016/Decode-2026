package org.firstinspires.ftc.teamcode;

import com.seattlesolvers.solverslib.geometry.Pose2d;
import com.seattlesolvers.solverslib.geometry.Rotation2d;
import com.seattlesolvers.solverslib.geometry.Translation2d;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.MMUtils;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;

public class RobotUtils {
    private static final Pose2d targetPoseBlue
            = new Pose2d(13,140, new Rotation2d(Math.toRadians(315)));

    //144 = field length
    private static final Pose2d targetPoseRed
            = new Pose2d(144 - targetPoseBlue.getX(),140, new Rotation2d(Math.toRadians(225)));

    public static Pose2d getTargetPose(){
        AllianceColor allianceColor = MMRobot.getInstance().currentOpMode.allianceColor;
        if(allianceColor == null
                || allianceColor == AllianceColor.BLUE){
            return targetPoseBlue;
        }

        return targetPoseRed;
    }

    public static double getAngleToTarget(){
        Pose2d robotPose = MMUtils.PedroPoseToSolversPose2d(
                MMDrivetrain.getInstance().getFollower().getPose());
        Pose2d targetPose = getTargetPose();

        Translation2d relativeTrl = targetPose.relativeTo(robotPose).getTranslation();
        return new Rotation2d(relativeTrl.getX(), relativeTrl.getY()).getDegrees();
    }

    public static double getDistanceToTarget() {
        Pose2d robotPose = MMUtils.PedroPoseToSolversPose2d(
                MMDrivetrain.getInstance().getFollower().getPose());
        Pose2d targetPose = getTargetPose();

        return robotPose.getTranslation().getDistance(targetPose.getTranslation());
    }
}
