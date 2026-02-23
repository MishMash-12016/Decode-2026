package org.firstinspires.ftc.teamcode.Libraries.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

  public static FollowerConstants followerConstants =
      new FollowerConstants()
          .mass(13)
          .forwardZeroPowerAcceleration(-54.8)
          .lateralZeroPowerAcceleration(-89.7)
          .useSecondaryTranslationalPIDF(false)
          .useSecondaryDrivePIDF(false)
          .secondaryHeadingPIDFCoefficients(new PIDFCoefficients(2,0.6,0,0))
          .centripetalScaling(0.0007)
          .translationalPIDFCoefficients(new PIDFCoefficients(0.087, 0, 0, 0))
          .headingPIDFCoefficients(new PIDFCoefficients(0.585, 0, 0, 0))
          .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.0075, 0, 0.001, 0, 0));
/*  public static FollowerConstants followerConstants =
      new FollowerConstants()
          .mass(10)
          .forwardZeroPowerAcceleration(-54.8)
          .lateralZeroPowerAcceleration(-89.7)
          .useSecondaryTranslationalPIDF(false)
          .useSecondaryHeadingPIDF(false)
          .useSecondaryDrivePIDF(false)
          .centripetalScaling(0)
          .translationalPIDFCoefficients(new PIDFCoefficients(0.087, 0, 0, 0))
          .headingPIDFCoefficients(new PIDFCoefficients(0.585, 0, 0, 0))
          .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.005, 0, 0, 0, 0));
*/
  public static MecanumConstants driveConstants =
      new MecanumConstants()
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
          .xVelocity(74)
          .yVelocity(58.2);

  //    public static PinpointConstants localizerConstants = new PinpointConstants()
  //            .forwardPodY(0 / 2.54)
  //            .strafePodX(0 / 2.54)
  //            .distanceUnit(DistanceUnit.INCH)
  //            .hardwareMapName("pinpoint")
  //            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
  //            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
  //            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);
  public static PinpointVisionConstants localizerConstants =
      new PinpointVisionConstants()
          .forwardPodY(9.75 / 2.54)
          .strafePodX(0 / 2.54)
          .distanceUnit(DistanceUnit.INCH)
          .hardwareMapName("pinpoint")
          .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
          .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
          .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
          .odometryStdDevs(0, 0, 0)
          .visionMeasurementStdDevs(0, 0, 0)
      /*.odometryStdDevs(0.001, 0.001, 0.001)
      .visionMeasurementStdDevs(0.0, 0.0, 0.0)*/ ;

  public static PathConstraints pathConstraints =
          //tune this shit
          new PathConstraints(0.995, 500, 1, 1);

  public static Follower createFollower(HardwareMap hardwareMap) {
    return new FollowerBuilder(followerConstants, hardwareMap)
        .mecanumDrivetrain(driveConstants)
        .setLocalizer(new PinpointVisionLocalizer(hardwareMap, localizerConstants))
        .pathConstraints(pathConstraints)
        .build();
  }
}
