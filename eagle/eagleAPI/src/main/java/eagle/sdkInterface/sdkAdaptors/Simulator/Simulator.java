package eagle.sdkInterface.sdkAdaptors.Simulator;

import eagle.Log;
import eagle.navigation.positioning.Position;
import eagle.sdkInterface.AdaptorLoader;
import eagle.sdkInterface.SDKAdaptor;

/** Simulator Class
 * @since     09/04/2015
 * <p>
 * Date Modified	01/08/2015 - Cameron Cross
 * @version 0.0.1
 * @author          Cameron Cross [7193432@student.swin.edu.au] */
public class Simulator extends SDKAdaptor {
    private boolean connected = false;
    private int maxSpeed = 0;
    private int maxRotateSpeed = 0;

    public Simulator() {
        super("Simulator","Siumulator","alpha","0.0.1");
        maxSpeed = 1;
        maxRotateSpeed = 30;
    }


    public Simulator(int speed, int rotateSpeed) {
        super("Simulator","Siumulator","alpha","0.0.1");
        maxSpeed = speed;
        maxRotateSpeed = rotateSpeed;
    }

    @Override
    public void loadDefaultSensorAdaptors(AdaptorLoader adaptorLoader) {

    }

    @Override
    public boolean connectToDrone() {
        connected = true;
        Log.log("Connected to Drone");
        return connected;
    }

    @Override
    public boolean disconnectFromDrone() {
        connected = false;
        Log.log("Disconnected to Drone");
        return true;
    }

    @Override
    public boolean isConnectedToDrone() {
        return connected;
    }

    @Override
    public boolean standbyDrone() {
        return false;
    }

    @Override
    public boolean resumeDrone() {
        return false;
    }

    @Override
    public boolean shutdownDrone() {
        return false;
    }

    @Override
    public boolean flyToRelative(Position position, double speed) {
        Position endPos = new Position(getPositionAssigned());
        endPos.add(position);

        double verticalDist = position.getAltitude();
        double longDist = position.getLongitude();
        double latDist = position.getLatitude();

        double maxDist;

        if (Math.abs(verticalDist) > Math.abs(longDist) && Math.abs(verticalDist) > Math.abs(latDist)) {
            maxDist = Math.abs(verticalDist);
        } else if (Math.abs(longDist) > Math.abs(verticalDist) && Math.abs(longDist) > Math.abs(latDist)) {
            maxDist = Math.abs(longDist);
        } else {
            maxDist = Math.abs(latDist);
        }

        verticalDist /= maxDist;
        longDist /= maxDist;
        latDist /= maxDist;

        while (!endPos.isEqual(getPositionAssigned())) {
            double altitude = getPositionAssigned().getAltitude();
            double longitude = getPositionAssigned().getLongitude();
            double latitude = getPositionAssigned().getLatitude();

            if (altitude - position.getAltitude() > verticalDist) {
                altitude += verticalDist*speed;
            } else {
                altitude = endPos.getAltitude();
            }
            if (longitude - position.getLongitude() > longDist) {
                longitude += longDist*speed;
            } else {
                longitude = endPos.getLongitude();
            }
            if (latitude - position.getLatitude() > latDist) {
                latitude += latDist*speed;
            } else {
                latitude = endPos.getLatitude();
            }

            Position newPos = new Position(longitude, latitude, altitude, position.getRoll(), position.getPitch(), position.getYaw());
            setPositionAssigned(newPos);
            delay(1000);

            Log.log("Current Position: "+ newPos.toString());
        }
        return true;
    }

    @Override
    public boolean flyToRelative(Position position) {
        return flyToRelative(position, maxSpeed);
    }

    @Override
    public boolean flyToGPS(Position position, double speed) {
        return false;
    }

    @Override
    public boolean flyToGPS(Position position) {
        return flyToGPS(position, maxSpeed);
    }

    @Override
    public Position getPositionInFlight() {
        return getPositionAssigned();
    }

    @Override
    public void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}