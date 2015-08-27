package eagle.sdkInterface.sensorAdaptors.accelerometerAdaptors;

import eagle.sdkInterface.sensorAdaptors.Accelerometer;
import eagle.sdkInterface.sensorAdaptors.SensorAdaptor;

/** Android Accelerometer Adaptor
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public class AndroidAccelerometer extends Accelerometer {
    public AndroidAccelerometer(){
        super("Android","Accelerometer","0.0.1");
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
    public AccelerometerData getData() {
        return null;
    }
}
