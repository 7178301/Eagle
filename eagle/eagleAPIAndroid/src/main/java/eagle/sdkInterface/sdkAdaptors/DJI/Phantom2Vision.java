package eagle.sdkInterface.sdkAdaptors.DJI;

import eagle.navigation.positioning.PositionMetric;
import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.PositionGPS;
import eagle.sdkInterface.AdaptorLoader;
import eagle.sdkInterface.SDKAdaptor;

/** DJI SDKAdaptor
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public class Phantom2Vision extends SDKAdaptor {

    //TODO Create method implementations

    public Phantom2Vision(){
        super("DJI","Phantom 2 Vision","1.0.6","0.0.1");
    }

    public void loadDefaultSensorAdaptors(AdaptorLoader adaptorLoader){

    }

    public boolean connectToDrone(){return false;}
    public boolean disconnectFromDrone(){return false;}
    public boolean isConnectedToDrone(){return false;}

    public boolean standbyDrone(){return false;}
    public boolean resumeDrone(){return false;}
    public boolean shutdownDrone(){return false;}

    @Override
    public boolean flyToRelative(PositionMetric position, double speed) {
        return false;
    }

    @Override
    public boolean flyToRelative(PositionMetric position) {
        return false;
    }

    public boolean flyToGPS(PositionGPS positionGPS, double speed){return false;}
    public boolean flyToGPS(PositionGPS positionGPS){return false;}


    public PositionMetric getPositionInFlight(){
        //TODO CREATE BELOW IMPLEMENTATION
        return new PositionMetric(0,0,0,new Angle(0),new Angle(0),new Angle(0));
    }

    @Override
    public void delay(int milliseconds){
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
