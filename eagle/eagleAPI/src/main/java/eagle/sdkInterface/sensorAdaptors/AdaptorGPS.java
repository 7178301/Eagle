package eagle.sdkInterface.sensorAdaptors;

import eagle.navigation.positioning.PositionGPS;

/**
 * Accelerometer Adaptor Interface
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */
public abstract class AdaptorGPS extends SensorAdaptor {
    private PositionGPS calibrationOffset = null;

    public AdaptorGPS(String adaptorManufacturer, String adaptorModel, String adaptorVersion) {
        super(adaptorManufacturer, adaptorModel, adaptorVersion);
    }

    public abstract boolean connectToSensor();

    public abstract PositionGPS getData();

    public PositionGPS getCalibratedData() {
        PositionGPS value = getData();
        if (value == null | calibrationOffset == null)
            return null;
        else {
            PositionGPS calibratedData = new PositionGPS(
                    value.getLongitude() - calibrationOffset.getLongitude(),
                    value.getLatitude() - calibrationOffset.getLatitude(),
                    value.getAltitude() - calibrationOffset.getAltitude(),
                    value.getRoll().minus(calibrationOffset.getRoll()),
                    value.getPitch().minus(calibrationOffset.getPitch()),
                    value.getYaw().minus(calibrationOffset.getYaw())
            );
            return calibratedData;
        }
    }

    public float getGPSAccuracy(){
        return 99999999;
    }

    public boolean setAndroidContext(Object object) {
        return false;
    }

    public PositionGPS getCalibrationOffset() {
        return calibrationOffset;
    }

    public boolean setCalibrationOffset(PositionGPS calibrationOffset) {
        if (calibrationOffset != null) {
            this.calibrationOffset = calibrationOffset;
            return true;
        } else
            return false;
    }
}