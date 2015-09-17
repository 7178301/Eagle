package au.edu.swin.pidgin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

import eagle.Drone;
import eagle.Log;
import eagle.TelnetServer;
import eagle.sdkInterface.AdaptorLoader;
import eagle.sdkInterface.sensorAdaptors.gpsAdaptors.JavaGPS;

public class Pidgin implements Log.LogCallback {
    Drone drone = new Drone();

    Pidgin(String adaptor) throws InterruptedException {
        drone.setSDKAdaptor(adaptor);
        TelnetServer ts = new TelnetServer(drone);
        Thread trd = new Thread(ts);
        trd.start();
        Log.addCallback(this);
        trd.join();
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        JavaGPS javaGPS = new JavaGPS();
        javaGPS.connectToSensor();
        Log.log(javaGPS.getData().toStringLong());

        AdaptorLoader adaptorLoader = new AdaptorLoader();
        HashMap sdkAdaptorMap = adaptorLoader.getSDKAdaptorMap();
        Set<String> set = sdkAdaptorMap.keySet();
        System.out.println("Choose your adaptor:");
        for (int i = 0; i < set.size(); i++) {
            System.out.println(i+") "+set.toArray()[i]);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int adaptor = Integer.parseInt(reader.readLine());
        Pidgin pidgin = new Pidgin((String) set.toArray()[adaptor]);
    }

    @Override
    public void handleMessage(String message) {
        System.out.println(message);
    }


}
