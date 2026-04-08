package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Servo.ServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.MMSubsystem;
import org.firstinspires.ftc.teamcode.MMRobot;


public class elevatorSubsystem extends MMSubsystem {
    private final double MAX_HEIGHT_TICKS = 2500;
    private final double MIN_HEIGHT_TICKS = 0;

    public static elevatorSubsystem instance;
    private final ServoSubsystem servo;
    private DcMotor motor;


    public static synchronized elevatorSubsystem getInstance(){
        if (instance == null){
            instance = new elevatorSubsystem("elevatorSubsystem");
        }
        return instance;
    }

    public elevatorSubsystem(String subsystem) {
        super(subsystem);
        servo = new ServoSubsystem("servoSubsystem");
        servo.withServo(MMRobot.getInstance().expansionHub, 0, Direction.FORWARD, 0);
        motor = MMRobot.getInstance().currentOpMode.hardwareMap.get(DcMotor.class, "motor");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public Command setPositionCommand(double position) {
        return new InstantCommand(() -> servo.setPosition(position), this);
    }

    public Command goToPositionCommand(int heightInTicks){
        return new InstantCommand(() -> {
            motor.setTargetPosition(heightInTicks);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(1.0);
        }, this);
    }
    public Command raiseElevator(int heightInTicks){
        if (heightInTicks > MAX_HEIGHT_TICKS) heightInTicks = (int) MAX_HEIGHT_TICKS;
        else if (heightInTicks < MIN_HEIGHT_TICKS) heightInTicks = (int) MIN_HEIGHT_TICKS;

        return goToPositionCommand(heightInTicks);
    }


}
