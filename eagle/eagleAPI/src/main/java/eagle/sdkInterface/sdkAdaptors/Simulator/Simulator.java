package eagle.sdkInterface.sdkAdaptors.Simulator;

import eagle.Log;
import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.PositionMetricDisplacement;
import eagle.navigation.positioning.PositionGPS;
import eagle.navigation.positioning.PositionMetric;
import eagle.sdkInterface.AdaptorLoader;
import eagle.sdkInterface.SDKAdaptor;
import eagle.sdkInterface.SDKAdaptorCallback;

/**
 * Simulator Class
 *
 * @author Cameron Cross [7193432@student.swin.edu.au]
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	01/08/2015 - Cameron Cross
 */
public class Simulator extends SDKAdaptor {
    private boolean connected = false;

    public Simulator() {
        super("Simulator", "Siumulator", "alpha", "0.0.1", 1, 2, 2, 30);
        try {
            setPositionAssigned(new PositionMetric(0, 0, 0, new Angle(0), new Angle(0), new Angle(0)));
            setHomePosition(getPositionAssigned());
        } catch (InvalidPositionTypeException e) {
            e.printStackTrace();
        }
    }


    public Simulator(int speed, int rotateSpeed) {
        super("Simulator", "Siumulator", "alpha", "0.0.1", speed, 2, 2, rotateSpeed);
    }

    @Override
    public void loadDefaultSensorAdaptors(AdaptorLoader adaptorLoader) {
        addSensorAdaptorGPS(adaptorLoader.getSensorAdaptorGPS("JavaGPS"));
    }

    @Override
    public boolean connectToDrone() {
        connected = true;
        Log.log("Simulator", "Connected to Drone");
        return connected;
    }

    @Override
    public boolean disconnectFromDrone() {
        connected = false;
        Log.log("Simulator", "Disconnected to Drone");
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
    public void flyTo(SDKAdaptorCallback sdkAdaptorCallback, PositionMetric position, double speed) {
        if (sdkAdaptorCallback == null || position == null)
            throw new IllegalArgumentException("Arguments must not be null");
        else {
            if (!connected) {
                sdkAdaptorCallback.onResult(false, "Not Connected To The Drone");
            } else {
                try {
                    setPositionAssigned(position);
                    Log.log("Simulator", "flyToMetric " + position.toString() + " SUCCESS");
                    sdkAdaptorCallback.onResult(true, "flyTo " + position.toString() + " SUCCESS");
                } catch (InvalidPositionTypeException e) {
                    Log.log("Simulator", "flyToMetric " + position.toString() + " FAIL " + e.getMessage());
                    sdkAdaptorCallback.onResult(false, "flyTo " + position.toString() + " FAIL");
                }
            }
        }
    }

    @Override
    public void flyTo(SDKAdaptorCallback sdkAdaptorCallback, PositionMetric position) {
        if (sdkAdaptorCallback == null || position == null)
            throw new IllegalArgumentException("Arguments must not be null");
        else
            flyTo(sdkAdaptorCallback, position, getMaxFlightSpeed());
    }

    @Override
    public void flyTo(SDKAdaptorCallback sdkAdaptorCallback, PositionGPS positionGPS, double speed) {
        if (sdkAdaptorCallback == null || positionGPS == null)
            throw new IllegalArgumentException("Arguments must not be null");
        else {
            if (!connected) {
                sdkAdaptorCallback.onResult(false, "Not Connected To The Drone");
            } else {
                try {
                    setPositionAssigned(positionGPS);
                    Log.log("Simulator", "flyToGPS " + positionGPS.toString() + " SUCCESS");
                    sdkAdaptorCallback.onResult(true, "flyTo " + positionGPS.toString() + " SUCCESS");
                } catch (InvalidPositionTypeException e) {
                    Log.log("Simulator", "flyToGPS " + positionGPS.toString() + " FAIL " + e.getMessage());
                    sdkAdaptorCallback.onResult(false, "flyTo " + positionGPS.toString() + " FAIL");
                }
            }
        }
    }

    @Override
    public void flyTo(SDKAdaptorCallback sdkAdaptorCallback, PositionGPS positionGPS) {
        if (sdkAdaptorCallback == null || positionGPS == null)
            throw new IllegalArgumentException("Arguments must not be null");
        else
            flyTo(sdkAdaptorCallback, positionGPS, getMaxFlightSpeed());
    }

    @Override
    public void flyTo(SDKAdaptorCallback sdkAdaptorCallback, PositionMetricDisplacement positionMetricDisplacement, double speed) {
        if (sdkAdaptorCallback == null || positionMetricDisplacement == null)
            throw new IllegalArgumentException("Arguments must not be null");
        else {
            if (!connected) {
                sdkAdaptorCallback.onResult(false, "Not Connected To The Drone");
            } else {
                try {
                    setPositionAssigned(getPositionAssigned().add(positionMetricDisplacement));
                    Log.log("Simulator", "flyToDisplacement " + positionMetricDisplacement.toString() + " SUCCESS");
                    sdkAdaptorCallback.onResult(true, "flyTo " + positionMetricDisplacement.toString() + " SUCCESS");
                } catch (InvalidPositionTypeException e) {
                    Log.log("Simulator", "flyToDisplacement " + positionMetricDisplacement.toString() + " FAIL " + e.getMessage());
                    sdkAdaptorCallback.onResult(false, "flyTo " + positionMetricDisplacement.toString() + " FAIL");
                }
            }
        }
    }

    @Override
    public void flyTo(SDKAdaptorCallback sdkAdaptorCallback, PositionMetricDisplacement positionMetricDisplacement) {
        if (sdkAdaptorCallback == null || positionMetricDisplacement == null)
            throw new IllegalArgumentException("Arguments must not be null");
        else
            flyTo(sdkAdaptorCallback, positionMetricDisplacement, getMaxFlightSpeed());
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