package org.firstinspires.ftc.teamcode.Camera;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;


import org.firstinspires.ftc.teamcode.subsystems.Camera;

@Config
//@AutoLog
public class CameraCommandGroups {
    public static SequentialCommandGroup StrafeToArtifactCommand() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> Camera.getInstance().switchToDetector()),
                new WaitUntilCommand(() -> Camera.getInstance().getPipelineIndex() == Camera.getInstance().currentPipeline),
                new InstantCommand(() -> Camera.getInstance().setPreviousResult()),

                CameraCommands.StrafeToArtifact())

//        new ConditionalCommand(
//                        new SequentialCommandGroup(
//                                //Lamlam side:
//                                new InstantCommand(() -> Camera.getInstance().setPreviousResult()),
//                                CameraCommands.StrafeToSample()
//                        ),
//                        new InstantCommand(()->Camera.getInstance().resetPreviousResult())
//                        ,
//                        ()->Camera.getInstance().isTargetVisible()
//                )
//        )
        ;
    }
}