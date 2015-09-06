package eagle.navigation.positioning;
/** Bearing
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 * @author          Cameron Cross */
public class Angle
{
	private double degrees;
	
	/**
	 * Constructor for bearing class
	 * @param angle input angle
	 */
	public Angle(double angle){
		degrees = angle;
		normalise();
	}
	public Angle(Angle angle){
		this.degrees=angle.getDegrees();
	}

	public void add(Angle val) {
		this.degrees+=val.getDegrees();
		normalise();
	}

	public void minus(Angle angle){
		this.degrees-=angle.getDegrees();
		normalise();
	}

	private void normalise() {
		while(degrees < 0) {
			degrees += 360;
		}
		while(degrees > 360) {
			degrees -= 360;
		}
	}

	public double getDegrees() {
		return degrees;
	}

	public boolean equals(Angle bearing){
		if(Double.compare(degrees,bearing.getDegrees())==0)
			return true;
		else
			return false;
	}

	/**
	 * convert to degrees minutes and seconds.
	 * based of: http://zonalandeducation.com/mmts/trigonometryRealms/degMinSec/degMinSec.htm
	 * @return string containing degrees minutes seconds
	 */
	//TODO Incorrectly Coded. Seconds should exist when minute is 0
	public String toStringLong(){
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

	public Angle compare(Angle bearing){
		return new Angle(-Double.compare(degrees,bearing.getDegrees()));
	}

	@Override
	public String toString(){
		return Double.toString(degrees);
	}
}
