package org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;

/** Cuttlefish DCMotor implementation. */
public class CuttleMotor {
  CuttleRevHub hub;
  int mPort;
  int sign = 1;
  public double power;

    /**
     * @param revHub the control/expension hub
     * @param port   the port it is connected to on the control/expension hub
     */
    public CuttleMotor(CuttleRevHub revHub, int port) {
        hub = revHub;
        mPort = port;
    }

    /**
     * @param revHub the control/expension hub
     * @param port   the port it is connected to on the control/expension hub
     */
    public CuttleMotor(CuttleRevHub revHub, int port, Direction direction) {
        this(revHub, port);
        setDirection(direction);
    }


  /**
   * MishMash added
   *
   * @return power sent to motor
   */
  public double getPower() {
      return power;
  }

  /**
   * @param power
   */
  public void setPower(double power) {
    this.power = power;
    sendPower();
  }

  /**
   * Send cached motor power to the hub. <br>
   * This is not necessary and should not be used under ordinary conditions.
   */
  public void sendPower() {
    hub.setMotorPower(mPort, sign * power);
  }

  /**
   * Set the direction of the motor
   *
   * @param direction
   */
  public void setDirection(Direction direction) {
    if (direction == Direction.FORWARD) {
      sign = 1;
    } else {
      sign = -1;
    }
  }

  /**
   * Get motor current in milli-amps. <br>
   * WARNING: This will poll the hub an extra time costing about 3ms.
   *
   * @return Motor current in milli-amps
   */
  public int getCurrent() {
    return this.hub.getMotorCurrent(this.mPort);
  }

    /**
     * Set the zero power behaviour of the motor.
     *
     * @param behaviour choose if the motor should break or coast
     */
  public void setZeroPowerBehaviour(DcMotor.ZeroPowerBehavior behaviour) {
    this.hub.setMotorZeroPowerBehaviour(this.mPort, behaviour);
  }
}
