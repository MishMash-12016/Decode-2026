package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Velocity.VelocityPidSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog
public class TransferSubsystem extends VelocityPidSubsystem {

    //TODO: generic values

    public static double KP = 0.09;
    public static double KI = 0.08;
    public static double KD = 0.006;

    public static double KS = 0.045;
    public static double KV = 0.047;
    public static double KA = 0.1;

    public static double VelTol = 10;

    public static TransferSubsystemAutoLogged instance;

    public static synchronized TransferSubsystemAutoLogged getInstance() {
        if (instance == null) {
            instance = new TransferSubsystemAutoLogged("ShooterSubsystem");
        }
        return instance;
    }
    public TransferSubsystem(String subsystemName) {
        super(subsystemName);

        MMRobot mmRobot = MMRobot.getInstance();

        withMotor(mmRobot.controlHub,1, Direction.REVERSE);

        withEncoder(mmRobot.controlHub,7,100,Direction.FORWARD);

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