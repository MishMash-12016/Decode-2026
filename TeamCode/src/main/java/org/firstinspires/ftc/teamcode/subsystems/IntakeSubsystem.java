package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Velocity.VelocityPidSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog
public class IntakeSubsystem extends VelocityPidSubsystem {

    //TODO: generic values

    public static double KP = 1;
    public static double KI = 0.0;
    public static double KD = 0.0;

    public static double KS = 0.0;
    public static double KV = 0.0;
    public static double KA = 0.0;

    public static double VelTol = 2;

    public static IntakeSubsystemAutoLogged instance;

    public static synchronized IntakeSubsystemAutoLogged getInstance() {
        if (instance == null) {
            instance = new IntakeSubsystemAutoLogged("IntakeSubsystem");
        }
        return instance;
    }

    public IntakeSubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        withMotor(mmRobot.expansionHub,2, Direction.FORWARD);
        withEncoder(mmRobot.expansionHub,0,0,Direction.FORWARD);
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