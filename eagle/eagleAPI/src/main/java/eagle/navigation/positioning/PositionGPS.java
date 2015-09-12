package eagle.navigation.positioning;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.sis.core.LatLon;

import static org.apache.sis.distance.DistanceUtils.getHaversineDistance;
import static org.apache.sis.distance.DistanceUtils.getPointOnGreatCircle;

/** Bearing
 * @since     06/09/2015
 * <p>
 * Date Modified	06/09/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 */
public class PositionGPS extends Position {
    private static double r_earth = 6378 * 1000; //Work in meters for everything

    public PositionGPS(double latitude, double longitude, double altitude, Angle roll, Angle pitch, Angle yaw) {
        super(latitude, longitude, altitude, roll, pitch, yaw);
    }


    public PositionGPS(PositionGPS positionGPS){
        super(positionGPS.getLatitude(), positionGPS.getLongitude(), positionGPS.getAltitude(), positionGPS.getRoll(), positionGPS.getPitch(), positionGPS.getYaw());
    }
    public PositionGPS(Position position){
        super(position.getLatitude(), position.getLongitude(), position.getAltitude(), position.getRoll(), position.getPitch(), position.getYaw());
    }

    @Override
    public Position add(PositionDisplacement position) {
        double bearing = Math.toDegrees(Math.atan(position.getLongitude() / position.getLatitude()));
        double dist = Math.sqrt(position.getLongitude()*position.getLongitude() + position.getLatitude()*position.getLatitude())/1000;


        LatLon endPos = getPointOnGreatCircle(latitude, longitude, dist, bearing);

        return new PositionGPS(endPos.getLat(),
                endPos.getLon(),
                altitude+position.getAltitude(),
                roll.add(position.getRoll()),
                pitch.add(position.getPitch()),
                yaw.add(position.getYaw()));
    }

    public PositionDisplacement compare(PositionGPS position) {
        double latdist = getHaversineDistance(getLatitude(), getLongitude(), position.getLatitude(), getLongitude());
        double longdist = getHaversineDistance(getLatitude(), getLongitude(), getLatitude(), position.getLongitude());

        return new PositionDisplacement(latdist,
                longdist,
                getAltitude()-position.getAltitude(),
                getRoll().compare(position.getRoll()),
                getPitch().compare(position.getPitch()),
                getYaw().compare(position.getYaw()));
    }

    @Override
    public Position copy() {
        return new PositionGPS(latitude, longitude, altitude, roll.copy(), pitch.copy(), yaw.copy());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PositionGPS))
            return false;
        if (obj == this)
            return true;

        PositionGPS position = (PositionGPS)obj;
        if(Double.compare(longitude,position.getLongitude())==0&&
                Double.compare(latitude,position.getLatitude())==0&&
                Double.compare(altitude,position.getAltitude())==0&&
                roll.equals(position.getRoll())&&
                pitch.equals(position.getPitch())&&
                yaw.equals(position.getYaw()))
            return true;
        else
            return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                // if deriving: appendSuper(super.hashCode()).
                append(longitude).
                append(latitude).
                append(altitude).
                append(roll).
                append(pitch).
                append(yaw).
                toHashCode();
    }
}
