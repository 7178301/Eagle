package au.edu.swin.sparrow.FlightTests;

import android.util.Log;

import au.edu.swin.sparrow.FlightTest;
import eagle.sdkInterface.SDKAdaptor;

/**
 * Created by cameron on 8/11/15.
 */
public class FlyUpFlyDown extends FlightTest {
    public FlyUpFlyDown(SDKAdaptor sdk) {
        super(sdk);
    }

    @Override
    public void flyRoutine() {
        try {
            sdkAdaptor.changeAltitudeRelative(1);
            sdkAdaptor.delay(1000);
            sdkAdaptor.changeAltitudeRelative(-1);
            sdkAdaptor.delay(1000);
        }
        catch (InterruptedException e) {
            Log.e("FlyUpFlyDown", "Something interupted the wait");
        }
    }
}
