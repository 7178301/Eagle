package eagle;

/*
 * Drone
 *
 *
 * for Drone adaptor
 *
 */

import eagle.navigation.Navigation;
import eagle.sdkInterface.Adaptor;
import eagle.sdkInterface.Sensor;

public class Drone {
    final private String apiVersion = "0.0.1";


    private Adaptor adaptor = null;
    private Sensor[] sensors = null;
    private Navigation navigation = null;

    double minSpeed = -0;
    double maxSpeed = -0;

    public Drone(){
        navigation = new Navigation(this);
    }

    public void start(){
        adaptor.init();
        while (!adaptor.getFlagReady()) {
            try {
                wait(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public String getAPIVersion(){
        return apiVersion;
    }

    public Adaptor adaptor(){
        return this.adaptor;
    }
}


//What if a drone needs to constantly update its position?