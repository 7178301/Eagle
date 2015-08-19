package eagle.sdkInterface.sdkAdaptors;

import eagle.navigation.positioning.Bearing;
import eagle.navigation.positioning.Position;
import eagle.sdkInterface.AdaptorLoader;
import eagle.sdkInterface.SDKAdaptor;

/** Flyver SDKAdaptor
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 * @author          Cameron Cross [7193432@student.swin.edu.au]*/
public class Flyver extends SDKAdaptor {
    public static String adapterVersion;

    //TODO Create method implementations

    public Flyver(){
        super("Flyver-SDK","alpha","0.0.1");
    }
    public void loadDefaultAdaptors(AdaptorLoader adaptorLoader){
        addAdaptorAccelerometer(adaptorLoader.getAdaptorAccelerometer("AndroidAccelerometer"));
        addAdaptorAltimeter(adaptorLoader.getAdaptorAltimeter("AndroidAltimeter"));
        addAdaptorCamera(adaptorLoader.getAdaptorCamera("AndroidCamera"));
        addAdaptorCompass(adaptorLoader.getAdaptorCompass("AndroidCompass"));
        addAdaptorGyroscope(adaptorLoader.getAdaptorGyroscope("AndroidGyroscope"));
    }

    public boolean connectToDrone(){return false;}
    public boolean disconnectFromDrone(){return false;}
    public boolean isConnectedToDrone(){return false;}

    public boolean standbyDrone(){return false;}
    public boolean resumeDrone(){return false;}
    public boolean shutdownDrone(){return false;}

    public boolean flyToAbsolute(Position position, double speed){return false;}
    public boolean flyToAbsolute(Position position){return false;}

    public Position getPositionInFlight(){
        //TODO CREATE BELOW IMPLEMENTATION
        return new Position(0,0,0,0,0,new Bearing(0));
    }

    @Override
    public void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateCurrentPosition(){};


}