package eagle.navigation.positioning;
/** Position
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 * @author          Cameron Cross [7193432@student.swin.edu.au]*/

public class PositionMetric extends Position{

    public PositionMetric(double longitude, double latitude, double altitude, Angle roll, Angle pitch, Angle yaw){
        super(longitude, latitude, altitude, roll, pitch, yaw);
    }

    public PositionMetric(PositionMetric positionMetric){
        super(positionMetric.getLongitude(), positionMetric.getLatitude(), positionMetric.getAltitude(), positionMetric.getRoll(), positionMetric.getPitch(), positionMetric.getYaw());
    }

    public PositionMetric(Position position){
        super(position.getLongitude(), position.getLatitude(), position.getAltitude(), position.getRoll(), position.getPitch(), position.getYaw());
    }

    public void add(PositionMetric positionMetric){
        this.longitude+=positionMetric.getLongitude();
        this.latitude+=positionMetric.getLatitude();
        this.altitude+=positionMetric.getAltitude();
        this.roll.add(positionMetric.getRoll());
        this.pitch.add(positionMetric.getPitch());
        this.yaw.add(positionMetric.getYaw());
    }

    public void minus(PositionMetric position){
        this.longitude-=position.getLongitude();
        this.latitude-=position.getLatitude();
        this.altitude-=position.getAltitude();
        this.roll.minus(position.getRoll());
        this.pitch.minus(position.getPitch());
        this.yaw.minus(position.getYaw());
    }

    public boolean equals(PositionMetric position) {
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
    public String toString(){
        return getLongitude()+" "+getLatitude()+" "+getAltitude()+" "+getRoll()+" "+getPitch()+" "+getYaw();
    }

    public String toStringLong(){
        return "Longitude: "+getLongitude()+", Latitude: "+getLatitude()+", Altitude: "+getAltitude()+", Roll: "+getRoll()+", Pitch: "+getPitch()+", Yaw: "+getYaw().toStringLong();
    }

    public PositionMetric compare(PositionMetric positionMetric) {
        return new PositionMetric(positionMetric.getLongitude()-getLongitude(),
                positionMetric.getLatitude()-getLatitude(),
                positionMetric.getAltitude()-getAltitude(),
                getRoll().compare(positionMetric.getRoll()),
                getPitch().compare(positionMetric.getPitch()),
                getYaw().compare(positionMetric.getYaw()));
    }
}
