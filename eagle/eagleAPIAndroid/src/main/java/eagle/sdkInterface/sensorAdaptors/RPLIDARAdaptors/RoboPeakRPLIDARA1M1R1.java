package eagle.sdkInterface.sensorAdaptors.RPLIDARAdaptors;

import eagle.Log;
import eagle.sdkInterface.sensorAdaptors.AdaptorRPLIDAR;

import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.IOIOConnectionManager.Thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
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

    private Uart rplidar = null;
    private int rxPin = -1;
    private int txPin = -1;
    private int baudRate = 115200;
    private IOIO ioio = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private int motorPin = -1;
    private PwmOutput rplidarMotor = null;
    private float[] RPLIDARData = new float[2];

    @Override
    public boolean connectToSensor() {
        if (ioio == null || rxPin == -1 || txPin == -1 || motorPin == -1) {
            return false;
        }
        try {
            rplidar = ioio.openUart(rxPin, txPin, baudRate, Uart.Parity.NONE, Uart.StopBits.ONE);
            inputStream = rplidar.getInputStream();
            outputStream = rplidar.getOutputStream();
            rplidarMotor = ioio.openPwmOutput(motorPin, 490);
            rplidarMotor.setDutyCycle(0);
            reset();
            Thread.sleep(1000);
            if(!isConnectedToSensor())
            {
                return false;
            }
            RPLidar_InfoPacket infoPacket = getInfoPacket();
            adaptorHardwareModel = Integer.toString(infoPacket.getModel());
            adaptorHardwareFirmwareVersion = Integer.toString(infoPacket.getFirmware_version());
            adaptorHardwareVersion = Integer.toString(infoPacket.getHardware_version());
            adaptorHardwareSerialNumber = Arrays.toString(infoPacket.getSerialnum());
            return startScan();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean disconnectFromSensor() {
        try {
            stop();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean setController(Object object) {
        if (object instanceof IOIO) {
            this.ioio = (IOIO) object;
            return connectToSensor();
        } else
            return false;
    }

    @Override
    public boolean isConnectedToSensor() {
        try {
            getInfoPacket();
            return inputStream.available() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isDataReady() {
        return isConnectedToSensor();
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

    private void reset() throws InterruptedException, IOException {
        sendRequest(RPLIDAR_CMD_RESET);
        Thread.sleep(1000);
        while (inputStream.available() != 0) {
            inputStream.read();
        }
    }

    private void stop() throws IOException, InterruptedException, ConnectionLostException {
        sendRequest(RPLIDAR_CMD_STOP);
        rplidarMotor.setDutyCycle(0);
        Thread.sleep(1);
    }

    private boolean startForceScan() throws InterruptedException, TimeoutException, IOException {
        reset();
        if (getHealthPacket().getError() != 0) {
            reset();
            return false;
        }
        sendRequest(RPLIDAR_CMD_FORCE_SCAN);
        if (!checkDescriptor(RPLIDAR_FORCE_SCAN_R_DESCRIPTOR)) {
            reset();
            return false;
        }
        return true;
    }

    private boolean startScan() throws InterruptedException, TimeoutException, IOException, ConnectionLostException {
        reset();
        rplidarMotor.setDutyCycle(1);
        if (getHealthPacket().getError() != 0) {
            reset();
            return false;
        }
        sendRequest(RPLIDAR_CMD_SCAN);
        if (!checkDescriptor(RPLIDAR_SCAN_R_DESCRIPTOR)) {
            reset();
            return false;
        }
        return true;
    }

    private byte[] getResponseDescriptor() throws IOException, TimeoutException, InterruptedException {
        byte[] read = new byte[7];
        int readCount = 0;
        int timeoutCount = 0;
        while (true) {
            if (inputStream.available() != 0) {
                read[readCount] = (byte) (inputStream.read());
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
        outputStream.write(RPLIDAR_CMD_SYNC_BYTE);
        outputStream.write(request);
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

    private RPLidar_InfoPacket getInfoPacket() throws IOException, TimeoutException, InterruptedException {

        byte[] data = new byte[20];

        sendRequest(RPLIDAR_CMD_GET_DEVICE_INFO);
        if (!checkDescriptor(RPLIDAR_GET_DEVICE_INFO_R_DESCRIPTOR))
            throw new IOException("Invalid Response");

        if (inputStream.read(data, 0, data.length) != data.length)
            throw new IOException("Invalid Response");

        return new RPLidar_InfoPacket(data);
    }

    private RPLidar_HealthPacket getHealthPacket() throws IOException, TimeoutException, InterruptedException {
        byte[] data = new byte[3];

        sendRequest(RPLIDAR_CMD_GET_DEVICE_HEALTH);
        if (!checkDescriptor(RPLIDAR_GET_DEVICE_HEALTH_R_DESCRIPTOR))
            throw new IOException("Invalid Response");

        if (inputStream.read(data, 0, data.length) != data.length)
            throw new IOException("Invalid Response");

        return new RPLidar_HealthPacket(data);
    }

    private RPLidar_DataPacket getDataPacket() throws IOException {
        byte[] data = new byte[5];

        // wait for 5 bytes of data
        int available = inputStream.available();
        while (available < 5) {
            available = inputStream.available();
        }
        Log.log("RoboPeakRPLIDARA1M1R1", "5 bytes Of Data Available");

        // clear the buffer of all but 1 packet (and possible part packet)
        for (int i = 0; i < (((available / 5) - 1) * 5); i++) {
            inputStream.read();
        }

        // read the valid packet
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) inputStream.read();
        }

        return new RPLidar_DataPacket(data);
    }
}

class RPLidar_DataPacket {
    private int sync_quality;
    private int angle;
    private int distance;

    RPLidar_DataPacket(byte[] data) {
        sync_quality = data[0];
        angle = (((int) data[2] & 0xFF) << 8) | ((int) data[1] & 0xFF);
        distance = (((int) data[4] & 0xFF) << 8) | ((int) data[3] & 0xFF);
    }

    public int getSync_quality() {
        return sync_quality;
    }

    public float getAngle() {
        return ((angle >> 1) / 64.0f);
    }

    public float getDistance() {
        return ((float) distance / 4.0f) / 1000.0f;
    }
}

class RPLidar_HealthPacket {
    private int status;
    private int error;

    RPLidar_HealthPacket(byte[] data) {
        status = data[0];
        error = (data[2] << 8) | data[1];
    }

    public int getStatus() {
        return status;
    }

    public int getError() {
        return error;
    }
}

class RPLidar_InfoPacket {
    private int model;
    private int firmware_version;
    private int hardware_version;
    private int[] serialnum = new int[16];

    RPLidar_InfoPacket(byte[] data) {
        model = data[0];
        firmware_version = (data[2] << 8) | data[1];
        hardware_version = data[3];
        for (int i = 0; i < 16; i++) {
            serialnum[i] = data[i + 4];
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