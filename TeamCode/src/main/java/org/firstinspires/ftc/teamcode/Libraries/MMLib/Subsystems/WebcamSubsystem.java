package org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems;

import Ori.Coval.Logging.Logger.KoalaLog;

import android.util.Size;

import com.pedropathing.ftc.FTCCoordinates;
import com.pedropathing.geometry.PedroCoordinates;
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
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;

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
            0, 15/2.54, 0, 0);
    private final YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES,
            0, -0, 180, 0);

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
            KoalaLog.log("WebcamSubsystem/cameraState", instance.visionPortal.getCameraState().toString(), true);
            KoalaLog.log("WebcamSubsystem/cameraEnabled", instance.visionPortal.getProcessorEnabled(instance.aprilTag), true);
            KoalaLog.log("WebcamSubsystem/" +
                    "fps", instance.visionPortal.getFps(), true);
            ArrayList<AprilTagDetection> detections = instance.aprilTag.getFreshDetections();
            if (detections != null){
                for (AprilTagDetection detection : detections) {
                    KoalaLog.log("WebcamSubsystem/has detection", true, true);
                    KoalaLog.log("WebcamSubsystem/decision margin", detection.decisionMargin, true);

                    //TODO: add filter by alliance
                    if(detection.decisionMargin > 10){
                        instance.distance = detection.ftcPose.range;
                        instance.angle = detection.ftcPose.bearing;
                        instance.robotPose = detection.robotPose;
                        MMDrivetrain.getInstance().addVisionMeasurement(getInstance().getRobotPosePedro(), detection.frameAcquisitionNanoTime);
                    }
                }
            }
            else {
                KoalaLog.log("WebcamSubsystem/has detection", false, true);
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
                .setTagLibrary(AprilTagGameDatabase.getDecodeTagLibrary())
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.RADIANS)
                .setCameraPose(cameraPosition, cameraOrientation)
                .setLensIntrinsics(458.939, 458.939, 424.652, 329.231)
                .setNumThreads(1)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(MMRobot.getInstance().currentOpMode.hardwareMap.get(WebcamName.class, "Webcam 1"));
        builder.setCameraResolution(new Size(800, 600));
        builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);
        builder.enableLiveView(false);
        builder.addProcessor(aprilTag);
        builder.setAutoStartStreamOnBuild(true);

        visionPortal = builder.build();
        visionPortal.setProcessorEnabled(aprilTag, true);
        visionPortal.resumeStreaming();
    }

    public Pose getRobotPosePedro(){
        KoalaLog.log("vision_raw_pose", new double[]{robotPose.getPosition().x,
                robotPose.getPosition().y,
                robotPose.getOrientation().getYaw(AngleUnit.RADIANS)}, true);

        Pose pedroPose = FTCCoordinates.INSTANCE.convertToPedro(new Pose(
                robotPose.getPosition().x,
                robotPose.getPosition().y,
                robotPose.getOrientation().getYaw(AngleUnit.RADIANS)));
        KoalaLog.log("vision_raw_pose", new double[]{pedroPose.getX(),
                pedroPose.getY(),
                pedroPose.getHeading()}, true);
        return FTCCoordinates.INSTANCE.convertToPedro(new Pose(
            robotPose.getPosition().x,
            robotPose.getPosition().y,
            robotPose.getOrientation().getYaw(AngleUnit.RADIANS)));
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
        new Pose(0,0,0, FTCCoordinates.INSTANCE).getAsCoordinateSystem(PedroCoordinates.INSTANCE);

        return aprilTagID;
    }
}
