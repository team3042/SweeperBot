/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.commands;

import edu.wpi.first.wpilibj.Timer;
import org.team3042.sweep.SweeperBot;

/**
 *
 * @author NewUser
 */
public class RPMTest extends CommandBase {
    
    double oldTime = 0;
    double oldPosition = 0;
    int currentIndex = -1;
    double[] speeds = new double[20];
    
    Timer timer = new Timer();
    
    public RPMTest() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        driveTrain.setMotors(1, 1);
        timer.reset();
        timer.start();
        while(timer.get() > 2);
        
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double currentPosition = driveTrain.getLeftEncoder();
        double currentTime = timer.get();
        
        if(currentIndex >= 0){
            double speed = (currentPosition - oldPosition)/(currentTime - oldTime);
            speeds[currentIndex] = speed;
        }
        
        oldPosition = currentPosition;
        oldTime = currentTime;
        currentIndex++;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return currentIndex == 20;
    }

    // Called once after isFinished returns true
    protected void end() {
        double totalSpeeds = 0;
        for(int i = 0; i < speeds.length; i++){
            totalSpeeds += speeds[i];
        }
        double averageSpeed = totalSpeeds / speeds.length;
        
        System.out.println("average speed: " + averageSpeed);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
