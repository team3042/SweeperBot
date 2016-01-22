/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
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
    
    
    //Motor Scaling
    double encoderDistancePerPulse = 1;
    
    public DriveTrain() {
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
        setMotors(left, right);
    }
    
    public void setMotors(double left, double right) {
        
        left = scaleLeft(left);
        right = scaleRight(right);
        
        left = safetyTest(left);
        right = safetyTest(right);
        
        leftMotor.set(left);
        rightMotor.set(right);
    }
    
    public void setMotorsRaw(double left, double right) {
        
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
    
    
    public double getRightEncoder(){
        return rightEncoder.getDistance();
    }
    
    public double getLeftEncoder(){
        return leftEncoder.getDistance();
    }
    
    public double getRightSpeed(){
        return rightEncoder.getRate();
    }
    
    public double getLeftSpeed(){
        return leftEncoder.getRate();
    }
    
    public void resetEncoders(){
        rightEncoder.reset();
        leftEncoder.reset();
    }
    
    public double scaleLeft(double left){
        left = 1.0197*left - 0.0697;
        return left;
    }
    
    public double scaleRight (double right) {
        return -right;
    }
    
    public double motorPowerToEncoderTicks(double motorPower){
        double encoder = Math.abs(4319*motorPower) - 425.65;
        encoder = (encoder > 0)? encoder:0;
        
        encoder *= motorPower/Math.abs(motorPower);
        
        return encoder;
    }
}
