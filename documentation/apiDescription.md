#Drone API Design

We provide an Interface class that all modules must implement.

We should provide some kind of version checking, so that as the API is updated, the modules are still usable.

##Data Types
All standard measurement types are incorporated.  
Units only have to be entered in one standard and internal conversions will apply them appropriately.
###Position Types
![Alt text](http://g.gravizo.com/g?
  abstract Position {
  	private double longitude;
  	private double latitude;
  	private double altitude;
  	private double roll;
  	private double pitch;
  	private double yaw;
  	public Position();
  	public double getLongitude();
  	public double getLatitude();
  	public double getAltitude();
  	public double getRoll();
  	public double getPitch();
  	public double getYaw();
  	public String toString();
  }
  class RelativePosition extends Position {
  	public RelativePosition add(RelativePosition b);
  	public RelativePosition minus(RelativePosition b);
  	public String toString();
  }
  class AbsolutePosition extends Position {
  	public GPSPosition add(RelativePosition b);
  	public GPSPosition minus(RelativePosition b);
  	public RelativePosition minus(GPSPosition b);
  	public String toString();
  }
  class Bearing {
  	private Double degrees;
  	Bearing(double);
  	String toString() //output degrees minutes seconds
  	Bearing add(Bearing b)
  }
)


###SDK Adaptor (Drone Interface)
![Alt text](http://g.gravizo.com/g?
  abstract DroneAPI {  
  	private String sdkVersion;
  	private String adaptorVersion;
  	public abstract void init();
  	public abstract void shutDown();
  	public abstract String getAdaptorVersion();
  	public abstract boolean flyToRelative(RelativePosition position, double speed);
  	public abstract boolean flyToRelative(RelativePosition position);
  	public abstract boolean flyToAbsolute(AbsolutePosition position, double speed);
  	public abstract boolean flyToAbsolute(AbsolutePosition position);
  	public abstract boolean changeLongitudeRelative(double altitude,double speed);
  	public abstract boolean changeLongitudeRelative(double altitude);
  	public abstract boolean changeLatitudeRelative(double altitude,double speed);
  	public abstract boolean changeLatitudeRelative(double altitude);
  	public abstract boolean changeAltitudeRelative(double altitude,double speed);
  	public abstract boolean changeAltitudeRelative(double altitude);
  	public abstract boolean changeLongitudeAbsolute(double altitude,double speed);
  	public abstract boolean changeLongitudeAbsolute(double altitude);
  	public abstract boolean changeLatitudeAbsolute(double altitude,double speed);
  	public abstract boolean changeLatitudeAbsolute(double altitude);
  	public abstract boolean changeAltitudeAbsolute(double altitude,double speed);
  	public abstract boolean changeAltitudeAbsolute(double altitude);
  	public abstract boolean changeYaw(double yaw);
  	public abstract double getLongitude();
  	public abstract double getLatitude();
  	public abstract double getAltitude();
  	public abstract double getRoll();
  	public abstract double getPitch();
  	public abstract double getYaw();
  	public abstract boolean getFlagReady();
  }
)
###Camera Interface Module
![Alt text](http://g.gravizo.com/g?
  interface CameraAPI {
  	void initCamera();
  	void takePhoto();
  }
)
###Range Finder Interface Module
![Alt text](http://g.gravizo.com/g?
  interface RangeFinder {
  	void initRangeFinder();
  	double getDistance(Bearing direction);
  }
)
