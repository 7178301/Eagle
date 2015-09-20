package eagle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.PositionDisplacement;
import eagle.navigation.positioning.PositionGPS;
import eagle.navigation.positioning.PositionMetric;
import eagle.sdkInterface.SDKAdaptor;

/**
 * Created by Cameron on 4/09/2015.
 */
public class ScriptingEngine {
    SDKAdaptor adaptor;

    static Map<String, String> commands = new HashMap<String, String>() {
        {
            put("CONNECTTODRONE", "CONNECTTODRONE | Connect to the drone");
            put("DISCONNECTFROMDRONE", "DISCONNECTFROMDRONE | Disconnect from the drone");
            put("ISCONNECTEDTODRONE", "ISCONNECTEDTODRONE | Get the status of the connection from the drone");
            put("STANDBYDRONE", "STANDBYDRONE | Standby the drone");
            put("RESUMEDRONE", "RESUMEDRONE | Resume the drone");
            put("SHUTDOWNDRONE", "SHUTDOWNDRONE | Shutdown the drone");
            put("GETADAPTORVERSION", "GETADAPTORVERSION | Get the adaptor version");
            put("GETSDKVERSION", "GETSDKVERSION | Get the SDK version");
            put("GETADAPTORNAME", "GETADAPTORNAME | Get the adaptor name");
            put("GETADAPTORMANUFACTURER", "GETADAPTORMANUFACTURER | Get the adaptor manufacturer");
            put("GETADAPTORMODEL", "GETADAPTORMODEL | Get the adaptor model");
            put("FLYTO", "FLYTO _PositionType_ _longitude_ _latitude_ _altitude_ _Angle_ _[speed]_ | Fly the drone to a given position with type _PositionType_ (GPS, METRIC, DISPLACEMENT)");
            put("CHANGELONGITUDE", "CHANGELONGITUDE _PositionType_ _longitude_ _[speed]_ | Change the longitude");
            put("CHANGELATITUDE", "CHANGELATITUDE _PositionType_ _latitude_ _[speed]_ | Change the latitude");
            put("CHANGEALTITUDE", "CHANGEALTITUDE _PositionType_ _altitude_ _[speed]_ | Change the altitude");
            put("CHANGEYAW", "CHANGEYAW _PositionType_ _yaw_ _[speed]_ | Change the yaw");
            put("GOHOME", "GOHOME | Flys the drone to its home position");
            put("GETPOSITIONASSIGNED", "GETPOSITIONASSIGNED | prints out the drones current position");
            put("GETHOMEPOSITION", "GETHOMEPOSITION | prints the home position of the drone");
            put("SETHOMEPOSITION", "SETHOMEPOSITION _PositionType_ _longitude_ _latitude_ _altitude_ _Angle_ | Set the home position");
            put("DELAY", "DELAY _time_ | Delays for _time_ milliseconds");
            put("HELP", "HELP _[command]_ | Prints a list of commands");
            put("LOG", "LOG _message_ | Write a message to the log");
        }
    };

    ScriptingEngine(SDKAdaptor sdkAdaptor) {
        this.adaptor = sdkAdaptor;
    }

