package eagle.sdkInterface.sensorAdaptors;

/**
 * Accelerometer Adaptor Interface
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */
public abstract class AdaptorAccelerometer extends SensorAdaptor {
    private float[] calibrationOffset = null;

    public AdaptorAccelerometer(String adaptorManufacturer, String adaptorModel, String adaptorVersion) {
        super(adaptorManufacturer, adaptorModel, adaptorVersion);
    }

    public abstract boolean connectToSensor();

    public abstract float[] getData();

    public float[] getCalibratedData() {
        float[] value = getData();
        if (value == null | getCalibrationOffset() == null | value.length < 3)
            return null;
        else {
            float[] calibratedData = new float[3];
            calibratedData[0] = value[0] - getCalibrationOffset()[0];
            calibratedData[1] = value[1] - getCalibrationOffset()[1];
            calibratedData[2] = value[2] - getCalibrationOffset()[2];
            return calibratedData;
        }
    }

    public float[] getCalibrationOffset() {
        return calibrationOffset;
    }

    public boolean setCalibrationOffset(float[] calibrationOffset) {
        if (calibrationOffset != null && calibrationOffset.length == 3) {
            this.calibrationOffset = calibrationOffset;
            return true;
        } else
            return false;
    }

    public boolean setAndroidContext(Object object) {
        return false;
    }
}