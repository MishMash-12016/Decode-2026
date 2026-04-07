package org.firstinspires.ftc.teamcode.Subsystems;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Servo.ServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.MMSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;


public class elevatorSubsystem extends MMSubsystem {
    private final double open = 1.0;
    private final double close = 0;

    public static elevatorSubsystem instance;
    private final ServoSubsystem servo;


    public static synchronized elevatorSubsystem getInstance(){
        if (instance == null){
            instance = new elevatorSubsystem("elevatorSubsystem");
        }
        return instance;
    }

    public elevatorSubsystem(String subsystem) {
        super(subsystem);
        this.servo = new ServoSubsystem("servoSubsystem");
        this.servo.withServo(MMRobot.getInstance().expansionHub, 0, Direction.FORWARD, 0);
    }


    public double getClose() {
        return close;
    }

    public double getOpen() {
        return open;
    }

    public Command setPositionCommand(double position) {
        return new InstantCommand(() -> servo.setPosition(position), this);
    }
}
