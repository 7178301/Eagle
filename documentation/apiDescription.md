#Drone API Design

We provide an Interface class that all modules must implement.

We should provide some kind of version checking, so that as the API is updated, the modules are still usable.

##Data Types
All standard measurement types are incorporated.  
Units only have to be entered in one standard and internal conversions will apply them appropriately.
###Position Types
![Alt text](http://g.gravizo.com/g?
  interface Position {
  	private double longitude;
  	private double latitude;
  	private double altitude;
  	public double getLongitude();
  	public double getLatitude();
  	public double getAltitude();
  }
  class RelativePosition extends Position {
  	public RelativePosition add(RelativePosition b);
  	public RelativePosition minus(RelativePosition b);
  }
  class AbsolutePosition extends Position {
  	public GPSPosition add(RelativePosition b);
  	public GPSPosition minus(RelativePosition b);
  	public RelativePosition minus(GPSPosition b);
  }
  class GPSPosition extends AbsolutePosition {
  	public GPSPosition add(RelativePosition b);
  	public GPSPosition minus(RelativePosition b);
  	public RelativePosition minus(GPSPosition b);
  }
)
###Bearing Type
![Alt text](http://g.gravizo.com/g?
  class Bearing {
  	private Double degrees;
  	Bearing(double);
  	String toString() //output degrees minutes seconds
  	Bearing add(Bearing b)
  }
)

###SDK Interface Module (Drone Interface)
![Alt text](http://g.gravizo.com/g?
  interface DroneAPI {
        static private int apiVersion;
        private AbsolutePosition home;
        private AbsolutePosition currentPosition;
        public int getAPIVersion();
        public abstract void flyTo(Position pos, Bearing bear, Double speed);
        public abstract void takeOff(Position pos);
        public abstract void land(Position pos);
        public void wait(Double time);
        public void setHome(AbsolutePosition pos);
	public Position getPosition();
	public RelativePosition getDistanceToHome();
	public void flyHome(Bearing bear, Double speed);
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
