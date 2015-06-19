package eagle.sdkInterface.sdkAdaptors;

import eagle.sdkInterface.SDKAdaptor;
import eagle.navigation.positioning.AbsolutePosition;
import eagle.navigation.positioning.RelativePosition;

/** Flyver SDKAdaptor
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public class Flyver extends SDKAdaptor {
    public static String adapterVersion;

    //TODO Create method implementations

    public Flyver(){
        super("Flyver-SDK","alpha","0.0.1");
    }

    public void connect(){};
    public void disconnect(){};
    public void shutdown(){};

    public boolean flyToRelative(RelativePosition position, double speed){return false;};
    public boolean flyToRelative(RelativePosition position){return false;};

    public boolean flyToAbsolute(AbsolutePosition position, double speed){return false;};
    public boolean flyToAbsolute(AbsolutePosition position){return false;};

    public boolean changeLongitudeRelative(double altitude,double speed){return false;};
    public boolean changeLongitudeRelative(double altitude){return false;};
    public boolean changeLatitudeRelative(double altitude,double speed){return false;};
    public boolean changeLatitudeRelative(double altitude){return false;};
    public boolean changeAltitudeRelative(double altitude,double speed){return false;};
    public boolean changeAltitudeRelative(double altitude){return false;};

    public boolean changeLongitudeAbsolute(double altitude,double speed){return false;};
    public boolean changeLongitudeAbsolute(double altitude){return false;};
    public boolean changeLatitudeAbsolute(double altitude,double speed){return false;};
    public boolean changeLatitudeAbsolute(double altitude){return false;};
    public boolean changeAltitudeAbsolute(double altitude,double speed){return false;};
    public boolean changeAltitudeAbsolute(double altitude){return false;};

    public boolean changeYaw(double yaw){return false;};

    public double getLongitude(){return 0;};
    public double getLatitude(){return 0;};
    public double getAltitude(){return 0;};
    public double getRoll(){return 0;};
    public double getPitch(){return 0;};
    public double getYaw(){return 0;};

    //Drones that have a ready flag should return it status otherwise always true.
    public boolean getFlagReady(){return false;};
}