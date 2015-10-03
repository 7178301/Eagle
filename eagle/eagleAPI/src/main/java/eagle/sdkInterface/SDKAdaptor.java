package eagle.sdkInterface;

import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.PositionDisplacement;
import eagle.navigation.positioning.PositionGPS;
import eagle.navigation.positioning.PositionMetric;
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

    final public String adaptorName;
    final public String adaptorManufacturer;
    final public String adaptorModel;
    final public String sdkVersion;
    final public String adaptorVersion;

    private Position homePosition = null;

    final public SDKAdaptorFlightStack sdkAdaptorStack;

    private double maxSpeed = 0;
    private double maxRotationSpeed = 0;

    //TODO create way to set current assigned position
    private Position currentPositionAssigned = null;

    public SDKAdaptor(String adaptorManufacturer, String adaptorModel, String sdkVersion, String adaptorVersion, double maxSpeed, double maxRotateSpeed) {
        this.adaptorName = adaptorManufacturer + " " + adaptorModel;
        this.adaptorManufacturer = adaptorManufacturer;
        this.adaptorModel = adaptorModel;
        this.sdkVersion = sdkVersion;
        this.adaptorVersion = adaptorVersion;
        this.maxSpeed = maxSpeed;
        this.maxRotationSpeed = maxRotateSpeed;
        sdkAdaptorStack = new SDKAdaptorFlightStack(this);
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

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getMaxRotationSpeed() {
        return maxRotationSpeed;
    }

    public abstract boolean flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionMetric positionMetric, double speed);

    public abstract boolean flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionMetric positionMetric);

    public boolean flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionGPS position, double speed){
        sdkAdaptorCallback.onResult(false,"flyTo(SDKAdaptorCallback,PositionGPS,double) not implemented in adaptor");
        return false;
    }

    public boolean flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionGPS position){
        sdkAdaptorCallback.onResult(false,"flyTo(SDKAdaptorCallback,PositionGPS) not implemented in adaptor");
        return false;
    }

    public abstract boolean flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionDisplacement position, double speed);

    public abstract boolean flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionDisplacement position);

    final public boolean changeLongitudeDisplacement(final SDKAdaptorCallback sdkAdaptorCallback, double longitude, double speed) {
        return flyTo(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
                sdkAdaptorCallback.onResult(booleanResult, stringResult);

            }
        }, new PositionDisplacement(0, longitude, 0, new Angle(0), new Angle(0), new Angle(0)), speed);
    }

    final public boolean changeLongitudeDisplacement(final SDKAdaptorCallback sdkAdaptorCallback, double longitude) {
        return flyTo(sdkAdaptorCallback, new PositionDisplacement(0, longitude, 0, new Angle(0), new Angle(0), new Angle(0)));
    }

    final public boolean changeLatitudeDisplacement(final SDKAdaptorCallback sdkAdaptorCallback, double latitude, double speed) {
        return flyTo(sdkAdaptorCallback, new PositionDisplacement(latitude, 0, 0, new Angle(0), new Angle(0), new Angle(0)), speed);
    }

    final public boolean changeLatitudeDisplacement(final SDKAdaptorCallback sdkAdaptorCallback, double latitude) {
        return flyTo(sdkAdaptorCallback, new PositionDisplacement(latitude, 0, 0, new Angle(0), new Angle(0), new Angle(0)));
    }

    final public boolean changeAltitudeDisplacement(final SDKAdaptorCallback sdkAdaptorCallback, double altitude, double speed) {
        return flyTo(sdkAdaptorCallback, new PositionDisplacement(0, 0, altitude, new Angle(0), new Angle(0), new Angle(0)), speed);
    }

    final public boolean changeAltitudeDisplacement(final SDKAdaptorCallback sdkAdaptorCallback, double altitude) {
        return flyTo(sdkAdaptorCallback, new PositionDisplacement(0, 0, altitude, new Angle(0), new Angle(0), new Angle(0)));
    }

    final public boolean changeYawDisplacement(final SDKAdaptorCallback sdkAdaptorCallback, Angle yaw, double speed) {
        return flyTo(sdkAdaptorCallback, new PositionDisplacement(0, 0, 0, new Angle(0), new Angle(0), yaw), speed);
    }

    final public boolean changeYawDisplacement(final SDKAdaptorCallback sdkAdaptorCallback, Angle yaw) {
        return flyTo(sdkAdaptorCallback, new PositionDisplacement(0, 0, 0, new Angle(0), new Angle(0), yaw));
    }

    final public boolean changeLongitudeMetric(final SDKAdaptorCallback sdkAdaptorCallback, double longitude, double speed) {
        return flyTo(sdkAdaptorCallback, new PositionMetric(0, longitude, 0, new Angle(0), new Angle(0), new Angle(0)), speed);
    }

    final public boolean changeLongitudeMetric(final SDKAdaptorCallback sdkAdaptorCallback, double longitude) {
        return flyTo(sdkAdaptorCallback, new PositionMetric(0, longitude, 0, new Angle(0), new Angle(0), new Angle(0)));
    }

    final public boolean changeLatitudeMetric(final SDKAdaptorCallback sdkAdaptorCallback, double latitude, double speed) {
        return flyTo(sdkAdaptorCallback, new PositionMetric(latitude, 0, 0, new Angle(0), new Angle(0), new Angle(0)), speed);
    }

    final public boolean changeLatitudeMetric(final SDKAdaptorCallback sdkAdaptorCallback, double latitude) {
        return flyTo(sdkAdaptorCallback, new PositionMetric(latitude, 0, 0, new Angle(0), new Angle(0), new Angle(0)));
    }

    final public boolean changeAltitudeMetric(final SDKAdaptorCallback sdkAdaptorCallback, double altitude, double speed) {
        return flyTo(sdkAdaptorCallback, new PositionMetric(0, 0, altitude, new Angle(0), new Angle(0), new Angle(0)), speed);
    }

    final public boolean changeAltitudeMetric(final SDKAdaptorCallback sdkAdaptorCallback, double altitude) {
        return flyTo(sdkAdaptorCallback, new PositionMetric(0, 0, altitude, new Angle(0), new Angle(0), new Angle(0)));
    }

    final public boolean changeYawMetric(final SDKAdaptorCallback sdkAdaptorCallback, Angle yaw, double speed) {
        return flyTo(sdkAdaptorCallback, new PositionMetric(0, 0, 0, new Angle(0), new Angle(0), yaw), speed);
    }

    final public boolean changeYawMetric(final SDKAdaptorCallback sdkAdaptorCallback, Angle yaw) {
        return flyTo(sdkAdaptorCallback, new PositionMetric(0, 0, 0, new Angle(0), new Angle(0), yaw));
    }

    final public boolean changeLongitudeGPS(final SDKAdaptorCallback sdkAdaptorCallback, double longitude, double speed) {
        this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLatitude(), longitude, getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), getPositionAssigned().getYaw());
        return flyTo(sdkAdaptorCallback, (PositionGPS) this.currentPositionAssigned, speed);
    }

    final public boolean changeLongitudeGPS(final SDKAdaptorCallback sdkAdaptorCallback, double longitude) {
        this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLatitude(), longitude, getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), getPositionAssigned().getYaw());
        return flyTo(sdkAdaptorCallback, (PositionGPS) this.currentPositionAssigned);
    }

    final public boolean changeLatitudeGPS(final SDKAdaptorCallback sdkAdaptorCallback, double latitude, double speed) {
        this.currentPositionAssigned = new PositionGPS(latitude, getPositionAssigned().getLongitude(), getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), getPositionAssigned().getYaw());
        return flyTo(sdkAdaptorCallback, (PositionGPS) this.currentPositionAssigned, speed);
    }

    final public boolean changeLatitudeGPS(final SDKAdaptorCallback sdkAdaptorCallback, double latitude) {
        this.currentPositionAssigned = new PositionGPS(latitude, getPositionAssigned().getLongitude(), getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), getPositionAssigned().getYaw());
        return flyTo(sdkAdaptorCallback, (PositionGPS) this.currentPositionAssigned);
    }

    final public boolean changeAltitudeGPS(final SDKAdaptorCallback sdkAdaptorCallback, double altitude, double speed) {
        this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLatitude(), getPositionAssigned().getLongitude(), altitude, new Angle(0), new Angle(0), getPositionAssigned().getYaw());
        return flyTo(sdkAdaptorCallback, (PositionGPS) this.currentPositionAssigned, speed);
    }

    final public boolean changeAltitudeGPS(final SDKAdaptorCallback sdkAdaptorCallback, double altitude) {
        this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLatitude(), getPositionAssigned().getLongitude(), altitude, new Angle(0), new Angle(0), getPositionAssigned().getYaw());
        return flyTo(sdkAdaptorCallback, (PositionGPS) this.currentPositionAssigned);
    }

    final public boolean changeYawGPS(final SDKAdaptorCallback sdkAdaptorCallback, Angle yaw, double speed) {
        this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLatitude(), getPositionAssigned().getLongitude(), getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), yaw);
        return flyTo(sdkAdaptorCallback, (PositionGPS) this.currentPositionAssigned, speed);
    }

    final public boolean changeYawGPS(final SDKAdaptorCallback sdkAdaptorCallback, Angle yaw) {
        this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLatitude(), getPositionAssigned().getLongitude(), getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), yaw);
        return flyTo(sdkAdaptorCallback, (PositionGPS) this.currentPositionAssigned);
    }

    final public boolean goHome(final SDKAdaptorCallback sdkAdaptorCallback, double speed) {
        if (homePosition instanceof PositionMetric && currentPositionAssigned instanceof PositionMetric) {
            return flyTo(sdkAdaptorCallback, (PositionMetric) homePosition, speed);
        } else if (homePosition instanceof PositionGPS && currentPositionAssigned instanceof PositionGPS) {
            this.currentPositionAssigned = new PositionGPS(homePosition);
            return flyTo(sdkAdaptorCallback, (PositionGPS) homePosition, speed);
        } else
            sdkAdaptorCallback.onResult(false, "Unknown Home Position Type");
        return false;
    }

    final public boolean goHome(final SDKAdaptorCallback sdkAdaptorCallback) {
        if (homePosition instanceof PositionMetric && currentPositionAssigned instanceof PositionMetric) {
            return flyTo(sdkAdaptorCallback, (PositionMetric) homePosition);
        } else if (homePosition instanceof PositionGPS && currentPositionAssigned instanceof PositionGPS) {
            this.currentPositionAssigned = new PositionGPS(homePosition);
            return flyTo(sdkAdaptorCallback, (PositionGPS) homePosition);
        } else
            sdkAdaptorCallback.onResult(false, "Unknown Home Position Type");
        return false;
    }

    public Position getPositionAssigned() {
        return currentPositionAssigned;
    }

    public void setPositionAssigned(Position pos) throws InvalidPositionException {
        if (pos instanceof PositionDisplacement) {
            throw new InvalidPositionException();
        }
        currentPositionAssigned = pos;
    }

    public abstract Position getPositionInFlight();

    public void setHomePosition(Position position) throws InvalidPositionException {
        if (position instanceof PositionDisplacement) {
            throw new InvalidPositionException();
        }
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

    public boolean setAndroidContext(Object object) {
        return false;
    }

    public boolean setController(Object object) {
        return false;
    }

    public abstract void delay(int milliseconds);

    public class InvalidPositionException extends Throwable {
    }
}