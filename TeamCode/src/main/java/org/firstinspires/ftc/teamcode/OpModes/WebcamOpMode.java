package org.firstinspires.ftc.teamcode.OpModes;

import android.util.Size;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@TeleOp
@Config
public class WebcamOpMode extends MMOpMode {
    private AprilTagProcessor tagProcessor;
    private final int frameWidth = 1280;


    public WebcamOpMode() {
        super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION, AllianceColor.BLUE);
    }

    @Override
    public void onInit() {
        tagProcessor = AprilTagProcessor.easyCreateWithDefaults();
        VisionPortal.Builder vBuilder = new VisionPortal.Builder();

        vBuilder.setCamera(hardwareMap.get(WebcamName.class, "webcam"));
        vBuilder.addProcessor(tagProcessor);
        vBuilder.setCameraResolution(new Size(frameWidth, 720));
        vBuilder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        VisionPortal vision = vBuilder.build();

        vision.resumeStreaming();

    }

    @Override
    public void onPlayLoop() {
//        List<AprilTagDetection> result = tagProcessor.getDetections();
//        telemetry.addData("AprilTag ID: ",tagProcessor.getDetections());
//        telemetry.update();
//
        List<AprilTagDetection> detections = tagProcessor.getDetections();

        for (AprilTagDetection detection : detections) {
            int id = detection.id;
            telemetry.addData("Tag ID", id);
        }
        telemetry.update();
    }
}
