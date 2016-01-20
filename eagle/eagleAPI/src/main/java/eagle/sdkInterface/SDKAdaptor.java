package eagle.sdkInterface;

import java.util.ArrayList;

import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.PositionMetricDisplacement;
import eagle.navigation.positioning.PositionGPS;
import eagle.navigation.positioning.PositionMetric;
import eagle.network.ScriptingEngine;
import eagle.sdkInterface.sensorAdaptors.AdaptorAccelerometer;
import eagle.sdkInterface.sensorAdaptors.AdaptorBarometer;
import eagle.sdkInterface.sensorAdaptors.AdaptorBearing;
import eagle.sdkInterface.sensorAdaptors.AdaptorCamera;
import eagle.sdkInterface.sensorAdaptors.AdaptorGPS;
import eagle.sdkInterface.sensorAdaptors.AdaptorGyroscope;
import eagle.sdkInterface.sensorAdaptors.AdaptorLIDAR;
import eagle.sdkInterface.sensorAdaptors.AdaptorMagnetic;
import eagle.sdkInterface.sensorAdaptors.AdaptorRPLIDAR;
import eagle.sdkInterface.sensorAdaptors.AdaptorUltrasonic;

/**
 * Abstract SDKAdaptor Class
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */
public abstract class SDKAdaptor {

    private ArrayList<AdaptorAccelerometer> accelerometers = new ArrayList<>();
    private ArrayList<AdaptorBarometer> barometers = new ArrayList<>();
    private ArrayList<AdaptorBearing> bearings = new ArrayList<>();
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

    final public ScriptingEngine scriptingEngine = new ScriptingEngine(this);

    final public SDKAdaptorTaskQueue sdkAdaptorStack;

    private double maxFlightSpeed = 0;
    private double maxYawAngularVelocity = 0;
    private double maxAscentSpeed = 0;
    private double maxDescentSpeed = 0;

    //TODO create way to set current assigned position
    private Position currentPositionAssigned = null;

    /**
     * Abstract constructor to be implemented in every subclass constructor
     * @param adaptorManufacturer The SDK adaptors applicable manufacturer
     * @param adaptorModel The SDK adaptors applicable model
     * @param sdkVersion The adaptors applicable SDK version
     * @param adaptorVersion The current adaptor version
     * @param maxFlightSpeed The SDK adaptors maximum flight speed in meters/second
     * @param maxRotateSpeed The SDK adaptors maximum rotation speed in degrees/second
     * @param maxAscentSpeed The SDK adaptors maximum ascending speed in meters/second
     * @param maxDescentSpeed The SDK adaptors maximum descending speed in meters/second
     */
    public SDKAdaptor(String adaptorManufacturer, String adaptorModel, String sdkVersion, String adaptorVersion, double maxFlightSpeed, double maxRotateSpeed, double maxAscentSpeed, double maxDescentSpeed) {
        this.adaptorName = adaptorManufacturer + " " + adaptorModel;
        this.adaptorManufacturer = adaptorManufacturer;
        this.adaptorModel = adaptorModel;
        this.sdkVersion = sdkVersion;
        this.adaptorVersion = adaptorVersion;
        this.maxFlightSpeed = maxFlightSpeed;
        this.maxYawAngularVelocity = maxRotateSpeed;
        this.maxAscentSpeed = maxAscentSpeed;
        this.maxDescentSpeed = maxDescentSpeed;
        sdkAdaptorStack = new SDKAdaptorTaskQueue(this);
    }

    /**
     * Load the default sensors as described by a manufacturer
     * @param adaptorLoader Adaptor loader to be used
     */
    public abstract void loadDefaultSensorAdaptors(AdaptorLoader adaptorLoader);

    /**
     * Command to initiate connections with the drone
     * @return Success result
     */
    public abstract boolean connectToDrone();

    /**
     * Command to close connections with the drone. Should also ensure drone is not in the air.
     * @return Success result
     */
    public abstract boolean disconnectFromDrone();

    /**
     * Check to see if drone connections are initiated
     * @return Success result
     */
    public abstract boolean isConnectedToDrone();

    /**
     * Halt the drone in its tasks, if its in the air, hover in its current location
     * @return Success result
     */
    public abstract boolean standbyDrone();

