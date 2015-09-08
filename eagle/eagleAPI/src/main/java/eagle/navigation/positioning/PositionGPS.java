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
    public PositionGPS(double longitude, double latitude, double altitude, Angle roll, Angle pitch, Angle yaw) {
        super(longitude, latitude, altitude, roll, pitch, yaw);
    }


    public PositionGPS(PositionGPS positionGPS){
        super(positionGPS.getLongitude(), positionGPS.getLatitude(), positionGPS.getAltitude(), positionGPS.getRoll(), positionGPS.getPitch(), positionGPS.getYaw());
    }
    public PositionGPS(Position position){
        super(position.getLongitude(), position.getLatitude(), position.getAltitude(), position.getRoll(), position.getPitch(), position.getYaw());
    }

    @Override
    public Position add(PositionDisplacement position) {
        //TODO: Haversine GPS calculations;
//        return new PositionDisplacement(longitude+position.getLongitude(),
//                latitude+position.getLatitude(),
//                altitude+position.getAltitude(),
//                roll.add(position.getRoll()),
//                pitch.add(position.getPitch()),
//                yaw.add(position.getYaw()));
        return null;
    }

    public PositionDisplacement compare(PositionGPS positionGPS) {
        //TODO: Haversine GPS calculations
//        return new PositionGPS(getLongitude()-positionGPS.getLongitude(),
//                getLatitude()-positionGPS.getLatitude(),
//                getAltitude()-positionGPS.getAltitude(),
//                getRoll().compare(positionGPS.getRoll()),
//                getPitch().compare(positionGPS.getPitch()),
//                getYaw().compare(positionGPS.getYaw()));
        return null;
    }

    @Override
    public Position copy() {
        return new PositionGPS(longitude, latitude, altitude, roll.copy(), pitch.copy(), yaw.copy());
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
