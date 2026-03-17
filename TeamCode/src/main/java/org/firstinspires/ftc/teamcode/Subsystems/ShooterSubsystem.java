package org.firstinspires.ftc.teamcode.Subsystems;

import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Volts;

import Ori.Coval.Logging.AutoLog;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import edu.wpi.first.sysid.SysIdRoutine;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Velocity.VelocityPidSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.exterpolation.ExterpolationMap;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.RobotUtils;

@Config
@AutoLog
public class ShooterSubsystem extends VelocityPidSubsystem {
    static Motor encoder;

    ExterpolationMap exterpolationMap = new ExterpolationMap()
            .put(1, 6);

    public static double KP = 0.045;
    public static double KI = 0;
    public static double KD = 0;

    public static double KS = 0.097;
    public static double KV = 0.0121;
    public static double KA = 0;

    public static double RESOLUTION = 28.0 / (29.0 / 33.0);
    public static double VelTol = 1;
    public static double closeSpeed = 40;
    public static double midSpeed = 45;
    public static double farSpeed = 52;

    public static ShooterSubsystem instance;

    public static synchronized ShooterSubsystem getInstance() {
        if (instance == null) {
            if (MMRobot.getInstance().currentOpMode.opModeType == OpModeType.NonCompetition.DEBUG ||
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

        withMotor("CHPort0", Direction.FORWARD, RESOLUTION);
        withMotor("CHPort1", Direction.FORWARD);

        withPid(KP, KI, KD);

        withZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        withVelocityTolerance(VelTol);

        withFeedforward(KS, KV, KA);

        withDebugPidSuppliers(
                () -> KP,
                () -> KI,
                () -> KD,
                null,
                () -> VelTol,
                null,
                null,
                null,
                () -> KS,
                () -> KV,
                () -> KA);

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

    public Command sysidQuasistatic(SysIdRoutine.Direction direction) {
        return sysIdRoutine.quasistatic(direction);
    }

    public Command sysidDynamic(SysIdRoutine.Direction direction) {
        return sysIdRoutine.dynamic(direction);
    }

    public Command rest() {
    return new SequentialCommandGroup(
            stopCommand(),
            new WaitUntilCommand(()-> getVelocity() < closeSpeed + 8),
            closeSpeed());
    }

    public Command speedByLocation() {
        return (RobotUtils.getDistanceToTarget() < 115) ?
                closeSpeed() : getToAndHoldSetPointCommand(farSpeed);
    }
    public Command closeSpeed() {
        return getToAndHoldSetPointCommand(
                () -> RobotUtils.getDistanceToTarget() < 75 ? closeSpeed : midSpeed);
    }


    @Override
    public void resetHub() {
        instance = null;
    }
}


