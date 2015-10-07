package eagle.sdkInterface.sensorAdaptors.cameraAdaptors;

import dji.sdk.api.Camera.DJICameraSystemState;
import dji.sdk.api.DJIDrone;
import dji.sdk.api.Gimbal.DJIGimbalAttitude;
import dji.sdk.interfaces.DJICameraSystemStateCallBack;
import dji.sdk.interfaces.DJIGimbalUpdateAttitudeCallBack;
import eagle.sdkInterface.sensorAdaptors.AdaptorCamera;

/**
 * Created by Lardi on 07/10/2015.
 */
public class DJICamera extends AdaptorCamera implements DJICameraSystemStateCallBack, DJIGimbalUpdateAttitudeCallBack {

    private DJICameraSystemState djiCameraSystemState = null;
    private DJIGimbalAttitude djiGimbalAttitude = null;
    private DJIDrone djiDrone = null;

    public DJICamera(String adaptorManufacturer, String adaptorModel, String adaptorVersion) {
        super("DJI", "DJICamera", "0.0.1");
    }

    @Override
    public byte[] getData() {
        return null;
    }

    @Override
    public boolean connectToSensor() {
        DJIDrone.getDjiCamera().setDjiCameraSystemStateCallBack(this);
        DJIDrone.getDjiGimbal().setGimbalUpdateAttitudeCallBack(this);
        DJIDrone.getDjiCamera().startUpdateTimer(1000);
        DJIDrone.getDjiGimbal().startUpdateTimer(1000);
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
}
