package eagle.navigation.testFlights;

import eagle.Drone;
import eagle.navigation.TestFlight;

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


        //try {
        getDrone().getSDKAdaptor().changeAltitudeDisplacement(1);
        getDrone().getSDKAdaptor().delay(1000);
        getDrone().getSDKAdaptor().changeAltitudeDisplacement(-1);
        getDrone().getSDKAdaptor().delay(1000);
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