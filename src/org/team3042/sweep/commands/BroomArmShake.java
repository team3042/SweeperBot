/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.commands;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author NewUser
 */
public class BroomArmShake extends CommandBase {
    
    public BroomArmShake() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(broomArm);
    }
    
    Timer time = new Timer();
    double oldTime = 0;
    double delay = 0.2;
    double dTime;
   
    // Called just before this Command runs the first time
    protected void initialize() {
        broomArm.raise();
        oldTime = 0;
        time.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double currentTime = time.get();
        dTime = currentTime - oldTime;
        if(dTime > delay) {
            broomArm.toggleShake();
            oldTime = currentTime;
        }
        
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        broomArm.raise();
    }
}
