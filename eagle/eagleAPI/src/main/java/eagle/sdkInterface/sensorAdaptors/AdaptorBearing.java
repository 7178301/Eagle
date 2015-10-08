package eagle.sdkInterface.sensorAdaptors;

/**
 * Bearing Adaptor Interface
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 04/10/2015
 * <p/>
 * Date Modified	0410/2015 - Nicholas
 */
public abstract class AdaptorBearing extends SensorAdaptor {

    public static final int YAW = 0;
    public static final int PITCH = 1;
    public static final int ROLL = 2;

    private float[] calibrationOffset = null;

    public AdaptorBearing(String adaptorManufacturer, String adaptorModel, String adaptorVersion) {
        super(adaptorManufacturer, adaptorModel, adaptorVersion);
    }

    public abstract boolean connectToSensor();

    public abstract float[] getData();

    public float[] getCalibratedData() {
        float[] value = getData();
        if (value == null || getCalibrationOffset() == null || value.length < 3)
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
        this.calibrationOffset = calibrationOffset;
        return true;
    }

    public boolean setAndroidContext(Object object) {
        return false;
    }
}