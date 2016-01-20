package eagle.sdkInterface;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import eagle.Drone;
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
 * Adaptor Loader
 * Used to retrieve the list of SDK & sensor adaptors and return a specific adaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 17/06/2015
 * <p/>
 * Date Modified	17/06/2015 - Nicholas
 */
public class AdaptorLoader {

    private HashSet<String> sdkAdaptorPaths = new HashSet<>(Arrays.asList("DJI.Phantom2Vision", "Flyver.F450Flamewheel", "Simulator.Simulator"));
    private HashSet<String> accelerometerAdaptorPaths = new HashSet<>(Arrays.asList("AndroidAccelerometer"));
    private HashSet<String> barometerAdaptorPaths = new HashSet<>(Arrays.asList("AndroidBarometer"));
    private HashSet<String> bearingAdaptorPaths = new HashSet<>(Arrays.asList("AndroidBearing", "DJIBearing"));
    private HashSet<String> cameraAdaptorPaths = new HashSet<>(Arrays.asList("AndroidCamera", "LinkSpriteSEN12804", "DJICamera"));
    private HashSet<String> magneticAdaptorPaths = new HashSet<>(Arrays.asList("AndroidMagnetic"));
    private HashSet<String> gpsAdaptorPaths = new HashSet<>(Arrays.asList("AndroidGPS", "JavaGPS", "DJIGPS"));
    private HashSet<String> gyroscopeAdaptorPaths = new HashSet<>(Arrays.asList("AndroidGyroscope"));
    //private HashSet<String> LIDARAdaptorPaths = new HashSet<>(Arrays.asList());
    private HashSet<String> RPLIDARAdaptorPaths = new HashSet<>(Arrays.asList("RoboPeakRPLIDARA1M1R1"));
    private HashSet<String> ultrasonicAdaptorPaths = new HashSet<>(Arrays.asList("SeeedStudioSEN10737P"));

    /**
     * Returns a collection of the SDK adaptor paths
     * @return Collection of SDK adaptor paths
     */
    public HashMap getSDKAdaptorMap() {
        HashMap<String, SDKAdaptor> sdkAdaptors = new HashMap<>();
        for (String path : sdkAdaptorPaths)
            sdkAdaptors.put(path, getSDKAdaptor(path));
        return sdkAdaptors;
    }

    /**
     * Returns a collection of the accelerometer adaptor paths
     * @return Collection of accelerometer adaptor paths
     */
    public HashMap getSensorAdaptorAccelerometerMap() {
        HashMap<String, AdaptorAccelerometer> accelerometerAdaptors = new HashMap<>();
        for (String path : accelerometerAdaptorPaths)
            accelerometerAdaptors.put(path, getSensorAdaptorAccelerometer(path));
        return accelerometerAdaptors;
    }

    /**
     * Returns a collection of the camera adaptor paths
     * @return Collection of camera adaptor paths
     */
    public HashMap getSensorAdaptorCameraMap() {
        HashMap<String, AdaptorCamera> cameraAdaptors = new HashMap<>();
        for (String path : cameraAdaptorPaths)
            cameraAdaptors.put(path, getSensorAdaptorCamera(path));
        return cameraAdaptors;
    }

    /**
     * Returns a collection of the barometer adaptor paths
     * @return Collection of barometer adaptor paths
     */
    public HashMap getSensorAdaptorBarometerMap() {
        HashMap<String, AdaptorBarometer> barometerAdaptors = new HashMap<>();
        for (String path : barometerAdaptorPaths)
            barometerAdaptors.put(path, getSensorAdaptorBarometer(path));
        return barometerAdaptors;
    }

    /**
     * Returns a collection of the bearing adaptor paths
     * @return Collection of bearing adaptor paths
     */
    public HashMap getSensorAdaptorBearingMap() {
        HashMap<String, AdaptorBearing> bearingAdaptors = new HashMap<>();
        for (String path : bearingAdaptorPaths)
            bearingAdaptors.put(path, getSensorAdaptorBearing(path));
        return bearingAdaptors;
    }

    /**
     * Returns a collection of the GPS adaptor paths
     * @return Collection of GPS adaptor paths
     */
    public HashMap getSensorAdaptorGPSMap() {
        HashMap<String, AdaptorGPS> gpsAdaptors = new HashMap<>();
        for (String path : gpsAdaptorPaths)
            gpsAdaptors.put(path, getSensorAdaptorGPS(path));
        return gpsAdaptors;
    }

    /**
     * Returns a collection of the gyroscope adaptor paths
     * @return Collection of gyroscope adaptor paths
     */
    public HashMap getSensorAdaptorGyroscopeMap() {
        HashMap<String, AdaptorGyroscope> gyroscopeAdaptors = new HashMap<>();
        for (String path : gyroscopeAdaptorPaths)
            gyroscopeAdaptors.put(path, getSensorAdaptorGyroscope(path));
        return gyroscopeAdaptors;
    }

