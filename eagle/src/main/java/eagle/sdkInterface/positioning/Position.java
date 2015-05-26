package eagle.sdkInterface.positioning;

public abstract class Position
{
	private double longitude;
	private double latitude;
	private double altitude;
	
	public Position(double longitude, double latitude, double altitude) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
	}
	
	public double getLongitude() 
	{
		return longitude;
	}
	
	public double getLatitude() 
	{
		return latitude;
	}
	
	public double getAltitude() 
	{
		return altitude;
	}
	

}
