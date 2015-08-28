package eagle.sdkInterface.sensorAdaptors.RPLIDARAdaptors;

import eagle.sdkInterface.sensorAdaptors.AdaptorRPLIDAR;
/** Robo Peak RB-See-90 Ultrasonic Adaptor
 * @since     14/06/2015
 * <p>
 * Date Modified	14/06/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public class RoboPeakRPLIDARA1M1R1 extends AdaptorRPLIDAR {
    public RoboPeakRPLIDARA1M1R1(){
        super("Robo Peak","RPLIDAR A1M1R1","0.0.1");
    }
    private float[] RPLIDARData;

    //TODO Following Method Need Proper Implementation
    public boolean connectToSensor(){
        return false;
    }
    //TODO Following Method Need Proper Implementation
    public boolean isConnectedToSensor(){
        return false;
    }
    @Override
    public float[] getData() {
        return RPLIDARData;
    }
}
