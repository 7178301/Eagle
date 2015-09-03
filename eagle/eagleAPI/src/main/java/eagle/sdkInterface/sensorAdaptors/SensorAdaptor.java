package eagle.sdkInterface.sensorAdaptors;

/**
 * Abstract SensorAdaptor Class
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */
public abstract class SensorAdaptor {

    private String adaptorName = null;
    private String adaptorVersion = null;
    private String adaptorManufacturer = null;
    private String adaptorModel = null;

    public SensorAdaptor(String adaptorManufacturer, String adaptorModel, String adaptorVersion) {
        this.adaptorName = adaptorManufacturer + " " + adaptorModel;
        this.adaptorManufacturer = adaptorManufacturer;
        this.adaptorModel = adaptorModel;
        this.adaptorVersion = adaptorVersion;
    }

    public abstract boolean isConnectedToSensor();

    public abstract boolean isDataReady();

    public String getAdaptorVersion() {
        return adaptorVersion;
    }

    public String getAdaptorName() {
        return adaptorName;
    }

    public String getAdaptorManufacturer() {
        return adaptorManufacturer;
    }

    public String getAdaptorModel() {
        return adaptorModel;
    }

    public boolean setSensorPins(int[] pins){
        return false;
    }

    public String[] getSensorPinsDescription(){
        return new String[0];
    }
}