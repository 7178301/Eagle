package eagle.navigation;

import eagle.Drone;

/**
 * Flight Simulation
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @author Cameron Cross [7193432@student.swin.edu.au]
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */
public abstract class TestFlight {

    Drone drone;
    private String name;
    private String description;
    private double maxSpeed = 0;
    private double maxRotateSpeed = 0;

    public TestFlight(Drone drone, String name, String description, double maxSpeed, double maxRotateSpeed) {
        this.drone = drone;
        this.name = name;
        this.description = description;
        this.maxSpeed = maxSpeed;
        this.maxRotateSpeed = maxRotateSpeed;
    }

    public Drone getDrone() {
        return drone;
    }


    public abstract boolean runTestFlight();

}