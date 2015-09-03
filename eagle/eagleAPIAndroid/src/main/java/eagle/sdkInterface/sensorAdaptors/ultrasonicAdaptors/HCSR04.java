package eagle.sdkInterface.sensorAdaptors.ultrasonicAdaptors;

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

    private int triggerPin;
    private int echoPin;
    private DigitalOutput trigger;
    private PulseInput echo;
    private float echoSeconds;
    private float echoDistanceCm;
    private IOIO ioio;

    public HCSR04() {
        super("HC-SR04", "HC-SR04", "0.0.1");
    }

    public boolean connectToSensor() {
        if (ioio == null) {
            return false;
        }
        try {
            trigger = ioio.openDigitalOutput(triggerPin);
            echo = ioio.openPulseInput(echoPin, PulseMode.POSITIVE);
        } catch (ConnectionLostException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean setAndroidContext(Object object) {
        if (object instanceof IOIO) {
            this.ioio = (IOIO) object;
            return true;
        } else
            return false;
    }

    @Override
    public boolean isConnectedToSensor() {
        if (trigger != null && echo != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isDataReady() {
        return false;
    }

    @Override
    public float getData() {
        try {
            trigger.write(false);
            Thread.sleep(5);
            trigger.write(true);
            Thread.sleep(1);
            trigger.write(false);
            echoSeconds = (echo.getDuration() * 1000 * 1000);
            echoDistanceCm = echoSeconds / 29 / 2;
        } catch (ConnectionLostException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return echoDistanceCm;
    }

    @Override
    public String[] getSensorPinsDescription() {
        String[] temp = new String[2];
        temp[0] = "Trigger Pin";
        temp[1] = "Echo Pin";
        return temp;
    }

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