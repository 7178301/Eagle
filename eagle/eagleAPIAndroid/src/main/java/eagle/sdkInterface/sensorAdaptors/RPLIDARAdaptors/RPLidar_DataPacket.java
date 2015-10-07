package eagle.sdkInterface.sensorAdaptors.RPLIDARAdaptors;

/**
 * Robo Peak RPLIDAR Data Packet
 *
 * @author Angela Lau [7160852@student.swin.edu.au]
 * @version 0.0.1
 * @since 04/09/2015
 * <p/>
 * Date Modified	07/10/2015 - Angela
 */
public class RPLidar_DataPacket {
    private int sync_quality;
    private int angle;
    private int distance;

    RPLidar_DataPacket(byte[] data)
    {
        sync_quality = data[0];
        angle = (((int)data[2]&0xFF)<<8) | ((int)data[1]&0xFF);
        distance = (((int)data[4]&0xFF)<<8) | ((int)data[3]&0xFF);
    }

    public int getSync_quality() {
        return sync_quality;
    }

    public float getAngle() {
        return ((angle>>1)/64.0f);
    }

    public float getDistance() {
        return ((float)distance/4.0f)/1000.0f;
    }
}
