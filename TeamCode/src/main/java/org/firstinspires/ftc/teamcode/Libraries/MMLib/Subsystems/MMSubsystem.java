package org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems;

import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.MMRobot;

public abstract class MMSubsystem extends SubsystemBase {

    public MMSubsystem() {
        super();
        MMRobot.getInstance().subsystems.add(this);
    }
    public abstract void resetHub();
}
