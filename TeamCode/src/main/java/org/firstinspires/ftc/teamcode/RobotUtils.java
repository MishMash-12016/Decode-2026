package org.firstinspires.ftc.teamcode;

import Ori.Coval.Logging.AutoLogOutput;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.geometry.Pose2d;
import com.seattlesolvers.solverslib.geometry.Rotation2d;
import com.seattlesolvers.solverslib.geometry.Translation2d;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.MMUtils;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;

public class RobotUtils {

    private static final Translation2d closeBlueTargetPose
            = new Translation2d(1,138);
    private static final Translation2d farBlueTargetPose
            = new Translation2d(7,143);

    ///144 = field length
    private static final Translation2d closeRedTargetPose
            = new Translation2d(144 - 1,138);
    private static final Translation2d farRedTargetPose
            = new Translation2d(144 - 7,143);

    public static Translation2d getTargetPose(){
        Follower follower = MMDrivetrain.getInstance().getFollower();
        AllianceColor allianceColor = MMRobot.getInstance().currentOpMode.allianceColor;
        if(allianceColor == null || allianceColor == AllianceColor.BLUE){
            return follower.getPose().getY() > 50 ? closeBlueTargetPose : farBlueTargetPose;
        }
        return follower.getPose().getY() > 50 ? closeRedTargetPose : farRedTargetPose;
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
