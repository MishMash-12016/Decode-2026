package org.firstinspires.ftc.teamcode.Libraries.MMLib;

import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.AutoLogPose2d;
import Ori.Coval.Logging.Logger.KoalaLog;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.geometry.Rotation2d;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import org.firstinspires.ftc.teamcode.Libraries.MMLib.PID.Controllers.PIDController;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.MMSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.MMUtils;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.Libraries.pedroPathing.Drawing;
import org.firstinspires.ftc.teamcode.Libraries.pedroPathing.HoldPointCommand;
import org.firstinspires.ftc.teamcode.Libraries.pedroPathing.PinpointVisionLocalizer;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.RobotUtils;

@AutoLog
@Config
public class MMDrivetrain extends MMSubsystem {

    public double slowModeRatioForward = 0.3;
    public double slowModeRatioLateral = 0.3;
    public double slowModeRatioRotation = 0.2;

    public PIDController headingPID;
    public static double headingKP = 0.004;
    public static double headingKI = 0.0;
    public static double headingKD = 0.00013;
    public static double headingKS = 0.09;


    public PIDController secondaryHeadingPID;
    public static double secondaryHeadingKP = 0.017;
    public static double secondaryHeadingKI = 0.00005;
    public static double secondaryHeadingKD = 0.000007;
    public static double secondaryHeadingKS = 0.03;

    public static double headingTolerance = 0.50;
    public static double switchToSecondaryHeading = 10;

    private double headingPIDPower;
    private double secondaryHeadingPIDPower;


    private static MMDrivetrain instance;
    private static Follower follower;

    public static synchronized MMDrivetrain getInstance() {
        if (instance == null) {
            instance = new MMDrivetrainAutoLogged();
        }

        if (follower == null) {
            follower = Constants.createFollower(MMRobot.getInstance().currentOpMode.hardwareMap);
        }

        if (follower == null) {
            follower = Constants.createFollower(MMRobot.getInstance().currentOpMode.hardwareMap);
        }
        return instance;
    }

    public Follower getFollower() {
        if (follower == null) {
            follower = Constants.createFollower(MMRobot.getInstance().currentOpMode.hardwareMap);
        }
        return follower;
    }

    public static void update() {
        if (instance != null) {
            follower.update(); // updates the follower
            Drawing.drawDebug(follower);
        }
    }

    public MMDrivetrain() {
        headingPID = new PIDController(headingKP, headingKI, headingKD);
        headingPID.enableContinuousInput(0, 360);
        secondaryHeadingPID = new PIDController(secondaryHeadingKP, secondaryHeadingKI, secondaryHeadingKD);
        secondaryHeadingPID.enableContinuousInput(0, 360);
        secondaryHeadingPID.setTolerance(headingTolerance);

        follower = Constants.createFollower(MMRobot.getInstance().currentOpMode.hardwareMap);

        withDebugPidSuppliers(
                () -> headingKP, () -> headingKI, () -> headingKD,
                () -> secondaryHeadingKP, () -> secondaryHeadingKI,
                () -> secondaryHeadingKD,()->headingKS,()->secondaryHeadingKS,
                () -> headingTolerance, ()-> switchToSecondaryHeading);
    }

    @Override
    public void resetHub() {
        //        instance = null;
    }

    public CommandBase driveCommand(
            DoubleSupplier forwardDrive,
            DoubleSupplier lateralDrive,
            DoubleSupplier heading,
            BooleanSupplier slowMode) {
        return this.driveCommand(forwardDrive, lateralDrive, heading, true, slowMode);
    }

