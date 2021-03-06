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
        double maxPosition = accelerateTrajectory[accelerateTrajectory.length - 1][1];
        
        for(int i = accelerateTrajectory.length - 1; i >= 0; i--) {
            deccelerateTrajectory[accelerateTrajectory.length - i - 1][0] = maxTime - accelerateTrajectory[i][0];
            deccelerateTrajectory[accelerateTrajectory.length - i - 1][1] = maxPosition - accelerateTrajectory[i][1];
            deccelerateTrajectory[accelerateTrajectory.length - i - 1][2] = accelerateTrajectory[i][2];
        }
        
        return deccelerateTrajectory;
    }
}
