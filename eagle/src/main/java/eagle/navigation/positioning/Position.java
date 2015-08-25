package eagle.navigation.positioning;
/** Position
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 * @author          Cameron Cross [7193432@student.swin.edu.au]*/

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    public Position(Position position){
        this.longitude=position.longitude;
        this.latitude=position.latitude;
        this.altitude=position.altitude;
        this.roll=position.roll;
        this.pitch=position.pitch;
        this.yaw=position.yaw;
    }

    public double getLongitude(){return this.longitude;};
    public double getLatitude(){return this.latitude;};
    public double getAltitude(){return this.altitude;};
    public double getRoll(){return this.roll;};
    public double getPitch(){return this.pitch;};
    public Bearing getYaw(){return this.yaw;};

    public void addRelative(Position position){
        this.longitude+=position.getLongitude();
        this.latitude+=position.getLatitude();
        this.altitude+=position.getAltitude();
        this.yaw.addRelative(position.getYaw());
    }
    public  void addAbsolute(Position position){
        this.longitude=this.longitude-(this.longitude-position.getLongitude());
        this.latitude=this.latitude-(this.latitude-position.getLatitude());
        this.altitude=this.altitude-(this.altitude-position.getAltitude());
        this.yaw.addAbsolute(position.getYaw());
    }

    public String toString(){
        return "Longitude: "+getLongitude()+", Latitude: "+getLatitude()+", Altitude: "+getAltitude()+", Roll: "+getRoll()+", Pitch: "+getPitch()+", Yaw: "+getYaw();
    }

    public boolean isEqual(Position position) {
        return (Math.abs(position.getAltitude() - altitude) < 0.00001 &&
                Math.abs(position.getLatitude() - latitude) < 0.00001 &&
                Math.abs(position.getLongitude() - longitude) < 0.00001 &&
                Math.abs(position.getPitch() - pitch) < 0.00001 &&
                Math.abs(position.getRoll() - roll) < 0.00001 &&
                position.getYaw().isEqual(yaw));
    }
}
