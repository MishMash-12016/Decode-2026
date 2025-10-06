package org.firstinspires.ftc.teamcode.subsystems;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.MMSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.Logger.KoalaLog;

@Config
//@AutoLog

public class Camera extends MMSubsystem {
    public final Limelight3A camera;

    public LLResult previousResultDetector;

    public int currentPipeline = 0;

    public int detectorPipeline = 0;

    private boolean initiated = false;


    //detection parameters for distance and strafe:
    public static double CAMERA_HEIGHT = 445;
    public static double CAMERA_ANGLE = 90 - 35.0;
    public static double TARGET_HEIGHT = 39;



    public static double dx = -1;
    public static double dy = -1;

    public static double timesAngleFailed = 0;
    public static double timesPipelineSwitchFail = 0;

    private int sampleColorID; //current color need to be detected
    private static Camera instance;


    public Camera() {
        super();
        MMRobot.getInstance().subsystems.add(this);

        camera = MMRobot.getInstance().currentOpMode.hardwareMap.get(Limelight3A.class, "limelight");
        InitializeCamera();
        camera.pipelineSwitch(currentPipeline);
    }

    public static synchronized Camera getInstance() {
        if (instance == null) {
            instance = new Camera();
        }
        return instance;
    }

    public void InitializeCamera() {
        camera.setPollRateHz(100);
        camera.start();
    }

    public void SetInitiated(){
        initiated = true;
    }

    //Get degrees in X axis getting cameraResult
    public double getTx(LLResult cameraResult) {
        if (cameraResult == null) {
            return 0;
        }
        return cameraResult.getTx();
    }

    //Get degrees in X axis getting cameraResult
    public double getTy(LLResult cameraResult) {
        if (cameraResult == null) {
            return 0;
        }
        return cameraResult.getTy();
    }

    //Get distance in Y axis with given cameraResult
    public Double getDistance(LLResult lastResult) {
        double ty = getTy(lastResult);
        if (ty == 0) {
            return 0.0;
        }
        double angleToGoalDegrees = CAMERA_ANGLE - ty;
        double angleToGoalRadians = Math.toRadians(angleToGoalDegrees);
        double distanceMM = (TARGET_HEIGHT - CAMERA_HEIGHT) / Math.tan(angleToGoalRadians);
        return Math.abs(distanceMM);
    }

    //Get distance in Y axis with given cameraResult
    public Double getDistance() {
        if (dy == 0) {
            return 0.0;
        }
        double angleToGoalDegrees = CAMERA_ANGLE - dy;
        double angleToGoalRadians = Math.toRadians(angleToGoalDegrees);
        double distanceMM = (TARGET_HEIGHT - CAMERA_HEIGHT) / Math.tan(angleToGoalRadians);
        return Math.abs(distanceMM);
    }



    public double getStrafeOffset(LLResult lastResult) {
        if (lastResult != null) {
            double tx = getTx(lastResult);
            if (tx != 0) {
                double tanTX = Math.tan(Math.toRadians(tx));
                double height = CAMERA_HEIGHT - TARGET_HEIGHT;
                double distanceY = getDistance(lastResult);
                double diagonalLength = Math.sqrt(height * height + distanceY * distanceY);
                return tanTX * diagonalLength / 2.54 / 10;
            }
        }
        return 0;
    }

    public double getStrafeOffset() {
        if (dx != 0) {
            double tanTX = Math.tan(Math.toRadians(dx));
            double height = CAMERA_HEIGHT - TARGET_HEIGHT;
            double distanceY = getDistance();
            double diagonalLength = Math.sqrt(height * height + distanceY * distanceY);
            return tanTX * diagonalLength / 2.54 / 10;
        }
        return 0;
    }

    public double getPipelineIndex() {
        return camera.getStatus().getPipelineIndex();
    }

    @Override
    public void resetHub() {}

    public boolean isDataOld(){
        return camera.getLatestResult().getStaleness() >= 100;
    }

    public void setPreviousResult() {
        previousResultDetector = camera.getLatestResult();
    }

    public void trackGreen() {
        currentPipeline = 0;
        detectorPipeline = 0;
    }

    public void trackPurple() {
        currentPipeline = 1;
        detectorPipeline = 1;
    }

    //Switch to neural-detector based detection pipepline (AI omg ooga booga big words I love man)
    public boolean switchToDetector() {
        currentPipeline = detectorPipeline;
        if (!camera.pipelineSwitch(currentPipeline)) {
            //telemetry.addData("failed to switch to detector", 0);
            timesPipelineSwitchFail += 1;
            return false;
        }

        return true;
    }

    public boolean isTargetVisible(){
        return camera.getLatestResult() != null;
    }
    public LLResult GetResult(){
        return camera.getLatestResult();
    }

    public LLResult GetPreviousDetectorResult(){
        return previousResultDetector;
    }


    //Doing every moment, it updates the python inputs, and then updates the cameraResult to the latest and freshest one. and telemtry, a lot of telemtry.
    @Override
    public void periodic() {
//        AutoLogManager.periodic();
        //updating the python endlessly
//        if (!initiated) return;

        dx = getStrafeOffset(GetPreviousDetectorResult());
        dy = (getDistance(GetPreviousDetectorResult())) / 25.4;

        KoalaLog.log("distanceX periodic",dx,true);
        KoalaLog.log("distanceY periodic",dy,true);
        FtcDashboard.getInstance().getTelemetry().addData("distanceX periodic dash", dx);
        FtcDashboard.getInstance().getTelemetry().addData("distanceY periodic dash", dy);
    }
}