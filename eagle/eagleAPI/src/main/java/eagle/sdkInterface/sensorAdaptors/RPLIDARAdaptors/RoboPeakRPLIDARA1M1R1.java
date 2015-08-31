package eagle.sdkInterface.sensorAdaptors.RPLIDARAdaptors;

import eagle.sdkInterface.sensorAdaptors.AdaptorRPLIDAR;

/**
 * Robo Peak RB-See-90 Ultrasonic Adaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 14/06/2015
 * <p/>
 * Date Modified	14/06/2015 - Nicholas
 */
public class RoboPeakRPLIDARA1M1R1 extends AdaptorRPLIDAR {
    public RoboPeakRPLIDARA1M1R1() {
        super("Robo Peak", "RPLIDAR A1M1R1", "0.0.1");
    }

    private float[] RPLIDARData;

    //TODO Following Method Need Proper Implementation
    public boolean connectToSensor() {
        return false;
    }

    //TODO Following Method Need Proper Implementation
    public boolean isConnectedToSensor() {
        return false;
    }

    @Override
    public float[] getData() {
        return RPLIDARData;
    }

    //TODO Following Method Need Proper Implementation
    public boolean isDataReady() {
        return true;
    }
}
