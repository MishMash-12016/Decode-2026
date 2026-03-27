package org.firstinspires.ftc.teamcode.OpModes;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import Ori.Coval.Logging.Logger.KoalaLog;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class FakeDrive extends SubsystemBase {

    public FakeDrive(){
        RobotConfig config = null;
        try{
            config = RobotConfig.fromGUISettings();
        } catch (Exception e) {
            // Handle exception as needed
            e.printStackTrace();
        }

        // Configure AutoBuilder last
        assert config != null;
        AutoBuilder.configure(
            this::getPose, // Robot pose supplier
            this::resetPose, // Method to reset odometry (will be called if your auto has a starting pose)
            this::getRobotRelativeSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
            (speeds, feedforwards) -> driveRobotRelative(speeds), // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds. Also optionally outputs individual module feedforwards
            new PPHolonomicDriveController( // PPHolonomicController is the built in path following controller for holonomic drive trains
                new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
                new PIDConstants(5.0, 0.0, 0.0) // Rotation PID constants
            ),
            config, // The robot configuration
            () -> false,
            this // Reference to this subsystem to set requirements
        );
    }

    ChassisSpeeds chassisSpeeds = new ChassisSpeeds();
    Pose2d pose = new Pose2d();
    ElapsedTime elapsedTime = new ElapsedTime();
    private void driveRobotRelative(ChassisSpeeds speeds) {
        chassisSpeeds = speeds;

        double elapsed = elapsedTime.seconds();
        KoalaLog.log("fake/elapsed", elapsed, true);
        KoalaLog.log("fake/speed", chassisSpeeds.vyMetersPerSecond + chassisSpeeds.vxMetersPerSecond, true);
        pose = pose.plus(new Transform2d(
            new Translation2d(chassisSpeeds.vxMetersPerSecond * elapsed,
                chassisSpeeds.vyMetersPerSecond * elapsed),
            new Rotation2d(chassisSpeeds.omegaRadiansPerSecond * elapsed)));
        elapsedTime.reset();
    }

    public Pose2d getPose(){
        return pose;
    }
    public void resetPose(Pose2d pose2d){
        pose = pose2d;
    }

    public ChassisSpeeds getRobotRelativeSpeeds(){
        return chassisSpeeds;
    }

    @Override
    public void periodic() {
        super.periodic();
    }
}
