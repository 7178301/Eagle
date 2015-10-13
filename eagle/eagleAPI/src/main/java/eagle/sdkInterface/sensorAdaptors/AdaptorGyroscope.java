package eagle.sdkInterface.sensorAdaptors;

/**
 * Gyroscope Adaptor Interface
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */
public abstract class AdaptorGyroscope extends SensorAdaptor {
    private float[] calibrationOffset = null;

    public AdaptorGyroscope(String adaptorManufacturer, String adaptorModel, String adaptorVersion) {
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

    public boolean setAndroidContext(Object object) {
        return false;
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

    @Override
    public String toString() {
        boolean uncalibrated = false;
        float data[] = getCalibratedData();
        if (data == null) {
            uncalibrated = true;
            data = getData();
            if (data == null) {
                return "No Data Available";
            }
        }

        StringBuilder sb = new StringBuilder();
        if (uncalibrated) {
            sb.append("Uncalibrarted Data: ");
        } else {
            sb.append("Calibrated Data: ");
        }
        sb.append("X-axis: ");
        sb.append(data[0]);
        sb.append(" Y-axis: ");
        sb.append(data[1]);
        sb.append(" Z-axis: ");
        sb.append(data[2]);
        return sb.toString();
    }

}