    public String executeInstruction(String instruction) throws InvalidInstructionException {
        if (adaptor == null) {
            return "NO DRONE ADAPTOR";
        }
        try {
            String[] array = instruction.toUpperCase().split(" ");
            switch (array[0]) {
                case "CONNECTTODRONE":
                    if (array.length == 1) {
                        if (adaptor.connectToDrone()) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "DISCONNECTFROMDRONE":
                    if (array.length == 1) {
                        if (adaptor.disconnectFromDrone()) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "ISCONNECTEDTODRONE":
                    if (array.length == 1) {
                        if (adaptor.isConnectedToDrone()) {
                            return "TRUE";
                        } else {
                            return "FALSE";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "STANDBYDRONE":
                    if (array.length == 1) {
                        if (adaptor.standbyDrone()) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "RESUMEDRONE":
                    if (array.length == 1) {
                        if (adaptor.resumeDrone()) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "SHUTDOWNDRONE":
                    if (array.length == 1) {
                        if (adaptor.shutdownDrone()) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "GETADAPTORVERSION":
                    if (array.length == 1) {
                        return adaptor.getAdaptorVersion();
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "GETSDKVERSION":
                    if (array.length == 1) {
                        return adaptor.getSdkVersion();
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "GETADAPTORNAME":
                    if (array.length == 1) {
                        return adaptor.getAdaptorName();
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "GETADAPTORMANUFACTURER":
                    if (array.length == 1) {
                        return adaptor.getAdaptorManufacturer();
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "GETADAPTORMODEL":
                    if (array.length == 1) {
                        return adaptor.getAdaptorModel();
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "FLYTO":
                    if (array.length == 6) {

                        double lon = Double.parseDouble(array[2]);
                        double lat = Double.parseDouble(array[3]);
                        double alt = Double.parseDouble(array[4]);
                        double bea = Double.parseDouble(array[5]);
                        boolean returnval;
                        switch (array[1]) {
                            case "GPS":
                            case "G":
                            case "-G":
                                returnval = adaptor.flyTo(new PositionGPS(lon, lat, alt, new Angle(0), new Angle(0), new Angle(bea)));
                                break;
                            case "METRIC":
                            case "M":
                            case "-M":
                                returnval = adaptor.flyTo(new PositionMetric(lon, lat, alt, new Angle(0), new Angle(0), new Angle(bea)));
                                break;
                            case "DISPLACEMENT":
                            case "DISP":
                            case "D":
                            case "-D":
                                returnval = adaptor.flyTo(new PositionDisplacement(lon, lat, alt, new Angle(0), new Angle(0), new Angle(bea)));
                                break;
                            default:
                                throw new InvalidInstructionException("Invalid Position Type: " + instruction);
                        }
                        if (returnval == true) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else if (array.length == 7) {
                        double lon = Double.parseDouble(array[2]);
                        double lat = Double.parseDouble(array[3]);
                        double alt = Double.parseDouble(array[4]);
                        double bea = Double.parseDouble(array[5]);
                        double spe = Double.parseDouble(array[6]);
                        boolean returnval;
                        switch (array[1]) {
                            case "GPS":
                            case "G":
                            case "-G":
                                returnval = adaptor.flyTo(new PositionGPS(lon, lat, alt, new Angle(0), new Angle(0), new Angle(bea)), spe);
                                break;
                            case "METRIC":
                            case "M":
                            case "-M":
                                returnval = adaptor.flyTo(new PositionMetric(lon, lat, alt, new Angle(0), new Angle(0), new Angle(bea)), spe);
                                break;
                            case "DISPLACEMENT":
                            case "DISP":
                            case "D":
                            case "-D":
                                returnval = adaptor.flyTo(new PositionDisplacement(lon, lat, alt, new Angle(0), new Angle(0), new Angle(bea)), spe);
                                break;
                            default:
                                throw new InvalidInstructionException("Invalid Position Type: " + instruction);
                        }
                        if (returnval) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "CHANGELONGITUDE":
                    if (array.length == 3) {
                        double val = Double.parseDouble(array[2]);
                        boolean returnval;
                        switch (array[1]) {
                            case "GPS":
                            case "G":
                            case "-G":
                                returnval = adaptor.changeLongitudeGPS(val);
                                break;
                            case "METRIC":
                            case "M":
                            case "-M":
                                returnval = adaptor.changeLongitudeMetric(val);
                                break;
                            case "DISPLACEMENT":
                            case "DISP":
                            case "D":
                            case "-D":
                                returnval = adaptor.changeLongitudeDisplacement(val);
                                break;
                            default:
                                throw new InvalidInstructionException("Invalid Position Type: " + instruction);
                        }
                        if (returnval) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else if (array.length == 4) {
                        double val = Double.parseDouble(array[2]);
                        double spe = Double.parseDouble(array[3]);
                        boolean returnval;
                        switch (array[1]) {
                            case "GPS":
                            case "G":
                            case "-G":
                                returnval = adaptor.changeLongitudeGPS(val, spe);
                                break;
                            case "METRIC":
                            case "M":
                            case "-M":
                                returnval = adaptor.changeLongitudeMetric(val, spe);
                                break;
                            case "DISPLACEMENT":
                            case "DISP":
                            case "D":
                            case "-D":
                                returnval = adaptor.changeLongitudeDisplacement(val, spe);
                                break;
                            default:
                                throw new InvalidInstructionException("Invalid Position Type: " + instruction);
                        }
                        if (returnval) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "CHANGELATITUDE":
                    if (array.length == 3) {
                        double val = Double.parseDouble(array[2]);
                        boolean returnval;
                        switch (array[1]) {
                            case "GPS":
                            case "G":
                            case "-G":
                                returnval = adaptor.changeLatitudeGPS(val);
                                break;
                            case "METRIC":
                            case "M":
                            case "-M":
                                returnval = adaptor.changeLatitudeMetric(val);
                                break;
                            case "DISPLACEMENT":
                            case "DISP":
                            case "D":
                            case "-D":
                                returnval = adaptor.changeLatitudeDisplacement(val);
                                break;
                            default:
                                throw new InvalidInstructionException("Invalid Position Type: " + instruction);
                        }
                        if (returnval) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else if (array.length == 4) {
                        double val = Double.parseDouble(array[2]);
                        double spe = Double.parseDouble(array[3]);
                        boolean returnval;
                        switch (array[1]) {
                            case "GPS":
                            case "G":
                            case "-G":
                                returnval = adaptor.changeLatitudeGPS(val, spe);
                                break;
                            case "METRIC":
                            case "M":
                            case "-M":
                                returnval = adaptor.changeLatitudeMetric(val, spe);
                                break;
                            case "DISPLACEMENT":
                            case "DISP":
                            case "D":
                            case "-D":
                                returnval = adaptor.changeLatitudeDisplacement(val, spe);
                                break;
                            default:
                                throw new InvalidInstructionException("Invalid Position Type: " + instruction);
                        }
                        if (returnval) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "CHANGEALTITUDE":
                    if (array.length == 3) {
                        double val = Double.parseDouble(array[2]);
                        boolean returnval;
                        switch (array[1]) {
                            case "GPS":
                            case "G":
                            case "-G":
                                returnval = adaptor.changeAltitudeGPS(val);
                                break;
                            case "METRIC":
                            case "M":
                            case "-M":
                                returnval = adaptor.changeAltitudeMetric(val);
                                break;
                            case "DISPLACEMENT":
                            case "DISP":
                            case "D":
                            case "-D":
                                returnval = adaptor.changeAltitudeDisplacement(val);
                                break;
                            default:
                                throw new InvalidInstructionException("Invalid Position Type: " + instruction);
                        }
                        if (returnval == true) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else if (array.length == 4) {
                        double val = Double.parseDouble(array[2]);
                        double spe = Double.parseDouble(array[3]);
                        boolean returnval;
                        switch (array[1]) {
                            case "GPS":
                            case "G":
                            case "-G":
                                returnval = adaptor.changeAltitudeGPS(val, spe);
                                break;
                            case "METRIC":
                            case "M":
                            case "-M":
                                returnval = adaptor.changeAltitudeMetric(val, spe);
                                break;
                            case "DISPLACEMENT":
                            case "DISP":
                            case "D":
                            case "-D":
                                returnval = adaptor.changeAltitudeDisplacement(val, spe);
                                break;
                            default:
                                throw new InvalidInstructionException("Invalid Position Type: " + instruction);
                        }
                        if (returnval == true) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "CHANGEYAW":
                    if (array.length == 3) {
                        double val = Double.parseDouble(array[2]);
                        boolean returnval;
                        switch (array[1]) {
                            case "GPS":
                            case "G":
                            case "-G":
                                returnval = adaptor.changeYawGPS(new Angle(val));
                                break;
                            case "METRIC":
                            case "M":
                            case "-M":
                                returnval = adaptor.changeYawMetric(new Angle(val));
                                break;
                            case "DISPLACEMENT":
                            case "DISP":
                            case "D":
                            case "-D":
                                returnval = adaptor.changeYawDisplacement(new Angle(val));
                                break;
                            default:
                                throw new InvalidInstructionException("Invalid Position Type: " + instruction);
                        }
                        if (returnval == true) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else if (array.length == 4) {
                        double val = Double.parseDouble(array[2]);
                        double spe = Double.parseDouble(array[3]);
                        boolean returnval;
                        switch (array[1]) {
                            case "GPS":
                            case "G":
                            case "-G":
                                returnval = adaptor.changeYawGPS(new Angle(val), spe);
                                break;
                            case "METRIC":
                            case "M":
                            case "-M":
                                returnval = adaptor.changeYawMetric(new Angle(val), spe);
                                break;
                            case "DISPLACEMENT":
                            case "DISP":
                            case "D":
                            case "-D":
                                returnval = adaptor.changeYawDisplacement(new Angle(val), spe);
                                break;
                            default:
                                throw new InvalidInstructionException("Invalid Position Type: " + instruction);
                        }
                        if (returnval == true) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "GOHOME":
                    if (array.length == 1) {
                        adaptor.goHome();
                    } else if (array.length == 2) {
                        adaptor.goHome(Double.parseDouble(array[1]));
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                    break;
                case "GETPOSITIONASSIGNED":
                    if (array.length == 1) {
                        return adaptor.getPositionAssigned().toString();
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "GETHOMEPOSITION":
                    if (array.length == 1) {
                        return adaptor.getHomePosition().toString();
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "SETHOMEPOSITION":
                    if (array.length == 6) {
                        double lon = Double.parseDouble(array[2]);
                        double lat = Double.parseDouble(array[3]);
                        double alt = Double.parseDouble(array[4]);
                        double bea = Double.parseDouble(array[5]);
                        try {
                        switch (array[1]) {
                            case "GPS":
                            case "G":
                            case "-G":
                                adaptor.setHomePosition(new PositionGPS(lon, lat, alt, new Angle(0), new Angle(0), new Angle(bea)));
                                break;
                            case "METRIC":
                            case "M":
                            case "-M":
                                adaptor.setHomePosition(new PositionMetric(lon, lat, alt, new Angle(0), new Angle(0), new Angle(bea)));
                                break;
                            default:
                                throw new InvalidInstructionException("Invalid Position Type: " + instruction);
                        }
                        } catch (SDKAdaptor.InvalidPositionException e) {
                            throw new InvalidInstructionException("Invalid Position given: "+instruction);
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                    break;

                case "DELAY":
                    if (array.length == 2) {
                        int time = Integer.parseInt(array[1]);
                        adaptor.delay(time);
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                    break;

                case "HELP":
                    if (array.length == 1) {
                        return join(commands.keySet());
                    } else if (array.length == 2) {
                        if (commands.containsKey(array[1])) {
                            return commands.get(array[1]);
                        } else {
                            return "UNKNOWN COMMAND";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "LOG":
                    if (array.length >= 1) {
                        StringBuilder sb = new StringBuilder();
                        String parts[] = instruction.split(" ");
                        for (int i = 1; i < parts.length; i++) {
                            sb.append(parts[i]);
                            sb.append(" ");
                        }
                        Log.log(sb.toString());
                        return "SUCCESS";
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }

                default:
                    throw new InvalidInstructionException(instruction);
            }
        } catch (NumberFormatException e) {
            throw new InvalidInstructionException(instruction);
        }
        return "SUCCESS";

    }

    public final void runRoutine(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                executeInstruction(line);
            }
        } catch (IOException | InvalidInstructionException e) {
            e.printStackTrace();
        }
    }

    private String join(Set<String> strings) {
        StringBuilder sb = new StringBuilder();
        for (String str : strings) {
            sb.append(str);
            sb.append(" ");
        }
        return sb.toString();
    }

    public class InvalidInstructionException extends Exception {
        public InvalidInstructionException(String s) {
            super(s);
        }
    }
}