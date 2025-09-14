package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Position.PositionPidSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;
import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog
public class TurretSubsystem extends PositionPidSubsystem {

    //TODO: generic values
    public static double KP = 1;
    public static double KI = 0.0;
    public static double KD = 0.0;

    public static double POSITION_TOLERANCE = 0.05;
    public static double VELOCITY_TOLERANCE = 0.0;

    public static double RATIO = 3.30 / 1;
    public static double RESOLUTION = 8192;


    // Singleton instance
    public static TurretSubsystemAutoLogged instance;

    /**
     * Get the singleton instance of ElevatorSubsystem.
     */
    public static synchronized TurretSubsystemAutoLogged getInstance() {
        if (instance == null) {
            instance = new TurretSubsystemAutoLogged("TurretSubsystem");
        }
        return instance;
    }


    public TurretSubsystem(String subsystemName) {
        super(subsystemName);

        MMRobot mmRobot = MMRobot.getInstance();
        //TODO: Ports Not Correct

        withEncoder(mmRobot.controlHub,3,(RESOLUTION*RATIO)/360, Direction.REVERSE);

        withCrServo(mmRobot.controlHub, 2,Direction.FORWARD);
//        withCrServo(mmRobot.controlHub, 1,Direction.FORWARD);


        withPid(KP, KI, KD);
    }
}