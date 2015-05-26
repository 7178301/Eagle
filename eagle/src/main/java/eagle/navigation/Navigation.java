package eagle.navigation;

import eagle.Drone;
import eagle.navigation.positioning.AbsolutePosition;
import eagle.navigation.positioning.RelativePosition;

public class Navigation{

    private Drone drone;
    private AbsolutePosition home;

    public Navigation(Drone drone){
        this.drone=drone;
    }

    public Boolean setHome() {
        this.home=getAbsolutePosition();
        return true;
    }

    public RelativePosition getDistanceToHome(){
        return home.minus(getAbsolutePosition());
    }

    public void flyHome(double speed) {
        drone.adaptor().flyToAbsolute(home, speed);
    }
    public void flyHome() {
        drone.adaptor().flyToAbsolute(home);
    }

    public AbsolutePosition getAbsolutePosition()
    {
        return new AbsolutePosition(drone.adaptor().getLatitude(),drone.adaptor().getLongitude(),drone.adaptor().getAltitude(),drone.adaptor().getRoll(),drone.adaptor().getPitch(),drone.adaptor().getYaw());
    }

}