    /**
     * Returns a collection of the LIDAR adaptor paths
     * @return Collection of gyroscope adaptor paths
     */
    /*public HashMap getSensorAdaptorLIDARMap(){
        HashMap<String,AdaptorLIDAR> LIDARAdaptors = new HashMap<>();
        for(String path:LIDARAdaptorPaths)
            LIDARAdaptors.put(path, getSensorAdaptorLIDAR(path));
        return LIDARAdaptors;
    }*/

    /**
     * Returns a collection of the Magnetic adaptor paths
     * @return Collection of magnetic adaptor paths
     */
    public HashMap getSensorAdaptorMagneticMap() {
        HashMap<String, AdaptorMagnetic> magneticAdaptors = new HashMap<>();
        for (String path : magneticAdaptorPaths)
            magneticAdaptors.put(path, getSensorAdaptorMagnetic(path));
        return magneticAdaptors;
    }

    /**
     * Returns a collection of the RPLIDAR adaptor paths
     * @return Collection of magnetic adaptor paths
     */
    public HashMap getSensorAdaptorRPLIDARMap() {
        HashMap<String, AdaptorRPLIDAR> RPLIDARAdaptors = new HashMap<>();
        for (String path : RPLIDARAdaptorPaths)
            RPLIDARAdaptors.put(path, getSensorAdaptorRPLIDAR(path));
        return RPLIDARAdaptors;
    }

    /**
     * Returns a collection of the ultrasonic adaptor paths
     * @return Collection of ultrasonic adaptor paths
     */
    public HashMap getSensorAdaptorUltrasonicMap() {
        HashMap<String, AdaptorUltrasonic> ultrasonicAdaptors = new HashMap<>();
        for (String path : ultrasonicAdaptorPaths)
            ultrasonicAdaptors.put(path, getSensorAdaptorUltrasonic(path));
        return ultrasonicAdaptors;
    }

