package eagle.sdkInterface.sensorAdaptors.gpsAdaptors;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import eagle.navigation.positioning.Bearing;
import eagle.navigation.positioning.Position;
import eagle.sdkInterface.sensorAdaptors.AdaptorGPS;

/**
 * Android GPS Adaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 31/08/2015
 * <p/>
 * Date Modified	31/08/2015 - Nicholas
 */

public class AndroidGPS extends AdaptorGPS {
    private Context context = null;
    private Position gpsData;
    private float accuracy;

    public AndroidGPS() {
        super("Android", "GPS", "0.0.1");
    }

    public boolean connectToSensor() {
        if (this.context == null)
            return false;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
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
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER))
            return true;
        else
            return false;
    }

    public Position getData() {
        return gpsData;
    }

    @Override
    public boolean isDataReady() {
        if (gpsData == null)
            return false;
        else
            return true;
    }

    @Override
    public float getGPSAccuracy(){
        return accuracy;
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location.hasAccuracy()) {
                if (location.hasBearing()) {
                    gpsData = new Position(location.getLongitude(), location.getLatitude(), location.getAltitude(), 0, 0, new Bearing(location.getBearing()));
                    accuracy = location.getAccuracy();
                } else if (gpsData != null) {
                    gpsData = new Position(location.getLongitude(), location.getLatitude(), location.getAltitude(), 0, 0, gpsData.getYaw());
                    accuracy = location.getAccuracy();
                } else {
                    gpsData = new Position(location.getLongitude(), location.getLatitude(), location.getAltitude(), 0, 0, new Bearing(0));
                    accuracy = location.getAccuracy();
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
