package eagle.navigation.positioning;
/** Position
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 * @author          Cameron Cross [7193432@student.swin.edu.au]*/
public class Position{
	private double longitude;
	private double latitude;
	private double altitude;
    private double roll;
	private double pitch;
	private Bearing yaw;

    public Position(double longitude, double latitude, double altitude,double roll, double pitch, Bearing yaw){
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
    public Bearing getYaw(){return this.yaw;};

    public Position add(Position position){
        return new Position(this.longitude+position.longitude,this.latitude+position.getLatitude(),this.altitude+position.getAltitude(),this.roll+position.getRoll(),this.pitch+position.getPitch(),this.yaw.add(position.getYaw()));
    }
    public Position minus(Position position){
        return new Position(this.longitude-position.getLongitude(),this.latitude-position.getLatitude(),this.altitude-position.getAltitude(),this.roll-position.getRoll(),this.pitch-position.getPitch(),this.yaw.add(position.getYaw()));
    }

    public String toString(){
        return "Longitude: "+getLongitude()+", Latitude: "+getLatitude()+", Altitude: "+getAltitude()+", Roll: "+getRoll()+", Pitch: "+getPitch()+", Yaw: "+getYaw();
    }
}
