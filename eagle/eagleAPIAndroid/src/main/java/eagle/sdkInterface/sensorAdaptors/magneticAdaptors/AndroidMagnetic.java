package eagle.sdkInterface.sensorAdaptors.magneticAdaptors;


import eagle.sdkInterface.sensorAdaptors.Magnetic;

/** Android Gyroscope Adaptor
 * @since     27/08/2015
 * <p>
 * Date Modified	27/08/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public class AndroidMagnetic extends Magnetic {
    public AndroidMagnetic(){
        super("Android","Magnetic","0.0.1");
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
}
