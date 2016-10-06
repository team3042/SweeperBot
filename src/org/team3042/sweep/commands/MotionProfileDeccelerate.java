/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.commands;

/**
 *
 * @author NewUser
 */



public class MotionProfileDeccelerate {
    
    MotionProfileAccelerate accelerate;
    
    public MotionProfileDeccelerate(int itp, double time1, double time2, 
                    double maxVelocity) {
        accelerate = new MotionProfileAccelerate(itp, time1, time2, maxVelocity);
        
        
    }
    
    public double[][] calculateProfile() {
        double[][] accelerateTrajectory = accelerate.calculateProfile();
        double[][] deccelerateTrajectory = new double[accelerateTrajectory.length][3];
        double maxTime = accelerateTrajectory[accelerateTrajectory.length - 1][0];
        
        for(int i = accelerateTrajectory.length - 1; i >= 0; i--) {
            deccelerateTrajectory[accelerateTrajectory.length - 1][0] = maxTime - accelerateTrajectory[i][0];
        }
        
        return deccelerateTrajectory;
    }
}
