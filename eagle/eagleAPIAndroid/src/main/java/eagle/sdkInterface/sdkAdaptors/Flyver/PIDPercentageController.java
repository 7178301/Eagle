package eagle.sdkInterface.sdkAdaptors.Flyver;


/**
 * This is a PID Controller used for the horizontal stabilization of the drone
 * It is used for angle control
 * Source from Androcopter project: https://code.google.com/p/andro-copter/
 */
public class PIDPercentageController {
    public PIDPercentageController(double kp, double ki, double kd, double smoothingStrength) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.smoothingStrength = smoothingStrength;
        previousDifference = 0.0f;

        integrator = 0.0f;
        differencesMean = 0.0f;
    }

    public double getInput(double targetAngle, double currentAngle, double dt) {
        // The complete turn can be done, so we have to be careful around the
        // "+180°, -180°" limit.
        double difference = targetAngle - currentAngle;

        // Now, the PID computation can be done.
        double input = 0.0f;

        // Proportional part.
        input += difference * kp;

        // Integral part.
        integrator += difference * ki * dt;
        input += integrator;
        // Derivative part, with filtering.
        differencesMean = differencesMean * smoothingStrength
                + difference * (1 - smoothingStrength);
        double derivative = (differencesMean - previousDifference) / dt;
        previousDifference = differencesMean;
        input += derivative * kd;

        if (input > 1) {
            return 1;
        } else if (input < -1) {
            return -1;
        } else {
            return input;
        }
    }

    public void setCoefficients(double kp, double ki, double kd) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
    }

    public void resetIntegrator() {
        integrator = 0.0f;
    }
    private double kp, ki, kd, integrator, smoothingStrength, differencesMean,
            previousDifference;
}
