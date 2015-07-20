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

    public boolean changeLongitudeRelative(double longitude,double speed) {
        Position change = new Position(longitude,0,0,0,0,new Bearing(0));
        Position nextPos = getPosition().add(change);
        return flyToRelative(nextPos, speed);
    }
    public boolean changeLongitudeRelative(double longitude) {
        Position change = new Position(longitude,0,0,0,0,new Bearing(0));
        Position nextPos = getPosition().add(change);
        return flyToRelative(nextPos);
    }

    public boolean changeLatitudeRelative(double latitude,double speed) {
        Position change = new Position(0,latitude,0,0,0,new Bearing(0));
        Position nextPos = getPosition().add(change);
        return flyToRelative(nextPos, speed);
    }
    public boolean changeLatitudeRelative(double latitude) {
        Position change = new Position(0,latitude,0,0,0,new Bearing(0));
        Position nextPos = getPosition().add(change);
        return flyToRelative(nextPos);
    }
    public boolean changeAltitudeRelative(double altitude,double speed) {
        Position change = new Position(0,0,altitude,0,0,new Bearing(0));
        Position nextPos = getPosition().add(change);
        return flyToRelative(nextPos, speed);
    }
    public boolean changeAltitudeRelative(double altitude) {
        Position change = new Position(0,0,altitude,0,0,new Bearing(0));
        Position nextPos = getPosition().add(change);
        return flyToRelative(nextPos);
    }
    public boolean changeYawRelative(Bearing yaw,double speed) {
        Position change = new Position(0,0,0,0,0,yaw);
        Position nextPos = getPosition().add(change);
        return flyToRelative(nextPos, speed);
    }
    public boolean changeYawRelative(Bearing yaw) {
        Position change = new Position(0,0,0,0,0,yaw);
        Position nextPos = getPosition().add(change);
        return flyToRelative(nextPos);
    }

    public boolean changeLongitudeAbsolute(double longitude,double speed) {return false;}
    public boolean changeLongitudeAbsolute(double longitude) {return false;}
    public boolean changeLatitudeAbsolute(double latitude,double speed) {return false;}
    public boolean changeLatitudeAbsolute(double latitude) {return false;}
    public boolean changeAltitudeAbsolute(double altitude,double speed) {return false;}
    public boolean changeAltitudeAbsolute(double altitude) {return false;}
    public boolean changeYawAbsolute(Bearing yaw,double speed) {return false;}
    public boolean changeYawAbsolute(Bearing yaw) {return false;}

    public void updateCurrentPosition(){};

}