package eagle.sdkInterface.sdkAdaptors.Simulator;

import eagle.Log;
import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.PositionDisplacement;
import eagle.navigation.positioning.PositionGPS;
import eagle.navigation.positioning.PositionMetric;
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
        super("Simulator", "Siumulator", "alpha", "0.0.1");
        maxSpeed = 1;
        maxRotateSpeed = 30;
        try {
            setPositionAssigned(new PositionMetric(0,0,0,new Angle(0),new Angle(0),new Angle(0)));
            setHomePosition(getPositionAssigned());
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }
    }


    public Simulator(int speed, int rotateSpeed) {
        super("Simulator", "Siumulator", "alpha", "0.0.1");
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
    public boolean flyTo(PositionMetric position, double speed) {
        if (!connected) return false;
        try {
            setPositionAssigned(position);
        } catch (InvalidPositionException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean flyTo(PositionMetric position) {
        return flyTo(position, maxSpeed);
    }

    @Override
    public boolean flyTo(PositionGPS position, double speed) {
        if (!connected) return false;
        try {
            setPositionAssigned(position);
        } catch (InvalidPositionException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean flyTo(PositionGPS position) {
        return flyTo(position, maxSpeed);
    }

    @Override
    public boolean flyTo(PositionDisplacement position, double speed) {
        if (!connected) return false;
        try {
            setPositionAssigned(getPositionAssigned().add(position));
        } catch (InvalidPositionException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean flyTo(PositionDisplacement position) {
        return flyTo(position, maxSpeed);
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