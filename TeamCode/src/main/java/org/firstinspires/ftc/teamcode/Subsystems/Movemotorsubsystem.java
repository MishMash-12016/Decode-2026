package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.seattlesolvers.solverslib.command.Command;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleRevHub;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.MMSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Base.MotorOrCrServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Base.PidBaseSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.MMMotorOrCrServo;
import org.firstinspires.ftc.teamcode.MMRobot;

import java.util.function.DoubleSupplier;

public abstract class Movemotorsubsystem extends PidBaseSubsystem {

    public static Movemotorsubsystem instance;
    public static double RESOLUTION = 28.0 / (29.0 / 33.0);

    public static synchronized Movemotorsubsystem getInstance() {
        if (instance == null) {
            instance = new Movemotorsubsystem("Movemotorsubsystem") {
            };
        }
        return instance;
    }
    public Movemotorsubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        withMotor(MMRobot.getInstance().controlHub, 0, Direction.FORWARD );
        withEncoder(mmRobot.expansionHub, 0, RESOLUTION, Direction.REVERSE);
    }

    @Override
    public Command getToAndHoldSetPointCommand(DoubleSupplier setPoint) {
        return null;
    }

}

