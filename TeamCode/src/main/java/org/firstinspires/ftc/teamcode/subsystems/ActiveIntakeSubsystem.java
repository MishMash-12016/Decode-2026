package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Velocity.VelocityPidSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog
public class ActiveIntakeSubsystem extends VelocityPidSubsystem {

    //TODO: generic values

    public static double KP = 0.09;
    public static double KI = 0.001;
    public static double KD = 0.002;

    public static double KS = 0.045;
    public static double KV = 0.047;
    public static double KA = 0.1;

    public static double VelTol = 2;

    public static ActiveIntakeSubsystemAutoLogged instance;

    public static synchronized ActiveIntakeSubsystemAutoLogged getInstance() {
        if (instance == null) {
            instance = new ActiveIntakeSubsystemAutoLogged("ActiveIntakeSubsystem");
        }
        return instance;
    }

    public ActiveIntakeSubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        withMotor(mmRobot.controlHub,3, Direction.FORWARD);

        withEncoder(mmRobot.controlHub,2,100,Direction.REVERSE);

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