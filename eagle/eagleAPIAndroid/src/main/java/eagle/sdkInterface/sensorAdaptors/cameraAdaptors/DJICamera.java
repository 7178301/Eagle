package eagle.sdkInterface.sensorAdaptors.cameraAdaptors;

import dji.sdk.api.Camera.DJICameraSettingsTypeDef;
import dji.sdk.api.Camera.DJICameraSystemState;
import dji.sdk.api.Camera.DJICameraTypeDef;
import dji.sdk.api.DJIDrone;
import dji.sdk.api.DJIDroneTypeDef;
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
    private DJICameraSettingsTypeDef.CameraVisionType cameraVisionType = null;

    public DJICamera() {
        super("DJI", "Camera", "0.0.1");
    }

    @Override
    public byte[] getData() {
        return null;
    }

    @Override
    public boolean connectToSensor() {
        Log.log("DJICamera", "Checking Drone Connection Status");
        if (DJIDrone.getDjiCamera() == null || DJIDrone.getDjiGimbal() == null)
            return false;
        DJIDrone.getDjiCamera().stopUpdateTimer();
        Log.log("DJICamera", "Starting  DJI Camera Polling Interval");
        if (!DJIDrone.getDjiCamera().startUpdateTimer(1000))
            return false;
        DJIDrone.getDjiGimbal().stopUpdateTimer();
        Log.log("DJICamera", "Starting  DJI Gimble Polling Interval");
        if (!DJIDrone.getDjiGimbal().startUpdateTimer(1000))
            return false;
        DJIDrone.getDjiCamera().setDjiCameraSystemStateCallBack(this);
        DJIDrone.getDjiGimbal().setGimbalUpdateAttitudeCallBack(this);
        DJIDrone.getDjiCamera().setReceivedVideoDataCallBack(this);
        if (DJIDrone.getDjiCamera().getCameraConnectIsOk()) {
            while (DJIDrone.getDjiCamera().getCameraVersion().length() == 0) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            attitudeMaxPitch = DJIDrone.getDjiGimbal().getGimbalPitchMaxAngle();
            attitudeMinPitch = DJIDrone.getDjiGimbal().getGimbalPitchMinAngle();
            if (DJIDrone.getDjiCamera().getCameraVersion().contains("g")) {
                cameraVisionType = DJICameraSettingsTypeDef.CameraVisionType.Camera_Type_Plus;
                attitudeMaxSpeed = 900;
            } else
                cameraVisionType = DJICameraSettingsTypeDef.CameraVisionType.Camera_Type_Vision;
            return true;
        } else
            return false;
    }

    @Override
    public boolean disconnectFromSensor() {
        if (DJIDrone.getDjiCamera() != null) {
            Log.log("DJICamera", "Stopping  DJI Camera Polling Interval");
            DJIDrone.getDjiCamera().stopUpdateTimer();
            DJIDrone.getDjiCamera().setReceivedVideoDataCallBack(null);
            DJIDrone.getDjiCamera().setDjiCameraSystemStateCallBack(null);
            djiCameraSystemState = null;
        } else
            return false;
        if (DJIDrone.getDjiGimbal() != null) {
            Log.log("DJICamera", "Stopping  DJI Gimble Polling Interval");
            DJIDrone.getDjiGimbal().stopUpdateTimer();
            DJIDrone.getDjiGimbal().setGimbalUpdateAttitudeCallBack(null);
            djiGimbalAttitude = null;
        } else
            return false;
        return true;
    }

    @Override
    public boolean isConnectedToSensor() {
        return DJIDrone.getDjiCamera() != null && DJIDrone.getDjiCamera().getCameraConnectIsOk() && djiGimbalAttitude != null && djiCameraSystemState != null;
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
    public void startRecord(final SDKAdaptorCallback sdkAdaptorCallback) {
        if (isConnectedToSensor())
            DJIDrone.getDjiCamera().startRecord(new DJIExecuteResultCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (sdkAdaptorCallback != null && djiError.errorCode == DJIError.RESULT_OK) {
                        Log.log("DJICamera", "Start Record SUCCESS");
                        sdkAdaptorCallback.onResult(true, djiError.errorDescription);
                    } else if (sdkAdaptorCallback != null) {
                        Log.log("DJICamera", "Start Record FAIL " + djiError.errorDescription);
                        sdkAdaptorCallback.onResult(false, "Start Record FAIL " + djiError.errorDescription);
                    }
                }
            });
        else if (sdkAdaptorCallback != null) {
            Log.log("DJICamera", "Start Record FAIL - Not Connected To The Camera");
            sdkAdaptorCallback.onResult(false, "Start Record FAIL - Not Connected To The Camera");
        }
    }

    @Override
    public void stopRecord(final SDKAdaptorCallback sdkAdaptorCallback) {
        if (isConnectedToSensor())
            DJIDrone.getDjiCamera().stopRecord(new DJIExecuteResultCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (sdkAdaptorCallback != null && djiError.errorCode == DJIError.RESULT_OK) {
                        Log.log("DJICamera", "Stop Record SUCCESS");
                        sdkAdaptorCallback.onResult(true, djiError.errorDescription);
                    } else if (sdkAdaptorCallback != null) {
                        Log.log("DJICamera", "Stop Record FAIL " + djiError.errorDescription);
                        sdkAdaptorCallback.onResult(false, "Stop Record FAIL " + djiError.errorDescription);
                    }
                }
            });
        else if (sdkAdaptorCallback != null) {
            Log.log("DJICamera", "Stop Record FAIL - Not Connected To The Camera");
            sdkAdaptorCallback.onResult(false, "Stop Record FAIL - Not Connected To The Camera");
        }
    }

    @Override
    public void updateCameraAttitude(final SDKAdaptorCallback sdkAdaptorCallback, final int roll, final int pitch, final int yaw) {
        updateCameraAttitude(sdkAdaptorCallback, roll, pitch, yaw, attitudeMaxSpeed);
    }

    @Override
    public void updateCameraAttitude(final SDKAdaptorCallback sdkAdaptorCallback, final int roll, final int pitch, final int yaw, final int speed) {
        if (isConnectedToSensor()) {
            DJIGimbalRotation djiGimbalRotationRoll = null;
            DJIGimbalRotation djiGimbalRotationPitch = null;
            DJIGimbalRotation djiGimbalRotationYaw = null;
            if (cameraVisionType == DJICameraSettingsTypeDef.CameraVisionType.Camera_Type_Vision) {
                Log.log("DJICamera", "Camera Type Vision");
                if (pitch != 0) {
                    if (pitch > attitudeMaxPitch)
                        djiGimbalRotationPitch = new DJIGimbalRotation(true, true, true, attitudeMaxPitch);
                    else if (pitch < attitudeMinPitch)
                        djiGimbalRotationPitch = new DJIGimbalRotation(true, true, true, attitudeMinPitch);
                    else
                        djiGimbalRotationPitch = new DJIGimbalRotation(true, true, true, pitch);
                }
                DJIDrone.getDjiGimbal().updateGimbalAttitude(djiGimbalRotationPitch, djiGimbalRotationRoll, djiGimbalRotationYaw);
                if (sdkAdaptorCallback != null && djiGimbalAttitude != null) {
                    Log.log("DJICamera", "Update Camera Attitude SUCCESS " + getCameraAttitude()[0] + "," + getCameraAttitude()[1] + "," + getCameraAttitude()[2]);
                    sdkAdaptorCallback.onResult(true, "Update Camera Attitude SUCCESS " + getCameraAttitude()[0] + "," + getCameraAttitude()[1] + "," + getCameraAttitude()[2]);
                } else if (sdkAdaptorCallback != null) {
                    Log.log("DJICamera", "Update Camera Attitude FAIL - No Feedback From Gimbal");
                    sdkAdaptorCallback.onResult(false, "Update Camera Attitude FAIL - No Feedback From Gimbal");
                }
            } else if (cameraVisionType == DJICameraSettingsTypeDef.CameraVisionType.Camera_Type_Plus) {
                Log.log("DJICamera", "Camera Type Plus");
                final DJIGimbalRotation djiGimbalRotationStop = new DJIGimbalRotation(true, false, false, 0);
                final int pitchDifference;
                if (pitch > attitudeMaxPitch)
                    pitchDifference = (int) djiGimbalAttitude.pitch - attitudeMaxPitch;
                else if (pitch < attitudeMinPitch)
                    pitchDifference = (int) djiGimbalAttitude.pitch - attitudeMinPitch;
                else
                    pitchDifference = (int) djiGimbalAttitude.pitch - pitch;
                if (pitch != 0 && pitchDifference > 0)
                    djiGimbalRotationPitch = new DJIGimbalRotation(true, true, false, speed);
                else if (pitch != 0)
                    djiGimbalRotationPitch = new DJIGimbalRotation(true, false, false, speed);
                DJIDrone.getDjiGimbal().updateGimbalAttitude(djiGimbalRotationPitch, djiGimbalRotationRoll, djiGimbalRotationYaw);
                if (djiGimbalRotationPitch != null) {
                    final DJIGimbalRotation finalDjiGimbalRotationPitch = djiGimbalRotationPitch;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (finalDjiGimbalRotationPitch.direction)
                                    Thread.sleep(Math.abs(pitchDifference - 16));
                                else
                                    Thread.sleep(Math.abs(pitchDifference + 20));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            DJIDrone.getDjiGimbal().updateGimbalAttitude(djiGimbalRotationStop, djiGimbalRotationStop, djiGimbalRotationStop);
                            Log.log("DJICamera", "Update Camera Attitude SUCCESS " + getCameraAttitude()[0] + "," + getCameraAttitude()[1] + "," + getCameraAttitude()[2]);
                            sdkAdaptorCallback.onResult(true, "Update Camera Attitude SUCCESS " + getCameraAttitude()[0] + "," + getCameraAttitude()[1] + "," + getCameraAttitude()[2]);
                        }
                    }).start();
                }
            }
        } else if (sdkAdaptorCallback != null)

        {
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
        if (sensorAdaptorCameraLiveFeedCallbacks != null) {
            for (SensorAdaptorCameraLiveFeedCallback sensorAdaptorCameraLiveFeedCallback : sensorAdaptorCameraLiveFeedCallbacks)
                sensorAdaptorCameraLiveFeedCallback.onSensorEvent(videoBuffer, size);
        }
    }
}
