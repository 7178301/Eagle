package eagle.sdkInterface.sensorAdaptors.ultrasonicAdaptors;

import eagle.sdkInterface.sensorAdaptors.Ultrasonic;
/** Seeed Studio RB-See-90 Ultrasonic Adaptor
 * @since     14/06/2015
 * <p>
 * Date Modified	14/06/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public class SeeedStudioSEN10737P extends Ultrasonic {
    public SeeedStudioSEN10737P(){
        super("Seeed Studio","SEN10737P","0.0.1");
    }

    //TODO Following Method Need Propper Implementation
    public boolean calibrateSensor(){
        return true;
    }
    //TODO Following Method Need Propper Implementation
    public boolean connectToSensor(){
        return true;
    }
    //TODO Following Method Need Propper Implementation
    public boolean isConnectedToSensor(){
        return true;
    }
    //TODO Following Method Need Propper Implementation
    public void setConfiguration(){}
    //TODO Following Method Need Propper Implementation
    @Override
    public double getData(){
        return 0.0;
    }
}
