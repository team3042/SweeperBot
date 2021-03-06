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
public class AutoDrive extends CommandBase {
    
    // Defining types of auto modes
    int STRAIGHT = 0, TURN_LEFT = 1, TURN_RIGHT = 2;
    
    // Constants to be calculated based on length and curve of requested path
    double leftDistance, rightDistance, leftMinSpeed, leftMaxSpeed, rightMinSpeed, rightMaxSpeed;
    
    // Goal values to be used while the command is running to update the motors
    double leftGoalPosition = 0, leftGoalSpeed = 0, rightGoalPosition = 0, rightGoalSpeed = 0, leftSpeed = 0, rightSpeed = 0;
    
    //Type of drive(straight, left, right)
    int autoType;
    
    //Current point
    int pointNumber = 0;

    //Time between each point in ms
    int itp = 30;

    //Time for each filter in ms
    double time1 = 400, time2 = 200; //TODO Adjust values for sweeperot based on accel data

    double wheelbaseWidth = 2.23;
    
    DynamicMotionProfileGenerator motionProfileLeft, motionProfileRight;
    double[][] profileLeft, profileRight;
    double currentError = 0, sumCurrentError = 0, currentHeading = 0, headingError = 0, sumHeadingError = 0;
    
    boolean finished = false;
    
    //Creating timer and variables for change in time
    Timer timer = new Timer();
    private double oldTime = 0, dT = 0;
    
    //Creating PID values
    private double P = 0.006, I = 0, D = 0.00, pTurn = 0.06, iTurn = 0.01, dTurn = 0.000;
    
    //Maximum Acceleration
    //final private double MAX_ACCEL = 0.25 * ((MAINTAIN_SPEED >= 0) ? 1 : -1);
    
    public AutoDrive(double distance, double minSpeed, double maxSpeed, double radius, int autoType) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(driveTrain);
        /*
        OUTER = Math.abs(leftOrRight - 1);
        INNER = leftOrRight;
        
        outerRadius = radius + 13.4375;
        innerRadius = radius - 13.4375;
        
        outerMaintainSpeed = speed;
        innerMaintainSpeed = outerMaintainSpeed * innerRadius / outerRadius;
        
        outerDistance = distance;
        innerDistance = outerDistance * innerRadius / outerRadius;
        */
        this.autoType = autoType;
        
        if(autoType == STRAIGHT) {
            leftDistance = distance;
            rightDistance = distance;

            leftMinSpeed = minSpeed;
            leftMaxSpeed = maxSpeed;
            rightMinSpeed = minSpeed;
            rightMaxSpeed = maxSpeed;
            
            pTurn = 0.08;
            iTurn = 0.0032;
            dTurn = 0.15;
    	}
    	else if(autoType == TURN_LEFT) {
            double leftRadius = radius - wheelbaseWidth / 2;
            double rightRadius = radius + wheelbaseWidth / 2;

            double leftScale, rightScale;
            //Creating scale for each side in relation to center
            if(radius != 0) {
                leftScale = leftRadius / radius;
                rightScale = rightRadius / radius;
            }
            else {
                leftScale = leftRadius;
                rightScale = rightRadius;
            }

            leftScale /= Math.max(leftScale,  rightScale);
            rightScale /= Math.max(leftScale,  rightScale);

            leftDistance = leftScale * distance;
            rightDistance = rightScale * distance;
            leftMinSpeed = leftScale * minSpeed;
            leftMaxSpeed = leftScale * maxSpeed;
            rightMinSpeed = rightScale * minSpeed;
            rightMaxSpeed = rightScale * maxSpeed;
            
            pTurn = 0.006;
            iTurn = 0.00001;
            dTurn = 0.0016;
    	}
    	else if(autoType == TURN_RIGHT) {
            double leftRadius = radius + wheelbaseWidth / 2;
            double rightRadius = radius - wheelbaseWidth / 2;

            double leftScale, rightScale;
            //Creating scale for each side in relation to center
            if(radius != 0) {
                leftScale = leftRadius / radius;
                rightScale = rightRadius / radius;
            }
            else {
                leftScale = leftRadius;
                rightScale = rightRadius;
            }

            leftScale /= Math.max(leftScale,  rightScale);
            rightScale /= Math.max(leftScale,  rightScale);

            leftDistance = leftScale * distance;
            rightDistance = rightScale * distance;
            leftMinSpeed = leftScale * minSpeed;
            leftMaxSpeed = leftScale * maxSpeed;
            rightMinSpeed = rightScale * minSpeed;
            rightMaxSpeed = rightScale * maxSpeed;
            
            pTurn = 0.006;
            iTurn = 0.00001;
            dTurn = 0.0001;
    	}
        
        currentError = 0;
        sumCurrentError = 0;
        currentHeading = 0;
        headingError = 0;
        sumHeadingError = 0;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        driveTrain.resetEncoders();
        //driveTrain.resetGyro();
        
        motionProfileLeft = new DynamicMotionProfileGenerator(itp, time1, time2, leftMinSpeed, leftMaxSpeed, leftDistance);
        motionProfileRight = new DynamicMotionProfileGenerator(itp, time1, time2, rightMinSpeed, rightMaxSpeed, rightDistance);
    	
    	profileLeft = motionProfileLeft.calculateProfile();
        profileRight = motionProfileRight.calculateProfile();
    	
