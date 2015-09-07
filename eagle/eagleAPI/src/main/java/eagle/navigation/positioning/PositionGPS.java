package eagle.navigation.positioning;

/** Bearing
 * @since     06/09/2015
 * <p>
 * Date Modified	06/09/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 */
public class PositionGPS extends Position {
    public PositionGPS(double longitude, double latitude, double altitude,Angle roll, Angle pitch, Angle yaw) {
        super(longitude, latitude, altitude, roll, pitch, yaw);
    }


    public PositionGPS(PositionGPS positionGPS){
        super(positionGPS.getLongitude(), positionGPS.getLatitude(), positionGPS.getAltitude(), positionGPS.getRoll(), positionGPS.getPitch(), positionGPS.getYaw());
    }
    public PositionGPS(Position position){
        super(position.getLongitude(), position.getLatitude(), position.getAltitude(), position.getRoll(), position.getPitch(), position.getYaw());
    }

    public PositionGPS compare(PositionGPS positionGPS) {
        return new PositionGPS(getLongitude()-positionGPS.getLongitude(),
                getLatitude()-positionGPS.getLatitude(),
                getAltitude()-positionGPS.getAltitude(),
                getRoll().compare(positionGPS.getRoll()),
                getPitch().compare(positionGPS.getPitch()),
                getYaw().compare(positionGPS.getYaw()));
    }
}
