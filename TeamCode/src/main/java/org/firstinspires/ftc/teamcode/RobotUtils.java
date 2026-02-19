package org.firstinspires.ftc.teamcode;

import com.seattlesolvers.solverslib.geometry.Pose2d;
import com.seattlesolvers.solverslib.geometry.Rotation2d;
import com.seattlesolvers.solverslib.geometry.Translation2d;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.MMUtils;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;

import Ori.Coval.Logging.AutoLogOutput;

public class RobotUtils {
    private static final Translation2d targetPoseBlue
            = new Translation2d(0,144);

    ///144 = field length
    private static final Translation2d targetPoseRed
            = new Translation2d(144,144);

    public static Translation2d getTargetPose(){
        AllianceColor allianceColor = MMRobot.getInstance().currentOpMode.allianceColor;
        if(allianceColor == null || allianceColor == AllianceColor.BLUE){
            return targetPoseBlue;
        }

        return targetPoseRed;
    }

    public static Rotation2d getAngleToTarget(){
        Pose2d robotPose = MMUtils.PedroPoseToSolversPose2d(
                MMDrivetrain.getInstance().getFollower().getPose());
        Translation2d targetPose = getTargetPose();

        Translation2d toTarget =
                targetPose.minus(robotPose.getTranslation());

        return new Rotation2d(
                Math.atan2(toTarget.getY(), toTarget.getX())
        );
    }


    @AutoLogOutput
    public static double getDistanceToTarget() {
        Pose2d robotPose = MMUtils.PedroPoseToSolversPose2d(
                MMDrivetrain.getInstance().getFollower().getPose());
        Translation2d targetPose = getTargetPose();

        return robotPose.getTranslation().getDistance(targetPose);
    }
}
