package eagle.navigation.positioning;

import eagle.navigation.Position;

/** Relative Position which extends Position
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public class RelativePosition extends Position
{
	public RelativePosition(double longitude, double latitude, double altitude,double roll, double pitch, double yaw){
		super(longitude, latitude, altitude,roll, pitch, yaw);
	}
	
	public RelativePosition add(RelativePosition b){
		return new RelativePosition(getLongitude()+b.getLongitude(),getLatitude()+b.getLatitude(),getAltitude()+b.getAltitude(),getRoll()+b.getRoll(),getPitch()+b.getPitch(),getYaw()+b.getYaw());
	}
	
	public RelativePosition minus(RelativePosition b){
		return new RelativePosition(getLongitude()-b.getLongitude(),getLatitude()-b.getLatitude(),getAltitude()-b.getAltitude(),getRoll()-b.getRoll(),getPitch()-b.getPitch(),getYaw()-b.getYaw());
	}
	
	public String toString(){
		return "Longitude: "+getLongitude()+", Latitude: "+getLatitude()+", Altitude: "+getAltitude()+", Roll: "+getRoll()+", Pitch: "+getPitch()+", Yaw: "+getYaw();
	}

}
