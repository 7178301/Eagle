package eagle.navigation;

import eagle.Drone;
import eagle.navigation.positioning.Position;

/**
 * API Navigation
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @author Glarah
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */

public class Navigation extends CollisionDetection {

    private Drone drone;
    private Position home;

    /**
     * Create the navigation logic for a drone.
     *
     * @param drone The drone to apply navigation to
     */
    public Navigation(Drone drone) {
        this.drone = drone;
    }

}