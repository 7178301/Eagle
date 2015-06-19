package eagle.sdkInterface.sensorAdaptors;

/** Camera Adaptor Interface
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au]
 * @author          Cameron Cross */
public abstract class Camera extends SensorAdaptor{

    public Camera(String adaptorManufacturer, String adaptorModel, String adaptorVersion){
        super(adaptorManufacturer,adaptorModel,adaptorVersion);
    }
    //how to say if a drone is capable of taking photos or not?
    //public abstract String takePhoto();
        // 1. Check camera.
        // 2. if okay, try taking a photo.
        // 3. Store the photo.
        // 4. send back location of storage

}