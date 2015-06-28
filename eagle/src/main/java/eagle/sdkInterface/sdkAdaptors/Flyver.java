package eagle.sdkInterface.sdkAdaptors;

import eagle.navigation.positioning.Bearing;
import eagle.navigation.positioning.Position;
import eagle.sdkInterface.SDKAdaptor;

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

    public boolean connectToDrone(){return false;}
    public boolean disconnectFromDrone(){return false;}
    public boolean isConnectedToDrone(){return false;}

    public boolean standbyDrone(){return false;}
    public boolean resumeDrone(){return false;}
    public boolean shutdownDrone(){return false;}

    public boolean flyToRelative(Position position, double speed){return false;}
    public boolean flyToRelative(Position position){return false;}

    public boolean changeLongitudeRelative(double altitude,double speed){return false;}
    public boolean changeLongitudeRelative(double altitude){return false;}
    public boolean changeLatitudeRelative(double altitude,double speed){return false;}
    public boolean changeLatitudeRelative(double altitude){return false;}
    public boolean changeAltitudeRelative(double altitude,double speed){return false;}
    public boolean changeAltitudeRelative(double altitude){return false;}
    public boolean changeYawRelative(Bearing yaw,double speed){return false;}
    public boolean changeYawRelative(Bearing yaw){return false;}

    public boolean changeLongitudeAbsolute(double altitude,double speed){return false;}
    public boolean changeLongitudeAbsolute(double altitude){return false;}
    public boolean changeLatitudeAbsolute(double altitude,double speed){return false;}
    public boolean changeLatitudeAbsolute(double altitude){return false;}
    public boolean changeAltitudeAbsolute(double altitude,double speed){return false;}
    public boolean changeAltitudeAbsolute(double altitude){return false;}
    public boolean changeYawAbsolute(Bearing yaw,double speed){return false;}
    public boolean changeYawAbsolute(Bearing yaw){return false;}

    public void updateCurrentPosition(){};

}