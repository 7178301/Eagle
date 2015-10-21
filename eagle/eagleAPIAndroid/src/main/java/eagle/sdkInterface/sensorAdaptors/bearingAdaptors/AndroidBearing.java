package eagle.sdkInterface.sensorAdaptors.bearingAdaptors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import eagle.sdkInterface.sensorAdaptors.AdaptorBearing;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorCallback;

/**
 * Android Magnetic Adaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 27/08/2015
 * <p/>
 * Date Modified	27/08/2015 - Nicholas
 */

public class AndroidBearing extends AdaptorBearing implements SensorEventListener, SensorAdaptorCallback {
    private Context context = null;
    private float[] bearingData = null;

    private float[] magneticData = null;
    private float[] accelerometerData = null;

    public AndroidBearing() {
        super("Android", "Bearing", "0.0.1");
    }

    public boolean connectToSensor() {
        if (this.context == null)
            return false;
        if(adaptorAccelerometer!=null&&adaptorMagnetic!=null) {
            adaptorMagnetic.addSensorAdaptorCallback(this);
            adaptorAccelerometer.addSensorAdaptorCallback(this);
            return true;
        }
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null && sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
            return true;
        } else
            return false;
    }

    //TODO Following Method Need Proper Implementation
    @Override
    public boolean disconnectFromSensor() {
        return false;
    }


    public boolean setAndroidContext(Object object) {
        if (object instanceof Context) {
            this.context = (Context) object;
            return true;
        } else
            return false;
    }

    public boolean isConnectedToSensor() {
        if (context == null)
            return false;
        if(adaptorAccelerometer!=null&&adaptorMagnetic!=null)
            return true;
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null && sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
            return true;
        else
            return false;
    }

    @Override
    public float[] getData() {
        return bearingData;
    }

    @Override
    public boolean isDataReady() {
        return bearingData!=null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticData = new float[3];
            magneticData[0] = event.values[0];
            magneticData[1] = event.values[1];
            magneticData[2] = event.values[2];
        } else if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerData = new float[3];
            accelerometerData[0] = event.values[0];
            accelerometerData[1] = event.values[1];
            accelerometerData[2] = event.values[2];
        }
        if (magneticData != null && accelerometerData != null) {

            bearingData = new float[3];
            float R[] = new float[9];
            float I[] = new float[9];

            if (SensorManager.getRotationMatrix(R, I, accelerometerData, magneticData)) {

                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                for (int i = 0; i < 3; i++) {
                    bearingData[i] = orientation[i] * 360 / (2 * (float) Math.PI);
                }
                if (bearingData[0] < 0)
                    bearingData[0] = 180 + (180 - Math.abs(bearingData[0]));
            }
            for (SensorAdaptorCallback currentSensorAdaptorCallback : sensorAdaptorCallback)
                currentSensorAdaptorCallback.onSensorChanged();

        }
    }

    @Override
    public void onSensorChanged(){
        magneticData=adaptorMagnetic.getCalibratedData();
        accelerometerData=adaptorAccelerometer.getCalibratedData();
        if(magneticData==null)
            magneticData=adaptorMagnetic.getData();
        if(accelerometerData==null)
            accelerometerData=adaptorAccelerometer.getData();
        if(accelerometerData!=null&&magneticData!=null){
            bearingData = new float[3];
            float R[] = new float[9];
            float I[] = new float[9];
            if (SensorManager.getRotationMatrix(R, I, accelerometerData, magneticData)) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                for (int i = 0; i < 3; i++) {
                    bearingData[i] = orientation[i] * 360 / (2 * (float) Math.PI);
                }
                if (bearingData[0] < 0)
                    bearingData[0] = 180 + (180 - Math.abs(bearingData[0]));
            }
            for (SensorAdaptorCallback currentSensorAdaptorCallback : sensorAdaptorCallback)
                currentSensorAdaptorCallback.onSensorChanged();
        }
    }
}