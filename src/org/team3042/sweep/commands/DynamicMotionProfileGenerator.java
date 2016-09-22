/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3042.sweep.commands;

/**
 *
 * @author Ethan
 */
public class DynamicMotionProfileGenerator {
    //Current point
    int currentPoint = 0;

    //Time between each point in ms
    int itp;

    //Time for each filter in ms
    double time1, time2;

    //Maximum speed in rotations / min
    double startVelocity, endVelocity, maxVelocity;

    //Distance traveled in rotations
    double distance;

    //Length of each filter
    double filterLength1, filterLength2;

    //Sum of each filter
    double filterSum1 = 0, filterSum2 = 0, filterTotalSum = 0;

    //Velocity and position at current point
    double currentVelocity = 0, currentPosition = 0, oldVelocity = 0;
    
    //Times used when start or end velocities are not zero
    double timeToMaxVelocity, zeroToMaxTime, startToMaxTime, startMaxTime;
    double timeToEndVelocity, maxToZeroTime, maxToEndTime, endMaxTime;

    //Time to decceleration in ms
    double timeToDeccel;

    //Total time in ms
    double totalTime;

    //Total number of points
    int totalPoints;

    //Array with all values of filterSum1
    double[] filterSums1;

    public DynamicMotionProfileGenerator(int itp, double time1, double time2, 
            double startVelocity, double endVelocity, double maxVelocity, double distance) {
            this.itp = itp;
            this.time1 = time1;
            this.time2 = time2;
            this.startVelocity = startVelocity;
            this.endVelocity = endVelocity;
            this.maxVelocity = maxVelocity;
            this.distance = distance;

            //Calculating additional time at max velocity due to not starting or ending at zero
            timeToMaxVelocity = (maxVelocity - startVelocity) / maxVelocity;
            zeroToMaxTime = maxVelocity * time1 / 2;
            startToMaxTime = (maxVelocity - startVelocity) * time1 / 2 + startVelocity * time1;
            startMaxTime = (zeroToMaxTime - timeToMaxVelocity * startToMaxTime) / maxVelocity;
            
            timeToEndVelocity = (maxVelocity - endVelocity) / maxVelocity;
            maxToZeroTime = maxVelocity * time1 / 2;
            maxToEndTime = (maxVelocity - endVelocity) * time1 / 2 + endVelocity * time1;
            endMaxTime = (maxToZeroTime - timeToEndVelocity * maxToEndTime) / maxVelocity;
            
            //Calculating lengths of two stage filter and number of points
            filterLength1 = Math.ceil(time1 / itp);
            filterLength2 = Math.ceil(time2 / itp);
            timeToDeccel = distance / maxVelocity * 1000;
            totalTime = timeToDeccel - time1 - time2 + startToMaxTime + maxToEndTime;
            totalPoints = (int) Math.ceil(totalTime / itp);
            filterSums1 = new double[totalPoints + 1];
    }

    private void runFilters() {
        //Running through first filter
        if (currentPoint * itp < startToMaxTime) {
            //Accelerating filter 1
            filterSum1 = currentPoint / filterLength1 + startVelocity / maxVelocity;
        }
        else if (currentPoint >= totalPoints - filterLength2) {
            filterSum1 = 0;
        }
        else if (currentPoint * itp >= timeToDeccel) {
            //Deccelerating filter 1
            filterSum1 = (totalPoints - filterLength2 - currentPoint) / filterLength1;
        }
        else {
            filterSum1 = 1;
        }

        //Creating filterSum2 from the sum of the last filterLength2 values of filterSum1(Boxcar filter)
        filterSums1[currentPoint] = filterSum1;
        int filter2Start = (int) ((currentPoint > filterLength2) ? currentPoint - filterLength2 + 1 : 0);
        filterSum2 = 0;
        for(int i = filter2Start; i <= currentPoint; i++) {
            filterSum2 += filterSums1[i];
        }

    }

    private void calculatePosition() {
            currentPosition += (oldVelocity + currentVelocity) / 2 * itp / 1000;
    }

    private void calculateVelocity() {
            oldVelocity = currentVelocity;
            currentVelocity = (filterSum1 + filterSum2) / (filterLength2 + 1) * maxVelocity;
    }

    public double[][] calculateProfile() {
            double[][] trajectory = new double[totalPoints + 1][3];

            double currentTime = 0;

            for(int i = 0; i <= totalPoints; i++) {
                    runFilters();
                    calculateVelocity();
                    calculatePosition();

                    trajectory[i][0] = currentTime;
                    trajectory[i][1] = currentPosition;
                    trajectory[i][2] = currentVelocity * 60;

                    currentTime += itp;
                    currentPoint++;
            }

            return trajectory;
    }
}
