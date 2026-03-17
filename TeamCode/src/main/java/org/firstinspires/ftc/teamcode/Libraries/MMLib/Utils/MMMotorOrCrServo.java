package org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils;

import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleCrServo;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleMotor;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.MMRobot;

/** A class that handles the interaction of motors and crServos as a single object */
public class MMMotorOrCrServo {

  private Motor motor;
  private CuttleCrServo crServo;
  private String crServoName;
  private int direction = 1;

  public MMMotorOrCrServo(Motor motor) {
    this.motor = motor;
  }

  public MMMotorOrCrServo(CuttleCrServo crServo) {
    this.crServo = crServo;
  }

  public MMMotorOrCrServo(String crServoName) {
    this.crServoName = crServoName;
    this.crServo = new CuttleCrServo(MMRobot.getInstance().currentOpMode.hardwareMap, crServoName);
  }

  /**
   * sets the motor/crServo power
   *
   * @param power motor power (-1.0 to 1.0)
   */
  public void setPower(double power) {
    if (motor != null) {
      motor.set(power * direction);
    } else if (crServo != null) {
      crServo.setPower(power);
    }
  }

  public void setDirection(Direction direction) {
    if (motor != null) {
      if (direction == Direction.FORWARD){
        this.direction = 1;
      }else{
        this.direction = -1;
      }
    } else if (crServo != null) {
      crServo.setDirection(direction);
    }
  }

  /** sets the zero power behavior(!! this only effect motors !!) */
  public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
    if (motor != null) {
      if(zeroPowerBehavior == ZeroPowerBehavior.BRAKE){
        motor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
      }
      else{
        motor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);
      }
    }
  }

  public double getPower() {
    if (motor != null) {
      return motor.getRawPower() * direction;
    } else {
      return crServo.getPower();
    }
  }
}
