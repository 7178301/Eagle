package eagle.sdkInterface.sdkAdaptors;

import eagle.sdkInterface.SDKAdaptor;
import eagle.navigation.positioning.AbsolutePosition;
import eagle.navigation.positioning.RelativePosition;

/** DJI SDKAdaptor
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public class DJIPhantom2Vision extends SDKAdaptor {

    //TODO Create method implementations

    public DJIPhantom2Vision(){
        super("DJI_SDK_Android","1.0.6","0.0.1");
    }

    public void connect(){};
    public void disconnect(){};
    public void shutdown(){};

    public boolean flyToRelative(RelativePosition position, double speed){return false;};
    public boolean flyToRelative(RelativePosition position){return false;};

    public boolean flyToAbsolute(AbsolutePosition position, double speed){return false;};
    public boolean flyToAbsolute(AbsolutePosition position){return false;};

    public boolean changeLongitudeRelative(double altitude,double speed){return false;};
    public boolean changeLongitudeRelative(double altitude){return false;};
    public boolean changeLatitudeRelative(double altitude,double speed){return false;};
    public boolean changeLatitudeRelative(double altitude){return false;};
    public boolean changeAltitudeRelative(double altitude,double speed){return false;};
    public boolean changeAltitudeRelative(double altitude){return false;};

    public boolean changeLongitudeAbsolute(double altitude,double speed){return false;};
    public boolean changeLongitudeAbsolute(double altitude){return false;};
    public boolean changeLatitudeAbsolute(double altitude,double speed){return false;};
    public boolean changeLatitudeAbsolute(double altitude){return false;};
    public boolean changeAltitudeAbsolute(double altitude,double speed){return false;};
    public boolean changeAltitudeAbsolute(double altitude){return false;};

    public boolean changeYaw(double yaw){return false;};

    public double getLongitude(){return 0;};
    public double getLatitude(){return 0;};
    public double getAltitude(){return 0;};
    public double getRoll(){return 0;};
    public double getPitch(){return 0;};
    public double getYaw(){return 0;};

    //Drones that have a ready flag should return it status otherwise always true.
    public boolean getFlagReady(){return false;};

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
