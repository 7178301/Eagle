package eagle.sdkInterface.sensorAdaptors.gpsAdaptors;

import eagle.Log;
import eagle.navigation.positioning.Angle;
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
    boolean ready = false;


    public JavaGPS() {
        super("JavaGPS", "NMEA_GPS", "0.0.1");
        serialPort = new SerialPort("/dev/ttyUSB0");
        try {
            serialPort.openPort();
            serialPort.setParams(4800, 8, 1, 0);
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
        ready = false;
        return new PositionGPS(latitude, longitude, altitude, new Angle(0), new Angle(0), new Angle(0));
    }

    @Override
    public boolean isConnectedToSensor() {
        return false;
    }

    @Override
    public boolean isDataReady() {
        return ready;
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
                        ready = true;
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
