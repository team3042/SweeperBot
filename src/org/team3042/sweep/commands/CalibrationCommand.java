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
public class CalibrationCommand extends CommandBase {  
    private float timeUntilEnd;
    
    private Timer timer;
    FileIO fileIO = new FileIO();
    
    public CalibrationCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        timer = new Timer();
        timer.start();
        
        fileIO.openFile(SmartDashboard.getString("Calibration File Dir"));
        
        timeUntilEnd = (float)SmartDashboard.getNumber("Calibration Length In Seconds");
    }

    protected void execute() {
        //Set the drivetrain to these speeds
        driveTrain.setMotors(
                SmartDashboard.getNumber("Calibration Motor Speed"),
                SmartDashboard.getNumber("Calibration Motor Speed"));
        
        outputCalibrationValuesToFile();
        
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return timeUntilEnd<=timer.get();
    }

    // Called once after isFinished returns true
    protected void end() {
        fileIO.closeFile();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        fileIO.closeFile();
    }
    
    private void outputCalibrationValuesToFile(){
        String completeOutPut;
        
        //The actual speed values: what speed the encoders say we are actually at.
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