package eagle.sdkInterface.positioning;

public class GPSPosition extends AbsolutePosition
{

	public GPSPosition(double longitude, double latitude, double altitude) 
	{
		super(longitude, latitude, altitude);
	}
	
	public GPSPosition add(RelativePosition b)
	{
		return null;
	}
	
	public GPSPosition minus(RelativePosition b)
	{
		return null;
	}
	
	public RelativePosition minus(GPSPosition b)
	{
		return null;
	}
	
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Longitude: ");
		sb.append(getLongitude());
		sb.append(", Latitude: ");
		sb.append(getLatitude());
		sb.append(", Altitude: ");
		sb.append(getAltitude());
		
		return sb.toString();
	}

}
