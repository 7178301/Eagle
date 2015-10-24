package eagle.sdkInterface.sensorAdaptors;

import java.util.HashSet;

import eagle.sdkInterface.SDKAdaptorCallback;
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

    //TODO Following Method Need Proper Implementation
    //public abstract Object saveImage();
    // Store the photo and send back location of storage
    // OR send back image data
    public abstract byte[] getData();

    public void takePicture(final SDKAdaptorCallback sdkAdaptorCallback){
        if(sdkAdaptorCallback!=null)
            sdkAdaptorCallback.onResult(false,"Function Not Implemented");
    }

    public void startRecord(final SDKAdaptorCallback sdkAdaptorCallback){
        if(sdkAdaptorCallback!=null)
            sdkAdaptorCallback.onResult(false,"Function Not Implemented");
    }

    public void stopRecord(final SDKAdaptorCallback sdkAdaptorCallback){
        if(sdkAdaptorCallback!=null)
            sdkAdaptorCallback.onResult(false,"Function Not Implemented");
    }

    public void updateCameraAttitude(final SDKAdaptorCallback sdkAdaptorCallback, int roll, int pitch, int yaw){
        if(sdkAdaptorCallback!=null)
            sdkAdaptorCallback.onResult(false,"Function Not Implemented");
    }

    public short[] getCameraAttitude(){
        return null;
    }

    public void addSensorAdaptorCameraLiveFeedallback(final SensorAdaptorCameraLiveFeedCallback sensorAdaptorCameraLiveFeedCallback) {
        if (sensorAdaptorCameraLiveFeedCallbacks==null)
            sensorAdaptorCameraLiveFeedCallbacks = new HashSet<>();
        this.sensorAdaptorCameraLiveFeedCallbacks.add(sensorAdaptorCameraLiveFeedCallback);
    }
}