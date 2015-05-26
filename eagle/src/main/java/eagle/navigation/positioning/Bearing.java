package eagle.navigation.positioning;

public class Bearing 
{
	private Double degrees;
	
	/**
	 * Constructor for bearing class
	 * @param angle input angle
	 */
	public Bearing(double angle) 
	{
		degrees = angle % 360;
		while(degrees < 0) {
			degrees += 360;
		}
	}

	/**
	 * convert to degrees minutes and seconds.
	 * based of: http://zonalandeducation.com/mmts/trigonometryRealms/degMinSec/degMinSec.htm
	 * @return string containing degrees minutes seconds
	 */
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append(degrees.intValue());
		sb.append("\u00B0");
		Double minutes = (degrees - Math.floor(degrees))*60;
		if (minutes != 0) {
			sb.append(minutes.intValue());
			sb.append("\'");
			Double seconds = (minutes - Math.floor(minutes))*60;
			if (seconds != 0) {
				sb.append(Math.round(seconds));
				sb.append("\"");
			}
		}

		return sb.toString();

	}


}
