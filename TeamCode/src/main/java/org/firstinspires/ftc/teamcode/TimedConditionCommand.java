package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandBase;
import java.util.function.BooleanSupplier;


public class TimedConditionCommand extends CommandBase {

    private BooleanSupplier condition;
    private ElapsedTime timer;
    private double time;
    private int counter = 0;
    public TimedConditionCommand(BooleanSupplier condition,double time){
        this.condition = condition;
        this.timer = new ElapsedTime();
        this.time = time;
    }


    @Override
    public void initialize() {
        timer.reset();
    }

    @Override
    public void execute() {
        if(!condition.getAsBoolean()){
            timer.reset();
            counter++;
        }
    }

    @Override
    public boolean isFinished() {
        return timer.seconds()>=3;
    }
}
