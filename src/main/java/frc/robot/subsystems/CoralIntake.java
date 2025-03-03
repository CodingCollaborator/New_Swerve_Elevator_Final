package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CoralIntake extends SubsystemBase {

    private TalonSRX coralIntake;
    private static final double PUSH_OUT = -0.3;
    private static final double BRAKE = 0.0;
    private static final double PULL_IN = 0.6;
    private static final double HOLD = 0.20;
    //VERY IMPORTANT, REMEMBER TO SET TO FULL SPEED FOR COMPETITION.

    public CoralIntake() {
        coralIntake = new TalonSRX(14);
    }

    public InstantCommand pullCoralIn() {
        return new InstantCommand(() -> coralIntake.set(TalonSRXControlMode.PercentOutput, PULL_IN));//# Starting At 5% Power
    }

    public InstantCommand pushCoralOut() {
        return new InstantCommand(() -> coralIntake.set(TalonSRXControlMode.PercentOutput, PUSH_OUT));//Starting At 5% Power
    }

    public InstantCommand hold() {
        return new InstantCommand(() -> coralIntake.set(TalonSRXControlMode.PercentOutput, HOLD));//Stop
    }

    public InstantCommand stopIntake() {
        return new InstantCommand(() -> coralIntake.set(TalonSRXControlMode.PercentOutput, BRAKE));//Stop
    }

}