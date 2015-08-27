package eagle.sdkInterface.sensorAdaptors.cameraAdaptors;

import android.content.Context;

import eagle.sdkInterface.sensorAdaptors.AdaptorCamera;

/** Android Camera Adaptor
 * @since     14/06/2015
 * <p>
 * Date Modified	14/06/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public class AndroidCamera extends AdaptorCamera {
    public AndroidCamera(){
        super("Android","Camera","0.0.1");
    }

    //TODO Following Method Need Propper Implementation
    public boolean connectToSensor(){
        return false;
    }
    //TODO Following Method Need Propper Implementation
    public boolean isConnectedToSensor(){
        return false;
    }
}
