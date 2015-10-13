package eagle.sdkInterface.sensorAdaptors;

import java.util.HashSet;

/**
 * Abstract Sensor Adaptor Class
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
    private String adaptorHardwareModel = null;
    protected String adaptorHardwareFirmwareVersion = null;
    protected String adaptorHardwareVersion = null;
    protected String adaptorHardwareSerialNumber = null;

    protected HashSet<eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorCallback> sensorAdaptorCallback;

    public SensorAdaptor(String adaptorManufacturer, String adaptorHardwareModel, String adaptorVersion) {
        this.adaptorName = adaptorManufacturer + " " + adaptorHardwareModel;
        this.adaptorManufacturer = adaptorManufacturer;
        this.adaptorHardwareModel = adaptorHardwareModel;
        this.adaptorVersion = adaptorVersion;
        sensorAdaptorCallback = new HashSet<>();
    }

    public abstract boolean connectToSensor();

    public abstract boolean disconnectFromSensor();

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

    public String getAdaptorHardwareModel() {
        return adaptorHardwareModel;
    }

    public String adaptorHardwareFirmwareVersion(){
        return adaptorHardwareFirmwareVersion;
    }

    public String adaptorHardwareVersion(){
        return adaptorHardwareVersion;
    }

    public String adaptorHardwareSerialNumber(){
        return adaptorHardwareSerialNumber;
    }

    public boolean setSensorPins(int[] pins) {
        return false;
    }

    public String[] getSensorPinsDescription() {
        return new String[0];
    }

    public boolean setSensorConfigurables(String[] confs) {
        return false;
    }

    public String[] getSensorConfigurables() {
        return new String[0];
    }

    public boolean setAndroidContext(Object object){
        return false;
    }

    public boolean setController(Object object){
        return false;
    }

    public void addSensorAdaptorCallback(final eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorCallback sensorAdaptorCallback) {
        this.sensorAdaptorCallback.add(sensorAdaptorCallback);
    }
}