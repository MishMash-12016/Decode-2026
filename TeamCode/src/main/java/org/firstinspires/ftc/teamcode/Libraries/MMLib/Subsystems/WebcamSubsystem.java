package org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems;

import Ori.Coval.Logging.Logger.KoalaLog;

import android.util.Size;

import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.AutoLogOutput;
import Ori.Coval.Logging.AutoLogPose2d;

@AutoLog
public class WebcamSubsystem extends MMSubsystem {

    public static WebcamSubsystem instance;

    public static synchronized WebcamSubsystem getInstance() {
        if (instance == null) {
            instance = new WebcamSubsystemAutoLogged();
        }
        return instance;
    }

    private final Position cameraPosition = new Position(DistanceUnit.INCH,
            0, 0, 0, 0);
    private final YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES,
            0, -90, 0, 0);

    private AprilTagProcessor aprilTag;
    public VisionPortal visionPortal;
    private int aprilTagID = 21;
    private double distance = 0;
    private double angle = 0;
    private Pose3D robotPose = new Pose3D(new Position(), new YawPitchRollAngles(AngleUnit.DEGREES, 0,0,0,0));
    public WebcamSubsystem(){
        initAprilTag();
    }

    @Override
    public void resetHub() {
        instance = null;
    }

    public static void update() {
        if (instance != null){
            KoalaLog.log("fps", instance.visionPortal.getFps(), true);
            KoalaLog.log("has detection", instance.aprilTag.getDetections().isEmpty(), true);
            for (AprilTagDetection detection : instance.aprilTag.getDetections()) {
                KoalaLog.log("decision margin", detection.decisionMargin, true);

                //TODO: add filter by alliance
                if(detection.decisionMargin > 28){
                    instance.distance = detection.ftcPose.range;
                    instance.angle = detection.ftcPose.bearing;
                    instance.robotPose = detection.robotPose;
                }
            }
        }
    }



    private void initAprilTag() {

        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagOutline(true)
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .setTagLibrary(getPedroDecodeField())
                .setOutputUnits(DistanceUnit.METER, AngleUnit.DEGREES)
                .setCameraPose(cameraPosition, cameraOrientation)
                .setLensIntrinsics(458.939, 458.939, 424.652, 329.231)
                .build();

        //aprilTag.setDecimation(3);
        VisionPortal.Builder builder = new VisionPortal.Builder();

        builder.setCamera(MMRobot.getInstance().currentOpMode.hardwareMap.get(WebcamName.class, "Webcam 1"));

        builder.setCameraResolution(new Size(640, 480));

        builder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);

        builder.addProcessor(aprilTag);

        visionPortal = builder.build();
        visionPortal.setProcessorEnabled(aprilTag, true);
        visionPortal.resumeStreaming();
    }

    public Pose getRobotPosePedro(){
        return new Pose(
            robotPose.getPosition().x,
            robotPose.getPosition().y,
            robotPose.getOrientation().getYaw(AngleUnit.RADIANS));
    }

    @AutoLogPose2d
    public double[] getAScopePose(){
        return new double[]{
            robotPose.getPosition().x,
            robotPose.getPosition().y,
            robotPose.getOrientation().getYaw()};
    }

    @AutoLogOutput
    public double getDistance() {
        return distance;
    }

    @AutoLogOutput
    public double getAngle() {
        return angle;
    }

    @AutoLogOutput
    public int getAprilTagID(){
        for (AprilTagDetection detection : aprilTag.getDetections()){
            aprilTagID = detection.id;
        }
        return aprilTagID;
    }

    /**
     * Get the {@link AprilTagLibrary} for the Decode FTC game
     * @return the {@link AprilTagLibrary} for the Decode FTC game
     *
     * !!!!!DO NOT CHANGE THIS EVERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
     * RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
     */
    public static AprilTagLibrary getPedroDecodeField(){
        //!!!!!DO NOT CHANGE THIS EVERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
        return new AprilTagLibrary.Builder()
                .addTag(20, "BlueTarget",
                        6.5, new VectorF(-58.3727f + 72.0f, -55.6425f + 72.0f, 29.5f), DistanceUnit.INCH,
                        new Quaternion(0.2182149f, -0.2182149f, -0.6725937f, 0.6725937f, 0))
                .addTag(21, "Obelisk_GPP",
                        6.5, DistanceUnit.INCH)
                .addTag(22, "Obelisk_PGP",
                        6.5, DistanceUnit.INCH)
                .addTag(23, "Obelisk_PPG",
                        6.5, DistanceUnit.INCH)
                .addTag(24, "RedTarget",
                        6.5, new VectorF(-58.3727f + 72.0f, 55.6425f + 72.0f, 29.5f), DistanceUnit.INCH,
                        new Quaternion(0.6725937f, -0.6725937f, -0.2182149f, 0.2182149f, 0))
                .build();
    }
}
