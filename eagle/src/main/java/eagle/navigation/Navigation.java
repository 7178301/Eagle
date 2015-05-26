package eagle.navigation;

import eagle.sdkInterface.Drone;
import eagle.sdkInterface.positioning.AbsolutePosition;
import eagle.sdkInterface.positioning.Position;

public class Navigation{

    private Drone drone;
    private AbsolutePosition home;

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



    public double getAltitude() {
        // Read altitude, return it.
        return drone.position.getAltitude();
    }

    public Boolean setHome(AbsolutePosition currentPosition) {
        // make a milestone, call it home.
        try{
            this.home=currentPosition;
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //this needs work
    public double distanceToHome() {
        //use geometry?
        return 0;
    }
    //public RelativePosition getDistanceToHome(){return home.minus(currentPosition);}

    public Boolean flyHome(double speed) {
        flyTo(home, speed);
        return null;
    }

    public Position getPosition()
    {
        return drone.position;
    }

    public boolean start(){
        try{
            drone.init();
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;

    }

    public void wait(double seconds){
        long milliseconds = (long) (seconds*1000);
        try {
            Thread.sleep(milliseconds);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}