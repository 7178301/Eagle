package eagle.navigation;

import eagle.Drone;
import eagle.navigation.positioning.PositionMetric;

/** API Navigation
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 * @author          Glarah */

public class Navigation extends CollisionDetection{

    private Drone drone;
    private PositionMetric home;

    /**Create the navigation logic for a drone.
     * @param drone The drone to apply navigation to*/
    public Navigation(Drone drone){
        this.drone=drone;
    }

}