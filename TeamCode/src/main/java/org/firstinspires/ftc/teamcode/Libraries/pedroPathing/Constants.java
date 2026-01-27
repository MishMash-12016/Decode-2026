package org.firstinspires.ftc.teamcode.Libraries.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.ftc.localization.localizers.PinpointLocalizer;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(10)
            .forwardZeroPowerAcceleration(-36)
            .lateralZeroPowerAcceleration(-78.6)
            .useSecondaryTranslationalPIDF(false)
            .useSecondaryHeadingPIDF(false)
            .useSecondaryDrivePIDF(false)
            .centripetalScaling(0)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.1, 0, 0, 0))
            .headingPIDFCoefficients(new PIDFCoefficients(0.9, 0, 0, 0))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.005, 0, 0, 0, 0));

    public static MecanumConstants driveConstants = new MecanumConstants()
            .leftFrontMotorName("fl")
            .leftRearMotorName("bl")
            .rightFrontMotorName("fr")
            .rightRearMotorName("br")
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)
//            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
//            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
//            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
//            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .useBrakeModeInTeleOp(true)
            .xVelocity(68.76)
            .yVelocity(54.08)
            ;

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(0 / 2.54)
            .strafePodX(0 / 2.54)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);



    public static PathConstraints pathConstraints = new PathConstraints(
            0.995,
            500,
            1.2,
            0.1
    );

    public static Follower createFollower(HardwareMap hardwareMap) {
//        double[] P = {0.155, 0.155, 0.0003}; // how much we trust the start pose(lower mean trust start pose more)
//        double[] processVariance = {0.31, 0.31, 0.0006}; // how much we trust the odometry(lower mean trust odo more)
//        double[] measurementVariance = {1.395, 1.395, 0.0025}; // how much we trust the vision(lower mean trust vision more)
//        int bufferSize = 200;
//
//        FusionLocalizer fusionLocalizer = new FusionLocalizer(
//            new PinpointLocalizer(hardwareMap, localizerConstants),
//            P,
//            processVariance,
//            measurementVariance,
//            bufferSize);
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(driveConstants)
                .setLocalizer(new PinpointLocalizer(hardwareMap,localizerConstants))
                .pathConstraints(pathConstraints)
                .build();
    }
}
