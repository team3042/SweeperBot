/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.commands;

import edu.wpi.first.wpilibj.Timer;
import org.team3042.sweep.RobotMap;

/**
 *
 * @author Robotics
 */
public class autoDrive extends CommandBase {
    
    Timer timer = new Timer();
    
    double dt;
    double oldTime = 0;
    double targetLeftVelocity = 0.5;
    double currentLeftVelocity = 0;
    final int ACCEL = 0;
    final int MID_PHASE = 1;
    final int DECCEL = 2;
    int phase = 0;
    
    int targetEncoderCounts;
    int encoderLeftStart;
    int encoderLeftCurrent;
    int dEncoderLeft;
    int encoderTicksStraight;
    
    public autoDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        timer.start();
        
        encoderLeftStart = driveTrain.getLeftEncoder();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        dt = timer.get() - oldTime;
        oldTime = timer.get();
        encoderLeftCurrent = driveTrain.getLeftEncoder();
        
        //Accelerating up to target velocity
        if (phase == ACCEL) {
            if (currentLeftVelocity + RobotMap.LEFT_MAX_ACCEL*dt < targetLeftVelocity) {
                currentLeftVelocity += dt * RobotMap.LEFT_MAX_ACCEL;
            }
            else {
                currentLeftVelocity = targetLeftVelocity;
                phase = MID_PHASE;
                dEncoderLeft = encoderLeftCurrent - encoderLeftStart;
                encoderTicksStraight = targetEncoderCounts - 2 * dEncoderLeft;
            }
        }
        
        //Constant speed
        if (encoderLeftCurrent < dEncoderLeft + encoderTicksStraight) {
            currentLeftVelocity = targetLeftVelocity;
        }
        else {
            phase = DECCEL;
        }
        //deccelerating
        
        if (phase == DECCEL) {
            if (currentLeftVelocity - RobotMap.LEFT_MAX_ACCEL * dt > 0) {
                currentLeftVelocity -= dt * RobotMap.LEFT_MAX_ACCEL;
            }
            else {
                currentLeftVelocity = 0;
            }
        }
        
        driveTrain.autoDrive(currentLeftVelocity);
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
    }
}
