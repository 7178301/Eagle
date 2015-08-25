package eagle.navigation.positioning;
/** Bearing
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 * @author          Cameron Cross */
public class Bearing 
{
	private double degrees;
	
	/**
	 * Constructor for bearing class
	 * @param angle input angle
	 */
	public Bearing(double angle){
		degrees = angle % 360;
		while(degrees < 0) {
			degrees += 360;
		}
	}
	public Bearing(Bearing bearing){
		this.degrees=bearing.getDegrees();
	}

	public void addRelative(Bearing val) {
		//TODO 360 Degree Boundry Calculation
		this.degrees+=val.getDegrees();
	}
	public void addAbsolute(Bearing bearing){
		//TODO 360 Degree Boundry Calculation
		this.degrees=this.degrees-(this.degrees-bearing.getDegrees());
	}

	public double getDegrees() {
		return degrees;
	}

	/**
	 * convert to degrees minutes and seconds.
	 * based of: http://zonalandeducation.com/mmts/trigonometryRealms/degMinSec/degMinSec.htm
	 * @return string containing degrees minutes seconds
	 */
	//TODO Incorrectly Coded. Seconds can exist when minute is 0
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(new Double(degrees).intValue());
		sb.append("\u00B0");
		Double minutes = (degrees - Math.floor(degrees))*60;
		if (minutes != 0){
			sb.append(minutes.intValue());
			sb.append("\'");
			Double seconds = (minutes - Math.floor(minutes))*60;
			if (seconds != 0){
				sb.append(Math.round(seconds));
				sb.append("\"");
			}
		}
		return sb.toString();
	}

	public boolean isEqual(Bearing bearing) {
		return Math.abs(degrees - bearing.degrees) < 0.01;
	}
}