    	finished = false;
    	leftGoalPosition = 0;
    	leftGoalSpeed = 0;
        rightGoalPosition = 0;
    	rightGoalSpeed = 0;
        
        currentError = 0;
        sumCurrentError = 0;
        currentHeading = 0;
        headingError = 0;
        sumHeadingError = 0;
    	
    	timer.reset();
    	timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double currentTime = timer.get() * 1000;
        dT = currentTime - oldTime;
        oldTime = currentTime;
        
        //double currentLeftError = leftGoalPosition -  driveTrain.getLeftEncoder();
        double oldCurrentError = currentError;
    	currentError = rightGoalPosition - (-2.3) * driveTrain.getRightEncoder();
        sumCurrentError += currentError;
        double dCurrentError = currentError - oldCurrentError;
        
        //Calculating change in angle since last iteration
        double leftDistance = leftGoalSpeed * dT / 1000;
        double rightDistance = rightGoalSpeed * dT / 1000;
        double radius, dTheta;
        if (autoType == TURN_LEFT) {
            radius = (leftDistance == rightDistance)? 10000000 : wheelbaseWidth * leftDistance / (leftDistance - rightDistance);
            
            dTheta = 360 * leftDistance / (2 * Math.PI * radius);
        } else if (autoType == TURN_RIGHT) {
            radius = (leftDistance == rightDistance)? 10000000 : wheelbaseWidth * rightDistance / (rightDistance - leftDistance);
            
            dTheta = -360 * rightDistance / (2 * Math.PI * radius);
        } else {
            radius = 10000000;
            
            dTheta = 0;
        }
        driveTrain.setGyroGoal(driveTrain.getGyroGoal() + dTheta * 3);
        
        //System.out.println("Left Distance: " + leftDistance + ", Right Distance: " + rightDistance + ", Radius: " + radius + ", dTheta: " + dTheta);
        
        double oldHeadingError = headingError;
        currentHeading = driveTrain.getGyro();
        headingError = driveTrain.getGyroGoal() - currentHeading;
        sumHeadingError += headingError;
        double dHeadingError = headingError - oldHeadingError;
        
        System.out.println("Gyro Actual: " + currentHeading + ", Gyro Goal: " + driveTrain.getGyroGoal() + "\n\n");
        
        
    	leftSpeed = leftGoalSpeed + pTurn * headingError + dTurn * dHeadingError +
                iTurn * sumHeadingError;
    	rightSpeed = rightGoalSpeed - (pTurn * headingError + dTurn * dHeadingError +
                iTurn * sumHeadingError) - ((autoType != STRAIGHT)? 0 : P * currentError  + I * sumCurrentError + D * dCurrentError);
        
        /*System.out.println("Left Speed: " + leftSpeed + ", Right Speed: " + rightSpeed +
                ",\nRight Position: " + (rightGoalPosition - currentRightError) + ", Right Goal: " + rightGoalPosition + "\n");
    	*/
    	driveTrain.setMotors(leftSpeed, rightSpeed);
    	
    	
    	//Interpolating the profile to find the exact current position and velocity
    	if(currentTime <= profileLeft[profileLeft.length - 1][0]) {
            for(int i = 1; i < profileLeft.length; i++) {
                if(profileLeft[i][0] >= currentTime) {

                    leftGoalPosition = profileLeft[i-1][1] + 
                                    (currentTime - profileLeft[i-1][0]) *
                                    (profileLeft[i][1] - profileLeft[i-1][1]) /
                                    (profileLeft[i][0] - profileLeft[i-1][0]);
                    leftGoalSpeed = profileLeft[i-1][2] + 
                                    (currentTime - profileLeft[i-1][0]) *
                                    (profileLeft[i][2] - profileLeft[i-1][2]) /
                                    (profileLeft[i][0] - profileLeft[i-1][0]);

                    //Converting from RPM to a percentage
                    leftGoalSpeed = driveTrain.kF * leftGoalSpeed * driveTrain.encCounts / 1023 / 60;
                    leftGoalPosition *= driveTrain.encCounts * 10;
                    break;
                }
            }
            
            for(int i = 1; i < profileRight.length; i++) {
                if(profileRight[i][0] >= currentTime) {

                    rightGoalPosition = profileRight[i-1][1] + 
                                    (currentTime - profileRight[i-1][0]) *
                                    (profileRight[i][1] - profileRight[i-1][1]) /
                                    (profileRight[i][0] - profileRight[i-1][0]);
                    rightGoalSpeed = profileRight[i-1][2] + 
                                    (currentTime - profileRight[i-1][0]) *
                                    (profileRight[i][2] - profileRight[i-1][2]) /
                                    (profileRight[i][0] - profileRight[i-1][0]);

                    //Converting from RPM to a percentage
                    rightGoalSpeed = driveTrain.kF * rightGoalSpeed * driveTrain.encCounts / 1023 / 60;
                    rightGoalPosition *= driveTrain.encCounts * 10;
                    break;
                }
            }
    	}
    	else {
            finished = true;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return finished;
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.println("Right Actual: " + (-2.3) * driveTrain.getRightEncoder() + ", Right Goal: " + rightGoalPosition + ", Right Ratio: " + 
                (rightGoalPosition / ((-2.3) * driveTrain.getRightEncoder())) + "\n");
        
        driveTrain.setMotorsRaw(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        driveTrain.setMotorsRaw(0, 0);
    }
}
