
package org.team3042.sweep;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.team3042.sweep.commands.BroomArmRaise;
import org.team3042.sweep.commands.BroomArmShake;
import org.team3042.sweep.commands.BroomArmSweep;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    public Joystick stickLeft = new Joystick(  RobotMap.LEFT_JOY_USB_PORT_1);
    public Joystick stickRight = new Joystick(  RobotMap.RIGHT_JOY_USB_PORT_2);
    // Button button = new JoystickButton(stick, buttonNumber);
    public Button buttonArmRaise = new JoystickButton(stickRight, 3);
    public Button buttonArmSweep = new JoystickButton(stickRight, 4);
    public Button buttonArmShake = new JoystickButton(stickRight, 2);
    
    // Another type of button you can create is a DigitalIOButton, which is
    // a button or switch hooked up to the cypress module. These are useful if
    // you want to build a customized operator interface.
    // Button button = new DigitalIOButton(1);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    
    public OI() {
        buttonArmShake.whileHeld(new BroomArmShake());
        buttonArmRaise.whenPressed(new BroomArmRaise());
        buttonArmSweep.whenPressed(new BroomArmSweep());
    }
}

