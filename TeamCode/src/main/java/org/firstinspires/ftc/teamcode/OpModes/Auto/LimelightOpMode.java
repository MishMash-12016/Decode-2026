package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;

import java.util.List;
@TeleOp
@Config
public class LimelightOpMode extends MMOpMode {
    public LimelightOpMode() {
        super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION, AllianceColor.BLUE);
    }
    private Limelight3A limelight3A;

    @Override
    public void onInit() {
        limelight3A = hardwareMap.get(Limelight3A.class,"limelight");
        limelight3A.pipelineSwitch(9);
        limelight3A.start();
    }

    @Override
    public void onPlayLoop() {
        LLResult llResult = limelight3A.getLatestResult();
        llResult.getFiducialResults();

        List<LLResultTypes.FiducialResult> tags = llResult.getFiducialResults();

        for (LLResultTypes.FiducialResult tag : tags) {
            telemetry.addData("AprilTag ID: ",tag.getFiducialId());
        }
        telemetry.update();
    }
}
