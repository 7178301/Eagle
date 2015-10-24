package eagle.sdkInterface.sensorAdaptors.gpsAdaptors;

import dji.sdk.api.DJIDrone;
import dji.sdk.api.GroundStation.DJIGroundStationFlyingInfo;
import dji.sdk.api.GroundStation.DJIGroundStationTypeDef;
import dji.sdk.interfaces.DJIGroundStationFlyingInfoCallBack;
import eagle.Log;
import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.PositionGPS;
import eagle.sdkInterface.sensorAdaptors.AdaptorGPS;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorCallback;

/**
 * DJI GPS Adaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 31/08/2015
 * <p/>
 * Date Modified	31/08/2015 - Nicholas
 */

public class DJIGPS extends AdaptorGPS implements DJIGroundStationFlyingInfoCallBack {

    private PositionGPS gpsData = null;
    private float accuracy = 999999;
    private DJIGroundStationFlyingInfo djiGroundStationFlyingInfo = null;

    public DJIGPS() {
        super("DJI", "GPS", "0.0.1");
    }

    public boolean connectToSensor() {
        Log.log("DJIGPS", "Checking DJI Application Key Permissions");
        if (DJIDrone.getLevel() < 1)
            return false;
        Log.log("DJIGPS", "Registering DJI GroundStation Polling Interval");
        DJIDrone.getDjiGroundStation().setGroundStationFlyingInfoCallBack(this);
        DJIDrone.getDjiGroundStation().stopUpdateTimer();
        if (!DJIDrone.getDjiGroundStation().startUpdateTimer(100)) {
            Log.log("DJIGPS", "Registering DJI GroundStation  Polling Interval FAIL");
            return false;
        }
        return true;
    }

    //TODO Following Method Need Proper Implementation
    @Override
    public boolean disconnectFromSensor() {
        Log.log("DJIGPS", "Stopping  DJI GroundStation Polling Interval");
        DJIDrone.getDjiGroundStation().stopUpdateTimer();
        DJIDrone.getDjiGroundStation().setGroundStationFlyingInfoCallBack(null);
        djiGroundStationFlyingInfo = null;
        return true;
    }

    public boolean isConnectedToSensor() {
        return DJIDrone.getDjiGroundStation() != null && djiGroundStationFlyingInfo != null;
    }

    public PositionGPS getData() {
        return gpsData;
    }

    @Override
    public boolean isDataReady() {
        if (gpsData == null || accuracy == 0)
            return false;
        else
            return true;
    }

    @Override
    public float getGPSAccuracy() {
        return accuracy;
    }

    @Override
    public void onResult(DJIGroundStationFlyingInfo djiGroundStationFlyingInfo) {
        this.djiGroundStationFlyingInfo = djiGroundStationFlyingInfo;
        if (djiGroundStationFlyingInfo != null) {
            switch (djiGroundStationFlyingInfo.gpsStatus) {
                case GS_GPS_Excellent:
                    accuracy = 3;
                    break;
                case GS_GPS_Good:
                    accuracy = 2;
                    break;
                case GS_GPS_Weak:
                    accuracy = 1;
                    break;
                case GS_GPS_Unknown:
                    accuracy = 0;
                    break;
            }
            if (djiGroundStationFlyingInfo.gpsStatus != DJIGroundStationTypeDef.GroundStationGpsStatus.GS_GPS_Unknown) {
                double roll360, pitch360, yaw360;
                if (djiGroundStationFlyingInfo.roll >= 0)
                    roll360 = Float.valueOf(djiGroundStationFlyingInfo.roll).doubleValue();
                else
                    roll360 = Float.valueOf(360 - Math.abs(djiGroundStationFlyingInfo.roll)).doubleValue();
                if (djiGroundStationFlyingInfo.pitch >= 0)
                    pitch360 = Float.valueOf(djiGroundStationFlyingInfo.pitch).doubleValue();
                else
                    pitch360 = Float.valueOf(360 - Math.abs(djiGroundStationFlyingInfo.pitch)).doubleValue();
                if (djiGroundStationFlyingInfo.yaw >= 0)
                    yaw360 = Float.valueOf(djiGroundStationFlyingInfo.yaw).doubleValue();
                else
                    yaw360 = Float.valueOf(360 - Math.abs(djiGroundStationFlyingInfo.yaw)).doubleValue();

                gpsData = new PositionGPS(djiGroundStationFlyingInfo.phantomLocationLatitude,
                        djiGroundStationFlyingInfo.phantomLocationLongitude,
                        Float.valueOf(djiGroundStationFlyingInfo.altitude).doubleValue(),
                        new Angle(roll360),
                        new Angle(pitch360),
                        new Angle(yaw360));
            }
            for (SensorAdaptorCallback sensorAdaptorCallback : sensorAdaptorCallbacks) {
                sensorAdaptorCallback.onSensorChanged();
            }
        }
    }
}
