/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author admin
 */
public class CalibrationCommand extends CommandBase {  
    private float timeUntilEnd;
    
    private Timer timer;
    
    public CalibrationCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        timer = new Timer();
        timer.start();
        
        timeUntilEnd = (float)SmartDashboard.getNumber("Calibration Length In Seconds");
    }

    // Called repeatedly when this Command is scheduled to run
    public float intervalTimer = 0;
    protected void execute() {
                System.out.println("Hi"+timer.get());
        //Set the drivetrain to these speeds
        driveTrain.drive(SmartDashboard.getNumber("Calibration Motor Speed"),SmartDashboard.getNumber("Calibration Motor Speed"));
        
        //Output the values at a certain interval
        if(intervalTimer<=timer.get()){
            outputCalibrationValuesToFile();
            intervalTimer = (float)timer.get()+(float)SmartDashboard.getNumber("Calibration Output Interval");
        } 
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return timeUntilEnd<=timer.get();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
    
    private void outputCalibrationValuesToFile(){
        String completeOutPut;
        
        //The actual speed values: what speed the encoders say we are actually at.
        String encoderValueLeft = Double.toString(driveTrain.getLeftEncoderSpeed());
        String encoderValueRight = Double.toString(driveTrain.getRightEncoderSpeed());
        
        completeOutPut = "Set speed value: " + "("+SmartDashboard.getNumber("Calibration Motor Speed")+")"+" Encoder Values: "+"("+encoderValueLeft+","+encoderValueRight+")" + " Time Since Started: "+timer.get();
        
        GRTFileIO.writeToFile(SmartDashboard.getString("Calibration File Dir"), completeOutPut);
        
    }
}