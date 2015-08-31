package eagle.sdkInterface.sensorAdaptors;

import eagle.navigation.positioning.Position;

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
    private Position calibrationOffset = null;

    public AdaptorGPS(String adaptorManufacturer, String adaptorModel, String adaptorVersion) {
        super(adaptorManufacturer, adaptorModel, adaptorVersion);
    }

    public abstract boolean connectToSensor();

    public abstract Position getData();

    public Position getCalibratedData() {
        Position value = getData();
        if (value == null | getCalibrationOffset() == null)
            return null;
        else {
            Position calibratedData = new Position(
                    value.getLongitude() - getCalibrationOffset().getLongitude(),
                    value.getLatitude() - getCalibrationOffset().getLatitude(),
                    value.getAltitude() - getCalibrationOffset().getAltitude(), 0, 0,
                    value.getYaw().minus(getCalibrationOffset().getYaw())
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

    public Position getCalibrationOffset() {
        return calibrationOffset;
    }

    public boolean setCalibrationOffset(Position calibrationOffset) {
        if (calibrationOffset != null) {
            this.calibrationOffset = calibrationOffset;
            return true;
        } else
            return false;
    }
}