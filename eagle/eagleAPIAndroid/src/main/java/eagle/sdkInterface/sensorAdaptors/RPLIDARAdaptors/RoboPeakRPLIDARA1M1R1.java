package eagle.sdkInterface.sensorAdaptors.RPLIDARAdaptors;

import eagle.sdkInterface.sensorAdaptors.AdaptorRPLIDAR;

import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.IOIOConnectionManager.Thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;

/**
 * Robo Peak RPLIDAR Adaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @author Angela Lau [7160852@student.swin.edu.au]
 * @version 0.0.1
 * @since 14/06/2015
 * <p/>
 * Date Modified	07/10/2015 - Angela
 */
public class RoboPeakRPLIDARA1M1R1 extends AdaptorRPLIDAR {
    public RoboPeakRPLIDARA1M1R1() {
        super("Robo Peak", "RPLIDAR A1M1R1", "0.0.1");
    }

    private static final byte RPLIDAR_CMD_SYNC_BYTE = (byte) 0xA5;
    private static final byte RPLIDAR_CMD_RESET = 0x40;
    private static final byte RPLIDAR_CMD_STOP = 0x25;
    private static final byte RPLIDAR_CMD_GET_DEVICE_INFO = 0x50;
    private static final byte RPLIDAR_CMD_GET_DEVICE_HEALTH = 0x52;
    private static final byte RPLIDAR_CMD_SCAN = 0x20;
    private static final byte RPLIDAR_CMD_FORCE_SCAN = 0x21;
    private static final byte RPLIDAR_ANS_SYNC_BYTE1 = (byte) 0xA5;
    private static final byte RPLIDAR_ANS_SYNC_BYTE2 = 0x5A;
    private static final byte[] RPLIDAR_GET_DEVICE_INFO_R_DESCRIPTOR = {RPLIDAR_ANS_SYNC_BYTE1, RPLIDAR_ANS_SYNC_BYTE2, 0x14, 0x00, 0x00, 0x00, 0x04};
    private static final byte[] RPLIDAR_GET_DEVICE_HEALTH_R_DESCRIPTOR = {RPLIDAR_ANS_SYNC_BYTE1, RPLIDAR_ANS_SYNC_BYTE2, 0x03, 0x00, 0x00, 0x00, 0x06};
    private static final byte[] RPLIDAR_SCAN_R_DESCRIPTOR = {RPLIDAR_ANS_SYNC_BYTE1, RPLIDAR_ANS_SYNC_BYTE2, 0x05, 0x00, 0x00, 0x40, (byte) 0x81};
    private static final byte[] RPLIDAR_FORCE_SCAN_R_DESCRIPTOR = {RPLIDAR_ANS_SYNC_BYTE1, RPLIDAR_ANS_SYNC_BYTE2, 0x05, 0x00, 0x00, 0x40, (byte) 0x81};

    private Uart lidar;
    private int rxPin;
    private int txPin;
    private int baudRate = 115200;
    private IOIO ioio;
    private InputStream in;
    private OutputStream out;
    private int motorPin;
    private PwmOutput lidarMotor;
    private float[] RPLIDARData = new float[2];

