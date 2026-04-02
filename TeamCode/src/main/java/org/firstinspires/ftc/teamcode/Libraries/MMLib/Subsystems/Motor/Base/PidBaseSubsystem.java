package org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Motor.Base;

import Ori.Coval.Logging.AutoLogOutput;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.button.Trigger;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleDigital;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleEncoder;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleRevHub;
import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.PID.Controllers.PIDController;

public abstract class PidBaseSubsystem extends MotorOrCrServoSubsystem {
  private CuttleEncoder encoder;
  public PIDController pidController = new PIDController(0, 0, 0);

  boolean shouldWrapAngle = false;
  double angleRange;

  // base
  public PidBaseSubsystem(String subsystemName) {
    super(subsystemName);
  }

  /**
   * Creates a Command that keeps the mechanism in place using PID control.
   *
   * @return a Command requiring this subsystem
   */
  // this command is the base for all the other pid commands
  public abstract Command getToAndHoldSetPointCommand(DoubleSupplier setPoint);

  /**
   * Creates a Command that keeps the mechanism in place using PID control.
   *
   * @return a Command requiring this subsystem
   */
  public Command getToAndHoldSetPointCommand(double setPoint) {
    return getToAndHoldSetPointCommand(() -> setPoint);
  }

  /**
   * Creates a Command that moves the mechanism to the specified setpoint using PID control.
   *
   * @param setPoint target setpoint (in encoder units, adjusted by ratio)
   * @return a Command requiring this subsystem
   */
  public Command getToSetpointCommand(double setPoint) {
    return getToAndHoldSetPointCommand(() -> setPoint).interruptOn(this::getAtSetpoint);
  }

  /**
   * Creates a Command that moves the mechanism to the specified setpoint using PID control.
   *
   * @param setPoint target setpoint (in encoder units, adjusted by ratio)
   * @return a Command requiring this subsystem
   */
  public Command getToSetpointCommand(DoubleSupplier setPoint) {
    return getToAndHoldSetPointCommand(setPoint).interruptOn(this::getAtSetpoint);
  }

  /**
   * Creates a Command that keeps the mechanism in its current setpoint place using PID control.
   *
   * @return a Command requiring this subsystem
   */
  public Command holdCurrentSetPointCommand() {
    return getToAndHoldSetPointCommand(() -> pidController.getSetpoint());
  }

  /**
   * Creates a Command that keeps the mechanism in its current setpoint place using PID control.
   *
   * @return a Command requiring this subsystem
   */
  public Command holdCurrentPoseCommand() {
    return getToAndHoldSetPointCommand(this::getPose);
  }

  /**
   * Returns the current position (pose) provided by the encoder.
   *
   * @return current pose in encoder units (divided by ratio)
   */
  @AutoLogOutput
  public double getPose() {
    if (shouldWrapAngle) {
      double angle = encoder.getPosition();
      angle = angle % angleRange;
      if (angle < 0) angle += angleRange;
      return angle;
    }
    return encoder.getPosition();
  }

  @AutoLogOutput
  public double getVelocity() {
    return encoder.getVelocity();
  }

  @AutoLogOutput
  public boolean getAtSetpoint() {
    return pidController.atSetpoint();
  }

  @AutoLogOutput
  public double getError() {
    return pidController.getError();
  }

  @AutoLogOutput
  public double getSetPoint() {
    return pidController.getSetpoint();
  }

  public void setSetpoint(double setpoint) {
    pidController.setSetpoint(setpoint);
  }

  public void setPose(double pose){
      encoder.setPose(pose);
  }

  public void withEncoder(CuttleRevHub revHub, int port, double cpr, Direction direction) {
    encoder = new CuttleEncoder(revHub, port, cpr);
  }

  /**
   * Configures a zero-position limit switch that resets encoder when activated.
   *
   * @param zeroSwitch digital switch input
   * @param zeroPose encoder value to set when switch is pressed
   */
  public void withZeroSwitch(CuttleDigital zeroSwitch, double zeroPose) {
    new Trigger(zeroSwitch::getState).whenActive(() -> setPose(zeroPose));
  }

  /**
   * Configures a zero-position limit switch that resets encoder when activated.
   *
   * @param zeroSupplier a supplier of when to zero the system
   * @param zeroPose encoder value to set when switch is pressed
   */
  public void withZeroSupplier(BooleanSupplier zeroSupplier, double zeroPose) {
    new Trigger(zeroSupplier).whenActive(() -> setPose(zeroPose));
  }

  /**
   * Updates PID gains.
   *
   * @param kp proportional gain
   * @param ki integral gain
   * @param kd derivative gain
   */
  public void withPid(double kp, double ki, double kd) {
    pidController.setPID(kp, ki, kd);
  }

  /**
   * Defines the Integral Zone (I-Zone) for the PID controller.
   *
   * @param iZone range around setpoint where integral is active
   */
  public void withIZone(double iZone) {
    pidController.setIZone(iZone);
  }

  /**
   * Restricts the integral accumulator within given bounds.
   *
   * @param minIntegralRange minimum accumulator value
   * @param maxIntegralRange maximum accumulator value
   */
  public void withIntegralRange(double minIntegralRange, double maxIntegralRange) {
    pidController.setIntegratorRange(minIntegralRange, maxIntegralRange);
  }

  /**
   * Restricts the integral accumulator within given bounds.
   *
   * @param minIntegralRange minimum accumulator value
   */
  public void withMinIntegralRange(double minIntegralRange) {
    withIntegralRange(minIntegralRange, pidController.getMinimumIntegral());
  }

  /**
   * Restricts the integral accumulator within given bounds.
   *
   * @param maxIntegralRange minimum accumulator value
   */
  public void withMaxIntegralRange(double maxIntegralRange) {
    withIntegralRange(pidController.getMinimumIntegral(), maxIntegralRange);
  }

  public void withSetpointLimit(double minSetpoint, double maxSetpoint) {
    pidController.setMinSetpoint(minSetpoint);
    pidController.setMaxSetpoint(maxSetpoint);
  }

  public void withWrappedAngleRange(double angleRange) {
    this.angleRange = angleRange;
    shouldWrapAngle = true;
    pidController.enableContinuousInput(0, angleRange);
  }
}
