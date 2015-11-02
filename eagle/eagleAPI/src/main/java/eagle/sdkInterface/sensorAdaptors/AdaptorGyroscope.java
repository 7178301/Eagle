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

    public static final int AXISX = 0;
    public static final int AXISY = 1;
    public static final int AXISZ = 2;


    private float[] calibrationOffset = null;

    public AdaptorGyroscope(String adaptorManufacturer, String adaptorModel, String adaptorVersion) {
        super(adaptorManufacturer, adaptorModel, adaptorVersion);
    }

    public abstract float[] getData();

    public float[] getCalibratedData() {
        float[] value = getData();
        if (value == null || getCalibrationOffset() == null || value.length < 3)
            return null;
        else {
            float[] calibratedData = new float[3];
            calibratedData[AXISX] = value[AXISX] - getCalibrationOffset()[AXISX];
            calibratedData[AXISY] = value[AXISY] - getCalibrationOffset()[AXISY];
            calibratedData[AXISZ] = value[AXISZ] - getCalibrationOffset()[AXISZ];
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
        sb.append(data[AXISX]);
        sb.append(" Y-axis: ");
        sb.append(data[AXISY]);
        sb.append(" Z-axis: ");
        sb.append(data[AXISZ]);
        return sb.toString();
    }

}