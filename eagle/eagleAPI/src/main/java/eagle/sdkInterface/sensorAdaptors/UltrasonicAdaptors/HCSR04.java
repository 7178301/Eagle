package eagle.sdkInterface.sensorAdaptors.UltrasonicAdaptors;

import eagle.sdkInterface.sensorAdaptors.AdaptorUltrasonic;

import ioio.lib.api.IOIO;
import ioio.lib.api.PulseInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PulseInput.PulseMode;
import ioio.lib.api.exception.ConnectionLostException;

/** Modtronix MOD-USONIC1 Ultrasonic Adaptor
 * @since     14/06/2015
 * <p>
 * Date Modified	28/08/2015 - Angela
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 * @author          Angela Lau [7160852@student.swin.edu.au] */
public class HCSR04 extends AdaptorUltrasonic {

    private int triggerPin;
    private int echoPin;
    private DigitalOutput trigger;
    private PulseInput echo;
    private float echoSeconds;
    private float echoDistanceCm;
    private IOIO ioio;

    public HCSR04(int triggerPin, int echoPin){
        super("HC-SR04","HC-SR04","0.0.1");
        this.triggerPin = triggerPin;
        this.echoPin = echoPin;
    }

    public boolean connectToSensor(){
        if(ioio == null) {
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
        if(object instanceof IOIO){
            this.ioio = (IOIO)object;
            return true;
        }else
            return false;
    }

    public boolean isConnectedToSensor(){
        if(trigger != null && echo !=null ){
        return true;
        }
        else {
            return false;
        }
    }

    @Override
    public float getData(){
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
}