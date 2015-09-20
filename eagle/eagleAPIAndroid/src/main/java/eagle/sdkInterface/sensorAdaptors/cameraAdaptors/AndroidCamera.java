package eagle.sdkInterface.sensorAdaptors.cameraAdaptors;

import eagle.sdkInterface.sensorAdaptors.AdaptorCamera;

/**
 * Android Camera Adaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 14/06/2015
 * <p/>
 * Date Modified	14/06/2015 - Nicholas
 */

public class AndroidCamera extends AdaptorCamera {
    public AndroidCamera() {
        super("Android", "Camera", "0.0.1");
    }

    //TODO Following Method Need Propper Implementation
    public boolean connectToSensor() {
        return false;
    }

    //TODO Following Method Need Propper Implementation
    public boolean isConnectedToSensor() {
        return false;
    }

    @Override

    public boolean isDataReady() {
        return false;
    }
}
