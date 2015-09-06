package eagle.sdkInterface;

import eagle.Drone;
import eagle.sdkInterface.sensorAdaptors.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Adaptor Loader
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 17/06/2015
 * <p/>
 * Date Modified	17/06/2015 - Nicholas
 */
public class AdaptorLoader {

    private HashSet<String> sdkAdaptorPaths = new HashSet<>(Arrays.asList("DJI.Phantom2Vision",
            "Flyver.F450Flamewheel", "Simulator.Simulator"));
    private HashSet<String> accelerometerAdaptorPaths = new HashSet<>(Arrays.asList("AndroidAccelerometer"));
    private HashSet<String> cameraAdaptorPaths = new HashSet<>(Arrays.asList("AndroidCamera", "LinkSpriteSEN12804"));
    private HashSet<String> magneticAdaptorPaths = new HashSet<>(Arrays.asList("AndroidMagnetic"));
    private HashSet<String> gpsAdaptorPaths = new HashSet<>(Arrays.asList("AndroidGPS"));
    private HashSet<String> gyroscopeAdaptorPaths = new HashSet<>(Arrays.asList("AndroidGyroscope"));
    //private HashSet<String> LIDARAdaptorPaths = new HashSet<>(Arrays.asList());
    private HashSet<String> RPLIDARAdaptorPaths = new HashSet<>(Arrays.asList("RoboPeakRPLIDARA1M1R1"));
    private HashSet<String> ultrasonicAdaptorPaths = new HashSet<>(Arrays.asList("SeeedStudioSEN10737P"));

    public HashMap getSDKAdaptorMap() {
        HashMap<String, SDKAdaptor> sdkAdaptors = new HashMap<>();
        for (String path : sdkAdaptorPaths)
            sdkAdaptors.put(path, getSDKAdaptor(path));
        return sdkAdaptors;
    }

    public HashMap getSensorAdaptorAccelerometerMap() {
        HashMap<String, AdaptorAccelerometer> accelerometerAdaptors = new HashMap<>();
        for (String path : accelerometerAdaptorPaths)
            accelerometerAdaptors.put(path, getSensorAdaptorAccelerometer(path));
        return accelerometerAdaptors;
    }

    public HashMap getSensorAdaptorListCamera() {
        HashMap<String, AdaptorCamera> cameraAdaptors = new HashMap<>();
        for (String path : cameraAdaptorPaths)
            cameraAdaptors.put(path, getSensorAdaptorCamera(path));
        return cameraAdaptors;
    }

    public HashMap getSensorAdaptorListGPS() {
        HashMap<String, AdaptorGPS> gpsAdaptors = new HashMap<>();
        for (String path : gpsAdaptorPaths)
            gpsAdaptors.put(path, getSensorAdaptorGPS(path));
        return gpsAdaptors;
    }

    public HashMap getSensorAdaptorListGyroscope() {
        HashMap<String, AdaptorGyroscope> gyroscopeAdaptors = new HashMap<>();
        for (String path : gyroscopeAdaptorPaths)
            gyroscopeAdaptors.put(path, getSensorAdaptorGyroscope(path));
        return gyroscopeAdaptors;
    }

    /*public HashMap getSensorAdaptorListLIDAR(){
        HashMap<String,AdaptorLIDAR> LIDARAdaptors = new HashMap<>();
        for(String path:LIDARAdaptorPaths)
            LIDARAdaptors.put(path, getSensorAdaptorLIDAR(path));
        return LIDARAdaptors;
    }*/
    public HashMap getSensorAdaptorListMagnetic() {
        HashMap<String, AdaptorMagnetic> magneticAdaptors = new HashMap<>();
        for (String path : magneticAdaptorPaths)
            magneticAdaptors.put(path, getSensorAdaptorMagnetic(path));
        return magneticAdaptors;
    }

    public HashMap getSensorAdaptorListRPLIDAR() {
        HashMap<String, AdaptorRPLIDAR> RPLIDARAdaptors = new HashMap<>();
        for (String path : RPLIDARAdaptorPaths)
            RPLIDARAdaptors.put(path, getSensorAdaptorRPLIDAR(path));
        return RPLIDARAdaptors;
    }

    public HashMap getSensorAdaptorListUltrasonic() {
        HashMap<String, AdaptorUltrasonic> ultrasonicAdaptors = new HashMap<>();
        for (String path : ultrasonicAdaptorPaths)
            ultrasonicAdaptors.put(path, getSensorAdaptorUltrasonic(path));
        return ultrasonicAdaptors;
    }

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