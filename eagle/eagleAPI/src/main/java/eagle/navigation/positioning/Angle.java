package eagle.navigation.positioning;

/**
 * Angle Class
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @author Cameron Cross
 * @version 0.0.1
 */
public final class Angle {
    private final double degrees;

    /**
     * Constructor for angle class
     *
     * @param angle input angle
     */
    public Angle(double angle) {
        degrees = normalise(angle);
    }

    public Angle(Angle angle) {
        this.degrees = angle.getDegrees();
    }

    public Angle add(Angle angle) {
        return new Angle(this.degrees + angle.getDegrees());
    }

    public Angle minus(Angle angle) {
        return new Angle(this.degrees - angle.getDegrees());

    }

    private double normalise(double angle) {
        while (angle < 0) {
            angle += 360;
        }
        while (angle > 360) {
            angle -= 360;
        }
        return angle;
    }

    public double getDegrees() {
        return degrees;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Angle))
            return false;
        if (obj == this)
            return true;

        Angle bearing = (Angle) obj;
        return (Double.compare(degrees, bearing.getDegrees()) == 0);
    }

    @Override
    public int hashCode() {
        int output = 5, prime2 = 89;
        output = prime2 * output + Double.valueOf(degrees).hashCode();
        return output;
    }

    /**
     * convert to degrees minutes and seconds.
     * based of: http://zonalandeducation.com/mmts/trigonometryRealms/degMinSec/degMinSec.htm
     *
     * @return string containing degrees minutes seconds
     */
    //TODO Incorrectly Coded. Seconds should exist when minute is 0
    public String toStringLong() {
        StringBuilder sb = new StringBuilder();
        sb.append(new Double(degrees).intValue());
        sb.append("\u00B0");
        Double minutes = (degrees - Math.floor(degrees)) * 60;
        if (minutes != 0) {
            sb.append(minutes.intValue());
            sb.append("\'");
            Double seconds = (minutes - Math.floor(minutes)) * 60;
            if (seconds != 0) {
                sb.append(Math.round(seconds));
                sb.append("\"");
            }
        }
        return sb.toString();
    }

    /**
     * Compares two angles
     *
     * @param bearing
     * @return Angle between this angle and bearing
     */
    public Angle compare(Angle bearing) {
        return new Angle(bearing.getDegrees() - degrees);
    }

    @Override
    public String toString() {
        return Double.toString(degrees);
    }

    public Angle copy() {
        return new Angle(degrees);
    }
}
