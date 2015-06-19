package eagle.sdkInterface.sensorAdaptors;

/** LIDAR Adaptor Interface
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public abstract class LIDAR extends SensorAdaptor {

    public LIDAR(String adaptorManufacturer, String adaptorModel, String adaptorVersion){
        super(adaptorManufacturer,adaptorModel,adaptorVersion);
    }

}