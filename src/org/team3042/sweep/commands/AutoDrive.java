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
public class AutoDrive extends CommandBase {
    
    //Defining phases
    private int ACCEL = 0, MAINTAIN = 1, DECCEL = 2, STOP = 3;
    private int phase = 0;
    
    //Creating timer and variables for change in time
    Timer time = new Timer();
    private double oldTime = 0, dT = 0;
    
    //Creating PID values
    private double P = 0.004, I = 0, D = 0;
    private final CorrectPID correctPID = new CorrectPID(P, I, D);
    
    //Encoder values at specific points
    private double leftEncoderStart = 0, rightEncoderStart = 0;
    
    //Goal Values each cycle
    private double goalSpeed, goalDistance;
    
    //The goal speed and distance for the MAINTAIN phase
    private double MAINTAIN_SPEED, MAINTAIN_DISTANCE;

    //Maximum Acceleration
    final private double MAX_ACCEL = 0.25 * ((MAINTAIN_SPEED >= 0) ? 1 : -1);
    
    
    private double outerRadius, innerRadius, outerMaintainSpeed, innerMaintainSpeed, outerDistance,
                   innerDistance;
    private final int OUTER, INNER;
    
    public AutoDrive(double radius, double speed, double distance, int leftOrRight) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(driveTrain);
        
        OUTER = Math.abs(leftOrRight - 1);
        INNER = leftOrRight;
        
        outerRadius = radius + 13.4375;
        innerRadius = radius - 13.4375;
        
        outerMaintainSpeed = speed;
        innerMaintainSpeed = outerMaintainSpeed * innerRadius / outerRadius;
        
        outerDistance = distance;
        innerDistance = outerDistance * innerRadius / outerRadius;
    }
    public AutoDrive(double speed, double distance) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(driveTrain);
        
        outerMaintainSpeed = speed;
        innerMaintainSpeed = speed;
        
        outerDistance = distance;
        innerDistance = distance;
        
        OUTER = 0;
        INNER = 1;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
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
