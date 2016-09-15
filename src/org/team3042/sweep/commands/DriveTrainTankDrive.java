/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team3042.sweep.SweeperBot;

/**
 *
 * @author NewUser
 */
public class DriveTrainTankDrive extends CommandBase {
    
    //Scale the joystick values to restrict maximum speed
    private final double speedScale = 1.0;
    Timer time = new Timer();
    
    //Inertial dampening
    final int LEFT = 0;
    final int RIGHT = 1;
    double[] oldTime = new double[] {0, 0};
    double[] currentPower = new double[] {0,0};
    double maxAccel = 4.0; //motor power per second
    
    public DriveTrainTankDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        SweeperBot.logger.log("Tank drive init",1);
        
        driveTrain.setMotors(0, 0);
        driveTrain.resetEncoders();
        time.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double leftPower = -oi.stickLeft.getY() * speedScale;
        double rightPower = -oi.stickRight.getY() * speedScale;
        
        leftPower = restrictAccel(leftPower, LEFT);
        rightPower = restrictAccel(rightPower, RIGHT);
        
        if(oi.lTrigger.get()){
            rightPower = leftPower;
        }
        else if (oi.rTrigger.get()){
            leftPower = rightPower;
        }
        
        SmartDashboard.putNumber("Left encoder", driveTrain.getLeftEncoder());
        SmartDashboard.putNumber("Right encoder", driveTrain.getRightEncoder());
        
        
        driveTrain.drive(leftPower, rightPower);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        SweeperBot.logger.log("Tank drive end",1);

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        SweeperBot.logger.log("Tank drive interrupt",1);
    }
    
    private double restrictAccel (double goalValue, int SIDE) {
        double currentTime = time.get();
        double dt = currentTime - oldTime[SIDE];
        oldTime[SIDE] = currentTime;
        
        double maxDSpeed = maxAccel * dt;
        maxDSpeed *= (goalValue >= currentPower[SIDE])? 1 : -1;
         
        currentPower[SIDE] = (Math.abs(maxDSpeed) > Math.abs(goalValue - currentPower[SIDE]))? 
                goalValue : maxDSpeed + currentPower[SIDE];
        return currentPower[SIDE];
    }
}
