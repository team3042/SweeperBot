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
    double oldTimeLeft = 0;
    double oldTimeRight = 0;
    double maxAccel = 0.33; //Percentage per second
    
    //Motor Scaling
    double overallScale = 1.0;
    double leftScale = 1 * overallScale;
    double rightScale = -1 * overallScale;
    
    public DriveTrain() {
        time.start();
        
        leftEncoder.start();
        rightEncoder.start();
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
        left = restrictAccel(leftMotor.get()/leftScale, left, false);
        right = restrictAccel(rightMotor.get()/rightScale, right, true);
        setMotors(left, right);
    }
    
    private void setMotors(double left, double right) {
                
        left *= leftScale;
        right *= rightScale;
        
        left = safetyTest(left);
        right = safetyTest(right);
        
        leftMotor.set(left);
        rightMotor.set(right);
    }
    
    private double safetyTest(double motorValue) {
        motorValue = (motorValue < -1) ? -1 : motorValue;
        motorValue = (motorValue > 1) ? 1 : motorValue;
        
        return motorValue;
    }
    
    private double restrictAccel(double currentValue, double goalValue, boolean isRight) {
        double currentTime = time.get();
        double oldTime = (isRight)? oldTimeRight : oldTimeLeft;
        double dt = currentTime - oldTime;
        if(isRight) {
            oldTimeRight = currentTime;
        }
        else {
            oldTimeLeft = currentTime;
        }
        
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
    
    public float getRightEncoder(){
        return rightEncoder.get();
    }
    
    public float getLeftEncoder(){
        System.out.println(leftEncoder.get());
        return leftEncoder.get();
    }
}