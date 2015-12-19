package org.team3042.sweep;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static final int leftMotor = 1;
    // public static final int rightMotor = 2;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static final int rangefinderPort = 1;
    // public static final int rangefinderModule = 1;
    public static final int LEFT_JOY_USB_PORT_1 = 1;
    public static final int RIGHT_JOY_USB_PORT_2 = 2;
    
    
    //PWM Ports
    public static final int DRIVE_TRAIN_LEFT_JAGUAR = 3;
    public static final int DRIVE_TRAIN_RIGHT_JAGUAR = 2;
    
    //Relays
    public static final int BROOM_ARM_LOWER_SOLENOID_SPIKE = 1;
    public static final int BROOM_ARM_UPPER_SOLENOID_SPIKE = 2;
    public static final int COMPRESSOR_SPIKE = 6;
    
    //Digital IO Ports
    public static final int COMPRESSOR_PRESSURE_SWITCH_DIO = 10;
    public static final int LEFT_ENCODER_A_DIO = 3;
    public static final int LEFT_ENCODER_B_DIO = 4;
    public static final int RIGHT_ENCODER_A_DIO = 1;
    public static final int RIGHT_ENCODER_B_DIO = 2;
    
    //Max acceleration of motors in speed increase per second
    public static final double MAX_ACCEL_LEFT = 0.5;
    public static final double MAX_ACCEL_RIGHT = 0.5;
    
    //Speed of motors in encoder ticks per second at a speed
    public static final double SPEED_LEFT = 10;
    public static final double SPEED_RIGHT = 10;
}

