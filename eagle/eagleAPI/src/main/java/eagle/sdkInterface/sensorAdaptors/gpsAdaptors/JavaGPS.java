package eagle.sdkInterface.sensorAdaptors.gpsAdaptors;

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
    SerialPort serialPort;
    String port = null;
    Thread thread = null;

    double altitude, longitude, latitude;
    boolean ready = false;

    public JavaGPS() {
        super("JavaGPS", "NMEA_GPS", "0.0.1");
    }

    //TODO Following Method Need Proper Implementation
    @Override
    public boolean connectToSensor() {
        return false;
    }

    //TODO Following Method Need Proper Implementation
    @Override
    public boolean disconnectFromSensor() {
        return false;
    }

    @Override
    public PositionGPS getData() {
        ready = false;
        return new PositionGPS(latitude, longitude, altitude, new Angle(0), new Angle(0), new Angle(0));
    }

    //TODO Following Method Need Proper Implementation
    @Override
    public boolean isConnectedToSensor() {
        return false;
    }

    @Override
    public boolean isDataReady() {
        return ready;
    }

    @Override
    public boolean setSensorConfigurables(String[] confs) {
        if (thread != null) {
            thread.interrupt();
        }
        if (confs.length != 1) {
            return false;
        }
        port = confs[0];
        serialPort = new SerialPort(port);
        try {
            serialPort.openPort();
            serialPort.setParams(4800, 8, 1, 0);
            thread = new Thread(new ReadThread());
            thread.start();
        } catch (SerialPortException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String[] getSensorConfigurables() {
        return new String[0];
    }

    class ReadThread implements Runnable {
        //protocol info from here: http://www.gpsinformation.org/dale/nmea.htm
        @Override
        public void run() {
            while (true) {
                try {
                    String gpsString = readString(serialPort);
                    //Log.log(gpsString);
                    String parts[] = gpsString.split(",");
                    if (parts[0].compareTo("$GPGGA") == 0) {
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

        private String readString(SerialPort serialPort) throws SerialPortException {
            StringBuilder sb = new StringBuilder();
            while (true) {
                byte[] val = serialPort.readBytes(1);
                if ((char) val[0] == '\n') {
                    return sb.toString();
                } else {
                    sb.append((char) val[0]);
                }
            }
        }

        private double parseAltitude(String part, String part1) {
            try {
                return Double.parseDouble(part);
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        private double parseLongitude(String part, String part1) {
            double degree = 0, minutes = 0;
            try {
                degree = Double.parseDouble(part.substring(0, 2));
                minutes = Double.parseDouble(part.substring(2));
            } catch (NumberFormatException e) {

            } catch (IndexOutOfBoundsException f) {

            }
            //01131.000,E
            return degree + minutes / 60;
        }

        private double parseLatitude(String part, String part1) {
            double degree = 0, minutes = 0;
            try {
                degree = Double.parseDouble(part.substring(0, 2));
                minutes = Double.parseDouble(part.substring(2));
            } catch (NumberFormatException e) {

            } catch (IndexOutOfBoundsException f) {

            }
            //4807.038,N
            return degree + minutes / 60;
        }
    }


}
