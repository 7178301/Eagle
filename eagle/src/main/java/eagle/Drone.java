package eagle;

/** Drone API
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 * @author          Glarah */

import eagle.navigation.Navigation;
import eagle.sdkInterface.SDKAdaptor;
import eagle.sdkInterface.SensorAdaptor;

public class Drone {
    final private String apiVersion = "0.0.1";


    private SDKAdaptor adaptor = null;
    private SensorAdaptor[] sensorAdaptors = null;
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

    public SDKAdaptor adaptor(){
        return this.adaptor;
    }
}


//What if a drone needs to constantly update its position?