package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleDigital;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleRevHub;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Position.PositionPidSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;
import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog
public class SpindexerSubsystem extends PositionPidSubsystem {

    CuttleDigital zeroSwitch =  new CuttleDigital(MMRobot.getInstance().expansionHub, 0);
    ColorSensor colorSensor = MMRobot.getInstance().currentOpMode.hardwareMap.get(ColorSensor.class,"spinColor");
    // the lowest value of light without a ball in
    public static final double ALPHA_TOLERANCE = 0.01;

    public static double KP = 0.002;
    public static double KI = 0.0;
    public static double KD = 0.0;

    public static double POSITION_TOLERANCE = 3.5;

    //ToDo: adjust ratio
    public static double RESOLUTION = 8192;

    public static final double FIRSTPOS = 0;
    public static final double SCNDPOS = FIRSTPOS+100;
    public static final double THIRDPOS = SCNDPOS+120;

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

        withEncoder(mmRobot.controlHub,3,RESOLUTION/360, Direction.REVERSE);

        withCrServo(mmRobot.controlHub, 0,Direction.FORWARD);
        withCrServo(mmRobot.controlHub, 1,Direction.FORWARD);

        withZeroSwitch(zeroSwitch,0);

        withPid(KP, KI, KD);
        withDebugPidSuppliers(
                ()-> KP,
                ()->KI,
                ()->KD,
                null,
                null,
                null,
                null,
                null
        );    }
    public float getAlphaColor(){
        return colorSensor.alpha();
    }
    public boolean getZeroSwitch(){
        return zeroSwitch.getState();
    }
}