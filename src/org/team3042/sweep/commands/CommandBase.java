package org.team3042.sweep.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team3042.sweep.OI;
import org.team3042.sweep.subsystems.BroomArm;
import org.team3042.sweep.subsystems.CompressorSubsystem;
import org.team3042.sweep.subsystems.DriveTrain;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author Author
 */
public abstract class CommandBase extends Command {

    public static OI oi;
    // Create a single static instance of all of your subsystems
    public static DriveTrain driveTrain = new DriveTrain();
    public static BroomArm broomArm = new BroomArm();
    public static CompressorSubsystem compressorSubsystem = new CompressorSubsystem();

    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();

        // Show what command your subsystem is running on the SmartDashboard
        //SmartDashboard.putData(exampleSubsystem);
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}
