package org.firstinspires.ftc.teamcode.subsystems;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.MMSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;

import java.util.List;

import Ori.Coval.Logging.Logger.KoalaLog;

@Config
//@AutoLog

public class Camera extends MMSubsystem {
    public final Limelight3A camera;
    public int currentPipeline = 0;

    public int detectorPipeline = 0;

    private boolean initiated = false; // If there is problems in the start of the teleop / auto make that
                                        //it switches pipelines 3 times. if only 1 pipeline is used dont use this


    //detection parameters for distance and strafe:
    public static double CAMERA_HEIGHT = 445;  //mm
    public static double CAMERA_ANGLE = 90 - 35.0; // degrees
    public static double TARGET_HEIGHT = 39; //mm


    public static double distanceX = -1; //in mm. the current result X distance. -1 means not detected anything yet
    public static double distanceY = -1; //in mm. the current result Y distance. -1 means not detected anything yet

    public static double timesPipelineSwitchFail = 0; // for debugging

    private static Camera instance;


    public Camera() {
        super();
        MMRobot.getInstance().subsystems.add(this);

        camera = MMRobot.getInstance().currentOpMode.hardwareMap.get(Limelight3A.class, "limelight");
        initializeCamera();
        camera.pipelineSwitch(currentPipeline);
    }

    public static synchronized Camera getInstance() {
        if (instance == null) {
            instance = new Camera();
        }
        return instance;
    }

    public void initializeCamera() {
        camera.setPollRateHz(100);
        camera.start();
        switchToDetector();
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

    //Get distance in Y axis with given cameraResult, returns in mm
    public Double getDistanceY(LLResult lastResult) {
        double ty = getTy(lastResult);
        if (ty == 0) {
            return 0.0;
        }
        double angleToGoalDegrees = CAMERA_ANGLE - ty;
        double angleToGoalRadians = Math.toRadians(angleToGoalDegrees);
        double distanceMM = (TARGET_HEIGHT - CAMERA_HEIGHT) / Math.tan(angleToGoalRadians);
        return Math.abs(distanceMM);
    }


//     TODO: if strafe offset does problems use this function, it is old but i think it is incorrect..
//    public double getStrafeOffset(LLResult lastResult) {
//        if (lastResult != null) {
//            double tx = getTx(lastResult);
//            if (tx != 0) {
//                double tanTX = Math.tan(Math.toRadians(tx));
//                double height = CAMERA_HEIGHT - TARGET_HEIGHT;
//                double distanceY = getDistanceY(lastResult);
//                double diagonalLength = Math.sqrt(height * height + distanceY * distanceY);
//                return tanTX * diagonalLength / 2.54 / 10;
//            }
//        }
//        return 0;
//    }

    // supposed to be more correct. returns in mm
    public double getStrafeOffset(LLResult lastResult) {
        if (lastResult != null) {
            double tx = getTx(lastResult);
            if (tx != 0) {
                double distanceY = getDistanceY(lastResult); // mm, forward on the floor
                double strafeMM = Math.tan(Math.toRadians(tx)) * distanceY; // mm, lateral on the floor
                return strafeMM; // mm
            }
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

    public void trackGreen() {
        currentPipeline = 0;
        detectorPipeline = 0;
    }

    public void trackPurple() {
        currentPipeline = 1;
        detectorPipeline = 1;
    }

    public void trackPurpleAndGreen() { //Todo: change in the limelight itself that pipeline 2 is both colors
        currentPipeline = 2;
        detectorPipeline = 2;
    }

    //Switch to neural-detector based detection pipepline (AI omg ooga booga big words Sharabi love's man).
    //It accounts the current color needed to be detected and switches to the according detector pipeline of that color
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
    public LLResult getLatestResult(){
        LLResult result = camera.getLatestResult();
        distanceX = getStrafeOffset(result); //mm
        distanceY = (getDistanceY(result)); //mm
        return result;
    }


    // gives the current artifact color
    public String getArtifactColor(LLResult result){
        List<LLResultTypes.DetectorResult> allDetectorResults = result.getDetectorResults();
        if (!allDetectorResults.isEmpty()) {
            LLResultTypes.DetectorResult detectorResult = allDetectorResults.get(0);
            int colorID = detectorResult.getClassId();
            if (colorID == 0){
                return "green";
            }
            else if (colorID == 1){
                return "purple";
            }
        }
        return "no result";
    }


    //Doing every moment, it updates the python inputs, and then updates the cameraResult to the latest and freshest one. and telemtry, a lot of telemtry.
    @Override
    public void periodic() {
//        AutoLogManager.periodic();
        //updating the python endlessly
//        if (!initiated) return;
        getLatestResult();

        KoalaLog.log("distanceX periodic", distanceX,true);
        KoalaLog.log("distanceY periodic", distanceY,true);
        FtcDashboard.getInstance().getTelemetry().addData("distanceX periodic dash", distanceX);
        FtcDashboard.getInstance().getTelemetry().addData("distanceY periodic dash", distanceY);
    }
}