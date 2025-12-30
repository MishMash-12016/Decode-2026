package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.Command;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Servo.ServoSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog

public class FunnelStopperSubsystem extends ServoSubsystem {

    //TODO: generic values
    public static double stopperOpen = 1.0;
    public static double stopperClose = 0.0;

    public static double POSITION_TOLERANCE = 0.1;



    public static FunnelStopperSubsystemAutoLogged instance;

    public static synchronized FunnelStopperSubsystemAutoLogged getInstance() {
        if (instance == null) {
            instance = new FunnelStopperSubsystemAutoLogged("FunnelStopperSubsystem");
        }
        return instance;
    }

    public FunnelStopperSubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        withServo("FunnelStoperSubsystem",Direction.FORWARD,0);
    }


}