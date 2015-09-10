package eagle.navigation.positioning;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * eagle.navigation.positioning
 *
 * @author cameron
 * @version 0.0.1
 * @since 9/8/15
 * <p/>
 */
public class PositionDisplacement extends Position {
    public PositionDisplacement(double latitude, double longitude, double altitude, Angle roll, Angle pitch, Angle yaw) {
        super(latitude, longitude, altitude, roll, pitch, yaw);
    }

    @Override
    public Position add(PositionDisplacement position) {
        return new PositionDisplacement(latitude+position.getLatitude(),
                longitude+position.getLongitude(),
                altitude+position.getAltitude(),
                roll.add(position.getRoll()),
                pitch.add(position.getPitch()),
                yaw.add(position.getYaw()));
    }

    @Override
    public Position copy() {
        return new PositionDisplacement(latitude, longitude, altitude, roll.copy(), pitch.copy(), yaw.copy());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PositionDisplacement))
            return false;
        if (obj == this)
            return true;

        PositionDisplacement position = (PositionDisplacement)obj;
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
