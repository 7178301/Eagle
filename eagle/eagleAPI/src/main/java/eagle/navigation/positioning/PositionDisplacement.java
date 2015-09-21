package eagle.navigation.positioning;

/**
 * eagle.navigation.positioning
 *
 * @author cameron
 * @version 0.0.1
 * @since 9/8/15
 * <p/>
 */
public final class PositionDisplacement extends Position {
    public PositionDisplacement(double latitude, double longitude, double altitude, Angle yaw) {
        super(latitude, longitude, altitude, yaw);
    }

    public PositionDisplacement(double latitude, double longitude, double altitude, Angle roll, Angle pitch, Angle yaw) {
        super(latitude, longitude, altitude, roll, pitch, yaw);
    }

    @Override
    public PositionDisplacement add(PositionDisplacement position) {
        return new PositionDisplacement(latitude + position.getLatitude(),
                longitude + position.getLongitude(),
                altitude + position.getAltitude(),
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

        PositionDisplacement position = (PositionDisplacement) obj;
        return (Double.compare(latitude, position.getLatitude()) == 0 &&
                Double.compare(longitude, position.getLongitude()) == 0 &&
                Double.compare(altitude, position.getAltitude()) == 0 &&
                roll.equals(position.getRoll()) &&
                pitch.equals(position.getPitch()) &&
                yaw.equals(position.getYaw()));
    }

    @Override
    public int hashCode() {
        int output = 5, prime2 = 89;
        output = prime2 * output + Double.valueOf(latitude).hashCode();
        output = prime2 * output + Double.valueOf(longitude).hashCode();
        output = prime2 * output + Double.valueOf(altitude).hashCode();
        output = prime2 * output + roll.hashCode();
        output = prime2 * output + pitch.hashCode();
        output = prime2 * output + yaw.hashCode();
        return output;
    }
}
