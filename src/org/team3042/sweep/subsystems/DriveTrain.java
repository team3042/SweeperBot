/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
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
    
    Gyro gyro = new Gyro(RobotMap.GYRO);
    double gyroGoal = 0;
    
    
    //Motor Scaling
    double encoderDistancePerPulse = 1;
    
    //TODO Determine actual values for these
    public int encCounts = 360;
    public double kF = 2.94;
    
    private boolean isMotionProfilePaused = false;
    
    public DriveTrain() {
        leftEncoder.start();
        rightEncoder.start();
        
        leftEncoder.setDistancePerPulse(encoderDistancePerPulse);
        rightEncoder.setDistancePerPulse(encoderDistancePerPulse);
        leftEncoder.setReverseDirection(true);
        
        gyro.reset();
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
    
    //Motor setting used exclusively in motion planning autonomous
    public void setMotorsRaw (double left, double right) {
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
    
    public void resetGyro() {
        gyro.reset();
        gyroGoal = 0;
    }
    
    public double getGyro() {
        return gyro.getAngle();
    }
    
    public double getGyroGoal() {
        return gyroGoal;
    }
    
    public void setGyroGoal(double newGoal) {
        gyroGoal = newGoal;
    }
    
    public double scaleLeft(double left){
        left = (left > .07) ? 1.0197*left - 0.0697 : left;
        return left;
    }
    
    public double scaleRight (double right) {
        return -right;
    }
    
    public double motorPowerToEncoderTicks(double motorPower){
        double encoder = (motorPower > .2) ? 4404*(motorPower - 0.165): 0;
                
        return encoder;
    }
    
    public boolean isProfilePaused() {
        return isMotionProfilePaused;
    }
    
    public void setProfilePaused(boolean paused) {
        isMotionProfilePaused = paused;
    }
}
