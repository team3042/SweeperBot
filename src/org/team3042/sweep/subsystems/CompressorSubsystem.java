/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Compressor;
import org.team3042.sweep.RobotMap;


/**
 *
 * @author NewUser
 */
public class CompressorSubsystem extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    Compressor compressor = new Compressor(RobotMap.COMPRESSOR_PRESSURE_SWITCH_DIO, 
            RobotMap.COMPRESSOR_SPIKE);
   
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public CompressorSubsystem() {
        compressor.start();
    }
}
