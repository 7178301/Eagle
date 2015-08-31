package eagle.sdkInterface.sdkAdaptors.DJI;

import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.Bearing;
import eagle.sdkInterface.AdaptorLoader;
import eagle.sdkInterface.SDKAdaptor;

/**
 * DJI SDKAdaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */
public class Phantom2Vision extends SDKAdaptor {

    //TODO Create method implementations

    public Phantom2Vision() {
        super("DJI", "Phantom 2 Vision", "1.0.6", "0.0.1");
    }

    public void loadDefaultSensorAdaptors(AdaptorLoader adaptorLoader) {

    }

    public boolean connectToDrone() {
        return false;
    }

    public boolean disconnectFromDrone() {
        return false;
    }

    public boolean isConnectedToDrone() {
        return false;
    }

    public boolean standbyDrone() {
        return false;
    }

    public boolean resumeDrone() {
        return false;
    }

    public boolean shutdownDrone() {
        return false;
    }

    public boolean flyToAbsolute(Position position, double speed) {
        return false;
    }

    public boolean flyToAbsolute(Position position) {
        return false;
    }


    public Position getPositionInFlight() {
        //TODO CREATE BELOW IMPLEMENTATION
        return new Position(0, 0, 0, 0, 0, new Bearing(0));
    }

    @Override
    public void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


//The following are example implementations
/*
    public boolean flyTo(Position destination, double speed){//default speed?
        if(drone.ready==true){
            if(destination.getAltitude()!=drone.position.getAltitude())
                drone.changeAltitude(destination.getAltitude());//callback?
            drone.linearFlyTo(destination.getLatitude(), destination.getLongitude());
            return true;
        }
        else
            return false;
    }

    public Boolean takeOff(double altitude) {
        if (drone.ready==true){
            drone.changeAltitude(altitude,drone.minSpeed);
            return true;
        }
        else
            return false;
    }

    public Boolean land(double speed) {
        hover(2000);
        //change the altitude for the following
        drone.changeAltitude(home.getAltitude(),drone.minSpeed);//lands in the same altitude as it took off from.
        drone.shutDown();
        return null;
    }

    public Boolean hover(long milsec){
        try{
            wait(milsec);
            //Can we somehow bring the speed to zero?
            return true;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

*/