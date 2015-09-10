package eagle.sdkInterface.sensorAdaptors.gpsAdaptors;

import org.iu.gps.GPSDriver;
import org.iu.gps.GPSInfo;
import org.iu.gps.GPSListener;

import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.PositionGPS;
import eagle.sdkInterface.sensorAdaptors.AdaptorGPS;

/**
 * eagle.sdkInterface.sensorAdaptors.gpsAdaptors
 *
 * @author cameron
 * @version 0.0.1
 * @since 9/9/15
 * <p/>
 */
public class JavaGPS extends AdaptorGPS implements GPSListener {
    PositionGPS currentPos = null;
    GPSDriver gpsDriver = new GPSDriver();
    public JavaGPS() {
        super("JavaGPS", "NMEA_GPS", "0.0.1");
    }

    @Override
    public boolean connectToSensor() {
        gpsDriver.addGPSListener(this);
        gpsDriver.enableHardware();
        return gpsDriver.isAlive();
    }

    @Override
    public PositionGPS getData() {
        return currentPos;
    }

    @Override
    public boolean isConnectedToSensor() {
        return gpsDriver.isAlive();
    }

    @Override
    public boolean isDataReady() {
        return !(currentPos == null);
    }

    @Override
    public void gpsEvent(GPSInfo gpsInfo) {
        currentPos = new PositionGPS(gpsInfo.longitude, gpsInfo.latitude, gpsInfo.geoidalHeight, new Angle(0), new Angle(0), new Angle(0));
    }
}