    /**
     * Resume the drone to complete its tasks
     * @return Success result
     */
    public abstract boolean resumeDrone();

    /**
     * Shut the drone down. If the drone is in the air, return to the take-off position and then shutdown.
     * @return Success result
     */
    public abstract boolean shutdownDrone();

    /**
     * Returns the API adaptor version
     * @return API adaptor version
     */
    public String getAdaptorVersion() {
        return adaptorVersion;
    }

    /**
     * Returns the SDK adaptor version
     * @return SDK adaptor version
     */
    public String getSdkVersion() {
        return sdkVersion;
    }

    /**
     * Returns the adaptor name
     * @return Adaptor name
     */
    public String getAdaptorName() {
        return adaptorName;
    }

    /**
     * Returns the adaptor manufacturer
     * @return Adaptor manufacturer
     */
    public String getAdaptorManufacturer() {
        return adaptorManufacturer;
    }

    /**
     * Returns the adaptor model
     * @return Adaptor model
     */
    public String getAdaptorModel() {
        return adaptorModel;
    }

    /**
     * Returns the maximum flight speed in Meters/Second
     * @return Max flight speed Meters/Second)
     */
    public double getMaxFlightSpeed() {
        return maxFlightSpeed;
    }

    /**
     * Returns the maximum angular velocity for the yaw axis in Degrees/second (Rotation Speed)
     * @return Max yaw angular velocity (Degrees/Second)
     */
    public double getMaxYawAngularVelocity() {
        return maxYawAngularVelocity;
    }

    /**
     * Returns the maximum ascending speed in meters/second
     * @return Max ascending speed (meters/second)
     */
    public double getMaxAscentSpeed() {
        return maxAscentSpeed;
    }

    /**
     * Returns the maximum descending speed in meters/second
     * @return Max descending speed (meters/second)
     */
    public double getMaxDescentSpeed() {
        return maxDescentSpeed;
    }

