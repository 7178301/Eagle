package eagle.sdkInterface;

import eagle.Drone;
import eagle.sdkInterface.sensorAdaptors.*;

import java.util.HashSet;

/** Adaptor Loader
 * @since     17/06/2015
 * <p>
 * Date Modified	17/06/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public class AdaptorLoader {

    HashSet<String> sdkAdaptors = null;
    HashSet<String> accelerometerAdaptors = null;
    HashSet<String> altimeterAdaptors = null;
    HashSet<String> cameraAdaptors = null;
    HashSet<String> compassAdaptors = null;
    HashSet<String> gyroscopeAdaptors = null;
    HashSet<String> RPLIDARAdaptors = null;
    HashSet<String> ultrasonicAdaptors = null;

    public AdaptorLoader(){
        sdkAdaptors= new HashSet<>();
        accelerometerAdaptors= new HashSet<>();
        altimeterAdaptors= new HashSet<>();
        cameraAdaptors= new HashSet<>();
        compassAdaptors= new HashSet<>();
        gyroscopeAdaptors= new HashSet<>();
        RPLIDARAdaptors= new HashSet<>();
        ultrasonicAdaptors= new HashSet<>();

        sdkAdaptors.add("DJIPhantom.DJIPhantom2Vision");
        sdkAdaptors.add("Flyver.Flyver");
        sdkAdaptors.add("Simulator.Simulator");
        accelerometerAdaptors.add("AndroidAccelerometer");
        altimeterAdaptors.add("AndroidAltimeter");
        cameraAdaptors.add("AndroidCamera");
        cameraAdaptors.add("LinkSpriteSEN12804");
        compassAdaptors.add("AndroidCompass");
        gyroscopeAdaptors.add("AndroidGyroscope");
        RPLIDARAdaptors.add("RoboPeakRPLIDARA1M1R1");
        ultrasonicAdaptors.add("SeeedStudioSEN10737P");
    }

    public HashSet getAdaptorListSDKs(){
        return sdkAdaptors;
    }
    public HashSet getAdaptorListAccelerometer(){
        return accelerometerAdaptors;
    }
    public HashSet getAdaptorListAltimeter(){
        return altimeterAdaptors;
    }
    public HashSet getAdaptorListCamera(){
        return cameraAdaptors;
    }
    public HashSet getAdaptorListCompass(){
        return compassAdaptors;
    }
    public HashSet getAdaptorListGyroscope(){
        return gyroscopeAdaptors;
    }
    public HashSet getAdaptorListRPLIDAR(){
        return RPLIDARAdaptors;
    }
    public HashSet getAdaptorListUltrasonic(){
        return ultrasonicAdaptors;
    }

    public SDKAdaptor getAdaptorSDKs(String adaptor){
        SDKAdaptor result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (sdkAdaptors.contains(adaptor)) {
            try {
                result = (SDKAdaptor) classLoader.loadClass("eagle.sdkInterface.sdkAdaptors."+adaptor).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }
    public Accelerometer getAdaptorAccelerometer(String adaptor){
        Accelerometer result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (accelerometerAdaptors.contains(adaptor)) {
            try {
                result = (Accelerometer) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.accelerometerAdaptors."+adaptor).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }
    public Altimeter getAdaptorAltimeter(String adaptor){
        Altimeter result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (altimeterAdaptors.contains(adaptor)) {
            try {
                result = (Altimeter) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.altimeterAdaptors."+adaptor).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }
    public Camera getAdaptorCamera(String adaptor){
        Camera result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (cameraAdaptors.contains(adaptor)) {
            try {
                result = (Camera) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.cameraAdaptors."+adaptor).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }
    public Compass getAdaptorCompass(String adaptor){
        Compass result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (compassAdaptors.contains(adaptor)) {
            try {
                result = (Compass) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.compassAdaptors."+adaptor).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }
    public Gyroscope getAdaptorGyroscope(String adaptor){
        Gyroscope result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (gyroscopeAdaptors.contains(adaptor)) {
            try {
                result = (Gyroscope) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.gyroscopeAdaptors."+adaptor).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }
    public RPLIDAR getAdaptorRPLIDAR(String adaptor){
        RPLIDAR result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (RPLIDARAdaptors.contains(adaptor)) {
            try {
                result = (RPLIDAR) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.RPLIDARAdaptors."+adaptor).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }
    public Ultrasonic getAdaptorUltrasonic(String adaptor){
        Ultrasonic result = null;
        ClassLoader classLoader = Drone.class.getClassLoader();
        if (ultrasonicAdaptors.contains(adaptor)) {
            try {
                result = (Ultrasonic) classLoader.loadClass("eagle.sdkInterface.sensorAdaptors.ultrasonicAdaptors." +adaptor).newInstance();
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