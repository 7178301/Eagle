package eagle.sdkInterface.sensorAdaptors.magneticAdaptors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import eagle.sdkInterface.sensorAdaptors.AdaptorMagnetic;

/**
 * Android Magnetic Adaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 27/08/2015
 * <p/>
 * Date Modified	27/08/2015 - Nicholas
 */

public class AndroidMagnetic extends AdaptorMagnetic implements SensorEventListener {
    private Context context = null;
    private float[] magneticData;

    public AndroidMagnetic() {
        super("Android", "Magnetic", "0.0.1");
    }

    public boolean connectToSensor() {
        if (this.context == null)
            return false;
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);
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
        if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null)
            return true;
        else
            return false;
    }

    @Override
    public float[] getData() {
        return magneticData;
    }
    @Override
    public boolean isDataReady() {
        if (magneticData==null)
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
        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticData = new float[3];
            magneticData[0] = event.values[0];
            magneticData[1] = event.values[1];
            magneticData[2] = event.values[2];
        }
    }
}
