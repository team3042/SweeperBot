/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author ethan
 */
public class AutoSweepGym extends CommandGroup {
    
    public AutoSweepGym() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.
        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
        
        addSequential(new DriveTrainResetGyro());
        
        addSequential(new AutoDriveAccelerate(0.3));
        double initialPasses = 2;
        for(int i = 1; i <= initialPasses; i++) {
            addSequential(new AutoDriveThereAndBack());
            if(i != initialPasses) {
                addSequential(new AutoTurnAround());
            }
        }
        /*
        addSequential(new AutoDrive(2, .3, 0.5, 0, 0));
        addSequential(new BroomArmShakeTimed(5));
        addSequential(new AutoDrive(-2, .3, -0.5, 0, 0));
        
        double secondPasses = 1;
        for(int i = 1; i <= secondPasses; i++) {
            addSequential(new AutoDriveThereAndBack());
            if(i != secondPasses) {
                //addSequential(new AutoTurnAround());
            }
        }
        */
        addSequential(new AutoDriveDeccelerate(0.3));
    }
}
