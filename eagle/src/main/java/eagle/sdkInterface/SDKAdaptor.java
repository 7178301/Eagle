package eagle.sdkInterface;

import java.util.HashMap;

import eagle.navigation.positioning.Bearing;
import eagle.navigation.positioning.Position;
import eagle.sdkInterface.sensorAdaptors.Accelerometer;
import eagle.sdkInterface.sensorAdaptors.Altimeter;
import eagle.sdkInterface.sensorAdaptors.Camera;
import eagle.sdkInterface.sensorAdaptors.Compass;
import eagle.sdkInterface.sensorAdaptors.Gyroscope;
import eagle.sdkInterface.sensorAdaptors.LIDAR;
import eagle.sdkInterface.sensorAdaptors.RPLIDAR;
import eagle.sdkInterface.sensorAdaptors.Ultrasonic;

/** Abstract SDKAdaptor Class
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public abstract class SDKAdaptor {

    private HashMap<String,Accelerometer> accelerometer = null;
    private HashMap<String,Altimeter> altimeter = null;
    private HashMap<String,Camera> camera = null;
    private HashMap<String,Compass> compass = null;
    private HashMap<String,Gyroscope> gyroscopes = null;
    private HashMap<String,LIDAR> lidar = null;
    private HashMap<String,RPLIDAR> rplidar = null;
    private HashMap<String,Ultrasonic> ultrasonic = null;

    private String adaptorName = null;
    private String sdkVersion = null;
    private String adaptorVersion = null;

    private Position homePosition;
    private Position currentPosition;

    public SDKAdaptor(String adaptorName, String sdkVersion, String adaptorVersion){
        this.adaptorName=adaptorName;
        this.sdkVersion=sdkVersion;
        this.adaptorVersion=adaptorVersion;
        this.homePosition=new Position(0,0,0,0,0,new Bearing(0));
        this.currentPosition=new Position(0,0,0,0,0,new Bearing(0));
    }

    public abstract boolean connectToDrone();
    public abstract boolean disconnectFromDrone();
    public abstract boolean isConnectedToDrone();

    public abstract boolean standbyDrone();
    public abstract boolean resumeDrone();
    public abstract boolean shutdownDrone();


    public String getAdaptorVersion(){
        return adaptorVersion;
    };
    public String getSdkVersion(){
        return sdkVersion;
    }
    public String getAdaptorName(){
        return adaptorName;
    }

    public abstract boolean flyToRelative(Position position, double speed);
    public abstract boolean flyToRelative(Position position);

    public Boolean flyToAbsolute(Position position, double speed){
        return null;
    }
    public Boolean flyToAbsolute(Position position){
        return null;
    }

    public abstract boolean changeLongitudeRelative(double longitude,double speed);
    public abstract boolean changeLongitudeRelative(double longitude);
    public abstract boolean changeLatitudeRelative(double latitude,double speed);
    public abstract boolean changeLatitudeRelative(double latitude);
    public abstract boolean changeAltitudeRelative(double altitude,double speed);
    public abstract boolean changeAltitudeRelative(double altitude);
    public abstract boolean changeYawRelative(Bearing yaw,double speed);
    public abstract boolean changeYawRelative(Bearing yaw);

    public abstract boolean changeLongitudeAbsolute(double longitude,double speed);
    public abstract boolean changeLongitudeAbsolute(double longitude);
    public abstract boolean changeLatitudeAbsolute(double latitude,double speed);
    public abstract boolean changeLatitudeAbsolute(double latitude);
    public abstract boolean changeAltitudeAbsolute(double altitude,double speed);
    public abstract boolean changeAltitudeAbsolute(double altitude);
    public abstract boolean changeYawAbsolute(Bearing yaw,double speed);
    public abstract boolean changeYawAbsolute(Bearing yaw);

    public void goHome(double speed) {
        flyToAbsolute(homePosition, speed);
    }
    public void goHome() {
        flyToAbsolute(homePosition);
    }

    public abstract void updateCurrentPosition();
    public Position getRelativePositionToHome(){
        updateCurrentPosition();
        return currentPosition.minus(homePosition);
    }
    public Position getPosition(){
        updateCurrentPosition();
        return currentPosition;
    }

    public void setHomePosition(Position position){
        homePosition=position;
    }
    public Position getHomePosition(){
        return homePosition;
    }
}