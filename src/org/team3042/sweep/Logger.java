/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author admi
 */
public class Logger extends FileIO{
    
    public static Logger logger;
    //Level 1-5, level 1 being the most important, and 0 meaning you dont want any messages
    public Logger(String FileName){
        
    }
    
    
    public void log(String message, int level){
        if(level<= SmartDashboard.getNumber("Logger Level")){
            if(SmartDashboard.getBoolean("Logger Use Console")){
                System.out.println(message);
            }
            if(SmartDashboard.getBoolean("Logger Use File")){
                writeToFile(message);
            }
        }
    }
}