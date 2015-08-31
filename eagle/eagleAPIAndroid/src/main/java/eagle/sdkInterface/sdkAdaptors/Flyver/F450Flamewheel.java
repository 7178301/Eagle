package eagle.sdkInterface.sdkAdaptors.Flyver;

import eagle.navigation.positioning.Bearing;
import eagle.navigation.positioning.Position;
import eagle.sdkInterface.AdaptorLoader;
import eagle.sdkInterface.SDKAdaptor;

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

    //TODO Create method implementations

    public F450Flamewheel() {
        super("FLyver", "F450 Flamewheel", "alpha", "0.0.1");
    }

    public void loadDefaultSensorAdaptors(AdaptorLoader adaptorLoader) {
        addSensorAdaptorAccelerometer(adaptorLoader.getSensorAdaptorAccelerometer("AndroidAccelerometer"));
        addSensorAdaptorCamera(adaptorLoader.getSensorAdaptorCamera("AndroidCamera"));
        addSensorAdaptorGyroscope(adaptorLoader.getSensorAdaptorGyroscope("AndroidGyroscope"));
        addSensorAdaptorMagnetic(adaptorLoader.getSensorAdaptorMagnetic("AndroidMagnetic"));
    }

    public boolean connectToDrone() {
        return false;
    }

    public boolean disconnectFromDrone() {
        return false;
    }

    public boolean isConnectedToDrone() {
        return false;
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

    public boolean flyToAbsolute(Position position, double speed) {
        return false;
    }

    public boolean flyToAbsolute(Position position) {
        return false;
    }

    public Position getPositionInFlight() {
        //TODO CREATE BELOW IMPLEMENTATION
        return new Position(0, 0, 0, 0, 0, new Bearing(0));
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

    ;


}