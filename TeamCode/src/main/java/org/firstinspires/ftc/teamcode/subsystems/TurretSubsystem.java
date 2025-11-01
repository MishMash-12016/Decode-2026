package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RamseteCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleDigital;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.MMDrivetrain;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Position.PositionPidSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.RobotUtils;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog
public class TurretSubsystem extends PositionPidSubsystem {

    //TODO: wrong ports & values
    CuttleDigital zeroSwitch =  new CuttleDigital(MMRobot.getInstance().expansionHub, 1);
    public static double KP = 1;
    public static double KI = 0.0;
    public static double KD = 0.0;

    public static double POSITION_TOLERANCE = 0.05;
    public static double VELOCITY_TOLERANCE = 0.0;
    public static double IZONE = 20;

    public static double RATIO = 3.30 / 1;
    public static double RESOLUTION = 8192;


    // Singleton instance
    public static TurretSubsystemAutoLogged instance;

    /**
     * Get the singleton instance of ElevatorSubsystem.
     */
    public static synchronized TurretSubsystemAutoLogged getInstance() {
        if (instance == null) {
            instance = new TurretSubsystemAutoLogged("SpindexerSubsystem");
        }
        return instance;
    }


    public TurretSubsystem(String subsystemName) {
        super(subsystemName);

        MMRobot mmRobot = MMRobot.getInstance();
        //TODO: Ports Not Correct

        withEncoder(mmRobot.expansionHub,3,(RESOLUTION*RATIO) /360, Direction.REVERSE);

        withCrServo(mmRobot.expansionHub, 0,Direction.FORWARD);
        withCrServo(mmRobot.expansionHub, 1,Direction.FORWARD);

        withZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        withAngleRange(360);

        withPid(KP, KI, KD);
        withIZone(IZONE);
        withPositionTolerance(POSITION_TOLERANCE);
        withDebugPidSuppliers(
                ()-> KP,
                ()->KI,
                ()->KD,
                ()->IZONE,
                ()->POSITION_TOLERANCE,
                null,
                null,
                null
        );
    }

    public boolean getZeroSwitch(){
        return zeroSwitch.getState();
    }
    public Command setPosition(double position){
        return new InstantCommand(() -> setPose(position));
    }

    public Command reset(){
        return new SequentialCommandGroup(
                setPowerInstantCommand(0.1),
                new WaitUntilCommand(()->(!(getZeroSwitch()))),
                setPosition(0),
                setPowerInstantCommand(0)
        );
    }
    public Command alignToTarget(){
        return getToAndHoldSetPointCommand(()-> RobotUtils.getAngleToTarget()
                - MMDrivetrain.getInstance().getFollower().getHeading());
    }


}