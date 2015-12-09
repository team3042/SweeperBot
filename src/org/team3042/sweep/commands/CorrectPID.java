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
public class CorrectPID {
    double P = 0,I = 0,D = 0;
    int ePrime = 0,E = 0;
    public CorrectPID(double p, double i, double d){
        P = p;
        I = i;
        D = d;
    }
    
    public void reset(){
        ePrime = 0;
        E = 0;
    }
    
    public double correction(int encoderValue, int goalEncoderValue){
        int e,de;
        e = encoderValue - goalEncoderValue;
        de = e - ePrime;
        E += e;
        ePrime = e;
        
        return (P * e) + (D * de) + (I * E);
    }
}
