package org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables;

import org.firstinspires.ftc.teamcode.MMRobot;

/**
 * this represents the OpMode type that has been activated.
 * <p>
 * the code that control on what to activate on which type, is located in the {@link MMRobot} class.
 * <p>
 * <b>COMPETITION:</b>
 * <p>
 * {@link Competition#TELEOP TELEOP} - Competition Teleop
 * <p>
 * {@link Competition#AUTO AUTO} - Competition Autonomous
 * <p>
 * <b>NON-COMPETITION:</b>
 * <p>
 * {@link NonCompetition#DEBUG DEBUG} - Special OpMode to control every system independently and make sure everything is working (or tuning servos).
 * <p>
 * {@link NonCompetition#EXPERIMENTING_NO_EXPANSION EXPERIMENTING_NO_EXPANSION}.
 * this can be used if u don't have an expansion plugged in, and u just want to experiment with the control hub.
 */


public interface OpModeType {

    enum Competition implements OpModeType {
        TELEOP,
        AUTO
    }

    enum NonCompetition implements OpModeType {
        DEBUG,
        DEBUG_SERVOHUB,
        EXPERIMENTING_NO_EXPANSION
    }
}