    public CommandBase driveAligned(
            DoubleSupplier forwardDrive,
            DoubleSupplier lateralDrive,
            boolean robotCentric,
            BooleanSupplier slowMode) {

        return (CommandBase) new RunCommand(() -> {
            Rotation2d target_angle = RobotUtils.getAngleToTarget().plus(Rotation2d.fromDegrees(180));
            double error = target_angle.getDegrees() - Math.toDegrees(getPose().getHeading());

            headingPIDPower = headingPID.calculate(
                    Math.toDegrees(getPose().getHeading()),
                    target_angle.getDegrees());
            secondaryHeadingPIDPower = secondaryHeadingPID.calculate(
                    Math.toDegrees(getPose().getHeading()),
                    target_angle.getDegrees());

            double headingPower = ((Math.abs(error) > switchToSecondaryHeading) ?
            (headingPIDPower + Math.copySign(headingKS, headingPIDPower)) :
            (secondaryHeadingPIDPower + Math.copySign(secondaryHeadingKS, secondaryHeadingPIDPower)));

            KoalaLog.log("heading_pid/current_heading", Math.toDegrees(getPose().getHeading()), true);
            KoalaLog.log("heading_pid/headingPow", headingPower, true);
            KoalaLog.log("heading_pid/headingPID", headingPID.calculate(Math.toDegrees(getPose().getHeading()), target_angle.getDegrees()), true);
            KoalaLog.log("heading_pid/headingPKS", Math.copySign(headingKS,error), true);
            KoalaLog.log("heading_pid/isSecondaryHeading", (Math.abs(error) < switchToSecondaryHeading), true);
            KoalaLog.log("heading_pid/target_heading",  target_angle.getDegrees(), true);
            KoalaLog.log("heading_pid/error", error, true);
            if (headingPower > 0.5) {
                headingPower = 0.5;
            }
            double maxPower = 1 - headingPower;

            double forwardDrivePower = 0;
            double lateralDrivePower = 0;
            double translationPowerSum = 0;

            forwardDrivePower = forwardDrive.getAsDouble();
            lateralDrivePower = lateralDrive.getAsDouble();

            translationPowerSum = Math.abs(forwardDrivePower) + Math.abs(lateralDrivePower);
            if (translationPowerSum > maxPower) {
                double normalizeTo1 = maxPower / translationPowerSum;

                forwardDrivePower *= normalizeTo1;
                lateralDrivePower *= normalizeTo1;
            }

            follower.setTeleOpDrive(
                    forwardDrivePower * (slowMode.getAsBoolean() ? slowModeRatioForward : 1),
                    lateralDrivePower * (slowMode.getAsBoolean() ? slowModeRatioLateral : 1),
                    headingPower,
                    robotCentric);
            follower.update();
        }, this)
                .beforeStarting(() -> follower.startTeleopDrive());
    }

    public CommandBase driveCommand(
            DoubleSupplier forwardDrive,
            DoubleSupplier lateralDrive,
            DoubleSupplier heading,
            boolean robotCentric,
            BooleanSupplier slowMode) {
        return (CommandBase)
                new RunCommand(
                        () -> {
                            double forwardDrivePower = 0;
                            double lateralDrivePower = 0;
                            double headingPower = 0;

                            forwardDrivePower = forwardDrive.getAsDouble();
                            lateralDrivePower = lateralDrive.getAsDouble();
                            headingPower = heading.getAsDouble();

                            double powerSum =
                                    Math.max(
                                            Math.abs(forwardDrivePower)
                                                    + Math.abs(lateralDrivePower)
                                                    + Math.abs(headingPower),
                                            1);
                            if (powerSum > 1) {
                                double normalizeTo1 = 1 / powerSum;

                                forwardDrivePower *= normalizeTo1;
                                lateralDrivePower *= normalizeTo1;
                                headingPower *= normalizeTo1;
                            }

                            follower.setTeleOpDrive(
                                    forwardDrivePower * (slowMode.getAsBoolean() ? slowModeRatioForward : 1),
                                    lateralDrivePower * (slowMode.getAsBoolean() ? slowModeRatioLateral : 1),
                                    headingPower * (slowMode.getAsBoolean() ? slowModeRatioRotation : 0.5),
                                    robotCentric);
                            follower.update();
                        }, this)
                        .beforeStarting(
                                () -> follower.startTeleopDrive());
    }

    public CommandBase turnCommand(double radians, boolean isLeft) {
        Pose temp =
                new Pose(
                        follower.getPose().getX(),
                        follower.getPose().getY(),
                        follower.getPose().getHeading() + (isLeft ? radians : -radians));
        return new HoldPointCommand(follower, temp, false);
    }

