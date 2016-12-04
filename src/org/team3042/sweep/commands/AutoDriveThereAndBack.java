/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author NewUser
 */
public class AutoDriveThereAndBack extends CommandGroup {
    
    public AutoDriveThereAndBack() {
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
        
        addSequential(new DriveTrainSetGyroGoal(0));
        addSequential(new AutoDrive(3.5, .3, 0.9, 0, 0)); 
        // WHEN UPDATING TURN PARAMETERS ALSO UPDATE IN AutoTurnAround!
        addSequential(new AutoDrive(1.8, .3, 1, 1.2, 1)); 
        addSequential(new DriveTrainSetGyroGoal(-180));
        addSequential(new AutoDrive(3.5, .3, 0.9, 0, 0)); 
    }
}
