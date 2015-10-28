package eagle.sdkInterface.sdkAdaptors.Flyver;

/**
 * eagle.sdkInterface.sdkAdaptors.Flyver
 *
 * @author cameron
 * @version 0.0.1
 * @since 10/28/15
 * <p/>
 */
public class ThrottleController {
    private final int motorZeroLevel;
    private final int motorDescentLevel;
    private final int motorMaxLevel;

    public ThrottleController(int motorZeroLevel, int motorDescentLevel, int motorMaxLevel) {
        this.motorZeroLevel = motorZeroLevel;
        this.motorDescentLevel = motorDescentLevel;
        this.motorMaxLevel = motorMaxLevel;
    }

    public double getInput(double throttlePercentage, float dt) {
        return throttlePercentage*(motorMaxLevel-motorDescentLevel)+(motorDescentLevel-motorZeroLevel);
    }
}
