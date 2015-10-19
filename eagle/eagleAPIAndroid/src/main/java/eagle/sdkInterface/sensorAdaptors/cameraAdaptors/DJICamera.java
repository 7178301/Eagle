package eagle.sdkInterface.sensorAdaptors.cameraAdaptors;

import dji.sdk.api.Camera.DJICameraSystemState;
import dji.sdk.api.DJIDrone;
import dji.sdk.api.DJIError;
import dji.sdk.api.Gimbal.DJIGimbalAttitude;
import dji.sdk.api.Gimbal.DJIGimbalRotation;
import dji.sdk.interfaces.DJICameraSystemStateCallBack;
import dji.sdk.interfaces.DJIExecuteResultCallback;
import dji.sdk.interfaces.DJIGimbalUpdateAttitudeCallBack;
import dji.sdk.interfaces.DJIReceivedVideoDataCallBack;
import eagle.Log;
import eagle.sdkInterface.SDKAdaptorCallback;
import eagle.sdkInterface.sensorAdaptors.AdaptorCamera;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorCameraLiveFeedCallback;

/**
 * DJI Camera Adaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 12/10/2015
 * <p/>
 * Date Modified	14/10/2015 - Nicholas
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
        if (DJIDrone.getLevel() < 1)
            return false;
        if (!DJIDrone.getDjiCamera().startUpdateTimer(1000))
            return false;
        if (!DJIDrone.getDjiGimbal().startUpdateTimer(1000))
            return false;
        DJIDrone.getDjiCamera().setDjiCameraSystemStateCallBack(this);
        DJIDrone.getDjiGimbal().setGimbalUpdateAttitudeCallBack(this);
        DJIDrone.getDjiCamera().setReceivedVideoDataCallBack(this);
        return DJIDrone.getDjiCamera().getCameraConnectIsOk();
    }

    @Override
    public boolean disconnectFromSensor() {
        return false;
    }

    @Override
    public boolean isConnectedToSensor() {
        return DJIDrone.getDjiCamera()!=null&&DJIDrone.getDjiCamera().getCameraConnectIsOk();
    }

    @Override
    public boolean isDataReady() {
        return djiCameraSystemState != null;
    }

    @Override
    public void takePicture(final SDKAdaptorCallback sdkAdaptorCallback) {
        if (isConnectedToSensor())
            DJIDrone.getDjiCamera().startTakePhoto(new DJIExecuteResultCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (sdkAdaptorCallback != null && djiError.errorCode == DJIError.RESULT_OK) {
                        Log.log("DJICamera", "Take Picture SUCCESS");
                        sdkAdaptorCallback.onResult(true, djiError.errorDescription);
                    } else if (sdkAdaptorCallback != null) {
                        Log.log("DJICamera", "Take Picture FAIL " + djiError.errorDescription);
                        sdkAdaptorCallback.onResult(false, "Take Picture FAIL " + djiError.errorDescription);
                    }
                }
            });
        else if (sdkAdaptorCallback != null) {
            Log.log("DJICamera", "Update Camera Attitude FAIL - Not Connected To The Camera");
            sdkAdaptorCallback.onResult(false, "Update Camera Attitude FAIL - Not Connected To The Camera");
        }
    }

    @Override
    public void startTakeVideo(final SDKAdaptorCallback sdkAdaptorCallback) {
        if (sdkAdaptorCallback != null)
            sdkAdaptorCallback.onResult(false, "Function Not Implemented");
    }

    @Override
    public void stopTakeVideo(final SDKAdaptorCallback sdkAdaptorCallback) {
        if (sdkAdaptorCallback != null)
            sdkAdaptorCallback.onResult(false, "Function Not Implemented");
    }

    @Override
    public void updateCameraAttitude(final SDKAdaptorCallback sdkAdaptorCallback, final int roll, final int pitch, final int yaw) {
        if (isConnectedToSensor()) {
            DJIGimbalRotation djiGimbalRotationRoll;
            DJIGimbalRotation djiGimbalRotationPitch;
            DJIGimbalRotation djiGimbalRotationYaw;
            if (roll != 0)
                djiGimbalRotationRoll = new DJIGimbalRotation(true, true, true, roll);
            else
                djiGimbalRotationRoll = new DJIGimbalRotation(false, false, false, 0);
            if (pitch != 0)
                djiGimbalRotationPitch = new DJIGimbalRotation(true, true, true, pitch);
            else
                djiGimbalRotationPitch = new DJIGimbalRotation(false, false, false, 0);
            if (yaw != 0)
                djiGimbalRotationYaw = new DJIGimbalRotation(true, true, true, yaw);
            else
                djiGimbalRotationYaw = new DJIGimbalRotation(false, false, false, 0);
            DJIDrone.getDjiGimbal().updateGimbalAttitude(djiGimbalRotationPitch, djiGimbalRotationRoll, djiGimbalRotationYaw);
            if (sdkAdaptorCallback != null && djiGimbalAttitude != null) {
                final boolean[] result = {false};
                Thread gimbleUpdateThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean rollComplete = false, pitchComplete = false, yawComplete = false;
                        if (0 == roll)
                            rollComplete = true;
                        if (0 == pitch)
                            pitchComplete = true;
                        if (0 == yaw)
                            yawComplete = true;
                        while (!rollComplete && !pitchComplete && !yawComplete) {
                            if (Double.valueOf(djiGimbalAttitude.roll).shortValue() == roll)
                                rollComplete = true;
                            if (Double.valueOf(djiGimbalAttitude.pitch).shortValue() == pitch)
                                pitchComplete = true;
                            if (Double.valueOf(djiGimbalAttitude.yaw).shortValue() == yaw)
                                yawComplete = true;
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        result[0] = true;
                        Log.log("DJICamera", "Update Camera Attitude SUCCESS");
                        sdkAdaptorCallback.onResult(true, "Update Camera Attitude SUCCESS");
                    }
                });
                gimbleUpdateThread.start();
                try {
                    gimbleUpdateThread.join(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!result[0]) {
                    Log.log("DJICamera", "Update Camera Attitude FAIL - TIMED OUT");
                    sdkAdaptorCallback.onResult(false, "Update Camera Attitude FAIL - TIMED OUT");
                }
            } else if (sdkAdaptorCallback != null) {
                Log.log("DJICamera", "Update Camera Attitude FAIL - No Feedback From Camera");
                sdkAdaptorCallback.onResult(false, "Update Camera Attitude FAIL - No Feedback From Camera");
            }
        } else if (sdkAdaptorCallback != null) {
            Log.log("DJICamera", "Update Camera Attitude FAIL - Not Connected To The Camera");
            sdkAdaptorCallback.onResult(false, "Update Camera Attitude FAIL - Not Connected To The Camera");
        }

    }

    @Override
    public short[] getCameraAttitude() {
        if (this.djiGimbalAttitude == null)
            return null;
        else {
            short[] tempAttitude = new short[3];
            tempAttitude[0] = Double.valueOf(djiGimbalAttitude.roll).shortValue();
            tempAttitude[1] = Double.valueOf(djiGimbalAttitude.pitch).shortValue();
            tempAttitude[2] = Double.valueOf(djiGimbalAttitude.yaw).shortValue();
            return tempAttitude;
        }
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
