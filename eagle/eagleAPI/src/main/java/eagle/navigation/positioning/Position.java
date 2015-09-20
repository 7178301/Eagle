package eagle.navigation.positioning;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/** Position
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 * @author          Cameron Cross [7193432@student.swin.edu.au]*/

public abstract class Position {
	protected final double longitude;
	protected final double latitude;
	protected final double altitude;
    protected final Angle roll;
	protected final Angle pitch;
	protected final Angle yaw;

    public Position(double latitude, double longitude, double altitude, Angle yaw) {
        this.latitude=latitude;
        this.longitude=longitude;
        this.altitude=altitude;
        this.roll=new Angle(0);
        this.pitch=new Angle(0);
        this.yaw=yaw;
    }

    public Position(double latitude, double longitude, double altitude, Angle roll, Angle pitch, Angle yaw){
        this.latitude=latitude;
        this.longitude=longitude;
        this.altitude=altitude;
        this.roll=roll;
        this.pitch=pitch;
        this.yaw=yaw;
    }
    public Position(Position position){
        this.latitude=position.latitude;
        this.longitude=position.longitude;
        this.altitude=position.altitude;
        this.roll=position.roll;
        this.pitch=position.pitch;
        this.yaw=position.yaw;
    }

    public double getLatitude(){return this.latitude;}
    public double getLongitude(){return this.longitude;}
    public double getAltitude(){return this.altitude;}
    public Angle getRoll(){return this.roll;}
    public Angle getPitch(){return this.pitch;}
    public Angle getYaw(){return this.yaw;}

    public abstract Position add(PositionDisplacement position);

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position))
            return false;
        if (obj == this)
            return true;

        Position position = (Position)obj;
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
        return new HashCodeBuilder(17, 31) // two randomly chosen prime numbers
                .append(longitude)
                .append(latitude)
                .append(altitude)
                .append(roll)
                .append(pitch)
                .append(yaw)
                .toHashCode();
    }

    @Override
    public String toString(){
        return getLatitude()+" "+getLongitude()+" "+getAltitude()+" "+getRoll()+" "+getPitch()+" "+getYaw();
    }

    public String toStringLong(){
        return "Latitude: "+getLatitude()+", Longitude: "+getLongitude()+", Altitude: "+getAltitude()+", Roll: "+getRoll()+", Pitch: "+getPitch()+", Yaw: "+getYaw().toStringLong();
    }

    public abstract Position copy();
}
