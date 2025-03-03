// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import edu.wpi.first.wpilibj.Encoder;
import autos.AutoRoutines;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.CoralIntake;
import frc.robot.subsystems.Elevator;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotContainer {

    private final Telemetry logger = new Telemetry(Constants.MaxSpeed);

    private final CommandXboxController joystick0 = new CommandXboxController(0); //#This Joystick Controls The Driving(Will Also Control Elevator)
    private final CommandXboxController joystick1 = new CommandXboxController(1);//#This Joystick Controls The Intake System


    private int alliance; //for Auto

    public RobotContainer(int all) //Is there a way to set this at the drive station.
    {
        configureBindings(all);
    }

    private void configureBindings(int all) {
        this.alliance=all;
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        Constants.drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            Constants.drivetrain.applyRequest(() ->
                Constants.drive.withVelocityX(-joystick0.getLeftY() * Constants.MaxSpeed * 0.5 * Math.pow(-1, alliance)) // Drive forward with negative Y (forward) #SpeedHalved
                    .withVelocityY(-joystick0.getLeftX() * Constants.MaxSpeed * 0.5 * Math.pow(-1, alliance)) // Drive left with negative X (left) #SpeedHalved
                    .withRotationalRate(-joystick0.getRightX() * Constants.MaxAngularRate * 0.5) // Drive counterclockwise with negative X (left) #SpeedHalved
            ) 
            //# When Command Is Executed, Request Is Applied Based on What Joystick Buttons Are Being Pressed

            //VERY IMPORTANT, REMEMBER TO SET TO FULL SPEED FOR COMPETITION.

        );

       

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        joystick0.back().and(joystick0.y()).whileTrue(Constants.drivetrain.sysIdDynamic(Direction.kForward));
        joystick0.back().and(joystick0.x()).whileTrue(Constants.drivetrain.sysIdDynamic(Direction.kReverse));
        joystick0.start().and(joystick0.y()).whileTrue(Constants.drivetrain.sysIdQuasistatic(Direction.kForward));
        joystick0.start().and(joystick0.x()).whileTrue(Constants.drivetrain.sysIdQuasistatic(Direction.kReverse));

        // reset the field-centric heading on left bumper press
        joystick0.leftBumper().onTrue(Constants.drivetrain.runOnce(() -> Constants.drivetrain.seedFieldCentric()));
        //#Left Bumper Reverses Forward and Backward)

        joystick0.a().onTrue(Constants.elevator.setHeight(0));             //  LevelOne/Reset/Feeder
        joystick0.x().onTrue(Constants.elevator.setHeight(40));            //  LevelTwo
        joystick0.y().onTrue(Constants.elevator.setHeight(134.63));        //  LevelThree
        joystick0.b().onTrue(Constants.elevator.setHeight(210));           //  LevelFour

        joystick1.a().onTrue(Constants.coralIntake.pullCoralIn()).onFalse(Constants.coralIntake.hold());//On true, button pressed, calls method.
        joystick1.b().onTrue(Constants.coralIntake.pushCoralOut()).onFalse(Constants.coralIntake.hold());
        joystick1.y().onTrue(Constants.coralIntake.hold()).onFalse(Constants.coralIntake.stopIntake());
        joystick1.x().onTrue(Constants.coralIntake.stopIntake()).onFalse(Constants.coralIntake.hold());


        Constants.drivetrain.registerTelemetry(logger::telemeterize);
    }

    public Command getAutonomousCommand() 
    {
        return AutoRoutines.Taxi.routine(Constants.drivetrain, alliance);
    }
}

//OBSOLETE BUTTON BINDINGS

 //joystick0.a().whileTrue(drivetrain.applyRequest(() -> brake));
        //#Constructs a Trigger around Button A that requests the DriveTrain Break
        //joystick0.b().whileTrue(drivetrain.applyRequest(() ->
        //point.withModuleDirection(new Rotation2d(-joystick0.getLeftY(), -joystick0.getLeftX()))
        //));
         //#Creates a Trigger around Button B that Points Wheels in Direction of Joystick - (Not Sure of Functionality)
//joystick0.a().onTrue(elevator.setToLevelOne()).onFalse(elevator.stopElevator());       
        //joystick0.b().onTrue(elevator.setToLevelTwo()).onFalse(elevator.stopElevator());        
        //joystick0.y().onTrue(elevator.setToLevelThree());       
        //joystick0.x().onTrue(elevator.setToLevelFour());      
        //joystick0.rightBumper().onTrue(elevator.setToLevelZero());  
        //joystick0.x().onTrue(elevator.moveDown()).onFalse(elevator.stopElevator()); 
        //joystick0.y().onTrue(elevator.moveUp()).onFalse(elevator.stopElevator()); 