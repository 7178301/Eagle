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
    private float calibrationOffset = 999999;

    public AdaptorBearing(String adaptorManufacturer, String adaptorModel, String adaptorVersion) {
        super(adaptorManufacturer, adaptorModel, adaptorVersion);
    }

    public abstract float getData();

    public double getCalibratedData() {
        if (getCalibrationOffset() == 999999)
            return 999999;
        else {
            return getData() - getCalibrationOffset();
        }
    }

    public float getCalibrationOffset() {
        return calibrationOffset;
    }

    public boolean setCalibrationOffset(float calibrationOffset) {
        this.calibrationOffset = calibrationOffset;
        return true;
    }
}