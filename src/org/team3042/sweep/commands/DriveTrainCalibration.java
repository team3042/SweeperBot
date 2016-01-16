/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import org.team3042.sweep.FileIO;

/**
 *
 * @author admin
 */
public class DriveTrainCalibration extends CommandBase {
    //The time until we set motors to zero speed
    private double timeUntilMotorStop;
    
    //The time until the command finishes
    //Allows time for robot to come to rest after motors are stopped
    private double timeUntilCommandStop;
    
    //The alloted time in seconds for the robot to come to rest 
    //after the motors shut off
    private final double decelerationTime = 1.0;
    
    //Tracks whether the motors are set to move, or are stopped
    private boolean motorsEngaged;
    
    private final Timer timer = new Timer();
    private final FileIO fileIO = new FileIO();
    
    public DriveTrainCalibration() {
        // Use requires() here to declare subsystem dependencies
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        timer.reset();
        timer.start();
        
        fileIO.openFile(SmartDashboard.getString("Calibration File Name"));
        
        //Determine the time to stop motors and time to stop command
        timeUntilMotorStop = (float)SmartDashboard.getNumber("Calibration Length In Seconds");
        timeUntilCommandStop =timeUntilMotorStop + decelerationTime;

        //Set the drivetrain to these speeds
        driveTrain.setMotors(
                SmartDashboard.getNumber("Calibration Motor Speed"),
                SmartDashboard.getNumber("Calibration Motor Speed"));
        motorsEngaged = true;
    }

    protected void execute() {   
        outputCalibrationValuesToFile();
        if (motorsEngaged && (timer.get() >= timeUntilMotorStop)) {
            driveTrain.setMotors(0.0, 0.0);
            motorsEngaged = false;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return timer.get() >= timeUntilCommandStop;
    }

    // Called once after isFinished returns true
    protected void end() {
        fileIO.closeFile();
        timer.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        fileIO.closeFile();
        timer.stop();
    }
    
    private void outputCalibrationValuesToFile(){
        String completeOutPut;
        
        String encoderValueLeft = Double.toString(driveTrain.getLeftEncoder());
        String encoderValueRight = Double.toString(driveTrain.getRightEncoder());
        
        SmartDashboard.putNumber("Right encoder", driveTrain.getRightEncoder());
        SmartDashboard.putNumber("Left encoder", driveTrain.getLeftEncoder());
        
        completeOutPut = "Set speed value: " + "("+SmartDashboard.getNumber("Calibration Motor Speed")
                +")"+" Encoder Values: "+"("+encoderValueLeft+","+encoderValueRight+")" 
                + " Time Since Started: "+timer.get();
        
        fileIO.writeToFile(completeOutPut);
    }
}