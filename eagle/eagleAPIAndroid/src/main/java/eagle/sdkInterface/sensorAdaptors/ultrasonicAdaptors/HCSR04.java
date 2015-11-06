package eagle.sdkInterface.sensorAdaptors.ultrasonicAdaptors;

import eagle.Log;
import eagle.sdkInterface.sensorAdaptors.AdaptorUltrasonic;

import ioio.lib.api.IOIO;
import ioio.lib.api.PulseInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PulseInput.PulseMode;
import ioio.lib.api.exception.ConnectionLostException;

/**
 * Modtronix MOD-USONIC1 Ultrasonic Adaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @author Angela Lau [7160852@student.swin.edu.au]
 * @version 0.0.1
 * @since 14/06/2015
 * <p/>
 * Date Modified	28/08/2015 - Angela
 */
public class HCSR04 extends AdaptorUltrasonic {

    private int triggerPin = -1;
    private int echoPin = -1;
    private DigitalOutput trigger;
    private PulseInput echo;
    private float echoSeconds;
    private float echoDistanceMetres; // Distance measurements in metres
    private IOIO ioio;

    public HCSR04() {
        super("HC-SR04", "HC-SR04", "0.0.1");
    }

    /**
     * Connects the HCSR04 ultrasonic sensor to the IOIO board
     * Will return false unless setController(...) and setSensorPins(...) has both been called successfully
     * @return true if connection was successful otherwise false
     */
    public boolean connectToSensor() {
        if (ioio == null || triggerPin == -1 || echoPin == -1) {
            Log.log("HC-SR04ConnectToSensor", "Checking IOIO Object & Pins FAIL: Not Set");
            return false;
        }
        try {
            trigger = ioio.openDigitalOutput(triggerPin);
            echo = ioio.openPulseInput(echoPin, PulseMode.POSITIVE);
        } catch (ConnectionLostException e) {
            e.printStackTrace();
            Log.log("HC-SR04ConnectToSensor", "Checking IOIO Object & Pins FAIL: Connection Lost");
            return false;
        }
        Log.log("HC-SR04ConnectToSensor", "Connected To Sensor");
        return true;
    }

    //TODO Following Method Need Proper Implementation
    @Override
    public boolean disconnectFromSensor() {
        return false;
    }

    /**
     * Sets the IOIO object
     * @param object must be a valid IOIO object
     * @return true if the object is a valid IOIO object otherwise false
     */
    public boolean setController(Object object) {
        if (object instanceof IOIO) {
            this.ioio = (IOIO) object;
            Log.log("HC-SR04SetIOIOController", "Set IOIO Object");
            return true;
        } else
            Log.log("HC-SR04SetIOIOController", "Checking Object FAIL: Not Valid IOIO Object");
            return false;
    }

    /**
     * Checks if a connection to the sensor is established
     * @return true if connection is successful otherwise false
     */
    @Override
    public boolean isConnectedToSensor() {
        if (trigger != null && echo != null) {
            Log.log("HC-SR04IsConnectedToSensor", "Connected");
            return true;
        } else {
            Log.log("HC-SR04IsConnectedToSensor", "Not Connected");
            return false;
        }
    }

    /**
     * Not relevant for this sensor
     * @return true
     */
    @Override
    public boolean isDataReady() {
        return true;
    }

    /**
     * Takes a sample from the sensor
     * This function takes the readings as well as returns it, which results in a blocking for at least 6ms
     * @return the distance measurement in metres
     */
    @Override
    public float getData() {
        try {
            trigger.write(false);
            Thread.sleep(5);
            trigger.write(true);
            Thread.sleep(1);
            trigger.write(false);
            echoSeconds = (echo.getDuration() * 1000 * 1000);
            echoDistanceMetres = (echoSeconds / 29 / 2) / 100;
        } catch (ConnectionLostException e) {
            e.printStackTrace();
            Log.log("HC-SR04GetData", "FAIL: Connection Lost");
            return -1;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.log("HC-SR04GetData", "FAIL: Interrupted");
            return -1;
        }
        return echoDistanceMetres;
    }

    /**
     * Gets the name of the pins used by the sensor
     * @return Trigger Pin and Echo Pin
     */
    @Override
    public String[] getSensorPinsDescription() {
        String[] temp = new String[2];
        temp[0] = "Trigger Pin";
        temp[1] = "Echo Pin";
        return temp;
    }

    /**
     * Sets the pin numbers
     * Inputs must be the same order as getSensorPinsDescription()
     * @param pins the pin numbers
     * @return true if the input is valid (integer array of size 2) otherwise false
     */
    @Override
    public boolean setSensorPins(int[] pins) {
        if (pins != null && pins.length == 2) {
            triggerPin = pins[0];
            echoPin = pins[1];
            return true;
        } else
            return false;
    }
}