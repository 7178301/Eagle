package eagle.sdkInterface.sdkAdaptors;

import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.Bearing;
import eagle.sdkInterface.SDKAdaptor;

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

    public boolean connectToDrone(){return false;}
    public boolean disconnectFromDrone(){return false;}
    public boolean isConnectedToDrone(){return false;}

    public boolean standbyDrone(){return false;}
    public boolean resumeDrone(){return false;}
    public boolean shutdownDrone(){return false;}


    public boolean flyToRelative(Position position, double speed){return false;}
    public boolean flyToRelative(Position position){return false;}

    public Boolean flyToAbsolute(Position position, double speed){return false;}
    public Boolean flyToAbsolute(Position position){return false;}

    public boolean changeLongitudeRelative(double altitude,double speed){return false;}
    public boolean changeLongitudeRelative(double altitude){return false;}
    public boolean changeLatitudeRelative(double altitude,double speed){return false;}
    public boolean changeLatitudeRelative(double altitude){return false;}
    public boolean changeAltitudeRelative(double altitude,double speed){return false;}
    public boolean changeAltitudeRelative(double altitude){return false;}
    public boolean changeYawRelative(Bearing yaw,double speed){return false;}
    public boolean changeYawRelative(Bearing yaw){return false;}

    public boolean changeLongitudeAbsolute(double altitude,double speed){
        return false;
    }
    public boolean changeLongitudeAbsolute(double altitude){
        return false;
    }
    public boolean changeLatitudeAbsolute(double altitude,double speed){
        return false;
    }
    public boolean changeLatitudeAbsolute(double altitude){
        return false;
    }
    public boolean changeAltitudeAbsolute(double altitude,double speed){
        return false;
    }
    public boolean changeAltitudeAbsolute(double altitude){
        return false;
    }
    public boolean changeYawAbsolute(Bearing yaw,double speed){
        return false;
    }
    public boolean changeYawAbsolute(Bearing yaw){
        return false;
    }

    public void updateCurrentPosition(){};
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
