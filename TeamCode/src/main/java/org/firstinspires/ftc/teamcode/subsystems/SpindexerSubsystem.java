package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Position.PositionPidSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;
import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog
public class SpindexerSubsystem extends PositionPidSubsystem {

    public static double KP = 1;
    public static double KI = 0.0;
    public static double KD = 0.0;

    public static double POSITION_TOLERANCE = 0.0;
    public static double VELOCITY_TOLERANCE = 0.0;

    //ToDo: adjust ratio
    public static double RATIO = 3.30 / 1;
    public static double RESOLUTION = 8192;

    public static final double FIRSTPOS = 0.0;
    public static final double SCNDPOS = 0.0;
    public static final double THIRDPOS = 0.0;


    public static SpindexerSubsystem instance;

    public static synchronized SpindexerSubsystem getInstance() {
        if (instance == null) {
            instance = new SpindexerSubsystemAutoLogged("SpindexerSubsystem");
        }
        return instance;
    }

    public SpindexerSubsystem(String subsystemName) {
        super(subsystemName);

        MMRobot mmRobot = MMRobot.getInstance();
        //TODO: Ports Not Correct

        withEncoder(mmRobot.controlHub,3,(RESOLUTION*RATIO)/360, Direction.REVERSE);

        withCrServo(mmRobot.controlHub, 0,Direction.FORWARD);
        withCrServo(mmRobot.controlHub, 1,Direction.FORWARD);

        withPid(KP, KI, KD);
    }
}