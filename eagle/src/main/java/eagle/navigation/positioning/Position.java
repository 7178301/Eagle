package eagle.navigation.positioning;
/** Position
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public abstract class Position{
	private double longitude;
	private double latitude;
	private double altitude;
    private double roll;
	private double pitch;
	private double yaw;

    public Position(double longitude, double latitude, double altitude,double roll, double pitch, double yaw){
        this.longitude=longitude;
        this.latitude=latitude;
        this.altitude=altitude;
        this.roll=roll;
        this.pitch=pitch;
        this.yaw=yaw;
    }

    public double getLongitude(){return this.longitude;};
    public double getLatitude(){return this.latitude;};
    public double getAltitude(){return this.altitude;};
    public double getRoll(){return this.roll;};
    public double getPitch(){return this.pitch;};
    public double getYaw(){return this.yaw;};

    public abstract String toString();
}
