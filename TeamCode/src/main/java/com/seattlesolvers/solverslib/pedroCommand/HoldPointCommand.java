/**
 * old pedro v1 command
 */


/*
package com.seattlesolvers.solverslib.pedroCommand;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierPoint;
import com.pedropathing.pathgen.Point;
import com.seattlesolvers.solverslib.command.CommandBase;

*/
/**
 * A command that calls {@link Follower#holdPoint(Pose)}
 *//*

public class HoldPointCommand extends CommandBase {
    private final Follower follower;
    private final Pose pose;
    private final boolean isFieldCentric;

    */
/**
     * Moves robot to a new {@link Pose} that is either field or robot centric
     * @param follower The follower object
     * @param pose The pose that the robot should go to (see isFieldCentric parameter)
     *             The following robot centric movements are true assuming that the robot is facing forwards to the long side on the submersible:
     *             {@link Pose#getX()} -Y is forwards, +Y is backwards
     *             {@link Pose#getY()} +X is left, -X is right
     *             {@link Pose#getHeading()} Heading is in radians, +heading turns left and -heading turns right
     * @param isFieldCentric Whether the move should be field centric or robot centric (based off the follower's position at the time of scheduling the command)
     *//*

    public HoldPointCommand(Follower follower, Pose pose, boolean isFieldCentric) {
        this.follower = follower;
        this.pose = pose;
        this.isFieldCentric = isFieldCentric;
    }

    public HoldPointCommand(Follower follower, Point point, double heading, boolean isFieldCentric) {
        this.follower = follower;
        this.pose = new Pose(point.getX(), point.getY(), heading);
        this.isFieldCentric = isFieldCentric;
    }

    public HoldPointCommand(Follower follower, BezierPoint bezierPoint, double heading, boolean isFieldCentric) {
        this.follower = follower;
        this.pose = new Pose(bezierPoint.getFirstControlPoint().getX(), bezierPoint.getFirstControlPoint().getY(), heading);
        this.isFieldCentric = isFieldCentric;
    }

    @Override
    public void initialize() {
        if (!isFieldCentric) {
            pose.add(follower.getPose());
        }

        follower.holdPoint(pose);
    }

    @Override
    public boolean isFinished() {
        return !follower.isBusy();
    }

    @Override
    public void end(boolean interrupted) {
        follower.breakFollowing();
    }
}*/
