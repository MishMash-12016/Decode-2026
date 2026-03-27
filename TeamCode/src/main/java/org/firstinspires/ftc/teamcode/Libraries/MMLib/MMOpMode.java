package org.firstinspires.ftc.teamcode.Libraries.MMLib;


import Ori.Coval.Logging.AutoLogManager;
import Ori.Coval.Logging.Logger.KoalaLog;
import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Subsystems.MMWebcamSubsystem;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceColor;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.AllianceSide;
import org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils.OpModeVeriables.OpModeType;
import org.firstinspires.ftc.teamcode.MMRobot;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;

/**
 * this class represents a wrapper for the default Teleop.
 * <p>
 * if it is used while calling the side and/or color val it initializes the MMRobot with the {@link OpModeType.Competition#TELEOP Teleop} type.
 * u might also use the constructor that only requires a {@link OpModeType.NonCompetition  NonComp} type,
 * this constructor let's u insert the type of {@link OpModeType.NonCompetition  NonComp} opmode u would like to use.
 * there are explanations in {@link OpModeType} that explains the 3 options u have.
 * there is the {@link OpModeType.NonCompetition#DEBUG Debug},
 * {@link OpModeType.NonCompetition#EXPERIMENTING_NO_EXPANSION Experimenting Without Expansion}.
 */
public abstract class MMOpMode extends LinearOpMode {
    List<LynxModule> hubs;
    public OpModeType opModeType;

    public AllianceColor allianceColor;
    public AllianceSide allianceSide;

    private final List<Runnable> runOnInit = new ArrayList<>();
    private final List<Command> commandsOnRun = new ArrayList<>();
    private double lastLoopTime = System.currentTimeMillis();

    /**
     * use this to choose a {@link OpModeType.NonCompetition NonComp} opmode.
     *
     * @param opModeType which opmode to activate
     */
    public MMOpMode(OpModeType opModeType, AllianceColor allianceColor) {
        this.opModeType = opModeType;
        this.allianceColor = allianceColor;
    }

    private void robotInit() {
//        if(opModeType == OpModeType.NonCompetition.DEBUG || opModeType == OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION){
//            KoalaLog.setup(hardwareMap);
//            KoalaLog.start();
//        }
//        else {
//            KoalaLog.setup(hardwareMap, true);
//        }
        KoalaLog.setup(hardwareMap);
        KoalaLog.start();

        MMRobot.getInstance().currentOpMode = this;
        MMRobot.getInstance().initializeSystems(opModeType);

        hubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
    }

    public abstract void onInit();

    public void onInitLoop() {}

    private void robotInitLoop(){
    }

    public void onPlay() {
        lastLoopTime = System.currentTimeMillis();
    }

    /**
     * Updates the {@link CommandScheduler}, {@link org.firstinspires.ftc.robotcore.external.Telemetry Telemetry}
     * and {@link org.firstinspires.ftc.teamcode.Libraries.CuttlefishFTCBridge.src.devices.CuttleRevHub Control & Expansion Hub} on Play Loop.
     */
    public void onPlayLoopUpdates() {
        for (LynxModule module : hubs) {
            module.clearBulkCache();
        }
        CommandScheduler.getInstance().run();                     //runs the scheduler
        MMDrivetrain.update();

        MMWebcamSubsystem.update();
        logCommandScheduler();

        KoalaLog.log("looptime", System.currentTimeMillis() - lastLoopTime, true);

        // TODO: move to the if below after check
        telemetry.addData("looptime", System.currentTimeMillis() - lastLoopTime);
        lastLoopTime = System.currentTimeMillis();
        telemetry.update();                                       //updates the telemetry
        if(opModeType == OpModeType.NonCompetition.DEBUG || opModeType == OpModeType.NonCompetition.EXPERIMENTING_NO_EXPANSION){
            AutoLogManager.periodic();
            FtcDashboard.getInstance().getTelemetry().update();       //updates the dashboard
        }
    }

    public void logCommandScheduler(){
        List<Command> scheduledCommands = CommandScheduler.getInstance().getScheduledCommands();
        String[] commandsName = new String[scheduledCommands.size()];
        for (int i = 0; i< commandsName.length; i++){
            commandsName[i] = scheduledCommands.get(i).getName();
        }

        KoalaLog.log("running commands name", commandsName, true);
    }
    public abstract void onPlayLoop();

    public void onEnd() {}

    /**
     * Cancels all previous commands and deletes the {@link MMRobot Robot Singleton}
     */
    public void reset() {
        //TODO reset also reset the default command
        CommandScheduler.getInstance().reset();
        KoalaLog.stop();
        ShooterSubsystem.instance = null;
    }

    public void addRunnableOnInit(Runnable... runOnInit) {
        this.runOnInit.addAll(Arrays.asList(runOnInit));
    }

    public void addCommandsOnRun(Command... commandsOnRun) {
        this.commandsOnRun.add(new InstantCommand().andThen(commandsOnRun));
        /*this was in order to solve the commandScheduler problem
          the problem was that the scheduler for some reason always ran the first instant command even tho it wasn't on yet*/
    }

    private void scheduleCommandsAndRun() {
        for (Runnable runnable : runOnInit) {
            runnable.run();
        }

        for (Command command : commandsOnRun) {
            command.schedule();
        }
    }

    @Override
    public void runOpMode() {
        robotInit();
        onInit();
        scheduleCommandsAndRun();

        try {
            while (opModeInInit()) {
                robotInitLoop();
                onInitLoop();
            }
            onPlay();
            while (!isStopRequested() && opModeIsActive()) {
                onPlayLoopUpdates();
                onPlayLoop();
            }
        } finally {
            try {
                onEnd();
            } finally {
                reset();
            }
        }
    }
}
