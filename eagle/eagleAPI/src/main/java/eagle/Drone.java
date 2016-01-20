package eagle;

/**
 * Drone API
 * Default class for controlling the Eagle API
 *
 * @version 0.0.1
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @author Glarah
 * @author Cameron Cross
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */

import eagle.navigation.Navigation;
import eagle.network.ScriptingEngine;
import eagle.sdkInterface.AdaptorLoader;
import eagle.sdkInterface.SDKAdaptor;

import java.util.HashMap;

public class Drone {


    final private String apiVersion = "0.0.1";

    private SDKAdaptor adaptor = null;
    private Navigation navigation = null;

    /**
     * Initialize Eagle API Logic
     */
    public Drone() {
        this.navigation = new Navigation(this);
    }

    /**
     * Get the current Eagle API version
     * @return Returns the API version
     */
    public String getAPIVersion() {
        return apiVersion;
    }

    /**
     * Returns All the known drone SDK adaptors
     * @return Returns a HashMap of SDK adaptors
     */
    public HashMap getSDKAdaptorMap() {
            return new AdaptorLoader().getSDKAdaptorMap();
    }

    /**
     * Return the current set drone SDK adaptor
     * @return Returns the set SDK Adaptor
     */
    public SDKAdaptor getSDKAdaptor() {
        return this.adaptor;
    }

    /**
     * Set the drone SDK Adaptor to work with
     * @param adaptor Applicable adaptor path (<Manufacturer>.<AdaptorName>)
     */
    public void setSDKAdaptor(String adaptor) {
        AdaptorLoader adaptorLoader = new AdaptorLoader();
        this.adaptor = adaptorLoader.getSDKAdaptor(adaptor);
        this.adaptor.loadDefaultSensorAdaptors(adaptorLoader);
    }
}