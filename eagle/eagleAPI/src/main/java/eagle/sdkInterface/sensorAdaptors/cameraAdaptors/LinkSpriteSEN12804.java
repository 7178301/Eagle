package eagle.sdkInterface.sensorAdaptors.cameraAdaptors;

import eagle.sdkInterface.sensorAdaptors.AdaptorCamera;

/**
 * Link Sprite SEN-12804 Camera Adaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 19/06/2015
 * <p/>
 * Date Modified	19/06/2015 - Nicholas
 */
public class LinkSpriteSEN12804 extends AdaptorCamera {
    public LinkSpriteSEN12804() {
        super("Link Sprite", "SEN-12804", "0.0.1");
    }

    //TODO Following Method Need Proper Implementation
    public boolean connectToSensor() {
        return true;
    }

    //TODO Following Method Need Proper Implementation
    public boolean isConnectedToSensor() {
        return true;
    }

    //TODO Following Method Need Proper Implementation
    public boolean isDataReady() {
        return true;
    }


}
