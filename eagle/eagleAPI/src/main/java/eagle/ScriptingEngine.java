package eagle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.PositionMetric;
import eagle.navigation.positioning.PositionGPS;
import eagle.sdkInterface.SDKAdaptor;

/**
 * ScriptingEngine Class
 *
 * @author Cameron Cross
 * @version 0.0.1
 * @since 04/09/2015
 * <p/>
 * Date Modified	04/09/2015 - Cameron
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
            put("FLYTORELATIVE", "FLYTORELATIVE _longitude_ _latitude_ _altitude_ _bearing_ _[speed]_ | Fly the drone to a given relative position");
            put("FLYTOGPS", "FLYTOGPS _longitude_ _latitude_ _altitude_ _bearing_ _[speed]_ | Fly the drone to a given GPS position");
            put("CHANGELONGITUDERELATIVE", "CHANGELONGITUDERELATIVE _longitude_ _[speed]_ | Change the longitude relative");
            put("CHANGELATITUDERELATIVE", "CHANGELATITUDERELATIVE _latitude_ _[speed]_ | Change the latitude relative");
            put("CHANGEALTITUDERELATIVE", "CHANGEALTITUDERELATIVE _altitude_ _[speed]_ | Change the altitude relative");
            put("CHANGEYAWRELATIVE", "CHANGEYAWRELATIVE _yaw_ _[speed]_ | Change the yaw relative");
            put("CHANGELONGITUDEGPS", "CHANGELONGITUDEGPS _longitude_ _[speed]_ | Change the longitude GPS");
            put("CHANGELATITUDEGPS", "CHANGELATITUDEGPS _latitude_ _[speed]_ | Change the latitude GPS");
            put("CHANGEALTITUDEGPS", "CHANGEALTITUDEGPS _altitude_ _[speed]_ | Change the altitude GPS");
            put("CHANGEYAWGPS", "CHANGEYAWGPS _yaw_ _[speed]_ | Change the yaw GPS");
            put("GOHOME", "GOHOME | Flys the drone to its home position");
            put("GETPOSITIONASSIGNED", "GETPOSITIONASSIGNED | prints out the drones current position");
            put("GETHOMEPOSITION", "GETHOMEPOSITION | prints the home position of the drone");
            put("SETHOMEPOSITION", "SETHOMEPOSITION _longitude_ _latitude_ _altitude_ _bearing_ | Set the home position");
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
                case "FLYTORELATIVE":
                    if (array.length == 5) {
                        double lon = Double.parseDouble(array[1]);
                        double lat = Double.parseDouble(array[2]);
                        double alt = Double.parseDouble(array[3]);
                        double bea = Double.parseDouble(array[4]);
                        PositionMetric newPos = new PositionMetric(lon, lat, alt, new Angle(0),new Angle(0), new Angle(bea));
                        if (adaptor.flyToRelative(newPos)) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else if (array.length == 6) {
                        double lon = Double.parseDouble(array[1]);
                        double lat = Double.parseDouble(array[2]);
                        double alt = Double.parseDouble(array[3]);
                        double bea = Double.parseDouble(array[4]);
                        double spe = Double.parseDouble(array[5]);
                        PositionMetric newPos = new PositionMetric(lon, lat, alt, new Angle(0),new Angle(0), new Angle(bea));
                        if (adaptor.flyToRelative(newPos, spe)) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "FLYTOGPS":
                    if (array.length == 5) {
                        double lon = Double.parseDouble(array[1]);
                        double lat = Double.parseDouble(array[2]);
                        double alt = Double.parseDouble(array[3]);
                        double bea = Double.parseDouble(array[4]);
                        PositionGPS newPos = new PositionGPS(lon, lat, alt, new Angle(0),new Angle(0), new Angle(bea));
                        if (adaptor.flyToGPS(newPos)) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else if (array.length == 6) {
                        double lon = Double.parseDouble(array[1]);
                        double lat = Double.parseDouble(array[2]);
                        double alt = Double.parseDouble(array[3]);
                        double bea = Double.parseDouble(array[4]);
                        double spe = Double.parseDouble(array[5]);
                        PositionGPS newPos = new PositionGPS(lon, lat, alt, new Angle(0),new Angle(0), new Angle(bea));
                        if (adaptor.flyToGPS(newPos, spe)) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "CHANGELONGITUDERELATIVE":
                    if (array.length == 2) {
                        double val = Double.parseDouble(array[1]);
                        if (adaptor.changeLongitudeRelative(val)) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else if (array.length == 3) {
                        double val = Double.parseDouble(array[1]);
                        double spe = Double.parseDouble(array[2]);
                        if (adaptor.changeLongitudeRelative(val, spe)) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "CHANGELATITUDERELATIVE":
                    if (array.length == 2) {
                        double val = Double.parseDouble(array[1]);
                        if (adaptor.changeLatitudeRelative(val)) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else if (array.length == 3) {
                        double val = Double.parseDouble(array[1]);
                        double spe = Double.parseDouble(array[2]);
                        if (adaptor.changeLatitudeRelative(val, spe)) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "CHANGEALTITUDERELATIVE":
                    if (array.length == 2) {
                        double val = Double.parseDouble(array[1]);
                        if (adaptor.changeAltitudeRelative(val)) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else if (array.length == 3) {
                        double val = Double.parseDouble(array[1]);
                        double spe = Double.parseDouble(array[2]);
                        if (adaptor.changeAltitudeRelative(val, spe)) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "CHANGEYAWRELATIVE":
                    if (array.length == 2) {
                        double val = Double.parseDouble(array[1]);
                        if (adaptor.changeYawRelative(new Angle(val))) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else if (array.length == 3) {
                        double val = Double.parseDouble(array[1]);
                        double spe = Double.parseDouble(array[2]);
                        if (adaptor.changeYawRelative(new Angle(val), spe)) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "CHANGELONGITUDEGPS":
                    if (array.length == 2) {
                        double val = Double.parseDouble(array[1]);
                        if (adaptor.changeLongitudeGPS(val)) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else if (array.length == 3) {
                        double val = Double.parseDouble(array[1]);
                        double spe = Double.parseDouble(array[2]);
                        if (adaptor.changeLongitudeGPS(val, spe)) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "CHANGELATITUDEGPS":
                    if (array.length == 2) {
                        double val = Double.parseDouble(array[1]);
                        if (adaptor.changeLatitudeGPS(val)) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else if (array.length == 3) {
                        double val = Double.parseDouble(array[1]);
                        double spe = Double.parseDouble(array[2]);
                        if (adaptor.changeLatitudeGPS(val, spe)) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "CHANGEALTITUDEGPS":
                    if (array.length == 2) {
                        double val = Double.parseDouble(array[1]);
                        if (adaptor.changeAltitudeGPS(val)) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else if (array.length == 3) {
                        double val = Double.parseDouble(array[1]);
                        double spe = Double.parseDouble(array[2]);
                        if (adaptor.changeAltitudeGPS(val, spe)) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                case "CHANGEYAWGPS":
                    if (array.length == 2) {
                        double val = Double.parseDouble(array[1]);
                        if (adaptor.changeYawGPS(new Angle(val))) {
                            return "SUCCESS";
                        } else {
                            return "FAIL";
                        }
                    } else if (array.length == 3) {
                        double val = Double.parseDouble(array[1]);
                        double spe = Double.parseDouble(array[2]);
                        if (adaptor.changeYawGPS(new Angle(val), spe)) {
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
                    if (array.length == 5) {
                        double lon = Double.parseDouble(array[1]);
                        double lat = Double.parseDouble(array[2]);
                        double alt = Double.parseDouble(array[3]);
                        double bea = Double.parseDouble(array[4]);
                        PositionMetric newPos = new PositionMetric(lon, lat, alt, new Angle(0),new Angle(0), new Angle(bea));
                        adaptor.setHomePosition(newPos);
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
