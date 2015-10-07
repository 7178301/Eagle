package eagle.sdkInterface.sensorAdaptors.RPLIDARAdaptors;

/**
 * Robo Peak RPLIDAR Device Health Packet
 *
 * @author Angela Lau [7160852@student.swin.edu.au]
 * @version 0.0.1
 * @since 04/09/2015
 * <p/>
 * Date Modified	07/10/2015 - Angela
 */
public class RPLidar_HealthPacket {
    private int status;
    private int error;

    RPLidar_HealthPacket(byte[] data)
    {
        status = data[0];
        error = (data[2]<<8) | data[1];
    }

    public int getStatus() {
        return status;
    }

    public int getError() {
        return error;
    }
}