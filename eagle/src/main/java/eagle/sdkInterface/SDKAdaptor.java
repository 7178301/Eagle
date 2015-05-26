package eagle.sdkInterface;

import eagle.navigation.positioning.AbsolutePosition;
import eagle.navigation.positioning.RelativePosition;
/** Abstract SDKAdaptor Class
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public abstract class SDKAdaptor {


    private String sdkVersion;
    private String adaptorVersion;

    public abstract void init();
    public abstract void shutDown();
    public abstract String getAdaptorVersion();

    public abstract boolean flyToRelative(RelativePosition position, double speed);
    public abstract boolean flyToRelative(RelativePosition position);

    public abstract boolean flyToAbsolute(AbsolutePosition position, double speed);
    public abstract boolean flyToAbsolute(AbsolutePosition position);

    public abstract boolean changeLongitudeRelative(double altitude,double speed);
    public abstract boolean changeLongitudeRelative(double altitude);
    public abstract boolean changeLatitudeRelative(double altitude,double speed);
    public abstract boolean changeLatitudeRelative(double altitude);
    public abstract boolean changeAltitudeRelative(double altitude,double speed);
    public abstract boolean changeAltitudeRelative(double altitude);

    public abstract boolean changeLongitudeAbsolute(double altitude,double speed);
    public abstract boolean changeLongitudeAbsolute(double altitude);
    public abstract boolean changeLatitudeAbsolute(double altitude,double speed);
    public abstract boolean changeLatitudeAbsolute(double altitude);
    public abstract boolean changeAltitudeAbsolute(double altitude,double speed);
    public abstract boolean changeAltitudeAbsolute(double altitude);

    public abstract boolean changeYaw(double yaw);

    public abstract double getLongitude();
    public abstract double getLatitude();
    public abstract double getAltitude();
    public abstract double getRoll();
    public abstract double getPitch();
    public abstract double getYaw();

    //Drones that have a ready flag should return it status otherwise always true.
    public abstract boolean getFlagReady();

}