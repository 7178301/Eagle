package eagle.sdkInterface.sensorAdaptors.gyroscopeAdaptors;

import eagle.sdkInterface.sensorAdaptors.Gyroscope;

/** Android Gyroscope Adaptor
 * @since     14/06/2015
 * <p>
 * Date Modified	14/06/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public class AndroidGyroscope extends Gyroscope {
    public AndroidGyroscope(){
        super("Android","Gyroscope","0.0.1");
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
    public GyroscopeData getData() {
        return null;
    }
}