    /**
     * Fly to a metric displacement position from the take-off position with a defined speed
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param positionMetric Displacement position
     * @param speed Speed to perform the operation (Meters/Second)
     */
    public abstract void flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionMetric positionMetric, double speed);

    /**
     * Fly to a metric displacement position from the takeoff position
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param positionMetric Displacement position
     */
    public abstract void flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionMetric positionMetric);

    /**
     * Fly to a GPS Co-ordinate position with a defined speed
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param positionGPS GPS Co-ordinate position
     * @param speed Speed to perform the operation (Meters/Second)
     */
    public void flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionGPS positionGPS, double speed) {
        if (sdkAdaptorCallback != null)
            sdkAdaptorCallback.onResult(false, "flyTo(SDKAdaptorCallback,PositionGPS,double) not implemented in adaptor");
    }

    /**
     * Fly to a GPS co-ordinate position
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param positionGPS GPS co-ordinate position
     */
    public void flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionGPS positionGPS) {
        if (sdkAdaptorCallback != null)
            sdkAdaptorCallback.onResult(false, "flyTo(SDKAdaptorCallback,PositionGPS) not implemented in adaptor");
    }

    /**
     * Fly to a metric displacement from the current position with a defined speed
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param positionMetricDisplacement Metric Displacement Position
     * @param speed speed to perform the opperation (Meters/Second)
     */
    public abstract void flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionMetricDisplacement positionMetricDisplacement, double speed);

    /**
     * Fly to a metric displacement from the current position
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param positionMetricDisplacement Metric displacement position
     */
    public abstract void flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionMetricDisplacement positionMetricDisplacement);

    /**
     * Change the current longitude with a displacement value at a defined speed
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param longitude Longitude displacement (Meters)
     * @param speed Speed to peform the operation (Meters/Second)
     */
    final public void changeLongitudeDisplacement(final SDKAdaptorCallback sdkAdaptorCallback, double longitude, double speed) {
        flyTo(sdkAdaptorCallback, new PositionMetricDisplacement(0, longitude, 0, new Angle(0), new Angle(0), new Angle(0)), speed);
    }

    /**
     * Change the current longitude with a displacement
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param longitude Longitude displacement (Meters)
     */
    final public void changeLongitudeDisplacement(final SDKAdaptorCallback sdkAdaptorCallback, double longitude) {
        flyTo(sdkAdaptorCallback, new PositionMetricDisplacement(0, longitude, 0, new Angle(0), new Angle(0), new Angle(0)));
    }

    /**
     * Change the current latitude with a displacement at a defined speed.
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param latitude Latitude displacement (Meters)
     * @param speed Speed to perform the operation (Meters/Second)
     */
    final public void changeLatitudeDisplacement(final SDKAdaptorCallback sdkAdaptorCallback, double latitude, double speed) {
        flyTo(sdkAdaptorCallback, new PositionMetricDisplacement(latitude, 0, 0, new Angle(0), new Angle(0), new Angle(0)), speed);
    }

    /**
     * Change the current latitude with a displacement
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param latitude Latitude displacement (Meters/Second)
     */
    final public void changeLatitudeDisplacement(final SDKAdaptorCallback sdkAdaptorCallback, double latitude) {
        flyTo(sdkAdaptorCallback, new PositionMetricDisplacement(latitude, 0, 0, new Angle(0), new Angle(0), new Angle(0)));
    }

    /**
     * Change the current altitude with a displacement at a defined speed
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param altitude Altitude displacement (Meters)
     * @param speed Speed to perform the operation (Meters/Second)
     */
    final public void changeAltitudeDisplacement(final SDKAdaptorCallback sdkAdaptorCallback, double altitude, double speed) {
        flyTo(sdkAdaptorCallback, new PositionMetricDisplacement(0, 0, altitude, new Angle(0), new Angle(0), new Angle(0)), speed);
    }

    /**
     * Change the current altitude with a displacement
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param altitude Altidude displacement (Meters)
     */
    final public void changeAltitudeDisplacement(final SDKAdaptorCallback sdkAdaptorCallback, double altitude) {
        flyTo(sdkAdaptorCallback, new PositionMetricDisplacement(0, 0, altitude, new Angle(0), new Angle(0), new Angle(0)));
    }

    /**
     * Change the yaw with a defined speed
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param yaw Angle Displacement (Degrees)
     * @param speed Speed to perform the operation (Degrees/Second)
     */
    final public void changeYawDisplacement(final SDKAdaptorCallback sdkAdaptorCallback, Angle yaw, double speed) {
        flyTo(sdkAdaptorCallback, new PositionMetricDisplacement(0, 0, 0, new Angle(0), new Angle(0), yaw), speed);
    }

    /**
     * Change the yaw
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param yaw Angle Displacement (Degrees/Second)
     */
    final public void changeYawDisplacement(final SDKAdaptorCallback sdkAdaptorCallback, Angle yaw) {
        flyTo(sdkAdaptorCallback, new PositionMetricDisplacement(0, 0, 0, new Angle(0), new Angle(0), yaw));
    }

    /**
     * Change the current longitude to a metric position displaced from the take-off position with a defined speed
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param longitude Longitude from take-off (Meters)
     * @param speed Speed to perform the operation (Meters/Second)
     */
    final public void changeLongitudeMetric(final SDKAdaptorCallback sdkAdaptorCallback, double longitude, double speed) {
        flyTo(sdkAdaptorCallback, new PositionMetric(0, longitude, 0, new Angle(0), new Angle(0), new Angle(0)), speed);
    }

    /**
     * Change the current longitude to a metric position displaced from the take-off position
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param longitude Longitude from take-off (Meters)
     */
    final public void changeLongitudeMetric(final SDKAdaptorCallback sdkAdaptorCallback, double longitude) {
        flyTo(sdkAdaptorCallback, new PositionMetric(0, longitude, 0, new Angle(0), new Angle(0), new Angle(0)));
    }

    /**
     * Change the current latitude to a metric position displaced from the take-off position with a defined speed
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param latitude Latitude from take-off (Meters)
     * @param speed Speed to perform the operation (Meters/Second)
     */
    final public void changeLatitudeMetric(final SDKAdaptorCallback sdkAdaptorCallback, double latitude, double speed) {
        flyTo(sdkAdaptorCallback, new PositionMetric(latitude, 0, 0, new Angle(0), new Angle(0), new Angle(0)), speed);
    }

    /**
     * Change the current latitude to a metric position displaced from the take-off position
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param latitude Latitude from take-off (Meters)
     */
    final public void changeLatitudeMetric(final SDKAdaptorCallback sdkAdaptorCallback, double latitude) {
        flyTo(sdkAdaptorCallback, new PositionMetric(latitude, 0, 0, new Angle(0), new Angle(0), new Angle(0)));
    }

    /**
     * Change the current latitude to a metric position displaced from the take-off position with a defined speed
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param altitude Altitude (Meters)
     * @param speed Speed to perform the operation (Meters/Second)
     */
    final public void changeAltitudeMetric(final SDKAdaptorCallback sdkAdaptorCallback, double altitude, double speed) {
        flyTo(sdkAdaptorCallback, new PositionMetric(0, 0, altitude, new Angle(0), new Angle(0), new Angle(0)), speed);
    }

    /**
     * Change the current latitude to a metric position displaced from the take-off position
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param altitude Altitude (Meters)
     */
    final public void changeAltitudeMetric(final SDKAdaptorCallback sdkAdaptorCallback, double altitude) {
        flyTo(sdkAdaptorCallback, new PositionMetric(0, 0, altitude, new Angle(0), new Angle(0), new Angle(0)));
    }

    /**
     * Change the current yaw to an Angle relative to the one at take-off with a defined speed
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param yaw Angle (Degrees)
     * @param speed Speed to perform the operation (Degrees/Second)
     */
    final public void changeYawMetric(final SDKAdaptorCallback sdkAdaptorCallback, Angle yaw, double speed) {
        flyTo(sdkAdaptorCallback, new PositionMetric(0, 0, 0, new Angle(0), new Angle(0), yaw), speed);
    }

    /**
     * Change the current yaw to an angle relatice to the one at take-off
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param yaw Angle (Degrees)
     */
    final public void changeYawMetric(final SDKAdaptorCallback sdkAdaptorCallback, Angle yaw) {
        flyTo(sdkAdaptorCallback, new PositionMetric(0, 0, 0, new Angle(0), new Angle(0), yaw));
    }

    /**
     * Change the current longitude to a GPS position at a defined speed
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param longitude Longitude position (xxx.xxxxxx)
     * @param speed Speed to perform operation (Meters/Second)
     */
    final public void changeLongitudeGPS(final SDKAdaptorCallback sdkAdaptorCallback, double longitude, double speed) {
        if (getPositionAssigned() != null) {
            this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLatitude(), longitude, getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), getPositionAssigned().getYaw());
            flyTo(sdkAdaptorCallback, (PositionGPS) this.currentPositionAssigned, speed);
        }
    }

    /**
     * Change the current longitude to a GPS position
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param longitude Longitude position (xxx.xxxxxx)
     */
    final public void changeLongitudeGPS(final SDKAdaptorCallback sdkAdaptorCallback, double longitude) {
        if (getPositionAssigned() != null) {
            this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLatitude(), longitude, getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), getPositionAssigned().getYaw());
            flyTo(sdkAdaptorCallback, (PositionGPS) this.currentPositionAssigned);
        }
    }

    /**
     * Change the current latitude to a GPS position with a defined speed
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param latitude Latitude position (xxx.xxxxxx)
     * @param speed Speed to perform operation (Meters/Second)
     */
    final public void changeLatitudeGPS(final SDKAdaptorCallback sdkAdaptorCallback, double latitude, double speed) {
        if (getPositionAssigned() != null) {
            this.currentPositionAssigned = new PositionGPS(latitude, getPositionAssigned().getLongitude(), getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), getPositionAssigned().getYaw());
            flyTo(sdkAdaptorCallback, (PositionGPS) this.currentPositionAssigned, speed);
        }
    }

    /**
     * Change the current latitude to a GPS position
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param latitude Latitude position (xxx.xxxxxx)
     */
    final public void changeLatitudeGPS(final SDKAdaptorCallback sdkAdaptorCallback, double latitude) {
        if (getPositionAssigned() != null) {
            this.currentPositionAssigned = new PositionGPS(latitude, getPositionAssigned().getLongitude(), getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), getPositionAssigned().getYaw());
            flyTo(sdkAdaptorCallback, (PositionGPS) this.currentPositionAssigned);
        }
    }

    /**
     * Change the current altitude position to a GPS position with a defined speed
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param altitude Altitude position (Meters)
     * @param speed Speed to perform the operation (Meters/Second)
     */
    final public void changeAltitudeGPS(final SDKAdaptorCallback sdkAdaptorCallback, double altitude, double speed) {
        if (getPositionAssigned() != null) {
            this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLatitude(), getPositionAssigned().getLongitude(), altitude, new Angle(0), new Angle(0), getPositionAssigned().getYaw());
            flyTo(sdkAdaptorCallback, (PositionGPS) this.currentPositionAssigned, speed);
        }
    }

    /**
     * Change the current altitude position to a GPS position
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param altitude Altitude position (Meters)
     */
    final public void changeAltitudeGPS(final SDKAdaptorCallback sdkAdaptorCallback, double altitude) {
        if (getPositionAssigned() != null) {
            this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLatitude(), getPositionAssigned().getLongitude(), altitude, new Angle(0), new Angle(0), getPositionAssigned().getYaw());
            flyTo(sdkAdaptorCallback, (PositionGPS) this.currentPositionAssigned);
        }
    }

    /**
     * Change the current yaw angle to a bearing angle with a defined speed
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param yaw Angle (Degrees)
     * @param speed Speed to perform the operation (Degrees/Second)
     */
    final public void changeYawGPS(final SDKAdaptorCallback sdkAdaptorCallback, Angle yaw, double speed) {
        if (getPositionAssigned() != null) {
            this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLatitude(), getPositionAssigned().getLongitude(), getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), yaw);
            flyTo(sdkAdaptorCallback, (PositionGPS) this.currentPositionAssigned, speed);
        }
    }

    /**
     * Change the current yaw angle to a bearing angle
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param yaw Angle (Degrees)
     */
    final public void changeYawGPS(final SDKAdaptorCallback sdkAdaptorCallback, Angle yaw) {
        if (getPositionAssigned() != null) {
            this.currentPositionAssigned = new PositionGPS(getPositionAssigned().getLatitude(), getPositionAssigned().getLongitude(), getPositionAssigned().getAltitude(), new Angle(0), new Angle(0), yaw);
            flyTo(sdkAdaptorCallback, (PositionGPS) this.currentPositionAssigned);
        }
    }

    /**
     * Fly to the take-off position with a defined speed
     * @param sdkAdaptorCallback Callback for completion status and error description
     * @param speed Speed to perform the operation (Meters/Second)
     */
    public void goHome(final SDKAdaptorCallback sdkAdaptorCallback, double speed) {
        if (homePosition instanceof PositionMetric && currentPositionAssigned instanceof PositionMetric) {
            flyTo(sdkAdaptorCallback, new PositionMetric(homePosition.add(new PositionMetricDisplacement(0, 0, 1, new Angle(0)))), speed);
            flyTo(sdkAdaptorCallback, (PositionMetric) homePosition, speed);
        } else if (homePosition instanceof PositionGPS && currentPositionAssigned instanceof PositionGPS) {
            this.currentPositionAssigned = new PositionGPS(homePosition.add(new PositionMetricDisplacement(0, 0, 1, new Angle(0))));
            flyTo(sdkAdaptorCallback, (PositionGPS) currentPositionAssigned, speed);
            this.currentPositionAssigned = new PositionGPS(homePosition);
            flyTo(sdkAdaptorCallback, (PositionGPS) homePosition, speed);
        } else if (sdkAdaptorCallback != null)
            sdkAdaptorCallback.onResult(false, "Unknown Home Position Type");

    }

    /**
     * Fly to the take-off position
     * @param sdkAdaptorCallback Callback for completion status and error description
     */
    public void goHome(final SDKAdaptorCallback sdkAdaptorCallback) {
        if (homePosition instanceof PositionMetric && currentPositionAssigned instanceof PositionMetric) {
            flyTo(sdkAdaptorCallback, new PositionMetric(homePosition.add(new PositionMetricDisplacement(0, 0, 1, new Angle(0)))));
            flyTo(sdkAdaptorCallback, (PositionMetric) homePosition);
        } else if (homePosition instanceof PositionGPS && currentPositionAssigned instanceof PositionGPS) {
            this.currentPositionAssigned = new PositionGPS(homePosition.add(new PositionMetricDisplacement(0, 0, 1, new Angle(0))));
            flyTo(sdkAdaptorCallback, (PositionGPS) currentPositionAssigned);
            this.currentPositionAssigned = new PositionGPS(homePosition);
            flyTo(sdkAdaptorCallback, (PositionGPS) homePosition);
        } else if (sdkAdaptorCallback != null)
            sdkAdaptorCallback.onResult(false, "Unknown Home Position Type");

    }

    /**
     * Return the assigned position for the drone to hover at.
     * @return Position (Type depends on drone specification & signal)
     */
    public Position getPositionAssigned() {
        return currentPositionAssigned;
    }

    /**
     * Set the position for the drone to hover at.
     * @param position Position
     * @throws InvalidPositionTypeException Exception if position type is of an incorrect type
     */
    public void setPositionAssigned(Position position) throws InvalidPositionTypeException {
        if (!(position instanceof PositionGPS)&&!(position instanceof PositionMetric))
            throw new InvalidPositionTypeException("PositionMetricDisplacement isn't a valid assigned position");
        currentPositionAssigned = position;
    }

    /**
     * Returns the position the drone is currently at.
     * @return Position the drone is currently at
     */
    public abstract Position getPositionInFlight();

    /**
     * Set the position to return if loss of signal and/or self landing
     * @param position Position
     * @throws InvalidPositionTypeException Exception if position type is of an incorrect type
     */
    public void setHomePosition(Position position) throws InvalidPositionTypeException {
        if (position instanceof PositionMetricDisplacement)
            throw new InvalidPositionTypeException("PositionMetricDisplacement isn't a valid home position");
        homePosition = position;
    }

    /**
     * Return the position to the drone will return to after loss of signal an/or self landing
     * @return Position
     */
    public Position getHomePosition() {
        return homePosition;
    }

    /**
     * Add a new accelerometer sensor adaptor to the SDK adaptor
     * @param adaptorAccelerometer AdaptorAccelerometer type
     */
    public void addSensorAdaptorAccelerometer(AdaptorAccelerometer adaptorAccelerometer) {
        if (adaptorAccelerometer != null)
            this.accelerometers.add(adaptorAccelerometer);
    }

    /**
     * Add a new barometer sensor adaptor to the SDK adaptor
     * @param adaptorBarometer AdaptorBarometer type
     */
    public void addSensorAdaptorBarometer(AdaptorBarometer adaptorBarometer) {
        this.barometers.add(adaptorBarometer);
    }

    /**
     * Add a new bearing sensor adaptor to the SDK adaptor
     * @param adaptorBearing AdaptorBearing type
     */
    public void addSensorAdaptorBearing(AdaptorBearing adaptorBearing) {
        if (adaptorBearing != null)
            this.bearings.add(adaptorBearing);
    }

    /**
     * Add a new camera sensor adaptor to the SDK adaptor
     * @param adaptorCamera AdaptorCamera type
     */
    public void addSensorAdaptorCamera(AdaptorCamera adaptorCamera) {
        if (adaptorCamera != null)
            this.cameras.add(adaptorCamera);
    }

    /**
     * Add a new GPS sensor adaptor to the SDK adaptor
     * @param adaptorGPS AdaptorGPS type
     */
    public void addSensorAdaptorGPS(AdaptorGPS adaptorGPS) {
        if (adaptorGPS != null)
            this.gps.add(adaptorGPS);
    }

    /**
     * Add a new gyroscope sensor adaptor to the SDK adaptor
     * @param adaptorGyroscope AdaptorGyroscope type
     */
    public void addSensorAdaptorGyroscope(AdaptorGyroscope adaptorGyroscope) {
        if (adaptorGyroscope != null)
            this.gyroscopes.add(adaptorGyroscope);
    }

    /**
     * Add a new LIDAR sensor adaptor to the SDK adaptor
     * @param adaptorLIDAR adaptorLIDAR type
     */
    public void addSensorAdaptorLIDAR(AdaptorLIDAR adaptorLIDAR) {
        if (adaptorLIDAR != null)
            this.lidars.add(adaptorLIDAR);
    }

    /**
     * Add a new magnetic sensor adaptor to the SDK adaptor
     * @param adaptorMagnetic AdaptorMagnetic type
     */
    public void addSensorAdaptorMagnetic(AdaptorMagnetic adaptorMagnetic) {
        if (adaptorMagnetic != null)
            this.magnetics.add(adaptorMagnetic);
    }

    /**
     * Add a new RPLIDAR sensor adaptor to the SDK adaptor
     * @param adaptorRPLIDAR AdaptorRPLIDAR type
     */
    public void addSensorAdaptorRPLIDAR(AdaptorRPLIDAR adaptorRPLIDAR) {
        if (adaptorRPLIDAR != null)
            this.rplidars.add(adaptorRPLIDAR);
    }

    /**
     * Add a new ultrasonic sensor adaptor to the SDK adaptor
     * @param adaptorUltrasonic AdaptorUltrasonic type
     */
    public void addSensorAdaptorUltrasonic(AdaptorUltrasonic adaptorUltrasonic) {
        if (adaptorUltrasonic != null)
            this.ultrasonics.add(adaptorUltrasonic);
    }

    /**
     * Return all of the accelerometer adaptors attached to the SDK adaptor
     * @return AdaptorAccelerometer's attached to the SDK adaptor
     */
    public ArrayList<AdaptorAccelerometer> getAccelerometers() {
        return accelerometers;
    }

    /**
     * Return all of the barometer adaptors attached to the SDK adaptor
     * @return AdaptorBarometer's attached to the SDK adaptor
     */
    public ArrayList<AdaptorBarometer> getBarometers() {
        return barometers;
    }

    /**
     * Return all of the bearing adaptors attached to the SDK adaptor
     * @return AdaptorBearing's attached to the SDK adaptor
     */
    public ArrayList<AdaptorBearing> getBearings() {
        return bearings;
    }

    /**
     * Return all of the camera adaptors attached to the SDK adaptor
     * @return AdaptorCamera's attached to the SDK adaptor
     */
    public ArrayList<AdaptorCamera> getCameras() {
        return cameras;
    }

    /**
     * Return all of the GPS adaptors attached to the SDK adaptor
     * @return AdaptorGPS's attached to the SDK adaptor
     */
    public ArrayList<AdaptorGPS> getGPSs() {
        return gps;
    }

    /**
     * Return all of the gyroscope adaptors attached to the SDK adaptor
     * @return AdaptorGyroscope's attached to the SDK adaptor
     */
    public ArrayList<AdaptorGyroscope> getGyroscopes() {
        return gyroscopes;
    }

    /**
     * Return all of the LIDAR adaptors attached to the SDK adaptor
     * @return AdaptorLIDAR's attached to the SDK adaptor
     */
    public ArrayList<AdaptorLIDAR> getLidars() {
        return lidars;
    }

    /**
     * Return all of the magnetic adaptors attached to the SDK adaptor
     * @return AdaptorMagnetic's attached to the SDK adaptor
     */
    public ArrayList<AdaptorMagnetic> getMagnetics() {
        return magnetics;
    }

    /**
     * Return all of the RPLIDAR adaptors attached to the SDK adaptor
     * @return AdaptorRPLIDAR's attached to the SDK adaptor
     */
    public ArrayList<AdaptorRPLIDAR> getRplidars() {
        return rplidars;
    }

    /**
     * Return all of the ultrasonic adaptors attached to the SDK adaptor
     * @return AdaptorUltrasonic's attached to the SDK adaptor
     */
    public ArrayList<AdaptorUltrasonic> getUltrasonics() {
        return ultrasonics;
    }

//TODO Add Remove Adaptor Functions

    /**
     * Set the AndroidContext if the SDK adaptor requires the AndroidContext
     * @param object
     * @return
     */
    public boolean setAndroidContext(Object object) {
        return false;
    }

    /**
     * Set the controller of the SDK adaptor if the SDK adaptor requires a controller
     * @param object
     * @return
     */
    public boolean setController(Object object) {
        return false;
    }

    /**
     * Set a delay for drone opperation. If the dron is in the air, it should hover at its position.
     * @param milliseconds
     */
    public abstract void delay(int milliseconds);

    /**
     * Exception for when an incorrect position type is used.
     */
    public class InvalidPositionTypeException extends Exception {
        /**
         * Exception for when an incorrect position type is used
         * @param message Error message
         */
        public InvalidPositionTypeException(String message) {
            super("Invalid Position Type: " + message);
        }
    }
}