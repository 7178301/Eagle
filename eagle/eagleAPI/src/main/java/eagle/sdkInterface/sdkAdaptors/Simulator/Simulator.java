package eagle.sdkInterface.sdkAdaptors.Simulator;

import eagle.Log;
import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.PositionDisplacement;
import eagle.navigation.positioning.PositionGPS;
import eagle.navigation.positioning.PositionMetric;
import eagle.sdkInterface.AdaptorLoader;
import eagle.sdkInterface.SDKAdaptor;
import eagle.sdkInterface.SDKAdaptorCallback;

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
        super("Simulator", "Siumulator", "alpha", "0.0.1",1,30);
        try {
            setPositionAssigned(new PositionMetric(0,0,0,new Angle(0),new Angle(0),new Angle(0)));
            setHomePosition(getPositionAssigned());
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }
    }


    public Simulator(int speed, int rotateSpeed) {
        super("Simulator", "Siumulator", "alpha", "0.0.1",speed,rotateSpeed);
    }

    @Override
    public void loadDefaultSensorAdaptors(AdaptorLoader adaptorLoader) {

        addSensorAdaptorGPS(adaptorLoader.getSensorAdaptorGPS("JavaGPS"));
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
    public boolean flyTo(SDKAdaptorCallback sdkAdaptorCallback, PositionMetric position, double speed) {
        if (!connected) {
            sdkAdaptorCallback.onResult(false, "Not Connected To The Drone");
            return false;
        }
        else {
            try {
                setPositionAssigned(position);
                sdkAdaptorCallback.onResult(true, "flyTo " + position.toString() + " SUCCESS");
                return true;
            } catch (InvalidPositionException e) {
                e.printStackTrace();
                sdkAdaptorCallback.onResult(false,"flyTo "+position.toString()+" FAIL");
                return false;
            }
        }
    }

    @Override
    public boolean flyTo(SDKAdaptorCallback sdkAdaptorCallback, PositionMetric position) {
        return flyTo(sdkAdaptorCallback, position, maxSpeed);
    }

    @Override
    public boolean flyTo(SDKAdaptorCallback sdkAdaptorCallback, PositionGPS position, double speed) {
        if (!connected) {
            sdkAdaptorCallback.onResult(false, "Not Connected To The Drone");
            return false;
        }
        else {
            try {
                setPositionAssigned(position);
                sdkAdaptorCallback.onResult(true, "flyTo " + position.toString() + " SUCCESS");
                return true;
            } catch (InvalidPositionException e) {
                e.printStackTrace();
                sdkAdaptorCallback.onResult(false, "flyTo " + position.toString() + " FAIL");
                return false;
            }
        }
    }

    @Override
    public boolean flyTo(SDKAdaptorCallback sdkAdaptorCallback, PositionGPS position) {
        return flyTo(sdkAdaptorCallback, position, maxSpeed);
    }

    @Override
    public boolean flyTo(SDKAdaptorCallback sdkAdaptorCallback, PositionDisplacement position, double speed) {
        if (!connected) {
            sdkAdaptorCallback.onResult(false, "Not Connected To The Drone");
            return false;
        }
        else {
            try {
                setPositionAssigned(getPositionAssigned().add(position));
                sdkAdaptorCallback.onResult(true, "flyTo " + position.toString() + " SUCCESS");
                return true;
            } catch (InvalidPositionException e) {
                e.printStackTrace();
                sdkAdaptorCallback.onResult(false, "flyTo " + position.toString() + " FAIL");
                return false;
            }
        }
    }

    @Override
    public boolean flyTo(SDKAdaptorCallback sdkAdaptorCallback, PositionDisplacement position) {
        return flyTo(sdkAdaptorCallback, position, maxSpeed);
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