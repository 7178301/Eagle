package eagle.sdkInterface.positioning;

public class AbsolutePosition extends Position 
{

	public AbsolutePosition(double longitude, double latitude, double altitude) 
	{
		super(longitude, latitude, altitude);
	}
	
	public AbsolutePosition add(RelativePosition b)
	{
		return null;
	}
	
	public AbsolutePosition minus(RelativePosition b)
	{
		return null;
	}
	
	public RelativePosition minus(AbsolutePosition b)
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
