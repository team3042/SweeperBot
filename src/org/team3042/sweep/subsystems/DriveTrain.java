/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.team3042.sweep.RobotMap;
import org.team3042.sweep.commands.DriveTrainTankDrive;

/**
 *
 * @author NewUser
 */
public class DriveTrain extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    Jaguar leftMotor = new Jaguar(RobotMap.DRIVE_TRAIN_LEFT_JAGUAR);
    Jaguar rightMotor = new Jaguar(RobotMap.DRIVE_TRAIN_RIGHT_JAGUAR);
    
    Encoder leftEncoder = new Encoder(RobotMap.LEFT_ENCODER_A_DIO, 
            RobotMap.LEFT_ENCODER_B_DIO);
    Encoder rightEncoder = new Encoder(RobotMap.RIGHT_ENCODER_A_DIO, 
            RobotMap.RIGHT_ENCODER_B_DIO);
    
    //Inertial dampening
    Timer time = new Timer();
    double oldTime = 0;
    double maxAccel = 1; //Percentage per second
    
    //Motor Scaling
    float leftScale = 1;
    float rightScale = -1;
    
    public DriveTrain() {
        time.start();
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DriveTrainTankDrive());
    }
    
    public void stop() {
        leftMotor.set(0);
        rightMotor.set(0);
        
    }
    
    public void drive(double left, double right) {
        setMotors(left, right);
    }
    
    private void setMotors(double left, double right) {
        
        //left = restrictAccel(leftMotor.get(), left);
        //right = restrictAccel(rightMotor.get(), right);
        
        left *= leftScale;
        right *= rightScale;
        
        left = safetyTest(left);
        right = safetyTest(right);
        
        leftMotor.set(left);
        rightMotor.set(right);
    }
    
    public void autoDrive(double left) {
        setMotors(left, 0);
    }
    
    private double safetyTest(double motorValue) {
        motorValue = (motorValue < -1) ? -1 : motorValue;
        motorValue = (motorValue > 1) ? 1 : motorValue;
        
        return motorValue;
    }
    
    private double restrictAccel(double currentValue, double goalValue) {
        double currentTime = time.get();
        double dt = currentTime - oldTime;
        oldTime = currentTime;
        double maxDSpeed = maxAccel * dt;
        maxDSpeed *= (goalValue >= currentValue)? 1 : -1;
        
        return (Math.abs(maxDSpeed) > Math.abs(goalValue - currentValue))? 
                goalValue : maxDSpeed + currentValue;
    }
    
    public float getLeftEncoderSpeed(){
        return 0;
    }
    
    public float getRightEncoderSpeed(){
        return 0;
    }
    
    public int getRightEncoder(){
        return rightEncoder.get();
    }
    
    public int getLeftEncoder(){
        return leftEncoder.get();
    }
}