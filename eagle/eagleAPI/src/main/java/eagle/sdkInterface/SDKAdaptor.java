package eagle.sdkInterface;

import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.Bearing;
import eagle.sdkInterface.sensorAdaptors.*;

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
    private String adaptorManufacturer = null;
    private String adaptorModel = null;
    private String sdkVersion = null;
    private String adaptorVersion = null;

    private Position homePosition;

    //TODO create way to set current assigned position
    private Position currentPositionAssigned;

    public SDKAdaptor(String adaptorManufacturer, String adaptorModel, String sdkVersion, String adaptorVersion){
        this.adaptorName=adaptorManufacturer+" "+adaptorModel;
        this.adaptorManufacturer=adaptorManufacturer;
        this.adaptorModel=adaptorModel;
        this.sdkVersion=sdkVersion;
        this.adaptorVersion=adaptorVersion;
        this.homePosition=new Position(0,0,0,0,0,new Bearing(0));
        this.currentPositionAssigned = new Position(0,0,0,0,0,new Bearing(0));
    }
    public abstract void loadDefaultSensorAdaptors(AdaptorLoader adaptorLoader);

    public abstract boolean connectToDrone();
    public abstract boolean disconnectFromDrone();
    public abstract boolean isConnectedToDrone();

    public abstract boolean standbyDrone();
    public abstract boolean resumeDrone();
    public abstract boolean shutdownDrone();

    public String getAdaptorVersion(){
        return adaptorVersion;
    }
    public String getSdkVersion(){
        return sdkVersion;
    }
    public String getAdaptorName(){
        return adaptorName;
    }
    public String getAdaptorManufacturer(){
        return adaptorManufacturer;
    }
    public String getAdaptorModel(){
        return adaptorModel;
    }

    public abstract boolean flyToRelative(Position position, double speed);
    public abstract boolean flyToRelative(Position position);

    public abstract boolean flyToGPS(Position position, double speed);
    public abstract boolean flyToGPS(Position position);

    public boolean changeLongitudeRelative(double longitude,double speed){
        return flyToRelative(new Position(longitude, 0, 0, 0, 0, new Bearing(0)), speed);
    }
    public boolean changeLongitudeRelative(double longitude){
        return flyToRelative(new Position(longitude, 0, 0, 0, 0, new Bearing(0)));
    }
    public boolean changeLatitudeRelative(double latitude,double speed){
        return flyToRelative(new Position(0, latitude, 0, 0, 0, new Bearing(0)), speed);
    }
    public boolean changeLatitudeRelative(double latitude){
        return flyToRelative(new Position(0, latitude, 0, 0, 0, new Bearing(0)));
    }
    public boolean changeAltitudeRelative(double altitude,double speed){
        return flyToRelative(new Position(0, 0, altitude, 0, 0, new Bearing(0)), speed);
    }
    public boolean changeAltitudeRelative(double altitude){
        return flyToRelative(new Position(0,0,altitude,0,0,new Bearing(0)));
    }
    public boolean changeYawRelative(Bearing yaw,double speed){
        return flyToRelative(new Position(0, 0, 0, 0, 0, yaw), speed);
    }
    public boolean changeYawRelative(Bearing yaw){
        return flyToRelative(new Position(0, 0, 0, 0, 0, yaw));
    }

    public boolean changeLongitudeGPS(double longitude, double speed){
        return flyToGPS(new Position(longitude, getPositionAssigned().getLatitude(), getPositionAssigned().getAltitude(), 0, 0, getPositionAssigned().getYaw()), speed);
    }
    public boolean changeLongitudeGPS(double longitude){
        return flyToGPS(new Position(longitude, getPositionAssigned().getLatitude(), getPositionAssigned().getAltitude(), 0, 0, getPositionAssigned().getYaw()));
    }
    public boolean changeLatitudeGPS(double latitude, double speed){
        return flyToGPS(new Position(getPositionAssigned().getLongitude(), latitude, getPositionAssigned().getAltitude(), 0, 0, getPositionAssigned().getYaw()), speed);
    }
    public boolean changeLatitudeGPS(double latitude){
        return flyToGPS(new Position(getPositionAssigned().getLongitude(), latitude, getPositionAssigned().getAltitude(), 0, 0, getPositionAssigned().getYaw()));
    }
    public boolean changeAltitudeGPS(double altitude, double speed){
        return flyToGPS(new Position(getPositionAssigned().getLongitude(), getPositionAssigned().getLatitude(), altitude, 0, 0, getPositionAssigned().getYaw()), speed);
    }
    public boolean changeAltitudeGPS(double altitude){
        return flyToGPS(new Position(getPositionAssigned().getLongitude(), getPositionAssigned().getLatitude(), altitude, 0, 0, getPositionAssigned().getYaw()));
    }
    public boolean changeYawGPS(Bearing yaw, double speed){
        return flyToGPS(new Position(getPositionAssigned().getLongitude(), getPositionAssigned().getLatitude(), getPositionAssigned().getAltitude(), 0, 0, yaw), speed);
    }
    public boolean changeYawGPS(Bearing yaw){
        return flyToGPS(new Position(getPositionAssigned().getLongitude(), getPositionAssigned().getLatitude(), getPositionAssigned().getAltitude(), 0, 0, yaw));
    }

    public void goHome(double speed) {
        flyToGPS(homePosition, speed);
    }
    public void goHome() {
        flyToGPS(homePosition);
    }

    public Position getPositionAssigned(){
        return currentPositionAssigned;
    }
    public void setPositionAssigned(Position pos) {
        currentPositionAssigned = pos;
    }
    public abstract Position getPositionInFlight();

    public void setHomePosition(Position position){
        homePosition=position;
    }
    public Position getHomePosition(){
        return homePosition;
    }

    public void addSensorAdaptorAccelerometer(Accelerometer accelerometer){
        if (this.accelerometer==null)
            this.accelerometer=new HashMap<>();
        this.accelerometer.put(accelerometer.getAdaptorName(),accelerometer);
    }
    public void addSensorAdaptorAltimeter(Altimeter altimeter){
        if (this.altimeter==null)
            this.altimeter=new HashMap<>();
        this.altimeter.put(altimeter.getAdaptorName(),altimeter);
    }
    public void addSensorAdaptorCamera(Camera camera){
        if (this.camera==null)
            this.camera=new HashMap<>();
        this.camera.put(camera.getAdaptorName(),camera);
    }
    public void addSensorAdaptorCompass(Compass compass){
        if (this.compass==null)
            this.compass=new HashMap<>();
        this.compass.put(compass.getAdaptorName(),compass);
    }
    public void addSensorAdaptorGyroscope(Gyroscope gyroscopes){
        if (this.gyroscopes==null)
            this.gyroscopes=new HashMap<>();
        this.gyroscopes.put(gyroscopes.getAdaptorName(),gyroscopes);
    }
    public void addSensorAdaptorLIDAR(LIDAR lidar){
        if (this.lidar==null)
            this.lidar=new HashMap<>();
        this.lidar.put(lidar.getAdaptorName(),lidar);
    }
    public void addSensorAdaptorRPLIDAR(RPLIDAR rplidar){
        if (this.rplidar==null)
            this.rplidar=new HashMap<>();
        this.rplidar.put(rplidar.getAdaptorName(),rplidar);
    }
    public void addSensorAdaptorUltrasonic(Ultrasonic ultrasonic){
        if (this.ultrasonic==null)
            this.ultrasonic=new HashMap<>();
        this.ultrasonic.put(ultrasonic.getAdaptorName(),ultrasonic);
    }

    public abstract void delay(int milliseconds);

    //TODO Add Remove Adaptor Functions

}