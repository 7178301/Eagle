package eagle.sdkInterface.sensorAdaptors.RPLIDARAdaptors;

/**
 * Robo Peak RPLIDAR Device Information Packet
 *
 * @author Angela Lau [7160852@student.swin.edu.au]
 * @version 0.0.1
 * @since 04/09/2015
 * <p/>
 * Date Modified	07/10/2015 - Angela
 */
public class RPLidar_InfoPacket {
    private int model;
    private int firmware_version;
    private int hardware_version;
    private int[] serialnum = new int[16];

    RPLidar_InfoPacket(byte[] data)
    {
        model = data[0];
        firmware_version = (data[2]<<8) | data[1];
        hardware_version = data[3];
        for(int i = 0; i<16; i++)
        {
            serialnum[i] = data[i+4];
        }
    }

    public int getModel() {
        return model;
    }

    public int getFirmware_version() {
        return firmware_version;
    }

    public int getHardware_version() {
        return hardware_version;
    }

    public int[] getSerialnum() {
        return serialnum;
    }
}
