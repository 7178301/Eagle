package eagle.sdkInterface.sensorAdaptors;

/** Accelerometer Adaptor Interface
 * @since     09/04/2015
 * <p>
 * Date Modified	26/05/2015 - Nicholas
 * @version 0.0.1
 * @author          Nicholas Alards [7178301@student.swin.edu.au] */
public abstract class Accelerometer extends SensorAdaptor{

    public class AccelerometerData{
        private double x;
        private double y;
        private double z;
        public AccelerometerData(double x, double y, double z){
            this.x=x;
            this.y=y;
            this.z=z;
        }
        public double getX() {
            return x;
        }
        public double getY() {
            return y;
        }
        public double getZ() {
            return z;
        }
        @Override
        public String toString() {
            return "X: "+x+" Y: "+y+" Z: "+z;
        }
        public boolean equals(AccelerometerData accelerometerData) {
            if(accelerometerData.getX()==x&&accelerometerData.getY()==y&&accelerometerData.getZ()==z)
                return true;
            else
                return false;
        }
    }

    public Accelerometer(String adaptorManufacturer, String adaptorModel, String adaptorVersion){
        super(adaptorManufacturer,adaptorModel,adaptorVersion);
    }

    public abstract AccelerometerData getData();

}