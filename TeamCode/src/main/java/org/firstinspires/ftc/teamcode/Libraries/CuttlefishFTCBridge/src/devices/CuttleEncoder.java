package org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;

/** Rotary encoder connected through a motor encoder port */
public class CuttleEncoder {
  public CuttleRevHub hub;
  private final double encTicks;
  private int direction = 1;
  public int mPort;
  private double poseOffset = 0.0;

  /**
   * @param revHub
   * @param port Motor port of the encoder
   * @param countsPerRevolution Number of counts per revolution of the encoder
   */
  public CuttleEncoder(CuttleRevHub revHub, int port, double countsPerRevolution) {
    hub = revHub;
    encTicks = countsPerRevolution;
    mPort = port;
    poseOffset += getPosition() * direction;
  }

  /** Get the rotation of the encoder in rotations */
  public double getPosition() {
    return (getCounts() / encTicks - poseOffset) * direction;
  }

  /**
   * @return the velocity in rps
   */
  public double getVelocity() {
    return hub.bulkData.getEncoderVelocity(mPort) / encTicks * direction;
  }

  /** Get the number of counts that the encoder has turned */
  public int getCounts() {
    return hub.bulkData.getEncoderPosition(mPort);
  }

  /**
   * Set the direction of the encoder.
   *
   * @param direction
   */
  public void setDirection(Direction direction) {
    if (direction == Direction.REVERSE) {
      this.direction = -1;
    } else {
      this.direction = 1;
    }
  }

  /**
   * Manually set the encoder's current position (in rotations, not radians).
   *
   * @param pose Desired position value
   */
  public void setPose(double pose) {
    poseOffset = (getCounts() / encTicks) - (pose * direction);
  }
}
