package eagle.sdkInterface;

/*
 * Drone Interface
 * Created by Lara
 *
 * Interface for Drone adaptor
 *
 */

import eagle.sdkInterface.positioning.Position;

public interface Drone {
	boolean ready = false;
	Position position = null;
	double minSpeed = 0;
	double maxSpeed = 0;
	String sdkVersion = null;

	void init();
	boolean linearFlyTo(double latitude, double longtitude, double speed);
	boolean linearFlyTo(double latitude, double longtitude);
	boolean changeAltitude(double altitude,double speed);
	boolean changeAltitude(double altitude);
	double getSpeed();
	void shutDown();
}


//What if a drone needs to constantly update its position?