package eagle.sdkInterface.sensorAdaptors;


/** Android Gyroscope Adaptor
 * @since     27/08/2015
 * <p>
 * Date Modified	27/08/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public abstract class Magnetic extends SensorAdaptor {
    public Magnetic(String adaptorManufacturer, String adaptorModel, String adaptorVersion){
        super(adaptorManufacturer,adaptorModel,adaptorVersion);
    }

    //TODO Following Method Need Proper Implementation
    //public abstract Object getData();
}
