package eagle.sdkInterface.sdkAdaptors;

import dji.sdk.api.DJIDrone;
import eagle.navigation.positioning.Bearing;
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

    public void connectToDrone(){}
    public void disconnectFronDrone(){}
    public void shutdownDrone(){}
    public boolean isConnectedToDrone(){return false;}


    public boolean flyToRelative(RelativePosition position, double speed){return false;}
    public boolean flyToRelative(RelativePosition position){return false;}

    public Boolean flyToAbsolute(AbsolutePosition position, double speed){return false;}
    public Boolean flyToAbsolute(AbsolutePosition position){return false;}

    public boolean changeLongitudeRelative(double altitude,double speed){return false;}
    public boolean changeLongitudeRelative(double altitude){return false;}
    public boolean changeLatitudeRelative(double altitude,double speed){return false;}
    public boolean changeLatitudeRelative(double altitude){return false;}
    public boolean changeAltitudeRelative(double altitude,double speed){return false;}
    public boolean changeAltitudeRelative(double altitude){return false;}
    public boolean changeYawRelative(Bearing yaw,double speed){return false;}
    public boolean changeYawRelative(Bearing yaw){return false;}

    public Boolean changeLongitudeAbsolute(double altitude,double speed){return null;}
    public Boolean changeLongitudeAbsolute(double altitude){return null;}
    public Boolean changeLatitudeAbsolute(double altitude,double speed){return null;}
    public Boolean changeLatitudeAbsolute(double altitude){return null;}
    public Boolean changeAltitudeAbsolute(double altitude,double speed){return null;}
    public Boolean changeAltitudeAbsolute(double altitude){return null;}
    public Boolean changeYawAbsolute(Bearing yaw,double speed){
        return null;
    }
    public Boolean changeYawAbsolute(Bearing yaw){
        return null;
    }


    public double getLongitude(){return 0;}
    public double getLatitude(){return 0;}
    public double getAltitude(){return 0;}
    public double getRoll(){return 0;}
    public double getPitch(){return 0;}
    public double getYaw(){return 0;}
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
