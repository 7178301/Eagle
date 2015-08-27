package eagle.sdkInterface.sensorAdaptors.RPLIDARAdaptors;

import eagle.sdkInterface.sensorAdaptors.RPLIDAR;
/** Robo Peak RB-See-90 Ultrasonic Adaptor
 * @since     14/06/2015
 * <p>
 * Date Modified	14/06/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public class RoboPeakRPLIDARA1M1R1 extends RPLIDAR {
    public RoboPeakRPLIDARA1M1R1(){
        super("Robo Peak","RPLIDAR A1M1R1","0.0.1");
    }

    //TODO Following Method Need Proper Implementation
    public boolean calibrateSensor(){
        return true;
    }
    //TODO Following Method Need Proper Implementation
    public boolean connectToSensor(){
        return true;
    }
    //TODO Following Method Need Proper Implementation
    public boolean isConnectedToSensor(){
        return true;
    }
    //TODO Following Method Need Propper Implementation
    public void setConfiguration(){}
}
