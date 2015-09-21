package eagle;

/**
 * Drone API
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
import eagle.sdkInterface.AdaptorLoader;
import eagle.sdkInterface.SDKAdaptor;

import java.util.HashMap;

public class Drone {


    final private String apiVersion = "0.0.1";


    private SDKAdaptor adaptor = null;
    private Navigation navigation = null;
    private AdaptorLoader adaptorLoader = null;

    private ScriptingEngine scriptingEngine = null;

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
        else {
            this.adaptorLoader = new AdaptorLoader();
            return adaptorLoader.getSDKAdaptorMap();
        }
    }

    public SDKAdaptor getSDKAdaptor() {
        return this.adaptor;
    }

    public void setSDKAdaptor(String adaptor) {
        this.adaptor = this.adaptorLoader.getSDKAdaptor(adaptor);
        this.adaptor.loadDefaultSensorAdaptors(adaptorLoader);
        this.scriptingEngine = new ScriptingEngine(this.adaptor);
    }

    public ScriptingEngine getScriptingEngine() {
        return this.scriptingEngine;
    }
}

//What if a drone needs to constantly update its position?