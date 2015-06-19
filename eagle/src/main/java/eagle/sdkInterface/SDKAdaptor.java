package eagle.sdkInterface;

import eagle.sdkInterface.sensorAdaptors.*;
import eagle.navigation.positioning.AbsolutePosition;
import eagle.navigation.positioning.RelativePosition;

import java.util.HashMap;

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

    public SDKAdaptor(String adaptorName, String sdkVersion, String adaptorVersion){
        this.adaptorName=adaptorName;
        this.sdkVersion=sdkVersion;
        this.adaptorVersion=adaptorVersion;
    }

    public abstract void connect();
    public abstract void disconnect();
    public abstract void shutdown();
    public String getAdaptorVersion(){
        return adaptorVersion;
    };
    public String getSdkVersion(){
        return sdkVersion;
    }
    public String getAdaptorName(){
        return adaptorName;
    }

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