    public CommandBase turnToCommand(double radians) {
        return new HoldPointCommand(
                follower,
                new Pose(follower.getPose().getX(), follower.getPose().getY(), Math.toRadians(radians)),
                true);
    }

    public CommandBase HoldPointCommand() {
        return new HoldPointCommand(follower, getPose(), true);
    }

    public CommandBase HoldPointCommand(Pose pose) {
        return new HoldPointCommand(follower, pose, true);
    }

    public CommandBase turnToDegreesCommand(double degrees) {
        return this.turnToCommand(Math.toRadians(degrees));
    }

    public CommandBase turnDegreesCommand(double degrees, boolean isLeft) {
        return this.turnCommand(Math.toRadians(degrees), isLeft);
    }

    public void resetYaw(AllianceColor allianceColor) {
        Pose pose = (allianceColor == AllianceColor.BLUE) ?
            follower.getPose().setHeading(Math.toRadians(180)) :
            follower.getPose().setHeading(Math.toRadians(0));
        follower.setPose(pose);
    }

    @AutoLogPose2d
    public double[] getAScopeTargetPose() {
        double inches_to_meter = 0.0254;
        Pose TargetPose = follower.getClosestPose().getPose();
        return new double[]{
                TargetPose.getX() * inches_to_meter,
                TargetPose.getY() * inches_to_meter,
                TargetPose.getHeading() * inches_to_meter
        };
    }

    @AutoLogPose2d
    public double[] getAScopePose() {
        //        double inches_to_meter = 0.0254;
        double inches_to_meter = 1.0;
        Pose pose = follower.getPose();
        return new double[]{
                pose.getX() * inches_to_meter, pose.getY() * inches_to_meter, pose.getHeading()
        };
    }

    /**
     * enables the Default Command(the default command is the drive field centric command)
     */
    public void enableTeleopDriveDefaultCommand(BooleanSupplier slowMode, AllianceColor allianceColor) {
        MMRobot mmRobot = MMRobot.getInstance();
        setDefaultCommand(
                (allianceColor == AllianceColor.BLUE) ?
                        driveCommand(
                                () -> -mmRobot.gamepadEx1.getLeftY(),
                                () -> mmRobot.gamepadEx1.getLeftX(),
                                () -> -mmRobot.gamepadEx1.getRightX(),
                                false, slowMode)
                        :
                        driveCommand(
                                () -> mmRobot.gamepadEx1.getLeftY(),
                                () -> -mmRobot.gamepadEx1.getLeftX(),
                                () -> -mmRobot.gamepadEx1.getRightX(),
                                false, slowMode)
                );
    }


    public Command enableDriveAligned(BooleanSupplier slowMode, AllianceColor allianceColor) {
        MMRobot mmRobot = MMRobot.getInstance();
        return (allianceColor == AllianceColor.BLUE) ?
                driveAligned(
                     () -> -mmRobot.gamepadEx1.getLeftY(),
                     () -> mmRobot.gamepadEx1.getLeftX(), false, slowMode)
                :
                driveAligned(
                     () -> mmRobot.gamepadEx1.getLeftY(),
                     () -> -mmRobot.gamepadEx1.getLeftX(), false, slowMode);
    }


    public void setPose(Pose pose) {
        follower.setPose(pose);
    }

    public Pose getPose() {
        return follower.getPose();
    }

    public void setPose(double x, double y, double heading) {
        this.setPose(new Pose(x, y, heading));
    }

    /**
     * disables the Default Command(the default command is the drive field centric command)
     */
    public void disableTeleopDriveDefaultCommand() {
        setDefaultCommand(new RunCommand(() -> {
        }, this));
    }

    public void setSlowModeRatioForward(double slowModeRatioForward) {
        this.slowModeRatioForward = slowModeRatioForward;
    }

    public void setSlowModeRatioLateral(double slowModeRatioLateral) {
        this.slowModeRatioLateral = slowModeRatioLateral;
    }

    public void setSlowModeRatioRotation(double slowModeRatioRotation) {
        this.slowModeRatioRotation = slowModeRatioRotation;
    }

