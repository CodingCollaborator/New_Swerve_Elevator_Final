/*
 * package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants;
import frc.robot.wrappers.MonitoredPIDController;

public class ElevatorCommand extends Command 
{
    private MonitoredPIDController elevatorController;
    public ElevatorCommand()
    {
        elevatorController = new MonitoredPIDController(.2, .0, .0, "Elevator Align");
        elevatorController.setSetpoint(0);
        elevatorController.setTolerance(.2);
    }
    public double getTargetElevatorSpeed() {
       
        return MathUtil.clamp(elevatorController.calculate(Constants.elevator.getElevatorPosition() ), -1, 1 );
    }
    @Override
    public boolean isFinished() {
        return elevatorController.atSetpoint();
    }
    public void setHeight(double height)
    {
        elevatorController.setSetpoint(height);
    }
    public double getDesiredHeight()
    {
        return elevatorController.getSetpoint();
    }
}
 */