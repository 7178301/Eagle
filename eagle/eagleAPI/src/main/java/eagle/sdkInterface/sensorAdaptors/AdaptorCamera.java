package eagle.sdkInterface.sensorAdaptors;

import java.util.HashSet;

import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorCameraLiveFeedCallback;

/**
 * Camera Adaptor Interface
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @author Cameron Cross
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */
public abstract class AdaptorCamera extends SensorAdaptor {

    protected HashSet<SensorAdaptorCameraLiveFeedCallback> sensorAdaptorCameraLiveFeedCallbacks;

    public AdaptorCamera(String adaptorManufacturer, String adaptorModel, String adaptorVersion) {
        super(adaptorManufacturer, adaptorModel, adaptorVersion);
    }

    public abstract byte[] getData();

    public abstract boolean connectToSensor();

    //TODO Following Method Need Proper Implementation
    //public abstract Object saveImage();
    // Store the photo and send back location of storage
    // OR send back image data

    public void addSensorAdaptorCameraLiveFeedallback(final SensorAdaptorCameraLiveFeedCallback sensorAdaptorCameraLiveFeedCallback) {
        if (sensorAdaptorCameraLiveFeedCallbacks==null)
            sensorAdaptorCameraLiveFeedCallbacks = new HashSet<>();
        this.sensorAdaptorCameraLiveFeedCallbacks.add(sensorAdaptorCameraLiveFeedCallback);
    }
}