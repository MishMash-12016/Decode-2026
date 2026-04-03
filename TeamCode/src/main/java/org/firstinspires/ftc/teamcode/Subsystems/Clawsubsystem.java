package org.firstinspires.ftc.teamcode.Subsystems;

import static org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.MMWebcamSubsystem.instance;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.utils.Direction;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.Servo.ServoSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVariables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;

import Ori.Coval.Logging.AutoLog;

@Config
@AutoLog
public class Clawsubsystem extends ServoSubsystem {

    public static double open = 0.95;
    public static double close = 0.00;

    public static Clawsubsystem instance;

    public static synchronized Clawsubsystem getInstance() {
        if (instance == null) {
                instance = new ClawsubsystemAutoLogged("Clawsubsystem");
            }

        return instance;
    }

    public Clawsubsystem(String subsystemName) {
        super(subsystemName);
        MMRobot mmRobot = MMRobot.getInstance();

        ///port 0
        withServo(MMRobot.getInstance().expansionHub, 1, Direction.FORWARD, 0);
    }

    public Command Clawopen(){
        return setPositionCommand(open);
    }

    public Command Clawclose(){
        return setPositionCommand(close);
    }

}

