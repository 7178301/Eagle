package eagle.navigation.positioning;

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