    /**
     * Returns the SDK adaptor for the provided path
     * @param path SDK Adaptor path
     * @return SDK Adaptor
     */
    public SDKAdaptor getSDKAdaptor(String path) {
        SDKAdaptor result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (sdkAdaptorPaths.contains(path)) {
            try {
                result = (SDKAdaptor) classLoader.loadClass("eagle.sdkInterface.sdkAdaptors." + path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }

    /**
     * Returns the accelerometer adaptor for the provided path
     * @param path Accelerometer adaptor path
     * @return Accelerometer Adaptor
     */
    public AdaptorAccelerometer getSensorAdaptorAccelerometer(String path) {
        AdaptorAccelerometer result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (accelerometerAdaptorPaths.contains(path)) {
            try {
                result = (AdaptorAccelerometer) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.accelerometerAdaptors." + path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }

    /**
     * Returns the barometer adaptor for the provided path
     * @param path Barometer adaptor path
     * @return Barometer adaptor
     */
    public AdaptorBarometer getSensorAdaptorBarometer(String path) {
        AdaptorBarometer result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (barometerAdaptorPaths.contains(path)) {
            try {
                result = (AdaptorBarometer) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.barometerAdaptors." + path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }

    /**
     * Returns the bearing adaptor for the provided path
     * @param path Bearing adaptor path
     * @return Bearing adaptor
     */
    public AdaptorBearing getSensorAdaptorBearing(String path) {
        AdaptorBearing result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (bearingAdaptorPaths.contains(path)) {
            try {
                result = (AdaptorBearing) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.bearingAdaptors." + path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }

    /**
     * Returns the camera adaptor for the provided path
     * @param path Camera adaptor path
     * @return Camera adaptor
     */
    public AdaptorCamera getSensorAdaptorCamera(String path) {
        AdaptorCamera result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (cameraAdaptorPaths.contains(path)) {
            try {
                result = (AdaptorCamera) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.cameraAdaptors." + path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }

    /**
     * Returns the GPS adaptor for the provided path
     * @param path GPS adaptor path
     * @return GPS adaptor
     */
    public AdaptorGPS getSensorAdaptorGPS(String path) {
        AdaptorGPS result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (gpsAdaptorPaths.contains(path)) {
            try {
                result = (AdaptorGPS) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.gpsAdaptors." + path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }

    /**
     * Return the gyroscope adaptor for the provided path
     * @param path Gyroscope adaptor path
     * @return Gyroscope adaptor
     */
    public AdaptorGyroscope getSensorAdaptorGyroscope(String path) {
        AdaptorGyroscope result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (gyroscopeAdaptorPaths.contains(path)) {
            try {
                result = (AdaptorGyroscope) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.gyroscopeAdaptors." + path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }

    /**
     * Return the LIDAR adaptor for the provided path
     * @param path LIDAR adaptor path
     * @return LIDAR adaptor
     */
    /*public AdaptorLIDAR getSensorAdaptorLIDAR(String path){
        AdaptorLIDAR result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (LIDARAdaptorPaths.contains(path)) {
            try {
                result = (AdaptorLIDAR) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.LIDARAdaptors."+path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }*/

    /**
     * Return the Magnetic adaptor for the provided path
     * @param path Magnetic adaptor path
     * @return Magnetic adaptor
     */
    public AdaptorMagnetic getSensorAdaptorMagnetic(String path) {
        AdaptorMagnetic result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (magneticAdaptorPaths.contains(path)) {
            try {
                result = (AdaptorMagnetic) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.magneticAdaptors." + path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }

    /**
     * Return the RPLIDAR adaptor for the provided path
     * @param path RPLIDAR adaptor path
     * @return RPLIDAR adaptor
     */
    public AdaptorRPLIDAR getSensorAdaptorRPLIDAR(String path) {
        AdaptorRPLIDAR result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (RPLIDARAdaptorPaths.contains(path)) {
            try {
                result = (AdaptorRPLIDAR) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.RPLIDARAdaptors." + path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }

    /**
     * Return the ultrasonic adaptor for the provided path
     * @param path Ultrasonic adaptor path
     * @return Ultrasonic adaptor
     */
    public AdaptorUltrasonic getSensorAdaptorUltrasonic(String path) {
        AdaptorUltrasonic result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (ultrasonicAdaptorPaths.contains(path)) {
            try {
                result = (AdaptorUltrasonic) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.ultrasonicAdaptors." + path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }
}


    /*
    Constructor
    Dynamically finds and instantiates adaptors

    Problem: Code works in java enviroment however fails in the android environment.
    Potential future implementation

    public AdaptorLoader(){

        Reflections reflections = new Reflections("eagle.sdkInterface.sdkAdaptors");
        if(reflections.getStore().keySet().contains("SubTypesScanner")) {
            sdkAdaptors = new HashSet();
            for(String value : reflections.getStore().get("SubTypesScanner").values())
                sdkAdaptors.add(value.replace('eagle.sdkInterface.',''));
        }

        reflections = new Reflections("eagle.sdkInterface.sensorAdaptors.accelerometerAdaptors");
        if(reflections.getStore().keySet().contains("SubTypesScanner")) {
            accelerometerAdaptors = new HashSet();
            for(String value : reflections.getStore().get("SubTypesScanner").values())
                accelerometerAdaptors.add(value.replace('eagle.sdkInterface.',''));
        }

        reflections = new Reflections("eagle.sdkInterface.sensorAdaptors.altimeterAdaptors");
        if(reflections.getStore().keySet().contains("SubTypesScanner")) {
            altimeterAdaptors = new HashSet();
            for(String value : reflections.getStore().get("SubTypesScanner").values())
                altimeterAdaptors.add(value.replace('eagle.sdkInterface.',''));
        }

        reflections = new Reflections("eagle.sdkInterface.sensorAdaptors.cameraAdaptors");
        if(reflections.getStore().keySet().contains("SubTypesScanner")) {
            cameraAdaptors = new HashSet();
            for(String value : reflections.getStore().get("SubTypesScanner").values())
                cameraAdaptors.add(value.replace('eagle.sdkInterface.',''));
        }

        reflections = new Reflections("eagle.sdkInterface.sensorAdaptors.compassAdaptors");
        if(reflections.getStore().keySet().contains("SubTypesScanner")) {
            compassAdaptors = new HashSet();
            for(String value : reflections.getStore().get("SubTypesScanner").values())
                compassAdaptors.add(value.replace('eagle.sdkInterface.',''));
        }

        reflections = new Reflections("eagle.sdkInterface.sensorAdaptors.gyroscopeAdaptors");
        if(reflections.getStore().keySet().contains("SubTypesScanner")) {
            gyroscopeAdaptors = new HashSet();
            for(String value : reflections.getStore().get("SubTypesScanner").values())
                gyroscopeAdaptors.add(value.replace('eagle.sdkInterface.',''));
        }

        reflections = new Reflections("eagle.sdkInterface.sensorAdaptors.RPLIDARAdaptors");
        if(reflections.getStore().keySet().contains("SubTypesScanner")) {
            RPLIDARAdaptors = new HashSet();
            for(String value : reflections.getStore().get("SubTypesScanner").values())
                RPLIDARAdaptors.add(value.replace('eagle.sdkInterface.',''));
        }

        reflections = new Reflections("eagle.sdkInterface.sensorAdaptors.ultrasonicAdaptors");
        if(reflections.getStore().keySet().contains("SubTypesScanner")) {
            ultrasonicAdaptors = new HashSet();
            for(String value : reflections.getStore().get("SubTypesScanner").values())
                ultrasonicAdaptors.add(value.replace('eagle.sdkInterface.',''));
        }

    }*/