    public void addVisionMeasurement(Pose pose, double timestampSeconds) {
        ((PinpointVisionLocalizer) follower.getPoseTracker().getLocalizer())
                .addVisionMeasurement(pose, timestampSeconds);
    }
    public static boolean isInstanceNull(){
        return instance == null;
    }

    public void reset() {
        instance = null;
    }

    private DoubleSupplier debugKpSupplier,
    debugKiSupplier,
    debugKdSupplier,
    secKpSupplier,
    secKiSupplier,
    secKdSupplier,
    headingKsSupplier,
    secKsSupplier,
    debugPositionToleranceSupplier,
    switchToSecondarySupplier;

    /**
     * add suppliers that when changed will auto update the pid values. any value you don't need just
     * put null
     *
     * @param debugKpSupplier                Kp
     * @param debugKiSupplier                Kd
     * @param debugKdSupplier                Ki
     * @param debugPositionToleranceSupplier position tolerance
     * @implNote !NOTICE THIS ONLY WORKS IF IN DEBUG MODE
     */
    public MMDrivetrain withDebugPidSuppliers(
            DoubleSupplier debugKpSupplier,
            DoubleSupplier debugKiSupplier,
            DoubleSupplier debugKdSupplier,
            DoubleSupplier secKpSupplier,
            DoubleSupplier secKiSupplier,
            DoubleSupplier secKdSupplier,
            DoubleSupplier headingKsSupplier,
            DoubleSupplier secKsSupplier,
            DoubleSupplier debugPositionToleranceSupplier,
            DoubleSupplier switchToSecondarySupplier) {

        this.debugKpSupplier = debugKpSupplier;
        this.debugKiSupplier = debugKiSupplier;
        this.debugKdSupplier = debugKdSupplier;
        this.secKpSupplier = secKpSupplier;
        this.secKiSupplier = secKiSupplier;
        this.secKdSupplier = secKdSupplier;
        this.headingKsSupplier = headingKsSupplier;
        this.secKsSupplier = secKsSupplier;
        this.debugPositionToleranceSupplier = debugPositionToleranceSupplier;
        this.switchToSecondarySupplier = switchToSecondarySupplier;
        return this;
    }

    public static void setSwitchToSecondaryHeading(double switchToSecondaryHeading) {
        MMDrivetrain.switchToSecondaryHeading = switchToSecondaryHeading;
    }
    public static void setHeadingKS(double headingKS) {
        MMDrivetrain.headingKS = headingKS;
    }
    public static void setSecHeadingKS(double secHeadingKS) {
        MMDrivetrain.secondaryHeadingKS = secondaryHeadingKS;
    }

    int a = 0;

    @Override
    public void periodic() {
        super.periodic();

        if (MMRobot.getInstance().currentOpMode != null) {
            a++;
            KoalaLog.log("heading_pid/per work", a, true);

            MMUtils.updateIfChanged(debugKpSupplier, headingPID::getP, headingPID::setP);
            MMUtils.updateIfChanged(debugKiSupplier, headingPID::getI, headingPID::setI);
            MMUtils.updateIfChanged(debugKdSupplier, headingPID::getD, headingPID::setD);

            MMUtils.updateIfChanged(secKpSupplier, secondaryHeadingPID::getP, secondaryHeadingPID::setP);
            MMUtils.updateIfChanged(secKiSupplier, secondaryHeadingPID::getI, secondaryHeadingPID::setI);
            MMUtils.updateIfChanged(secKdSupplier, secondaryHeadingPID::getD, secondaryHeadingPID::setD);

            MMUtils.updateIfChanged(headingKsSupplier, ()-> headingKS, MMDrivetrain::setHeadingKS);
            MMUtils.updateIfChanged(secKsSupplier, ()-> secondaryHeadingKS, MMDrivetrain::setSecHeadingKS);
            MMUtils.updateIfChanged(debugPositionToleranceSupplier, secondaryHeadingPID::getErrorTolerance, secondaryHeadingPID::setTolerance);
            MMUtils.updateIfChanged(switchToSecondarySupplier, ()-> switchToSecondaryHeading,MMDrivetrain::setSwitchToSecondaryHeading );
        }
    }
}
