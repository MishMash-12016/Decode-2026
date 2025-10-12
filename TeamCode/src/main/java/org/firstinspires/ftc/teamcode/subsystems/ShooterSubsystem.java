package org.firstinspires.ftc.teamcode.subsystems;

import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Volts;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.seattlesolvers.solverslib.command.Command;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Velocity.VelocityPidSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.exterpolation.ExterpolationMap;
import org.firstinspires.ftc.teamcode.MMRobot;

import java.util.function.DoubleSupplier;

import Ori.Coval.Logging.AutoLog;
import edu.wpi.first.sysid.SysIdRoutine;

@Config
@AutoLog
public class ShooterSubsystem extends VelocityPidSubsystem {

    ExterpolationMap exterpolationMap = new ExterpolationMap()
            .put(1,6);


    //TODO: tuned not ideal
    public static double KP = 0.6;
    public static double KI = 0.028;
    public static double KD = 0.01;

    public static double KS = 0.0;
    public static double KV = 0.0;
    public static double KA = 0.0;

    public static double VelTol = 5;
    public static double RESOLUTION = 28;


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

        //TODO: temp controlhub (needs to be ex)

        withMotor(mmRobot.expansionHub,1, Direction.REVERSE).minPower = 0;
        withMotor(mmRobot.expansionHub,0, Direction.FORWARD).minPower = 0;

        withEncoder(mmRobot.expansionHub,0,RESOLUTION,Direction.REVERSE);

        withPid(KP,KI,KD);

        withZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

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
    SysIdRoutine sysIdRoutine = new SysIdRoutine(
            new SysIdRoutine.Config(
                    Volts.of(0.01).per(Second),
                    Volts.of(0.6),
                    Seconds.of(10)),
            new SysIdRoutine.Mechanism(
                    this::setPower,
                    null,
                    this,
                    "shooter"
            )
    );

    public Command sysidQuasistatic(SysIdRoutine.Direction direction){
        return sysIdRoutine.quasistatic(direction);
    }

    public Command sysidDynamic(SysIdRoutine.Direction direction){
        return sysIdRoutine.dynamic(direction);
    }

    public Command aimSpeedToShut(DoubleSupplier distance) {
        return getToSetpointCommand(
                ()-> exterpolationMap.exterpolate(distance.getAsDouble()));
    }
}

