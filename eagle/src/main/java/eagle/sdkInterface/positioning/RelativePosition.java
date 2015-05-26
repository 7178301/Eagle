package eagle.sdkInterface.positioning;

public class RelativePosition extends Position
{
	public RelativePosition(double longitude, double latitude, double altitude) 
	{
		super(longitude, latitude, altitude);
	}
	
	public RelativePosition add(RelativePosition b)
	{
		RelativePosition rp = new RelativePosition(getLongitude()+b.getLongitude(), 
				getLatitude()+b.getLatitude(), 
				getAltitude()+b.getAltitude());
		return rp;
	}
	
	public RelativePosition minus(RelativePosition b)
	{
		RelativePosition rp = new RelativePosition(getLongitude()-b.getLongitude(), 
				getLatitude()-b.getLatitude(), 
				getAltitude()-b.getAltitude());
		return rp;
	}
	
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("X: ");
		sb.append(getLongitude());
		sb.append(", Y: ");
		sb.append(getLatitude());
		sb.append(", Z: ");
		sb.append(getAltitude());
		
		return sb.toString();
	}

}
