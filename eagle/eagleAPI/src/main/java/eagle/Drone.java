package eagle;

import eagle.navigation.Navigation;
import eagle.sdkInterface.AdaptorLoader;
import eagle.sdkInterface.SDKAdaptor;

import java.util.HashMap;

/**
 * Drone API
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @author Glarah
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */

public class Drone {
    final private String apiVersion = "0.0.1";


    private SDKAdaptor adaptor = null;
    private Navigation navigation = null;
    private AdaptorLoader adaptorLoader = null;

    double minSpeed = -0;
    double maxSpeed = -0;

    public Drone() {
        this.adaptorLoader = new AdaptorLoader();
        this.navigation = new Navigation(this);
    }

    public String getAPIVersion() {
        return apiVersion;
    }

    public HashMap getSDKAdaptorMap() {
        if (this.adaptorLoader != null)
            return this.adaptorLoader.getSDKAdaptorMap();
        else
            return new AdaptorLoader().getSDKAdaptorMap();
    }

    public SDKAdaptor getSDKAdaptor() {
        return this.adaptor;
    }

    public void setSDKAdaptor(String adaptor) {
        this.adaptor = this.adaptorLoader.getSDKAdaptor(adaptor);
        this.adaptor.loadDefaultSensorAdaptors(adaptorLoader);
    }

}


//What if a drone needs to constantly update its position?