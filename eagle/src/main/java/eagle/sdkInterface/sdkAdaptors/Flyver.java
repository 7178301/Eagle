package eagle.sdkInterface.sdkAdaptors;

import eagle.navigation.positioning.AbsolutePosition;
import eagle.navigation.positioning.RelativePosition;
import eagle.sdkInterface.Adaptor;

public class Flyver extends Adaptor {
    public static String adapterVersion;

    //TODO Create method implementations


    private String sdkVersion;
    private final String adaptorVersion="0.0.1";

    public void init(){};
    public void shutDown(){};
    public String getAdaptorVersion(){return adaptorVersion;};

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