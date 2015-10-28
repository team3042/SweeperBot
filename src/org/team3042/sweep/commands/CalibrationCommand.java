/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team3042.sweep.subsystems.DriveTrain;
/**
 *
 * @author admin
 */
public class CalibrationCommand extends CommandBase {  
    //The system time at which we start
    private float timeStarted;
    private float timeUntilEnd;
    
    public CalibrationCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        timeStarted = getSystemTimeInSeconds();
        timeUntilEnd = timeStarted+(float)SmartDashboard.getNumber("Calibration Length In Seconds");
    }

    // Called repeatedly when this Command is scheduled to run
    public float intervalTimer = 0;
    protected void execute() {
        //Set the drivetrain to these speeds
        driveTrain.drive(SmartDashboard.getNumber("Calibration Motor Speed"),SmartDashboard.getNumber("Calibration Motor Speed"));
        
        //Output the values at a certain interval
        if(intervalTimer<=getSystemTimeInSeconds()){
            outputCalibrationValuesToFile();
            intervalTimer = getSystemTimeInSeconds()+(float)SmartDashboard.getNumber("Calibration Output Interval");
        } 
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        //Time until end was the start system time added to the
        //amount of time we wanted it to run. so like systemTime + 5 or something
        //Therefore when time until end is LESS THAN SYSTEMTIME, then 5 seconds or whatever time you
        //have added 
        return timeUntilEnd<=getSystemTimeInSeconds();
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
        
        completeOutPut = "Set speed value: " + "("+SmartDashboard.getNumber("Calibration Motor Speed")+")"+" Encoder Values: "+"("+encoderValueLeft+","+encoderValueRight+")" + " Time Since Started: "+getTimeSinceStartedInSeconds();
        
        GRTFileIO.writeToFile(SmartDashboard.getString("Calibration File Dir"), completeOutPut);
        
    }
    
    //Gives the current system time, which can be a very high number, in seconds
    //I believe that system time does NOT start at 0 which is why we have other 
    //methods utilizing it to give an actual time since start.
    private float getSystemTimeInSeconds(){
        return System.currentTimeMillis()/1000;
    }

    //Gives the the seconds since the system has started
    //THIS IS ONLY USED IN THE CALIBRATION OUTPUT
    private float getTimeSinceStartedInSeconds(){
        return getSystemTimeInSeconds()-timeStarted;
    }
}
