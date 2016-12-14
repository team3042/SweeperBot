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
    
    public AutoDriveThereAndBack(int pass) {
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
        double courtLength = 8.2;
        if(pass != 1) {
            courtLength -= .3;
        }
        
        addSequential(new DriveTrainSetGyroGoal(1.5));
        addSequential(new AutoDrive(courtLength, .3, 1.2, 0, 0)); 
        
        if(pass == 1) {
            courtLength -= .3;
        }
        
        // WHEN UPDATING TURN PARAMETERS ALSO UPDATE IN AutoTurnAround! 
        
        addSequential(new AutoDrive(1.6, .3, 1, 1.4, 1)); 
        addSequential(new DriveTrainSetGyroGoal(-176));
        addSequential(new AutoDrive(courtLength, .3, 1.2, 0, 0)); 
                
    }
}
