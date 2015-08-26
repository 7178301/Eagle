package eagle.sdkInterface;

import eagle.Drone;
import eagle.sdkInterface.sensorAdaptors.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/** Adaptor Loader
 * @since     17/06/2015
 * <p>
 * Date Modified	17/06/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public class AdaptorLoader {

    private HashSet<String> sdkAdaptorPaths = new HashSet<>(Arrays.asList("DJI.Phantom2Vision",
            "Flyver.F450Flamewheel","Simulator.Simulator"));
    //private HashSet<String> LIDARAdaptorPaths = new HashSet<>(Arrays.asList());
    private HashSet<String> accelerometerAdaptorPaths = new HashSet<>(Arrays.asList("AndroidAccelerometer"));
    private HashSet<String> altimeterAdaptorPaths = new HashSet<>(Arrays.asList("AndroidAltimeter"));
    private HashSet<String> cameraAdaptorPaths = new HashSet<>(Arrays.asList("AndroidCamera", "LinkSpriteSEN12804"));
    private HashSet<String> compassAdaptorPaths = new HashSet<>(Arrays.asList("AndroidCompass"));
    private HashSet<String> gyroscopeAdaptorPaths = new HashSet<>(Arrays.asList("AndroidGyroscope"));
    private HashSet<String> RPLIDARAdaptorPaths = new HashSet<>(Arrays.asList("RoboPeakRPLIDARA1M1R1"));
    private HashSet<String> ultrasonicAdaptorPaths = new HashSet<>(Arrays.asList("SeeedStudioSEN10737P"));

    public HashMap getSDKAdaptorMap(){
        HashMap<String,SDKAdaptor> sdkAdaptors = new HashMap<>();
        for(String path:sdkAdaptorPaths)
            sdkAdaptors.put(path, getSDKAdaptor(path));
        return sdkAdaptors;
    }
    public HashMap getSensorAdaptorAccelerometerMap(){
        HashMap<String,Accelerometer> accelerometerAdaptors = new HashMap<>();
        for(String path:accelerometerAdaptorPaths)
            accelerometerAdaptors.put(path, getSensorAdaptorAccelerometer(path));
        return accelerometerAdaptors;
    }
    public HashMap getSensorAdaptorAltimeterMap(){
        HashMap<String,Altimeter> altimeterAdaptors = new HashMap<>();
        for(String path:altimeterAdaptorPaths)
            altimeterAdaptors.put(path, getSensorAdaptorAltimeter(path));
        return altimeterAdaptors;
    }
    public HashMap getSensorAdaptorListCamera(){
        HashMap<String,Camera> cameraAdaptors = new HashMap<>();
        for(String path:cameraAdaptorPaths)
            cameraAdaptors.put(path, getSensorAdaptorCamera(path));
        return cameraAdaptors;
    }
    public HashMap getSensorAdaptorListCompass(){
        HashMap<String,Compass> compassAdaptors = new HashMap<>();
        for(String path:compassAdaptorPaths)
            compassAdaptors.put(path, getSensorAdaptorCompass(path));
        return compassAdaptors;
    }
    public HashMap getSensorAdaptorListGyroscope(){
        HashMap<String,Gyroscope> gyroscopeAdaptors = new HashMap<>();
        for(String path:gyroscopeAdaptorPaths)
            gyroscopeAdaptors.put(path, getSensorAdaptorGyroscope(path));
        return gyroscopeAdaptors;
    }
    /*public HashMap getSensorAdaptorListLIDAR(){
        HashMap<String,RPLIDAR> LIDARAdaptors = new HashMap<>();
        for(String path:LIDARAdaptorPaths)
            LIDARAdaptors.put(path, getSensorAdaptorLIDAR(path));
        return LIDARAdaptors;
    }*/
    public HashMap getSensorAdaptorListRPLIDAR(){
        HashMap<String,RPLIDAR> RPLIDARAdaptors = new HashMap<>();
        for(String path:RPLIDARAdaptorPaths)
            RPLIDARAdaptors.put(path, getSensorAdaptorRPLIDAR(path));
        return RPLIDARAdaptors;
    }
    public HashMap getSensorAdaptorListUltrasonic(){
        HashMap<String,Ultrasonic> ultrasonicAdaptors = new HashMap<>();
        for(String path:ultrasonicAdaptorPaths)
            ultrasonicAdaptors.put(path, getSensorAdaptorUltrasonic(path));
        return ultrasonicAdaptors;
    }

    public SDKAdaptor getSDKAdaptor(String path){
        SDKAdaptor result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (sdkAdaptorPaths.contains(path)) {
            try {
                result = (SDKAdaptor) classLoader.loadClass("eagle.sdkInterface.sdkAdaptors."+path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }
    public Accelerometer getSensorAdaptorAccelerometer(String path){
        Accelerometer result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (accelerometerAdaptorPaths.contains(path)) {
            try {
                result = (Accelerometer) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.accelerometerAdaptors."+path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }
    public Altimeter getSensorAdaptorAltimeter(String path){
        Altimeter result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (altimeterAdaptorPaths.contains(path)) {
            try {
                result = (Altimeter) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.altimeterAdaptors."+path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }
    public Camera getSensorAdaptorCamera(String path){
        Camera result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (cameraAdaptorPaths.contains(path)) {
            try {
                result = (Camera) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.cameraAdaptors."+path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }
    public Compass getSensorAdaptorCompass(String path){
        Compass result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (compassAdaptorPaths.contains(path)) {
            try {
                result = (Compass) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.compassAdaptors."+path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }
    public Gyroscope getSensorAdaptorGyroscope(String path){
        Gyroscope result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (gyroscopeAdaptorPaths.contains(path)) {
            try {
                result = (Gyroscope) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.gyroscopeAdaptors."+path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }
    /*public RPLIDAR getSensorAdaptorLIDAR(String path){
        LIDAR result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (LIDARAdaptorPaths.contains(path)) {
            try {
                result = (LIDAR) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.LIDARAdaptors."+path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }*/
    public RPLIDAR getSensorAdaptorRPLIDAR(String path){
        RPLIDAR result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (RPLIDARAdaptorPaths.contains(path)) {
            try {
                result = (RPLIDAR) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.RPLIDARAdaptors."+path).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }
    public Ultrasonic getSensorAdaptorUltrasonic(String path){
        Ultrasonic result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (ultrasonicAdaptorPaths.contains(path)) {
            try {
                result = (Ultrasonic) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.ultrasonicAdaptors." +path).newInstance();
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