package eagle;

/** Drone API
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 * @author          Glarah */

import eagle.navigation.Navigation;
import eagle.navigation.positioning.Bearing;
import eagle.navigation.positioning.Position;
import eagle.sdkInterface.AdaptorLoader;
import eagle.sdkInterface.SDKAdaptor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class Drone {
    final private String apiVersion = "0.0.1";


    private SDKAdaptor adaptor = null;
    private Navigation navigation = null;
    private AdaptorLoader adaptorLoader = null;

    double minSpeed = -0;
    double maxSpeed = -0;

    public Drone(){
        this.adaptorLoader = new AdaptorLoader();
        this.navigation = new Navigation(this);
    }

    public String getAPIVersion(){
        return apiVersion;
    }
    public HashMap getSDKAdaptorMap(){
        if(this.adaptorLoader!=null)
            return this.adaptorLoader.getSDKAdaptorMap();
        else
            return new AdaptorLoader().getSDKAdaptorMap();
    }
    public SDKAdaptor getSDKAdaptor(){
        return this.adaptor;
    }
    public void setSDKAdaptor(String adaptor){
        this.adaptor = this.adaptorLoader.getSDKAdaptor(adaptor);
        this.adaptor.loadDefaultSensorAdaptors(adaptorLoader);
    }

    public String executeInstruction(String instruction) throws InvalidInstructionException {
        if (adaptor == null) {
            return "NO DRONE ADAPTOR";
        }
        try {
            String[] array = instruction.toUpperCase().split(" ");
            switch (array[0]) {
                case "GETPOSITION":
                    if (array.length == 1) {
                        return adaptor.getPositionAssigned().toString();
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
                case "DELAY":
                    if (array.length == 2) {
                        int time = Integer.parseInt(array[1]);
                        adaptor.delay(time);
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                    break;
                case "FLYTORELATIVE":
                    if (array.length == 7) {
                        double lon = Double.parseDouble(array[1]);
                        double lat = Double.parseDouble(array[2]);
                        double alt = Double.parseDouble(array[3]);
                        double rol = Double.parseDouble(array[4]);
                        double pit = Double.parseDouble(array[5]);
                        double bea = Double.parseDouble(array[6]);
                        Position newPos = new Position(lon, lat, alt, rol, pit, new Bearing(bea));
                        adaptor.flyToRelative(newPos);
                    } else if (array.length == 8) {
                        double lon = Double.parseDouble(array[1]);
                        double lat = Double.parseDouble(array[2]);
                        double alt = Double.parseDouble(array[3]);
                        double rol = Double.parseDouble(array[4]);
                        double pit = Double.parseDouble(array[5]);
                        double bea = Double.parseDouble(array[6]);
                        double spe = Double.parseDouble(array[7]);
                        Position newPos = new Position(lon, lat, alt, rol, pit, new Bearing(bea));
                        adaptor.flyToRelative(newPos, spe);
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                    break;
                case "FLYTOABSOLUTE":
                    if (array.length == 7) {
                        double lon = Double.parseDouble(array[1]);
                        double lat = Double.parseDouble(array[2]);
                        double alt = Double.parseDouble(array[3]);
                        double rol = Double.parseDouble(array[4]);
                        double pit = Double.parseDouble(array[5]);
                        double bea = Double.parseDouble(array[6]);
                        Position newPos = new Position(lon, lat, alt, rol, pit, new Bearing(bea));
                        adaptor.flyToAbsolute(newPos);
                    } else if (array.length == 8) {
                        double lon = Double.parseDouble(array[1]);
                        double lat = Double.parseDouble(array[2]);
                        double alt = Double.parseDouble(array[3]);
                        double rol = Double.parseDouble(array[4]);
                        double pit = Double.parseDouble(array[5]);
                        double bea = Double.parseDouble(array[6]);
                        double spe = Double.parseDouble(array[7]);
                        Position newPos = new Position(lon, lat, alt, rol, pit, new Bearing(bea));
                        adaptor.flyToAbsolute(newPos, spe);
                    } else {
                        throw new InvalidInstructionException("Wrong Number of Values: " + instruction);
                    }
                    break;
                default:
                    return "UNKNOWN COMMAND";
            }
        }
        catch (Exception e) {
            throw new InvalidInstructionException(instruction);
        }
        return "SUCCESS";

    }

    public final void runRoutine(String filename) {
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = br.readLine()) != null) {
                    executeInstruction(line);
                }
            } catch (InvalidInstructionException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class InvalidInstructionException extends Exception {
        public InvalidInstructionException(String s) {
            super(s);
        }
    }

}


//What if a drone needs to constantly update its position?