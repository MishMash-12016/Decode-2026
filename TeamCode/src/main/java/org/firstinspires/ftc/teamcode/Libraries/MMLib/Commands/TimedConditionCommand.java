package org.firstinspires.ftc.teamcode.Libraries.MMLib.Commands;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandBase;
import java.util.function.BooleanSupplier;

public class TimedConditionCommand extends CommandBase {

  private final BooleanSupplier condition;
  private final ElapsedTime timer;
  private final double startTime;

  public TimedConditionCommand(BooleanSupplier condition, double startTime) {
    this.condition = condition;
    this.timer = new ElapsedTime();
    this.startTime = startTime;
  }

  @Override
  public void initialize() {
    timer.reset();
  }

  @Override
  public void execute() {
    if (!condition.getAsBoolean()) {
      timer.reset();
    }
  }

  @Override
  public boolean isFinished() {
    return timer.milliseconds() >= startTime;
  }
}
