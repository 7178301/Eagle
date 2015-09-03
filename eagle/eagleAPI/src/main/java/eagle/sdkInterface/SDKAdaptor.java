package eagle.sdkInterface;

import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.Bearing;
import eagle.sdkInterface.sensorAdaptors.*;

import java.util.ArrayList;

/**
 * Abstract SDKAdaptor Class
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */
public abstract class SDKAdaptor {

    private ArrayList<AdaptorAccelerometer> accelerometers = new ArrayList<>();
    private ArrayList<AdaptorCamera> cameras = new ArrayList<>();
    private ArrayList<AdaptorGPS> gps = new ArrayList<>();
    private ArrayList<AdaptorGyroscope> gyroscopes = new ArrayList<>();
    private ArrayList<AdaptorLIDAR> lidars = new ArrayList<>();
    private ArrayList<AdaptorMagnetic> magnetics = new ArrayList<>();
    private ArrayList<AdaptorRPLIDAR> rplidars = new ArrayList<>();
    private ArrayList<AdaptorUltrasonic> ultrasonics = new ArrayList<>();

    private String adaptorName = null;
    private String adaptorManufacturer = null;
    private String adaptorModel = null;
    private String sdkVersion = null;
    private String adaptorVersion = null;

    private Position homePosition;

    //TODO create way to set current assigned position
    private Position currentPositionAssigned;

    public SDKAdaptor(String adaptorManufacturer, String adaptorModel, String sdkVersion, String adaptorVersion) {
        this.adaptorName = adaptorManufacturer + " " + adaptorModel;
        this.adaptorManufacturer = adaptorManufacturer;
        this.adaptorModel = adaptorModel;
        this.sdkVersion = sdkVersion;
        this.adaptorVersion = adaptorVersion;
        this.homePosition = new Position(0, 0, 0, 0, 0, new Bearing(0));
        this.currentPositionAssigned = new Position(0, 0, 0, 0, 0, new Bearing(0));
    }

    public abstract void loadDefaultSensorAdaptors(AdaptorLoader adaptorLoader);

    public abstract boolean connectToDrone();

    public abstract boolean disconnectFromDrone();

    public abstract boolean isConnectedToDrone();

    public abstract boolean standbyDrone();

    public abstract boolean resumeDrone();

    public abstract boolean shutdownDrone();

    public String getAdaptorVersion() {
        return adaptorVersion;
    }

    ;

    public String getSdkVersion() {
        return sdkVersion;
    }

    public String getAdaptorName() {
        return adaptorName;
    }

    public String getAdaptorManufacturer() {
        return adaptorManufacturer;
    }

    public String getAdaptorModel() {
        return adaptorModel;
    }

    public boolean flyToRelative(Position position, double speed) {
        return flyToAbsolute(new Position(getPositionInFlight().getLongitude() + position.getLongitude(), getPositionInFlight().getLatitude() + position.getLatitude(),
                getPositionInFlight().getAltitude() + position.getAltitude(), 0, 0, new Bearing(getPositionInFlight().getYaw().getDegrees() + position.getYaw().getDegrees())), speed);
    }

    public boolean flyToRelative(Position position) {
        return flyToAbsolute(new Position(getPositionInFlight().getLongitude() + position.getLongitude(), getPositionInFlight().getLatitude() + position.getLatitude(),
                getPositionInFlight().getAltitude() + position.getAltitude(), 0, 0, new Bearing(getPositionInFlight().getYaw().getDegrees() + position.getYaw().getDegrees())));
    }

    public abstract boolean flyToAbsolute(Position position, double speed);

    public abstract boolean flyToAbsolute(Position position);

    public boolean changeLongitudeRelative(double longitude, double speed) {
        return flyToRelative(new Position(longitude, 0, 0, 0, 0, new Bearing(0)), speed);
    }

    public boolean changeLongitudeRelative(double longitude) {
        return flyToRelative(new Position(longitude, 0, 0, 0, 0, new Bearing(0)));
    }

    public boolean changeLatitudeRelative(double latitude, double speed) {
        return flyToRelative(new Position(0, latitude, 0, 0, 0, new Bearing(0)), speed);
    }

    public boolean changeLatitudeRelative(double latitude) {
        return flyToRelative(new Position(0, latitude, 0, 0, 0, new Bearing(0)));
    }

    public boolean changeAltitudeRelative(double altitude, double speed) {
        return flyToRelative(new Position(0, 0, altitude, 0, 0, new Bearing(0)), speed);
    }

    public boolean changeAltitudeRelative(double altitude) {
        return flyToRelative(new Position(0, 0, altitude, 0, 0, new Bearing(0)));
    }

    public boolean changeYawRelative(Bearing yaw, double speed) {
        return flyToRelative(new Position(0, 0, 0, 0, 0, yaw), speed);
    }

    public boolean changeYawRelative(Bearing yaw) {
        return flyToRelative(new Position(0, 0, 0, 0, 0, yaw));
    }

