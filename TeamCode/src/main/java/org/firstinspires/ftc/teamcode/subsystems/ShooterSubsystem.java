package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleRevHub;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Velocity.VelocityPidSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog
public class ShooterSubsystem extends VelocityPidSubsystem {

    //TODO: generic values
    public static double KP = 1;
    public static double KI = 0.0;
    public static double KD = 0.0;

    public static double KS = 0.0;
    public static double KV = 0.0;
    public static double KA = 0.0;

    public static double VelTol = 0.5;
    public static double RESOLUTION = 28;
    CuttleRevHub ColorSensor;


    public static ShooterSubsystemAutoLogged instance;

    public static synchronized ShooterSubsystemAutoLogged getInstance() {
        if (instance == null) {
            instance = new ShooterSubsystemAutoLogged("ShooterSubsystem");
        }
        return instance;
    }


    public ShooterSubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        //TODO: Ports Not Correct

        withMotor(mmRobot.expansionHub,1, Direction.REVERSE);
        withMotor(mmRobot.expansionHub,0, Direction.FORWARD);

        withEncoder(mmRobot.expansionHub,0,RESOLUTION,Direction.FORWARD);

        withPid(KP,KI,KD);

        withZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        withVelocityTolerance(VelTol);

        withFeedforward(KS,KV,KA);

        withDebugPidSuppliers(
                ()-> KP,
                ()->KI,
                ()->KD,
                null,
                ()-> VelTol,
                null,
                null,
                null,
                ()-> KS,
                ()->KV,
                ()->KA
        );

    }

}