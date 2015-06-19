package eagle.sdkInterface.sensorAdaptors;
/** Abstract SensorAdaptor Class
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public abstract class SensorAdaptor {

    private String adaptorName = null;
    private String adaptorVersion = null;
    private String adaptorManufacturer = null;
    private String adaptorModel = null;

    public SensorAdaptor(String adaptorManufacturer, String adaptorModel, String adaptorVersion){
        this.adaptorName=adaptorManufacturer+" "+adaptorModel;
        this.adaptorManufacturer=adaptorManufacturer;
        this.adaptorModel=adaptorModel;
        this.adaptorVersion=adaptorVersion;
    }
    public String getAdaptorVersion(){
        return adaptorVersion;
    }
    public String getAdaptorName(){
        return adaptorName;
    }
    public String getAdaptorManufacturer(){
        return adaptorManufacturer;
    }
    public String getAdaptorModel(){
        return adaptorModel;
    }
}