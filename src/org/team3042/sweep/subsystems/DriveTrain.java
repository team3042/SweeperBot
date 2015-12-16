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

    Jaguar leftMotor = new Jaguar(RobotMap.DRIVE_TRAIN_LEFT_JAGUAR);
    Jaguar rightMotor = new Jaguar(RobotMap.DRIVE_TRAIN_RIGHT_JAGUAR);
    
    Encoder leftEncoder = new Encoder(RobotMap.LEFT_ENCODER_A_DIO, 
            RobotMap.LEFT_ENCODER_B_DIO);
    Encoder rightEncoder = new Encoder(RobotMap.RIGHT_ENCODER_A_DIO, 
            RobotMap.RIGHT_ENCODER_B_DIO);
    
    //Inertial dampening
    final int LEFT = 0;
    final int RIGHT = 1;
    Timer time = new Timer();
    double[] oldTime = new double[] {0, 0};
    double maxAccel = 0.33; //Percentage per second
    
    //Motor Scaling
    double overallScale = 0.5;
    double leftScale = 1 * overallScale;
    double rightScale = -1 * overallScale;
    double encoderDistancePerPulse = 1;
    
    public DriveTrain() {
        time.start();
        
        leftEncoder.start();
        rightEncoder.start();
        
        leftEncoder.setDistancePerPulse(encoderDistancePerPulse);
        rightEncoder.setDistancePerPulse(encoderDistancePerPulse);
        leftEncoder.setReverseDirection(true);
        
        
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
        left = restrictAccel(leftMotor.get()/leftScale, left, LEFT);
        right = restrictAccel(rightMotor.get()/rightScale, right, RIGHT);
        
        setMotors(left, right);
    }
    
    public void setMotors(double left, double right) {
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
    
    private double restrictAccel(double currentValue, double goalValue, int SIDE) {
        double currentTime = time.get();
        double dt = currentTime - oldTime[SIDE];
        oldTime[SIDE] = currentTime;
        
        double maxDSpeed = maxAccel * dt;
        maxDSpeed *= (goalValue >= currentValue)? 1 : -1;
         
        return (Math.abs(maxDSpeed) > Math.abs(goalValue - currentValue))? 
                goalValue : maxDSpeed + currentValue;
    }
    
    public double getRightEncoder(){
        return rightEncoder.getDistance();
    }
    
    public double getLeftEncoder(){
        return leftEncoder.getDistance();
    }
}
