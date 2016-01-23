/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team3042.sweep.RobotMap;

/**
 *
 * @author Ethan
 */
public class AutoDriveStraight extends CommandBase {
    
    //Defining phases
    private int ACCEL = 0, MAINTAIN = 1, DECCEL = 2, STOP = 3;
    private int phase = 0;
    
    //Creating timer and variables for change in time
    Timer time = new Timer();
    private double oldTime = 0, dT = 0;
    
    //Creating PID values
    private double P = 0.0003, I = 0, D = 0;
    private final CorrectPID correctPID = new CorrectPID(P, I, D);
    
    //Encoder values at specific points
    private double leftEncoderStart = 0, rightEncoderStart = 0;
    
    //Goal Values each cycle
    private double goalSpeed, goalDistance;
    
    //The goal speed and distance for the MAINTAIN phase
    final private double MAINTAIN_SPEED = 0.5, MAINTAIN_DISTANCE = 10000;

    //Maximum Acceleration
    final private double MAX_ACCEL = 0.25 * ((MAINTAIN_SPEED >= 0) ? 1 : -1);
        
    public AutoDriveStraight() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {    
        time.reset();
        time.start();
        oldTime = 0;
        
        phase = ACCEL;
        goalSpeed = .2;
        goalDistance = 0;
        correctPID.reset();
        
        //Getting initial encoder values
        leftEncoderStart = driveTrain.getLeftEncoder();
        rightEncoderStart = driveTrain.getRightEncoder();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        //Get the total distance traveled.
        double leftEncoder = driveTrain.getLeftEncoder() - leftEncoderStart;
        double rightEncoder = driveTrain.getRightEncoder() -rightEncoderStart;
        
        SmartDashboard.putNumber("Left encoder", leftEncoder);
        SmartDashboard.putNumber("Right encoder", rightEncoder);
        
        SmartDashboard.putNumber("Encoder Difference", leftEncoder - rightEncoder);
        
        //Determine the PID correction
        double leftPID = correctPID.correction(leftEncoder, goalDistance);
        double rightPID = correctPID.correction(rightEncoder, goalDistance);
        
        SmartDashboard.putNumber("Left PID", leftPID);
        SmartDashboard.putNumber("Right PID", rightPID);
                
        
        //Set the motor values
        driveTrain.setMotors(goalSpeed + leftPID, goalSpeed + rightPID);
        
        //Determine the time interval
        double currentTime = time.get();
        dT = currentTime - oldTime;
        oldTime = currentTime;
       
        //Determine velocity for the next cycle.
        if(phase == ACCEL) {
            goalSpeed += dT * MAX_ACCEL;
            if (goalSpeed > MAINTAIN_SPEED){
                phase = MAINTAIN;
                goalSpeed = MAINTAIN_SPEED;
            }
        }
        if (goalDistance >= MAINTAIN_DISTANCE) {
            phase = DECCEL;
        }
        if (phase == DECCEL) {
            goalSpeed -= dT * MAX_ACCEL;
            if (goalSpeed < 0){
                phase = STOP;
                goalSpeed = 0;
            }
        }

        //Determine goal distance for next cycle
        goalDistance += dT * driveTrain.motorPowerToEncoderTicks(goalSpeed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        //Returns true if robot has completed all three phases and stopped
        return phase == STOP;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