    public boolean changeLongitudeAbsolute(double longitude, double speed) {
        return flyToAbsolute(new Position(longitude, getPositionAssigned().getLatitude(), getPositionAssigned().getAltitude(), 0, 0, getPositionAssigned().getYaw()), speed);
    }

    public boolean changeLongitudeAbsolute(double longitude) {
        return flyToAbsolute(new Position(longitude, getPositionAssigned().getLatitude(), getPositionAssigned().getAltitude(), 0, 0, getPositionAssigned().getYaw()));
    }

    public boolean changeLatitudeAbsolute(double latitude, double speed) {
        return flyToAbsolute(new Position(getPositionAssigned().getLongitude(), latitude, getPositionAssigned().getAltitude(), 0, 0, getPositionAssigned().getYaw()), speed);
    }

    public boolean changeLatitudeAbsolute(double latitude) {
        return flyToAbsolute(new Position(getPositionAssigned().getLongitude(), latitude, getPositionAssigned().getAltitude(), 0, 0, getPositionAssigned().getYaw()));
    }

    public boolean changeAltitudeAbsolute(double altitude, double speed) {
        return flyToAbsolute(new Position(getPositionAssigned().getLongitude(), getPositionAssigned().getLatitude(), altitude, 0, 0, getPositionAssigned().getYaw()), speed);
    }

    public boolean changeAltitudeAbsolute(double altitude) {
        return flyToAbsolute(new Position(getPositionAssigned().getLongitude(), getPositionAssigned().getLatitude(), altitude, 0, 0, getPositionAssigned().getYaw()));
    }

    public boolean changeYawAbsolute(Bearing yaw, double speed) {
        return flyToAbsolute(new Position(getPositionAssigned().getLongitude(), getPositionAssigned().getLatitude(), getPositionAssigned().getAltitude(), 0, 0, yaw), speed);
    }

    public boolean changeYawAbsolute(Bearing yaw) {
        return flyToAbsolute(new Position(getPositionAssigned().getLongitude(), getPositionAssigned().getLatitude(), getPositionAssigned().getAltitude(), 0, 0, yaw));
    }

    public void goHome(double speed) {
        flyToAbsolute(homePosition, speed);
    }

    public void goHome() {
        flyToAbsolute(homePosition);
    }

    public Position getPositionAssigned() {
        return currentPositionAssigned;
    }

    public void setPositionAssigned(Position pos) {
        currentPositionAssigned = pos;
    }

    public abstract Position getPositionInFlight();

    public void setHomePosition(Position position) {
        homePosition = position;
    }

    public Position getHomePosition() {
        return homePosition;
    }

    public void addSensorAdaptorAccelerometer(AdaptorAccelerometer adaptorAccelerometer) {
        this.accelerometers.add(adaptorAccelerometer);
    }

    public void addSensorAdaptorCamera(AdaptorCamera adaptorCamera) {
        this.cameras.add(adaptorCamera);
    }

    public void addSensorAdaptorGPS(AdaptorGPS adaptorGPS) {
        this.gps.add(adaptorGPS);
    }

    public void addSensorAdaptorGyroscope(AdaptorGyroscope adaptorGyroscope) {
        this.gyroscopes.add(adaptorGyroscope);
    }

    public void addSensorAdaptorLIDAR(AdaptorLIDAR lidar) {
        this.lidars.add(lidar);
    }

    public void addSensorAdaptorMagnetic(AdaptorMagnetic magnetic) {
        this.magnetics.add(magnetic);
    }

    public void addSensorAdaptorRPLIDAR(AdaptorRPLIDAR adaptorRPLIDAR) {
        this.rplidars.add(adaptorRPLIDAR);
    }

    public void addSensorAdaptorUltrasonic(AdaptorUltrasonic adaptorUltrasonic) {
        this.ultrasonics.add(adaptorUltrasonic);
    }

    public abstract void delay(int milliseconds);

    public ArrayList<AdaptorAccelerometer> getAccelerometers() {
        return accelerometers;
    }

    public ArrayList<AdaptorCamera> getCameras() {
        return cameras;
    }

    public ArrayList<AdaptorGPS> getGPSs() {
        return gps;
    }

    public ArrayList<AdaptorGyroscope> getGyroscopes() {
        return gyroscopes;
    }

    public ArrayList<AdaptorLIDAR> getLidars() {
        return lidars;
    }

    public ArrayList<AdaptorMagnetic> getMagnetics() {
        return magnetics;
    }

    public ArrayList<AdaptorRPLIDAR> getRplidars() {
        return rplidars;
    }

    public ArrayList<AdaptorUltrasonic> getUltrasonics() {
        return ultrasonics;
    }

    public Position getCurrentPositionAssigned() {
        return currentPositionAssigned;
    }
//TODO Add Remove Adaptor Functions

    public boolean setAndroidContext(Object object) {
            return false;
    }

    public boolean setController(Object object){
        return false;
    }
}