/**
 * API Navigation
 *
 * Date Created     09/04/2015
 * Date Modified	26/05/2015 - Nicholas
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 * @author          Glarah
 */

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
    /**Sets home to be the current position*/
    public Boolean setHome() {
        this.home=getAbsolutePosition();
        return true;
    }
    /**Returns the displacement to home.
     *@return String output of MultiMEM*/
    public RelativePosition getDisplacementToHome(){
        return home.minus(getAbsolutePosition());
    }
    /**Returns the displacement to home.
     *@param speed Speed to travel at (m/s)*/
    public void flyHome(double speed) {
        drone.adaptor().flyToAbsolute(home, speed);
    }
    /**Send the drone to the home position*/
    public void flyHome() {
        drone.adaptor().flyToAbsolute(home);
    }
    /**Returns the current position of the drone.
     *@return AbsolutePosition Absolute Position*/
    public AbsolutePosition getAbsolutePosition()
    {
        return new AbsolutePosition(drone.adaptor().getLatitude(),drone.adaptor().getLongitude(),drone.adaptor().getAltitude(),drone.adaptor().getRoll(),drone.adaptor().getPitch(),drone.adaptor().getYaw());
    }

}