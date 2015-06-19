package eagle;

/** Drone API
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 * @author          Glarah */

import eagle.navigation.Navigation;
import eagle.sdkInterface.AdaptorLoader;
import eagle.sdkInterface.SDKAdaptor;
import eagle.sdkInterface.sdkAdaptors.Flyver;

import java.util.HashSet;

public class Drone {
    final private String apiVersion = "0.0.1";


    private SDKAdaptor adaptor = null;
    private Navigation navigation = null;
    private AdaptorLoader adaptorLoader = null;

    double minSpeed = -0;
    double maxSpeed = -0;

    public Drone(){
        this.adaptorLoader = new AdaptorLoader();
    }

    public void start(){
        adaptor.connect();
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

    public SDKAdaptor getAdaptor(){
        return this.adaptor;
    }
    public HashSet getSDKAdaptorList(){
        return this.adaptorLoader.getSDKAdaptorList();
    }
    public void setAdaptor(String adaptor){
        this.adaptor = this.adaptorLoader.getSDKAdaptor(adaptor);
        this.navigation = new Navigation(this);
    }

}


//What if a drone needs to constantly update its position?