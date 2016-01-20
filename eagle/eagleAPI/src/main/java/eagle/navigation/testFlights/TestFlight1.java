package eagle.navigation.testFlights;

import eagle.Drone;
import eagle.navigation.TestFlight;
import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.PositionMetricDisplacement;
import eagle.sdkInterface.SDKAdaptorCallback;

/**
 * Sample Flight Simulation 1
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */
public class TestFlight1 extends TestFlight {


    public TestFlight1(Drone drone, double speed, double rotation) {
        super(drone, "Initialization Test", "Initializes the drone, starts up the drone props at low speed, stops the props then disconnects", speed, rotation);
    }

    @Override
    public boolean runTestFlight() {
        if (!getDrone().getSDKAdaptor().connectToDrone())
            return false;

        final int[] result = new int[1];
        //try {
        getDrone().getSDKAdaptor().sdkAdaptorStack.add(new PositionMetricDisplacement(0, 0, 1, new Angle(0), new Angle(0), new Angle(0)), 1000);
        getDrone().getSDKAdaptor().sdkAdaptorStack.add(new PositionMetricDisplacement(0, 0, -1, new Angle(0), new Angle(0), new Angle(0)), 1000);
        getDrone().getSDKAdaptor().sdkAdaptorStack.run(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
                if (booleanResult)
                    result[0] = 1;
            }
        });
        while (getDrone().getSDKAdaptor().sdkAdaptorStack.isPaused()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //    }
        //catch (InterruptedException e) {

        //TODO INSERT LOG CLASS CONNECTORE HERE
        //Log.e("FlyUpFlyDown", "Something interupted the wait");
        //    }


        if (!getDrone().getSDKAdaptor().disconnectFromDrone())
            return false;
        else
            return true;
    }
}