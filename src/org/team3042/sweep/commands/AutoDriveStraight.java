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
    private int ACCEL = 0, MAINTAIN = 1, DECCEL = 2;
    private int phase = 0;
    
    //Encoder values at specific points
    private double encoderStart, encoderAccelEnd, encoderMaintainEnd;
    
    //Establishing goals
    private double goalSpeed = 0.5, goalDistance = 1000, goalAccel;
    
    //Current values
    private double currentSpeed = 0, currentSpeedLeft = 0,
            currentSpeedRight = 0, currentCorrectedSpeedLeft = 0,
            currentCorrectedSpeedRight = 0, goalCurrentDistanceLeft = 0, goalCurrentDistanceRight = 0;
    
    //Creating timer and variables for change in time
    Timer time = new Timer();
    private double oldTime = 0, dT = 0;
    
    //Creating PID values
    private double P = 0, I = 0, D = 0;
    private final CorrectPID correctPID = new CorrectPID(P, I, D);
    
    public AutoDriveStraight() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        time.reset();
        time.start();
        
        //Getting initial encoder values
        encoderStart = driveTrain.getLeftEncoder();
        
        //Ensuring acceleration is within maximum
         goalAccel = RobotMap.MAX_ACCEL;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        dT = time.get() - oldTime;
        oldTime = time.get();
        
        //Calculating expected distance traveled in last cycle
        goalCurrentDistanceLeft += driveTrain.motorPowerToEncoderTicks(currentSpeedLeft) * dT;
        goalCurrentDistanceRight += driveTrain.motorPowerToEncoderTicks(currentSpeedRight)* dT;
        
        //Accelerating robot up to the goal speed
        if(phase == ACCEL && currentSpeed < goalSpeed) {
            if(currentSpeed + goalAccel * dT < goalSpeed) {
                System.out.println("Accelerating!");
                currentSpeed += goalAccel * dT;
            }
            else {
                currentSpeed = goalSpeed;
                phase = MAINTAIN;
                encoderAccelEnd = driveTrain.getLeftEncoder() - encoderStart;
                encoderMaintainEnd = goalDistance - encoderAccelEnd;
            }
        }
        
        //Ending maintain phase after encoder has reached a threshold
        if(phase == MAINTAIN && driveTrain.getLeftEncoder() >= 
                encoderMaintainEnd + encoderStart) {
            phase = DECCEL;
        }
        
        //Deccelerating robot to a stop
        if(phase == DECCEL && currentSpeed > 0) {
            if(currentSpeed - goalAccel * dT > 0) {
                currentSpeed -= goalAccel * dT;
            }
            else {
                currentSpeed = 0;
            }
        }
        
        //Scaling speeds of each side
        currentSpeedLeft = driveTrain.scaleLeft(currentSpeed);
        currentSpeedRight = driveTrain.scaleRight(currentSpeed);
        
        //Running current values through PID and outputting to motors
        currentCorrectedSpeedLeft = correctPID.correction(
                driveTrain.getLeftEncoder(), goalCurrentDistanceLeft + encoderStart)
                + currentSpeedLeft;
        currentCorrectedSpeedRight = correctPID.correction(
                driveTrain.getRightEncoder(), goalCurrentDistanceRight + encoderStart)
                + currentSpeedRight;
        
        SmartDashboard.putNumber("Left Encoder", driveTrain.getLeftEncoder());
        SmartDashboard.putNumber("Right Encoder", driveTrain.getRightEncoder());
        
        driveTrain.setMotorsRaw(currentCorrectedSpeedLeft, currentCorrectedSpeedRight);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        //Raturns true if robot has completed all three phases and stopped
        return (phase == DECCEL && currentCorrectedSpeedLeft == 0 && currentCorrectedSpeedRight == 0);
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
