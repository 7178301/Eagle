package eagle.sdkInterface.sdkAdaptors.Flyver;


/**
 * This is a PID Controller used for the horizontal stabilization of the drone
 * It is used for angle control
 * Source from Androcopter project: https://code.google.com/p/andro-copter/
 */
public class PIDAngleController {
    public PIDAngleController(double kp, double ki, double kd, double smoothingStrength) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        previousDifference = 0.0f;

        integrator = 0.0f;
        differencesMean = 0.0f;
    }

    public double getInput(double targetAngle, double currentAngle, double dt) {
        // The complete turn can be done, so we have to be careful around the
        // "+180°, -180°" limit.
        double rawDifference = targetAngle - currentAngle;
        double difference = getMainAngle(rawDifference);
        boolean differenceJump = (difference != rawDifference);

        // Now, the PID computation can be done.
        double input = 0.0f;

        // Proportional part.
        input += difference * kp;

        // Integral part.
        integrator += difference * ki * dt;
        input += integrator;
        // Derivative part, with filtering.
        if (!differenceJump) {
            differencesMean = differencesMean * smoothingStrength
                    + difference * (1 - smoothingStrength);
            double derivative = (differencesMean - previousDifference) / dt;
            previousDifference = differencesMean;
            input += derivative * kd;
        } else {
            // Erase the history, because we are not reaching the target from
            // the "same side".
            differencesMean = 0.0f;
        }

        return input;
    }

    public void setCoefficients(double kp, double ki, double kd) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
    }

    public void resetIntegrator() {
        integrator = 0.0f;
    }

    // Return the smallest angle between two segments.
    public static double getMainAngle(double angle) {
        while (angle < -180.0f)
            angle += 360.0f;
        while (angle > 180.0f)
            angle -= 360.0f;

        return angle;
    }

    private double kp, ki, kd, integrator, smoothingStrength, differencesMean,
            previousDifference;
}