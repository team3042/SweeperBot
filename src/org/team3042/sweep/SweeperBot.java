/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team3042.sweep;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team3042.sweep.commands.CommandBase;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class SweeperBot extends IterativeRobot {

    //Command autonomousCommand;

    private String CALIBRATION_FILE_NAME = "CalibrationFile";
    private double CALIBRATION_MOTOR_SPEED = 0.25;
    private float CALIBRATION_LENGTH_IN_SECONDS = 2;
    private final int LOGGER_LEVEL = 1;
    
    public static Logger logger;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        //Creates logger
        SmartDashboard.putNumber("Logger level", LOGGER_LEVEL);
        logger = new Logger(true,true,LOGGER_LEVEL);
        logger.log("Robot init", 1);
        //Initialize the SmartDashboard
        SmartDashboard.putNumber("Calibration Length In Seconds", CALIBRATION_LENGTH_IN_SECONDS);
        SmartDashboard.putString("Calibration File Name", CALIBRATION_FILE_NAME);
        SmartDashboard.putNumber("Calibration Motor Speed", CALIBRATION_MOTOR_SPEED);
        SmartDashboard.putNumber("P value", .004);
        SmartDashboard.putNumber("Maintain distance", 1000);
        SmartDashboard.putNumber("Maintain speed", .3);
        
        // instantiate the command used for the autonomous period
        //autonomousCommand = new ExampleCommand();

        // Initialize all subsystems
        CommandBase.init();
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
        //autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
	// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        //autonomousCommand.cancel();
        Scheduler.getInstance().run();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    
    public void disabledInit(){
        Scheduler.getInstance().removeAll();
    }
    
}
