/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.team3042.sweep.RobotMap;

/**
 *
 * @author NewUser
 */
public class BroomArm extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    Solenoid upperSolenoid = new Solenoid(RobotMap.BROOM_ARM_UPPER_SOLENOID);
    Solenoid lowerSolenoid = new Solenoid(RobotMap.BROOM_ARM_LOWER_SOLENOID);
            
    boolean isRaised;   

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void sweep() {
        upperSolenoid.set(false);
        lowerSolenoid.set(false);
    }
    
    public void raise() {
        upperSolenoid.set(false);
        lowerSolenoid.set(true);
        isRaised = true;
    }
    
    public void upperShake() {
        upperSolenoid.set(true);
        lowerSolenoid.set(true);
        isRaised = false;
    }
    
    public void toggle() {
        if (isRaised = true) {
            upperShake();
        }
        else {
            raise();
        }
    }

}
