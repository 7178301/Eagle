package eagle.navigation.positioning;

/**
 * Position
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @author Cameron Cross [7193432@student.swin.edu.au]
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */

public final class PositionMetric extends Position {
    public PositionMetric(double latitude, double longitude, double altitude, Angle yaw) {
        super(latitude, longitude, altitude, yaw);
    }

    public PositionMetric(double latitude, double longitude, double altitude, Angle roll, Angle pitch, Angle yaw) {
        super(latitude, longitude, altitude, roll, pitch, yaw);
    }

    public PositionMetric(PositionMetric positionMetric) {
        super(positionMetric.getLatitude(), positionMetric.getLongitude(), positionMetric.getAltitude(), positionMetric.getRoll(), positionMetric.getPitch(), positionMetric.getYaw());
    }

    public PositionMetric(Position position) {
        super(position.getLatitude(), position.getLongitude(), position.getAltitude(), position.getRoll(), position.getPitch(), position.getYaw());
    }

    @Override
    public PositionMetric add(PositionDisplacement position) {
        return new PositionMetric(
                latitude + position.getLatitude(),
                longitude + position.getLongitude(),
                altitude + position.getAltitude(),
                roll.add(position.getRoll()),
                pitch.add(position.getPitch()),
                yaw.add(position.getYaw())
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PositionMetric))
            return false;
        if (obj == this)
            return true;

        PositionMetric position = (PositionMetric) obj;
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

    /**
     * Compare two metric positions
     *
     * @param positionMetric
     * @return PositionDisplacement needed to move from this position to positionMetric
     */
    public PositionDisplacement compare(PositionMetric positionMetric) {
        return new PositionDisplacement(getLatitude() - positionMetric.getLatitude(),
                getLongitude() - positionMetric.getLongitude(),
                getAltitude() - positionMetric.getAltitude(),
                getRoll().compare(positionMetric.getRoll()),
                getPitch().compare(positionMetric.getPitch()),
                getYaw().compare(positionMetric.getYaw()));
    }

    @Override
    public Position copy() {
        return new PositionMetric(latitude, longitude, altitude, roll.copy(), pitch.copy(), yaw.copy());
    }
}
