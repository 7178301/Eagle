package eagle.sdkInterface.sensorAdaptors.bearingAdaptors;

import eagle.sdkInterface.sensorAdaptors.AdaptorAccelerometer;
import eagle.sdkInterface.sensorAdaptors.AdaptorBearing;
import eagle.sdkInterface.sensorAdaptors.gpsAdaptors.DJIGPS;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorCallback;

/**
 * DJI Bearing Adaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */

public class DJIBearing extends AdaptorBearing {
    private DJIGPS controller = null;
    private SensorAdaptorCallback DJIGPSCallback = null;

    public DJIBearing() {
        super("DJI", "Accelerometer", "0.0.1");
    }

    @Override
    public boolean connectToSensor() {
        if (controller != null && !controller.isConnectedToSensor()) {
            if (controller.connectToSensor()) {
                controller.addSensorAdaptorCallback(DJIGPSCallback);
                return true;
            }
        } else if (controller != null) {
            controller.addSensorAdaptorCallback(DJIGPSCallback);
            return true;
        }
        return false;
    }

    //TODO Following Method Need Proper Implementation
    @Override
    public boolean disconnectFromSensor() {
        return false;
    }


    @Override
    public boolean setController(Object object) {
        if (object instanceof DJIGPS) {
            this.controller = (DJIGPS) object;
            DJIGPSCallback = new SensorAdaptorCallback() {
                @Override
                public void onSensorChanged() {
                    for (SensorAdaptorCallback sensorAdaptorCallback : sensorAdaptorCallbacks)
                        sensorAdaptorCallback.onSensorChanged();
                }
            };
            return true;
        } else
            return false;
    }

    @Override
    public boolean isConnectedToSensor() {
        return controller != null;
    }

    @Override
    public float[] getData() {
        if (controller.isDataReady()) {
            float[] tempFloat = new float[]{Double.valueOf(controller.getData().getRoll().getDegrees()).floatValue(), Double.valueOf(controller.getData().getPitch().getDegrees()).floatValue(), Double.valueOf(controller.getData().getYaw().getDegrees()).floatValue()};
            return tempFloat;
        } else
            return null;
    }

    @Override
    public boolean isDataReady() {
        return controller.isDataReady();
    }
}
