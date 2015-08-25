package eagle.sdkInterface.sdkAdaptors.Simulator;

import eagle.navigation.positioning.Position;
import eagle.sdkInterface.AdaptorLoader;
import eagle.sdkInterface.SDKAdaptor;

/**
 * Created by cameron on 8/13/15.
 */
public class Simulator extends SDKAdaptor {
    private boolean connected = false;
    private int maxSpeed = 0;
    private int maxRotateSpeed = 0;

    public Simulator() {
        super("Simulator","alpha","0.0.1");
        maxSpeed = 1;
        maxRotateSpeed = 30;
    }


    public Simulator(int speed, int rotateSpeed) {
        super("Simulator","alpha","0.0.1");
        maxSpeed = speed;
        maxRotateSpeed = rotateSpeed;
    }

    @Override
    public void loadDefaultAdaptors(AdaptorLoader adaptorLoader) {

    }

    @Override
    public boolean connectToDrone() {
        connected = true;
        return connected;
    }

    @Override
    public boolean disconnectFromDrone() {
        connected = false;
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
    public boolean flyToAbsolute(Position position, double speed) {

        double verticalDist = getPositionAssigned().getAltitude()-position.getAltitude();
        double longDist = getPositionAssigned().getLongitude()-position.getLongitude();
        double latDist = getPositionAssigned().getLatitude()-position.getLatitude();

        double maxDist;

        if (verticalDist > longDist && verticalDist > latDist) {
            maxDist = verticalDist;
        } else if (longDist > verticalDist && longDist > latDist) {
            maxDist = longDist;
        } else {
            maxDist = latDist;
        }

        verticalDist /= maxDist;
        longDist /= maxDist;
        latDist /= maxDist;




        while (!position.isEqual(getPositionAssigned())) {
            double altitude = getPositionAssigned().getAltitude();
            double longitude = getPositionAssigned().getLongitude();
            double latitude = getPositionAssigned().getLatitude();

            if (altitude - position.getAltitude() > verticalDist) {
                altitude += verticalDist*speed;
            } else {
                altitude = position.getAltitude();
            }
            if (longitude - position.getLongitude() > longDist) {
                longitude += longDist*speed;
            } else {
                longitude = position.getLongitude();
            }
            if (latitude - position.getLatitude() > latDist) {
                latitude += latDist*speed;
            } else {
                latitude = position.getLatitude();
            }

            Position newPos = new Position(longitude, latitude, altitude, position.getRoll(), position.getPitch(), position.getYaw());
            setPositionAssigned(newPos);
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {

            }
        }
        return true;
    }

    @Override
    public boolean flyToAbsolute(Position position) {
        return flyToAbsolute(position, maxSpeed);
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