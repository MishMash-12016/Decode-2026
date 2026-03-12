package org.firstinspires.ftc.teamcode;

import Ori.Coval.Logging.AutoLogOutput;
import Ori.Coval.Logging.Logger.KoalaLog;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.geometry.Pose2d;
import com.seattlesolvers.solverslib.geometry.Rotation2d;
import com.seattlesolvers.solverslib.geometry.Translation2d;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.MMPoint2D;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.MMUtils;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;

public class RobotUtils {

    private enum TargetPose {
        CLOSE_BLUE(new Translation2d(1,138)),
        MID_BLUE(new Translation2d(2,142)),
        FAR_BLUE(new Translation2d(5,143)),
        CLOSE_RED(new Translation2d(144 - 1,138)),
        MID_RED(new Translation2d(144 - 2,142)),
        FAR_RED(new Translation2d(144 - 5,143));

        public final Translation2d pose;

        TargetPose(Translation2d pose) {
            this.pose = pose;
        }

        static Translation2d get(int index, AllianceColor allianceColor) {
            if(allianceColor == AllianceColor.BLUE)
                return TargetPose.values()[index].pose;
            return TargetPose.values()[index + 3].pose;
        }
    }
    public static Translation2d getTargetPose(){
        Pose followerPose = MMDrivetrain.getInstance().getFollower().getPose();
        AllianceColor allianceColor = MMRobot.getInstance().currentOpMode.allianceColor;
        double limitLineY = MMUtils.mapValuesLinear(
                followerPose.getX(),
                new MMPoint2D(0, 140),
                new MMPoint2D(144, 90),
                allianceColor
        );
        if(followerPose.getY() < 50)
            return TargetPose.get(2, allianceColor);

        return (followerPose.getY() < limitLineY) ?
                TargetPose.get(1, allianceColor) : TargetPose.get(0, allianceColor);

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
