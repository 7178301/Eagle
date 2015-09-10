package eagle.navigation.positioning;

import org.apache.commons.lang3.builder.HashCodeBuilder;

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
        //code copied from: http://stackoverflow.com/a/15604480/5295129

        double dy = position.getLatitude();
        double dx = position.getLongitude();

        double new_latitude  = latitude  + (dy / r_earth) * (180 / Math.PI);
        double new_longitude = longitude + (dx / r_earth) * (180 / Math.PI) / Math.cos(latitude * 180/Math.PI);

        return new PositionDisplacement(new_latitude,
                new_longitude,
                altitude+position.getAltitude(),
                roll.add(position.getRoll()),
                pitch.add(position.getPitch()),
                yaw.add(position.getYaw()));
    }

    public PositionDisplacement compare(PositionGPS positionGPS) {
        //TODO: Haversine GPS calculations
        double latdist = (positionGPS.getLatitude()-latitude)*Math.PI*r_earth/180;
        double longdist = (positionGPS.getLongitude()-longitude)*Math.PI*r_earth/180*Math.cos(latitude * 180/Math.PI);

        return new PositionDisplacement(latdist,
                longdist,
                getAltitude()-positionGPS.getAltitude(),
                getRoll().compare(positionGPS.getRoll()),
                getPitch().compare(positionGPS.getPitch()),
                getYaw().compare(positionGPS.getYaw()));
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
