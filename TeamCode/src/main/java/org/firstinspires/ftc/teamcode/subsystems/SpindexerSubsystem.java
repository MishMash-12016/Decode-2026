package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
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
//    ColorSensor colorSensor = MMRobot.getInstance().currentOpMode.hardwareMap.get(ColorSensor.class,"spinColor");
    DistanceSensor distanceSensor = MMRobot.getInstance().currentOpMode.hardwareMap.get(DistanceSensor.class,"spinColor");
    // the lowest value of light without a ball in
    public static final double ALPHA_TOLERANCE = 4;

    public static double KP = 0.0032;
    public static double KI = 0.0000001;
    public static double KD = 0.0001;

    public static double POSITION_TOLERANCE = 3.5;

    //ToDo: adjust ratio
    public static double RESOLUTION = 8192;

    public static final double FIRSTPOS = 0;
    public static final double SCNDPOS = FIRSTPOS+120;
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

        withEncoder(mmRobot.controlHub,3,RESOLUTION/360, Direction.REVERSE);

        withCrServo(mmRobot.controlHub, 0,Direction.FORWARD);
        withCrServo(mmRobot.controlHub, 1,Direction.FORWARD);
        withZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        withZeroSwitch(zeroSwitch,10);
        withAngleRange(360);

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
//    public float getAlphaColor(){
//        return colorSensor.alpha();
//    }
    public double getDistance() {
        return distanceSensor.getDistance(DistanceUnit.CM);
    }
    public boolean getZeroSwitch(){
        return zeroSwitch.getState();
    }
    public Command setPosition(double position){
        return new InstantCommand(() -> setPose(position));
    }

    public static Command reset(){
        return new SequentialCommandGroup(
                SpindexerSubsystem.getInstance().setPowerInstantCommand(0.2),
                new WaitUntilCommand(()->(!(SpindexerSubsystem.getInstance().getZeroSwitch()))),
                SpindexerSubsystem.getInstance().setPosition(0),
                SpindexerSubsystem.getInstance().setPowerInstantCommand(0)
        );

    }
}