package frc.robot.subsystems;

import java.time.Instant;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.wrappers.MonitoredPIDController;
import edu.wpi.first.math.MathUtil;
import frc.robot.Constants;


public class Elevator extends SubsystemBase
{
    private SparkMax elevatorMotor;
    private SparkMaxConfig elevatorMotorConfig;
    private SparkClosedLoopController closedLoopController;
    private RelativeEncoder encoder;
    private MonitoredPIDController elevatorController;

    public Elevator()
    {
        elevatorMotor = new SparkMax(25, MotorType.kBrushless);
        closedLoopController = elevatorMotor.getClosedLoopController();
        encoder = elevatorMotor.getEncoder();
        elevatorMotorConfig = new SparkMaxConfig();

        elevatorMotorConfig.inverted(false).idleMode(IdleMode.kBrake);  // Sets direction, as well as what to do when idle.
        elevatorMotorConfig.encoder.positionConversionFactor(1).velocityConversionFactor(1);  //  Conversion factors for position and velocity of encoder.  (Set to default)
        //elevatorMotorConfig.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder).pid(1.0, 0.0, 0.0); // Tune PID Constants

        elevatorMotor.configure(elevatorMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        elevatorController = new MonitoredPIDController(0.05, 0.0, 0.0, "Elevator Align");
        elevatorController.setSetpoint(0);
        elevatorController.setTolerance(.1);
    }

    public double getTargetElevatorSpeed() {
       
        return MathUtil.clamp(elevatorController.calculate(this.getElevatorPosition() ), -1, 1 );
    }
    public InstantCommand setHeight(double desiredHeight)
    {
        return new InstantCommand(() -> elevatorController.setSetpoint(desiredHeight));
    }
    public double getDesiredHeight()
    {
        return elevatorController.getSetpoint();
    }

    public double getElevatorPosition() 
    {
        return elevatorMotor.getEncoder().getPosition();
    }
    public void setElevatorSpeed() 
    {
        double speed = this.getTargetElevatorSpeed();
        elevatorMotor.set(speed*0.25);//Stop
    }
    public void emergencyStop()
    {
        elevatorMotor.disable();
    }

}
