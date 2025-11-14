package org.firstinspires.ftc.teamcode.OpModes.Tele;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMOpMode;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.WebcamSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;

@TeleOp
public class webcamOpMode extends MMOpMode {

    public webcamOpMode() {
        super(OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION, AllianceColor.RED);
    }

    @Override
    public void onInit() {
        MMDrivetrain.getInstance().disableTeleopDriveDefaultCommand();

        WebcamSubsystem.getInstance();
    }

    @Override
    public void onPlayLoop() {
    }
}
