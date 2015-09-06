package eagle.navigation.positioning;
/** Position
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 * @author          Cameron Cross [7193432@student.swin.edu.au]*/

public class PositionMetric {
	private double longitude;
	private double latitude;
	private double altitude;
    private Angle roll;
	private Angle pitch;
	private Angle yaw;

    public PositionMetric(double longitude, double latitude, double altitude, Angle roll, Angle pitch, Angle yaw){
        this.longitude=longitude;
        this.latitude=latitude;
        this.altitude=altitude;
        this.roll=roll;
        this.pitch=pitch;
        this.yaw=yaw;
    }
    public PositionMetric(PositionMetric position){
        this.longitude=position.longitude;
        this.latitude=position.latitude;
        this.altitude=position.altitude;
        this.roll=position.roll;
        this.pitch=position.pitch;
        this.yaw=position.yaw;
    }

    public double getLongitude(){return this.longitude;};
    public double getLatitude(){return this.latitude;};
    public double getAltitude(){return this.altitude;};
    public Angle getRoll(){return this.roll;};
    public Angle getPitch(){return this.pitch;};
    public Angle getYaw(){return this.yaw;};

    public void add(PositionMetric position){
        this.longitude+=position.getLongitude();
        this.latitude+=position.getLatitude();
        this.altitude+=position.getAltitude();
        this.roll.add(position.getRoll());
        this.pitch.add(position.getPitch());
        this.yaw.add(position.getYaw());
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

    public PositionMetric compare(PositionMetric position) {
        return new PositionMetric(-Double.compare(longitude,position.getLongitude()),
                -Double.compare(latitude,position.getLatitude()),
                -Double.compare(altitude,position.getAltitude()),
                roll.compare(position.getRoll()),
                pitch.compare(position.getPitch()),
                yaw.compare(position.getYaw()));
    }
}
