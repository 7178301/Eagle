package eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks;

import java.awt.Image;

import eagle.navigation.positioning.PositionGPS;

/**
 *  Sensor Adaptor Callback Class
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 30/09/2015
 * <p/>
 * Date Modified	30/09/2015 - Nicholas
 */
public interface AdaptorGPSEventCallback {
    void onSensorEvent(PositionGPS positionGPS);
}
