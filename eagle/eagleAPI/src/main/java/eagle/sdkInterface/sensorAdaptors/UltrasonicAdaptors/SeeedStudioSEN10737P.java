package eagle.sdkInterface.sensorAdaptors.ultrasonicAdaptors;

import eagle.sdkInterface.sensorAdaptors.AdaptorUltrasonic;

/**
 * Seeed Studio RB-See-90 Ultrasonic Adaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 14/06/2015
 * <p/>
 * Date Modified	14/06/2015 - Nicholas
 */
public class SeeedStudioSEN10737P extends AdaptorUltrasonic {
    public SeeedStudioSEN10737P() {
        super("Seeed Studio", "SEN10737P", "0.0.1");
    }

    //TODO Following Method Need Propper Implementation
    public boolean connectToSensor() {
        return false;
    }

    //TODO Following Method Need Propper Implementation
    public boolean isConnectedToSensor() {
        return false;
    }

    //TODO Following Method Need Propper Implementation
    @Override
    public float getData() {
        return 0;
    }

    //TODO Following Method Need Proper Implementation
    public boolean isDataReady() {
        return true;
    }
}
