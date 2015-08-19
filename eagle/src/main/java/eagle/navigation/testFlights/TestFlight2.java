package eagle.navigation.testFlights;

import eagle.Drone;
import eagle.navigation.TestFlight;

/** Sample Flight Simulation 2
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public class TestFlight2 extends TestFlight {




    public TestFlight2(Drone drone,double speed, double rotation){
        super(drone,"TITLE","DESCRIPTION",speed,rotation);
    }

    @Override
    public boolean runTestFlight() {
        if (!getDrone().getAdaptor().connectToDrone())
            return false;



        //try {
        getDrone().getAdaptor().changeAltitudeRelative(1);
        getDrone().getAdaptor().delay(1000);
        getDrone().getAdaptor().changeAltitudeRelative(-1);
        getDrone().getAdaptor().delay(1000);
        //    }
        //catch (InterruptedException e) {

        //TODO INSERT LOG CLASS CONNECTORE HERE
        //Log.e("FlyUpFlyDown", "Something interupted the wait");
        //    }


        if (!getDrone().getAdaptor().disconnectFromDrone())
            return false;
        else
            return true;
    }

}