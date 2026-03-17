package org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;

/**
 * Cuttlefish compatible servo.
 * Contains a list of preset positions which can be appended to using the addPreset() function.
 *
 * */
public class CuttleServo{

    private double pos = 0.0;
    public int port;
    boolean enabled = false;
    final boolean FTCServo;
    final String servoName;
    double offset = 0.0;

    Direction direction = Direction.FORWARD;
    com.qualcomm.robotcore.hardware.Servo ftcServoDevice;

    /**
     * Initialize servo using hardwareMap
     * @param hardwareMap hardwareMap object
     * @param name Name of the servo in the config
     * */
    public CuttleServo(HardwareMap hardwareMap, String name)
    {
        FTCServo = true;
        ftcServoDevice = hardwareMap.get(com.qualcomm.robotcore.hardware.Servo.class,name);

        servoName = name;
    }

    /**
     * Set the target position of the servo
     * @param position Target position
     * */
    public void setPosition(double position) {
        double offsetPose = position + offset;
        pos = direction == Direction.REVERSE ? 1 - offsetPose : offsetPose;
        ftcServoDevice.setPosition(pos);
    }

    /**
     * Get the target position of the servo.
     * <br>
     * IMPORTANT: This will not give the actual position of the servo.
     * */
    public double getPosition() {
        return pos;
    }

    public CuttleServo setDirection(Direction direction) {
        this.direction = direction;
        return this;
    }

    public Direction getDirection() {
        return direction;
    }

    public CuttleServo setOffset(double offset) {
        this.offset = offset;
        return this;
    }

    public double getOffset(){
        return offset;
    }

    public boolean getFtcServo(){
        return FTCServo;
    }

    /**
     *
     * @return the servo name. null if servo is connected to a hub and doesnt have a name
     */
    public String getServoName() {
        return servoName;
    }
}
