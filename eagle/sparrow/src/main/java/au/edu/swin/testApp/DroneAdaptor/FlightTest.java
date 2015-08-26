package au.edu.swin.testApp.DroneAdaptor;

import eagle.sdkInterface.SDKAdaptor;

/**
 * Created by cameron on 8/11/15.
 */
public abstract class FlightTest {
    protected SDKAdaptor sdkAdaptor;
    protected FlightTest(SDKAdaptor sdk) {
        sdkAdaptor = sdk;
    }

    public void initDrone() {
        sdkAdaptor.connectToDrone();
    }
    public void shutdownDrone() {
        sdkAdaptor.shutdownDrone();
        sdkAdaptor.disconnectFromDrone();
    }

    public void runTest() {
        initDrone();
        flyRoutine();
        shutdownDrone();
    }

    public abstract void flyRoutine();
}