    public boolean connectToSensor() {
        if (ioio == null) {
            return false;
        }
        try {
            lidar = ioio.openUart(rxPin, txPin, baudRate, Uart.Parity.NONE, Uart.StopBits.ONE);
            in = lidar.getInputStream();
            out = lidar.getOutputStream();
            lidarMotor = ioio.openPwmOutput(motorPin, 490);
            lidarMotor.setDutyCycle(0);
            reset();
            return isConnectedToSensor();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean setAndroidContext(Object object) {
        if (object instanceof IOIO) {
            this.ioio = (IOIO) object;
            return true;
        } else
            return false;
    }

    public boolean isConnectedToSensor() {
        try {
            getInfoPacket();

            if (in.available() != 0) {
                return false;
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDataReady() {
        if(isConnectedToSensor()) {
            return true;
        }
        return false;
    }

    public void reset() throws InterruptedException, IOException {
        sendRequest(RPLIDAR_CMD_RESET);
        Thread.sleep(1000);
        while (in.available() != 0) {
            in.read();
        }
    }

    public void stop() throws IOException, InterruptedException, ConnectionLostException {
        sendRequest(RPLIDAR_CMD_STOP);
        lidarMotor.setDutyCycle(0);
        Thread.sleep(1);
    }

    public RPLidar_InfoPacket getInfoPacket() throws IOException, TimeoutException, InterruptedException {

        byte[] data = new byte[20];

        sendRequest(RPLIDAR_CMD_GET_DEVICE_INFO);
        if (checkDescriptor(RPLIDAR_GET_DEVICE_INFO_R_DESCRIPTOR) == false) {
            throw new IOException("Invalid Response");
        }

        if (in.read(data, 0, data.length) != data.length) {
            throw new IOException("Invalid Response");
        }
        return new RPLidar_InfoPacket(data);
    }

    public RPLidar_HealthPacket getHealthPacket() throws IOException, TimeoutException, InterruptedException {
        byte[] data = new byte[3];

        sendRequest(RPLIDAR_CMD_GET_DEVICE_HEALTH);
        if (checkDescriptor(RPLIDAR_GET_DEVICE_HEALTH_R_DESCRIPTOR) == false) {
            throw new IOException("Invalid Response");
        }

        if (in.read(data, 0, data.length) != data.length) {
            throw new IOException("Invalid Response");
        }
        return new RPLidar_HealthPacket(data);
    }

    public boolean startForceScan() throws InterruptedException, TimeoutException, IOException {
        reset();
        if (getHealthPacket().getError() != 0) {
            reset();
            return false;
        }
        sendRequest(RPLIDAR_CMD_FORCE_SCAN);
        if (checkDescriptor(RPLIDAR_FORCE_SCAN_R_DESCRIPTOR) == false) {
            reset();
            return false;
        }
        return true;
    }

    public boolean startScan() throws InterruptedException, TimeoutException, IOException, ConnectionLostException {
        reset();
        lidarMotor.setDutyCycle(1);
        if (getHealthPacket().getError() != 0) {
            reset();
            return false;
        }
        sendRequest(RPLIDAR_CMD_SCAN);
        if (checkDescriptor(RPLIDAR_SCAN_R_DESCRIPTOR) == false) {
            reset();
            return false;
        }
        return true;
    }

    public RPLidar_DataPacket getDataPacket() throws IOException {
        byte[] data = new byte[5];

        // wait for 5 bytes of data
        int available = in.available();
        while(available <5)
        {
            available = in.available();
        }
        System.out.println(in.available());

        // clear the buffer of all but 1 packet (and possible part packet)
        for(int i=0;i<(((available/5)-1)*5);i++)
        {
            in.read();
        }

        // read the valid packet
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) in.read();
        }

        return new RPLidar_DataPacket(data);
    }

    @Override
    public float[] getData() {
        try {
            RPLidar_DataPacket data = getDataPacket();

            RPLIDARData[0] = data.getAngle();
            RPLIDARData[1] = data.getDistance();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return RPLIDARData;
    }

    @Override
    public String[] getSensorPinsDescription() {
        String[] temp = new String[3];
        temp[0] = "RX Pin";
        temp[1] = "TX Pin";
        temp[2] = "Motor Pin";
        return temp;
    }

    @Override
    public boolean setSensorPins(int[] pins) {
        if (pins != null && pins.length == 3) {
            rxPin = pins[0];
            txPin = pins[1];
            motorPin = pins[2];
            return true;
        } else
            return false;
    }

    private byte[] getResponseDescriptor() throws IOException, TimeoutException, InterruptedException {
        byte[] read = new byte[7];
        int readCount = 0;
        int timeoutCount = 0;
        while (true) {
            if (in.available() != 0) {
                read[readCount] = (byte) (in.read());
                readCount++;
                if (readCount == 7) {
                    break;
                }
            } else {
                timeoutCount++;
                Thread.sleep(10);
                if (timeoutCount > 200) {
                    throw new TimeoutException("LIDAR Read Failure");
                }
            }
        }
        return read;
    }

    private void sendRequest(int request) throws IOException {
        out.write(RPLIDAR_CMD_SYNC_BYTE);
        out.write(request);
    }

    private boolean checkDescriptor(byte[] validDescriptor) {
        byte[] descriptor;

        try {
            descriptor = getResponseDescriptor();
        } catch (Exception e) {
            return false;
        }
        for (int i = 0; i < descriptor.length; i++) {
            if (descriptor[i] != validDescriptor[i]) {
                return false;
            }
        }
        return true;
    }
}