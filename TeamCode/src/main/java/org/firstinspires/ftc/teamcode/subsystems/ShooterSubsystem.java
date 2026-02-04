package org.firstinspires.ftc.teamcode.subsystems;

import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Volts;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.seattlesolvers.solverslib.command.Command;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Velocity.VelocityPidSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.MMUtils;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.exterpolation.ExterpolationMap;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.RobotUtils;

import java.util.function.DoubleSupplier;

import Ori.Coval.Logging.AutoLog;
import edu.wpi.first.sysid.SysIdRoutine;

@Config
@AutoLog
public class ShooterSubsystem extends VelocityPidSubsystem {

    ExterpolationMap exterpolationMap = new ExterpolationMap()
            .put(1,6);


    //TODO: tuned not ideal
    public static double KP = 0.0;
    public static double KI = 0.0;
    public static double KD = 0.0;

    public static double KS = 0.085176;
    public static double KV = 0.010802;
    public static double KA = 0.012526;


    public static double VelTol = 5;
    public static double RESOLUTION = 28;


    public static ShooterSubsystem instance;

    public static synchronized ShooterSubsystem getInstance() {
        if (instance == null) {
            if (MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG_SERVOHUB ||
                    MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG ||
                    MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION) {
                instance = new ShooterSubsystemAutoLogged("ShooterSubsystem");

            } else {
                instance = new ShooterSubsystem("ShooterSubsystem");
            }
        }
        return instance;
    }


    public ShooterSubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        withMotor(mmRobot.expansionHub,1, Direction.REVERSE);
        withMotor(mmRobot.expansionHub,0, Direction.FORWARD);

        withEncoder(mmRobot.expansionHub,0,RESOLUTION,Direction.FORWARD);

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
                    Volts.of(0.1).per(Second),
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

    public Command speedByDistance(double distance) {
        return getToAndHoldSetPointCommand(
                ()-> exterpolationMap.exterpolate(distance));
    }
    public Command speedByLocation() {
        return speedByDistance(RobotUtils.getDistanceToTarget());
    }
}

