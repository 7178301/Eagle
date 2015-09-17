package eagle.sdkInterface.sensorAdaptors.gpsAdaptors;

import eagle.Log;
import eagle.navigation.positioning.PositionGPS;
import eagle.sdkInterface.sensorAdaptors.AdaptorGPS;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 * eagle.sdkInterface.sensorAdaptors.gpsAdaptors
 *
 * @author cameron
 * @version 0.0.1
 * @since 9/9/15
 * <p/>
 */
public class JavaGPS extends AdaptorGPS {
    PositionGPS currentPos = null;
    static SerialPort serialPort;

    double altitude, longitude, latitude;


    public JavaGPS() {
        super("JavaGPS", "NMEA_GPS", "0.0.1");
        serialPort = new SerialPort("/dev/ttyUSB0");
        try {
            serialPort.openPort();
            serialPort.setParams(9600, 8, 1, 0);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean connectToSensor() {
        return false;
    }

    @Override
    public PositionGPS getData() {
        return currentPos;
    }

    @Override
    public boolean isConnectedToSensor() {
        return false;
    }

    @Override
    public boolean isDataReady() {
        return !(currentPos == null);
    }

    class readThread implements Runnable {
//protocol info from here: http://www.gpsinformation.org/dale/nmea.htm
        @Override
        public void run() {
            while (true)
            {
                try {
                    String gpsString = serialPort.readString();
                    Log.log(gpsString);
                    String parts[] = gpsString.split(",");
                    if (parts[0] == "$GPGGA") {
                        latitude = parseLatitude(parts[2], parts[3]);
                        longitude = parseLongitude(parts[4], parts[5]);
                        altitude = parseAltitude(parts[9], parts[10]);
                    }

                } catch (SerialPortException e) {
                    e.printStackTrace();
                }
            }
        }

        private double parseAltitude(String part, String part1) {
            return Double.parseDouble(part);
        }

        private double parseLongitude(String part, String part1) {
            double degree = Double.parseDouble(part.substring(0,2));
            double minutes = Double.parseDouble(part.substring(2));
            //01131.000,E
            return degree+minutes/60;
        }

        private double parseLatitude(String part, String part1) {
            double degree = Double.parseDouble(part.substring(0,2));
            double minutes = Double.parseDouble(part.substring(2));
            //4807.038,N
            return degree+minutes/60;
        }
    }


}
