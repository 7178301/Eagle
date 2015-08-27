package eagle.sdkInterface.sensorAdaptors;

/** Camera Adaptor Interface
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 * @author          Cameron Cross */
public abstract class AdaptorCamera extends SensorAdaptor{

    public AdaptorCamera(String adaptorManufacturer, String adaptorModel, String adaptorVersion){
        super(adaptorManufacturer,adaptorModel,adaptorVersion);
    }

    public abstract boolean connectToSensor();

    //TODO Following Method Need Proper Implementation
    //public abstract Object getData();
        // Store the photo and send back location of storage
        // OR send back image data

}