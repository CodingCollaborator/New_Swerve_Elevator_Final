package frc.robot.wrappers;

import edu.wpi.first.math.controller.PIDController;

public class MonitoredPIDController extends PIDController
{
    private String name;
    private double lastOutput;
    private boolean disabled;
    
    public MonitoredPIDController(double p, double i, double d, String n)
    {
        super(p, i, d);
        this.name = n;
        lastOutput = 0;
        disabled = false;
    }
    
    @Override
    public double calculate(double measurement)
    {
        if (!disabled){
            double output = super.calculate(measurement);
            return output;
        }
        return 0;
    }
    
    public void disable(){
        disabled = true;
    }

    public void enable(){
        disabled = false;
    }

    public boolean isDisabled(){
        return disabled;
    }
}

