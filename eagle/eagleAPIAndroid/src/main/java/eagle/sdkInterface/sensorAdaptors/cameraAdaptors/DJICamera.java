package eagle.sdkInterface.sensorAdaptors.cameraAdaptors;

import dji.sdk.api.Camera.DJICameraSystemState;
import dji.sdk.api.DJIDrone;
import dji.sdk.api.Gimbal.DJIGimbalAttitude;
import dji.sdk.interfaces.DJICameraSystemStateCallBack;
import dji.sdk.interfaces.DJIGimbalUpdateAttitudeCallBack;
import dji.sdk.interfaces.DJIReceivedVideoDataCallBack;
import eagle.sdkInterface.sensorAdaptors.AdaptorCamera;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorCameraLiveFeedCallback;

/**
 * Created by Lardi on 07/10/2015.
 */
public class DJICamera extends AdaptorCamera implements DJICameraSystemStateCallBack, DJIGimbalUpdateAttitudeCallBack, DJIReceivedVideoDataCallBack {

    private DJICameraSystemState djiCameraSystemState = null;
    private DJIGimbalAttitude djiGimbalAttitude = null;

    public DJICamera() {
        super("DJI", "DJICamera", "0.0.1");
    }

    @Override
    public byte[] getData() {
        return null;
    }

    @Override
    public boolean connectToSensor() {
        if(DJIDrone.getLevel()<1)
            return false;
        if(!DJIDrone.getDjiCamera().startUpdateTimer(1000))
            return false;
        if(!DJIDrone.getDjiGimbal().startUpdateTimer(1000))
            return false;
        DJIDrone.getDjiCamera().setDjiCameraSystemStateCallBack(this);
        DJIDrone.getDjiGimbal().setGimbalUpdateAttitudeCallBack(this);
        DJIDrone.getDjiCamera().setReceivedVideoDataCallBack(this);
        return DJIDrone.getDjiCamera().getCameraConnectIsOk();
    }

    @Override
    public boolean isConnectedToSensor() {
        return DJIDrone.getDjiCamera().getCameraConnectIsOk();
    }

    @Override
    public boolean isDataReady() {
        return djiCameraSystemState == null;
    }

    @Override
    public void onResult(DJICameraSystemState djiCameraSystemState) {
        this.djiCameraSystemState = djiCameraSystemState;
    }

    @Override
    public void onResult(DJIGimbalAttitude djiGimbalAttitude) {
        this.djiGimbalAttitude = djiGimbalAttitude;
    }

    @Override
    public void onResult(byte[] videoBuffer, int size) {
        for (SensorAdaptorCameraLiveFeedCallback sensorAdaptorCameraLiveFeedCallback : sensorAdaptorCameraLiveFeedCallbacks)
            sensorAdaptorCameraLiveFeedCallback.onSensorEvent(videoBuffer, size);
    }
}
