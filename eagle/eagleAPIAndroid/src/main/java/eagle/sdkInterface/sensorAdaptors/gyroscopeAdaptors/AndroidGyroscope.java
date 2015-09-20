package eagle.sdkInterface.sensorAdaptors.gyroscopeAdaptors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import eagle.sdkInterface.sensorAdaptors.AdaptorGyroscope;

/**
 * Android Gyroscope Adaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 14/06/2015
 * <p/>
 * Date Modified	14/06/2015 - Nicholas
 */

public class AndroidGyroscope extends AdaptorGyroscope implements SensorEventListener {
    private Context context = null;
    private float[] gyroscopeData;

    public AndroidGyroscope() {
        super("Android", "Gyroscope", "0.0.1");
    }

    public boolean connectToSensor() {
        if (this.context == null)
            return false;
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_FASTEST);
            return true;
        } else
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
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null)
            return true;
        else
            return false;
    }

    @Override
    public float[] getData() {
        return gyroscopeData;
    }
    @Override
    public boolean isDataReady() {
        if (gyroscopeData==null)
            return false;
        else
            return true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroscopeData = new float[3];
            gyroscopeData[0] = event.values[0];
            gyroscopeData[1] = event.values[1];
            gyroscopeData[2] = event.values[2];
        }
    }
}
