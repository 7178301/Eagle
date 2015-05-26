package eagle.navigation.positioning;

public class GPSPosition extends AbsolutePosition
{

	public GPSPosition(double longitude, double latitude, double altitude,double roll, double pitch, double yaw)
	{
		super(longitude, latitude, altitude,roll,pitch,yaw);
	}
	
	public GPSPosition add(RelativePosition b){
		return null;
	}
	public GPSPosition minus(RelativePosition b){
		return null;
	}
	public RelativePosition minus(GPSPosition b){
		return null;
	}
	
	public String toString(){
		return "Longitude: "+getLongitude()+", Latitude: "+getLatitude()+", Altitude: "+getAltitude()+", Roll: "+getRoll()+", Pitch: "+getPitch()+", Yaw: "+getYaw();
	}

}
