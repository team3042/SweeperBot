/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.team3042.sweep.RobotMap;

/**
 *
 * @author NewUser
 */
public class BroomArm extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    Relay upperSolenoidSpike = new Relay(RobotMap.BROOM_ARM_UPPER_SOLENOID_SPIKE);
    Relay lowerSolenoidSpike = new Relay(RobotMap.BROOM_ARM_LOWER_SOLENOID_SPIKE);
            
    boolean isRaised;   

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void sweep() {
        upperSolenoidSpike.set(Relay.Value.kOff);
        lowerSolenoidSpike.set(Relay.Value.kOff);
    }
    
    public void raise() {
        upperSolenoidSpike.set(Relay.Value.kOff);
        lowerSolenoidSpike.set(Relay.Value.kForward);
        isRaised = true;
    }
    
    public void upperShake() {
        upperSolenoidSpike.set(Relay.Value.kForward);
        lowerSolenoidSpike.set(Relay.Value.kForward);
        isRaised = false;
    }
    
    public void toggleShake() {
        if (isRaised) {
            upperShake();
        }
        else {
            raise();
        }
    }

}
