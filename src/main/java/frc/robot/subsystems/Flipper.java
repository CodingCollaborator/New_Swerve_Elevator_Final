package frc.robot.subsystems;

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

public class Flipper extends SubsystemBase {
    
    private SparkMax flipperMotor;
    private SparkMaxConfig flipperMotorConfig;
    private SparkClosedLoopController closedLoopController;
    private RelativeEncoder encoder;
    private MonitoredPIDController flipperController;

    public Flipper()
    {
        flipperMotor = new SparkMax(27, MotorType.kBrushless);
        closedLoopController = flipperMotor.getClosedLoopController();
        encoder = flipperMotor.getEncoder();
        flipperMotorConfig = new SparkMaxConfig();

        flipperMotorConfig.inverted(false).idleMode(IdleMode.kBrake);  // Sets direction, as well as what to do when idle.
        flipperMotorConfig.encoder.positionConversionFactor(1).velocityConversionFactor(1);  //  Conversion factors for position and velocity of encoder.  (Set to default)
        //flipperMotorConfig.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder).pid(1.0, 0.0, 0.0); // Tune PID Constants

        flipperMotor.configure(flipperMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        flipperController = new MonitoredPIDController(0.06, 0.0, 0.0, "Elevator Align");
        flipperController.setSetpoint(0);
        flipperController.setTolerance(.2);
    }

    public double getTargetFlipperSpeed() {
       
        return MathUtil.clamp(flipperController.calculate(this.getFlipperAngle() ), -1, 1 );
    }
    public InstantCommand setDesiredAngle(double desiredAngle)
    {
        return new InstantCommand(() -> flipperController.setSetpoint(desiredAngle));
    }
    public double getDesiredAngle()
    {
        return flipperController.getSetpoint();
    }

    public double getFlipperAngle() 
    {
        return flipperMotor.getEncoder().getPosition();
    }
    public void setFlipperSpeed() 
    {
        double speed = this.getTargetFlipperSpeed();
        flipperMotor.set(speed);//Stop
    }
    public void emergencyStop()
    {
        flipperMotor.disable();
    }

}
