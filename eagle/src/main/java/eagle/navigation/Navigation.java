package eagle.navigation;

import eagle.Drone;
import eagle.navigation.positioning.AbsolutePosition;
import eagle.navigation.positioning.RelativePosition;

/** API Navigation
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 * @author          Glarah */

public class Navigation extends CollisionDetection{

    private Drone drone;
    private AbsolutePosition home;

    /**Create the navigation logic for a drone.
     * @param drone The drone to apply navigation to*/
    public Navigation(Drone drone){
        this.drone=drone;
    }
    /**Sets home to be the current position*/
    public Boolean setHome() {
        this.home=getAbsolutePosition();
        return true;
    }
    /**Returns the displacement to home.
     *@return RelativePosition Relative Position from home*/
    public RelativePosition getDisplacementToHome(){
        return home.minus(getAbsolutePosition());
    }
    /**Returns the displacement to home.
     *@param speed Speed to travel at (m/s)*/
    public void flyHome(double speed) {
        drone.adaptor().flyToAbsolute(home, speed);
    }
    /**Sends the drone to the home position*/
    public void flyHome() {
        drone.adaptor().flyToAbsolute(home);
    }
    /**Returns the current position of the drone.
     *@return AbsolutePosition Absolute Position*/
    public AbsolutePosition getAbsolutePosition(){
        return new AbsolutePosition(drone.adaptor().getLatitude(),drone.adaptor().getLongitude(),drone.adaptor().getAltitude(),drone.adaptor().getRoll(),drone.adaptor().getPitch(),drone.adaptor().getYaw());
    }

}