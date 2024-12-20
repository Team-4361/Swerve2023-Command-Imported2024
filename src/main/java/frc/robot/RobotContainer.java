// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.auto.PIDAutoBalanceCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.Control;

import static frc.robot.Constants.Chassis.DRIVE_DEAD_ZONE;
import static frc.robot.Constants.ClimberPresets.*;
import static frc.robot.subsystems.swerve.SwerveDriveSubsystem.deadzone;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
    private final CommandJoystick xyStick = new CommandJoystick(Control.LEFT_STICK_ID);
    private final CommandJoystick zStick = new CommandJoystick(Control.RIGHT_STICK_ID);
    public static final CommandXboxController xbox = new CommandXboxController(Control.XBOX_CONTROLLER_ID);

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        // Configure the trigger bindings
        configureBindings();

        Robot.swerveDrive.setDefaultCommand(Robot.swerveDrive.run(() ->  {
            Robot.swerveDrive.autoDrive(
                    deadzone(-xyStick.getY(), DRIVE_DEAD_ZONE),
                    deadzone(-xyStick.getX(), DRIVE_DEAD_ZONE),
                    deadzone(zStick.getTwist(), 0.20)
            );
        }));

        // failsafe?
        /*if (Robot.wrist.getTargetRotation() > 500) {
            Robot.wrist.setTarget(0);
        }*/
    }

    /**
     * Use this method to define your trigger->command mappings. Triggers can be created via the
     * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
     * predicate, or via the named factories in {@link
     * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
     * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
     * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
     * joysticks}.
     */
    private void configureBindings() {
        xyStick.button(8).onTrue(Robot.swerveDrive.toggleFieldOrientedCommand());
        xyStick.button(12).onTrue(Robot.swerveDrive.resetGyroCommand());
        //xyStick.button(9).onTrue(Robot.swerveDrive.toggleClosedLoopCommand());

        //xyStick.button(10).onTrue(Commands.runOnce(()->Robot.pidControlEnabled = !Robot.pidControlEnabled));
        xyStick.trigger().or(zStick.trigger()).whileTrue(Robot.swerveDrive.holdPrecisionModeCommand());
        xyStick.button(3).whileTrue(Robot.swerveDrive.lockWheelCommand());

        xyStick.button(4).whileTrue(new PIDAutoBalanceCommand());

        //xyStick.button(5).or(zStick.button(6)).onTrue(Robot.pump.toggleLEDCommand());

        ///////////////////////////////// XBOX CONTROLS

        // TODO: CHANGE!
        xbox.a().onTrue(Commands.runOnce(() -> CLIMBER_PRESET_GROUP.setCurrentPreset(ZERO_POSITION_INDEX)));
        xbox.b().onTrue(Commands.runOnce(() -> CLIMBER_PRESET_GROUP.setCurrentPreset(FLOOR_CUBE_INDEX)));
        xbox.y().onTrue(Commands.runOnce(() -> CLIMBER_PRESET_GROUP.setCurrentPreset(HUMAN_STATION_INDEX)));
        xbox.x().onTrue(Commands.runOnce(() -> CLIMBER_PRESET_GROUP.setCurrentPreset(MID_CONE_INDEX)));
        xbox.povDown().onTrue(Commands.runOnce(() -> CLIMBER_PRESET_GROUP.setCurrentPreset(FLOOR_CONE_INDEX)));
        xbox.rightBumper().onTrue(Commands.runOnce(() -> CLIMBER_PRESET_GROUP.setCurrentPreset(HIGH_CONE_INDEX)));
/* 
        xbox.rightTrigger().whileTrue(Commands.runEnd(() -> {
            Robot.wrist.translateMotor(-xbox.getRightTriggerAxis()/2);
        }, () -> {
            Robot.wrist.translateMotor(0);
        }));

        xbox.leftTrigger().whileTrue(Commands.runEnd(() -> {
            Robot.wrist.translateMotor(xbox.getLeftTriggerAxis()/2);
        }, () -> {
            Robot.wrist.translateMotor(0);
        }));

        xbox.leftStick().onTrue(Commands.runOnce(() -> {
            Robot.wrist.resetEncoder();
            Robot.arm.getRotation().resetEncoder();
            Robot.arm.getExtension().resetEncoder();
        }));

        //xbox.leftBumper().onTrue(Commands.runOnce(() -> Robot.pump.toggleVacuum()));
    
        //xbox.povUp().onTrue(Robot.pump.openVacuumCommand());
        xbox.povLeft().onTrue(Commands.runOnce(() -> CLIMBER_PRESET_GROUP.setCurrentPreset(MANUAL_STATION_INDEX)));*/
    }
}