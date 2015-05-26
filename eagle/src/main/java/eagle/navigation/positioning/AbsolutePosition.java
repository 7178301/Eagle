package eagle.navigation.positioning;

public class AbsolutePosition extends Position {

	public AbsolutePosition(double longitude, double latitude, double altitude, double roll, double pitch, double yaw) {
		super(longitude, latitude, altitude, roll, pitch, yaw);
	}

	public AbsolutePosition add(RelativePosition b) {
		return null;
	}

	public AbsolutePosition minus(RelativePosition b) {
		return null;
	}

	public RelativePosition minus(AbsolutePosition b) {
		return null;
	}

	public String toString() {
		return "Longitude: " + getLongitude() + ", Latitude: " + getLatitude() + ", Altitude: " + getAltitude() + ", Roll: " + getRoll() + ", Pitch: " + getPitch() + ", Yaw: " + getYaw();
	}
}
