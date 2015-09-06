package eagle.sdkInterface;

import eagle.navigation.positioning.PositionMetric;
import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.PositionGPS;
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

    private PositionMetric homePosition;

    //TODO create way to set current assigned position
    private PositionMetric currentPositionAssigned;

    public SDKAdaptor(String adaptorManufacturer, String adaptorModel, String sdkVersion, String adaptorVersion) {
        this.adaptorName = adaptorManufacturer + " " + adaptorModel;
        this.adaptorManufacturer = adaptorManufacturer;
        this.adaptorModel = adaptorModel;
        this.sdkVersion = sdkVersion;
        this.adaptorVersion = adaptorVersion;
        this.homePosition = new PositionMetric(0, 0, 0, new Angle(0), new Angle(0), new Angle(0));
        this.currentPositionAssigned = new PositionMetric(0, 0, 0, new Angle(0), new Angle(0), new Angle(0));
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

    public abstract boolean flyToRelative(PositionMetric position, double speed);

    public abstract boolean flyToRelative(PositionMetric position);

    public abstract boolean flyToGPS(PositionGPS position, double speed);

    public abstract boolean flyToGPS(PositionGPS position);

    public boolean changeLongitudeRelative(double longitude, double speed) {
        PositionMetric prePosition = new PositionMetric(getPositionAssigned());
        if (flyToRelative(new PositionMetric(longitude, 0, 0, new Angle(0), new Angle(0), new Angle(0)), speed) && !getPositionAssigned().equals(prePosition))
            return true;
        else
            return false;
    }

    public boolean changeLongitudeRelative(double longitude) {
        PositionMetric prePosition = new PositionMetric(getPositionAssigned());
        if (flyToRelative(new PositionMetric(longitude, 0, 0, new Angle(0), new Angle(0), new Angle(0))) && !getPositionAssigned().equals(prePosition))
            return true;
        else
            return false;
    }

    public boolean changeLatitudeRelative(double latitude, double speed) {
        PositionMetric prePosition = new PositionMetric(getPositionAssigned());
        if (flyToRelative(new PositionMetric(0, latitude, 0, new Angle(0), new Angle(0), new Angle(0)), speed) && !getPositionAssigned().equals(prePosition))
            return true;
        else
            return false;
    }

    public boolean changeLatitudeRelative(double latitude) {
        PositionMetric prePosition = new PositionMetric(getPositionAssigned());
        if (flyToRelative(new PositionMetric(0, latitude, 0, new Angle(0), new Angle(0), new Angle(0))) && !getPositionAssigned().equals(prePosition))
            return true;
        else
            return false;
    }

    public boolean changeAltitudeRelative(double altitude, double speed) {
        PositionMetric prePosition = new PositionMetric(getPositionAssigned());
        if (flyToRelative(new PositionMetric(0, 0, altitude, new Angle(0), new Angle(0), new Angle(0)), speed) && !getPositionAssigned().equals(prePosition))
            return true;
        else
            return false;
    }

    public boolean changeAltitudeRelative(double altitude) {
        PositionMetric prePosition = new PositionMetric(getPositionAssigned());
        if (flyToRelative(new PositionMetric(0, 0, altitude, new Angle(0), new Angle(0), new Angle(0))) && !getPositionAssigned().equals(prePosition))
            return true;
        else
            return false;
    }

    public boolean changeYawRelative(Angle yaw, double speed) {
        PositionMetric prePosition = new PositionMetric(getPositionAssigned());
        if (flyToRelative(new PositionMetric(0, 0, 0, new Angle(0), new Angle(0), yaw), speed) && !getPositionAssigned().equals(prePosition))
            return true;
        else
            return false;
    }

    public boolean changeYawRelative(Angle yaw) {
        PositionMetric prePosition = new PositionMetric(getPositionAssigned());
        if (flyToRelative(new PositionMetric(0, 0, 0, new Angle(0), new Angle(0), yaw)) && !getPositionAssigned().equals(prePosition))
            return true;
        else
            return false;
    }

    public boolean changeLongitudeGPS(double longitude, double speed) {
        this.currentPositionAssigned = new PositionGPS(longitude, getPositionAssigned().getLatitude(), getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), getPositionAssigned().getYaw());
        return flyToGPS((PositionGPS) this.currentPositionAssigned, speed);
    }

    public boolean changeLongitudeGPS(double longitude) {
        this.currentPositionAssigned = new PositionGPS(longitude, getPositionAssigned().getLatitude(), getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), getPositionAssigned().getYaw());
        return flyToGPS((PositionGPS) this.currentPositionAssigned);
    }

    public boolean changeLatitudeGPS(double latitude, double speed) {
        this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLongitude(), latitude, getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), getPositionAssigned().getYaw());
        return flyToGPS((PositionGPS) this.currentPositionAssigned, speed);
    }

    public boolean changeLatitudeGPS(double latitude) {
        this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLongitude(), latitude, getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), getPositionAssigned().getYaw());
        return flyToGPS((PositionGPS) this.currentPositionAssigned);
    }

    public boolean changeAltitudeGPS(double altitude, double speed) {
        this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLongitude(), getPositionAssigned().getLatitude(), altitude, new Angle(0), new Angle(0), getPositionAssigned().getYaw());
        return flyToGPS((PositionGPS) this.currentPositionAssigned, speed);
    }

    public boolean changeAltitudeGPS(double altitude) {
        this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLongitude(), getPositionAssigned().getLatitude(), altitude, new Angle(0), new Angle(0), getPositionAssigned().getYaw());
        return flyToGPS((PositionGPS) this.currentPositionAssigned);
    }

    public boolean changeYawGPS(Angle yaw, double speed) {
        this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLongitude(), getPositionAssigned().getLatitude(), getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), yaw);
        return flyToGPS((PositionGPS) this.currentPositionAssigned, speed);
    }

    public boolean changeYawGPS(Angle yaw) {
        this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLongitude(), getPositionAssigned().getLatitude(), getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), yaw);
        return flyToGPS((PositionGPS) this.currentPositionAssigned);
    }

    public boolean goHome(double speed) {
        if (homePosition instanceof PositionMetric)
            return flyToRelative(currentPositionAssigned.compare(homePosition), speed);
        else if (homePosition instanceof PositionGPS && currentPositionAssigned instanceof PositionGPS)
            return flyToGPS((PositionGPS) currentPositionAssigned.compare(homePosition), speed);
        else
            return false;
    }

    public boolean goHome() {
        if (homePosition instanceof PositionMetric) {
            PositionMetric prePosition = new PositionMetric(getPositionAssigned());
            if (flyToRelative(currentPositionAssigned.compare(homePosition)) && !getPositionAssigned().equals(prePosition))
                return true;
            else
                return false;
        } else if (homePosition instanceof PositionGPS && currentPositionAssigned instanceof PositionGPS) {
            this.currentPositionAssigned = new PositionGPS((PositionGPS) homePosition);
            return flyToGPS(((PositionGPS) currentPositionAssigned).compare((PositionGPS) homePosition));
        } else
            return false;
    }

    public PositionMetric getPositionAssigned() {
        return currentPositionAssigned;
    }

    public void setPositionAssigned(PositionMetric pos) {
        currentPositionAssigned = pos;
    }

    public abstract PositionMetric getPositionInFlight();

    public void setHomePosition(PositionMetric position) {
        homePosition = position;
    }

    public PositionMetric getHomePosition() {
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

//TODO Add Remove Adaptor Functions

}