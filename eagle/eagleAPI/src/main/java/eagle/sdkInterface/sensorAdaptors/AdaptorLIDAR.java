package eagle.sdkInterface.sensorAdaptors;

/** AdaptorLIDAR Adaptor Interface
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public abstract class AdaptorLIDAR extends SensorAdaptor {
    private float calibrationOffset = 0;

    public AdaptorLIDAR(String adaptorManufacturer, String adaptorModel, String adaptorVersion){
        super(adaptorManufacturer,adaptorModel,adaptorVersion);
    }

    public abstract boolean connectToSensor();
    public abstract float getData();
    public float getCalibratedData(){
        return getData()-calibrationOffset;
    }
    public boolean setAndroidContext(Object object){return false;}
    public float getCalibrationOffset() {
        return calibrationOffset;
    }
    public void setCalibrationOffset(float calibrationOffset) {
        this.calibrationOffset = calibrationOffset;
    }

}