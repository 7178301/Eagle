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

public class Position {
    private double longitude;
    private double latitude;
    private double altitude;
    private double roll;
    private double pitch;
    private Bearing yaw = null;

    public Position(double longitude, double latitude, double altitude, double roll, double pitch, Bearing yaw) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public Position(Position position) {
        this.longitude = position.getLongitude();
        this.latitude = position.getLatitude();
        this.altitude = position.getAltitude();
        this.roll = position.getRoll();
        this.pitch = position.getPitch();
        this.yaw = position.getYaw();
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public double getRoll() {
        return this.roll;
    }

    public double getPitch() {
        return this.pitch;
    }

    public Bearing getYaw() {
        return this.yaw;
    }

    public void addRelative(Position position) {
        this.longitude += position.getLongitude();
        this.latitude += position.getLatitude();
        this.altitude += position.getAltitude();
        this.yaw.addRelative(position.getYaw());
    }

    public void addAbsolute(Position position) {
        this.longitude = this.longitude - (this.longitude - position.getLongitude());
        this.latitude = this.latitude - (this.latitude - position.getLatitude());
        this.altitude = this.altitude - (this.altitude - position.getAltitude());
        this.yaw.addAbsolute(position.getYaw());
    }

    public boolean equals(Position position) {
        if (longitude == position.getLongitude() &&
                latitude == position.getLatitude() &&
                altitude == position.getAltitude() &&
                roll == position.getRoll() &&
                pitch == position.pitch &&
                yaw.getDegrees() == position.getYaw().getDegrees())
            return true;
        else
            return false;
    }

    public String toString() {
        return "Longitude: " + getLongitude() + ", Latitude: " + getLatitude() + ", Altitude: " + getAltitude() + ", Roll: " + getRoll() + ", Pitch: " + getPitch() + ", Yaw: " + getYaw();
    }

    public boolean isEqual(Position position) {
        if (position.getAltitude() == altitude &&
                position.getLatitude() == latitude &&
                position.getLongitude() == longitude &&
                position.getPitch() == pitch &&
                position.getRoll() == roll &&
                position.getYaw().isEqual(yaw))
            return true;
        else
            return false;
    }
}
