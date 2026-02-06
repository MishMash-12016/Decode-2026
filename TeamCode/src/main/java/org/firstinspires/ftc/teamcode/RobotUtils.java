package org.firstinspires.ftc.teamcode;

import com.seattlesolvers.solverslib.geometry.Pose2d;
import com.seattlesolvers.solverslib.geometry.Rotation2d;
import com.seattlesolvers.solverslib.geometry.Translation2d;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.MMUtils;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;

public class RobotUtils {
    private static final Pose2d targetPoseBlue
            = new Pose2d(72-55.6425,72 - 58.3727, new Rotation2d(Math.toRadians(315)));

    //144 = field length
    private static final Pose2d targetPoseRed
            = new Pose2d(144 - targetPoseBlue.getX(),targetPoseBlue.getY(), new Rotation2d(Math.toRadians(225)));

    public static Pose2d getTargetPose(){
        AllianceColor allianceColor = MMRobot.getInstance().currentOpMode.allianceColor;
        if(allianceColor == null || allianceColor == AllianceColor.BLUE){
            return targetPoseBlue;
        }

        return targetPoseRed;
    }

    public static Rotation2d getAngleToTarget(){
        Pose2d robotPose = MMUtils.PedroPoseToSolversPose2d(
                MMDrivetrain.getInstance().getFollower().getPose());
        Pose2d targetPose = getTargetPose();

        Translation2d toTarget =
                targetPose.getTranslation().minus(robotPose.getTranslation());

        return new Rotation2d(
                Math.atan2(toTarget.getY(), toTarget.getX())
        );
    }


    public static double getDistanceToTarget() {
        Pose2d robotPose = MMUtils.PedroPoseToSolversPose2d(
                MMDrivetrain.getInstance().getFollower().getPose());
        Pose2d targetPose = getTargetPose();

        return robotPose.getTranslation().getDistance(targetPose.getTranslation());
    }
}
