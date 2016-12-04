/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.commands;

/**
 *
 * @author ethan
 */
public class DriveTrainSetGyroGoal extends CommandBase {
    
    double gyroGoal;
    
    public DriveTrainSetGyroGoal(double newGoal) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(driveTrain);
        
        gyroGoal = newGoal;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        driveTrain.setGyroGoal(gyroGoal);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
