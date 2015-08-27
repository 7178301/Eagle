package eagle.sdkInterface.sensorAdaptors.compassAdaptors;

import eagle.sdkInterface.sensorAdaptors.Compass;

/** Android Compass Adaptor
 * @since     14/06/2015
 * <p>
 * Date Modified	14/06/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public class AndroidCompass extends Compass {
    public AndroidCompass(){
        super("Android","Compass","0.0.1");
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
