package eagle.sdkInterface.sdkAdaptors.Flyver;

import android.content.Context;

import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.PositionDisplacement;
import eagle.navigation.positioning.PositionGPS;

import eagle.navigation.positioning.PositionMetric;
import eagle.sdkInterface.AdaptorLoader;
import eagle.sdkInterface.SDKAdaptor;
import ioio.lib.api.IOIO;

/**
 * F450Flamewheel SDKAdaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @author Cameron Cross [7193432@student.swin.edu.au]
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */
public class F450Flamewheel extends SDKAdaptor {

    public static String adapterVersion;
    private Context context;
    private IOIO controller;

    //TODO Create method implementations

    public F450Flamewheel() {
        super("Flyver", "F450 Flamewheel", "alpha", "0.0.1");
    }

    public void loadDefaultSensorAdaptors(AdaptorLoader adaptorLoader) {
        addSensorAdaptorAccelerometer(adaptorLoader.getSensorAdaptorAccelerometer("AndroidAccelerometer"));
        addSensorAdaptorCamera(adaptorLoader.getSensorAdaptorCamera("AndroidCamera"));
        addSensorAdaptorGPS(adaptorLoader.getSensorAdaptorGPS("AndroidGPS"));
        addSensorAdaptorGyroscope(adaptorLoader.getSensorAdaptorGyroscope("AndroidGyroscope"));
        addSensorAdaptorMagnetic(adaptorLoader.getSensorAdaptorMagnetic("AndroidMagnetic"));
    }

    public boolean connectToDrone() {
        if (controller!=null&&controller.getState()!=null)
            return true;
        else
            return false;
    }

    public boolean disconnectFromDrone() {
        return false;
    }

    public boolean isConnectedToDrone() {
        return false;
    }

    //TODO CREATE BELOW IMPLEMENTATION
    public Position getPositionInFlight() {
        return null;
    }

    public boolean standbyDrone() {
        return false;
    }

    public boolean resumeDrone() {
        return false;
    }

    public boolean shutdownDrone() {
        return false;
    }

    @Override
    public boolean flyTo(PositionMetric positionMetric, double speed) {
        return false;
    }

    @Override
    public boolean flyTo(PositionMetric positionMetric) {
        return false;
    }

    @Override
    public boolean flyTo(PositionGPS position, double speed) {
        return false;
    }

    @Override
    public boolean flyTo(PositionGPS position) {
        return false;
    }

    @Override
    public boolean flyTo(PositionDisplacement position, double speed) {
        return false;
    }

    @Override
    public boolean flyTo(PositionDisplacement position) {
        return false;
    }

    @Override
    public void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateCurrentPosition() {
    }

    @Override
    public boolean setAndroidContext(Object object) {
        if (object instanceof Context) {
            this.context = (Context) object;
            return true;
        } else
            return false;
    }

    @Override
    public boolean setController(Object object) {
        if (object instanceof IOIO) {
            this.controller = (IOIO) object;
            return true;
        } else
            return false;
    }
}