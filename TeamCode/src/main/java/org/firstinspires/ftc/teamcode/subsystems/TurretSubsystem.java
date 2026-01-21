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
import Ori.Coval.Logging.Logger.KoalaLog;

@Config
@AutoLog
public class TurretSubsystem extends PositionPidSubsystem {

    //TODO: wrong ports & values
    CuttleDigital zeroSwitch =  new CuttleDigital(MMRobot.getInstance().expansionHub, 1);
    public static double KP = 0.002;
    public static double KI = 0.02;
    public static double KD = 0.000005;

    public static double POSITION_TOLERANCE = 0.05;
    public static double VELOCITY_TOLERANCE = 0.0;
    public static double IZONE = 15;

    public static double RATIO = 132.0 / 40.0;
    public static double RESOLUTION = 8192;


    // Singleton instance
    public static TurretSubsystemAutoLogged instance;

    /**
     * Get the singleton instance of ElevatorSubsystem.
     */
    public static synchronized TurretSubsystemAutoLogged getInstance() {
        if (instance == null) {
            instance = new TurretSubsystemAutoLogged("TurretSubsystem");
        }
        return instance;
    }


    public TurretSubsystem(String subsystemName) {
        super(subsystemName);

        MMRobot mmRobot = MMRobot.getInstance();

        //todo: change to right hub&port
        withEncoder(mmRobot.expansionHub,1,(RESOLUTION*RATIO) /360, Direction.REVERSE);

        withCrServo(mmRobot.servoHub, 4,Direction.REVERSE);
        withCrServo(mmRobot.servoHub, 5,Direction.REVERSE);


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
        return getToAndHoldSetPointCommand(()->
                KoalaLog.log("angle_to_target", RobotUtils.getAngleToTarget(), true));
    }


}