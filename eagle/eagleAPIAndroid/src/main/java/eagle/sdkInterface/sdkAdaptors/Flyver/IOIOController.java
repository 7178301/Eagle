package eagle.sdkInterface.sdkAdaptors.Flyver;

import eagle.logging.Log;
import eagle.sdkInterface.sensorAdaptors.AdaptorBearing;
import eagle.sdkInterface.sensorAdaptors.bearingAdaptors.AndroidBearing;
import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;

/**
 * Created by cameron on 10/6/15.
 */
public class IOIOController {

        /*
  Arduino Quadcopter
  Author - Ben Ripley - Aug 8, 2014
*/

    private IOIO ioio;

    private PwmOutput motorFC;
    private PwmOutput motorFCC;
    private PwmOutput motorRC;
    private PwmOutput motorRCC;

    //-------PID Config----------
    static double ROLL_PID_KP = 2.4;
    static double ROLL_PID_KI = 0.1;
    static double ROLL_PID_KD = 0.4;
    static double ROLL_PID_MIN = -100;
    static double ROLL_PID_MAX = 100.0;

    static double PITCH_PID_KP = 2.4;
    static double PITCH_PID_KI = 0.1;
    static double PITCH_PID_KD = 0.4;
    static double PITCH_PID_MIN = -100.0;
    static double PITCH_PID_MAX = 100.0;

    static double YAW_PID_KP = 2.2;
    static double YAW_PID_KI = 0;
    static double YAW_PID_KD = 0.2;
    static double YAW_PID_OUT_MIN = -100;
    static double YAW_PID_OUT_MAX = 100;
    static double YAW_PID_IN_MIN = -180;
    static double YAW_PID_IN_MAX = 180;

    //-------Motor PWM Levels
    static int MOTOR_ZERO_LEVEL = 1000;
    static int MOTOR_MAX_LEVEL = 2023;

    //declare pid controllers
    private PIDController roll_controller;
    private PIDController pitch_controller;
    private PIDController yaw_controller;

    // RX Signals
    double throttle = MOTOR_ZERO_LEVEL;

    // PID variables
    double pid_roll_setpoint = 0;
    double pid_pitch_setpoint = 0;
    double pid_yaw_setpoint = 0;

    private AndroidBearing bearing;

    private Thread thread;

    private static final String TAG = "IOIOController";

    IOIOController() {
        Log.log(TAG, "Setting up IOIO IOIOController");
        roll_controller = new PIDController(ROLL_PID_KP, ROLL_PID_KI, ROLL_PID_KD);
        pitch_controller = new PIDController(PITCH_PID_KP, PITCH_PID_KI, PITCH_PID_KD);
        yaw_controller = new PIDController(YAW_PID_KP, YAW_PID_KI, YAW_PID_KD);

        setpoint_update(0, 0, 0);

        roll_controller.setOutputRange(ROLL_PID_MIN, ROLL_PID_MAX);
        pitch_controller.setOutputRange(PITCH_PID_MIN, PITCH_PID_MAX);
        yaw_controller.setOutputRange(YAW_PID_IN_MIN, YAW_PID_IN_MAX);

        roll_controller.setInputRange(ROLL_PID_MIN, ROLL_PID_MAX);
        pitch_controller.setInputRange(PITCH_PID_MIN, PITCH_PID_MAX);
        yaw_controller.setInputRange(YAW_PID_OUT_MIN, YAW_PID_OUT_MAX);

        yaw_controller.setContinuous();

        roll_controller.enable();
        pitch_controller.enable();
        yaw_controller.enable();


    }

    void setIOIO(IOIO ioio) {
        if (this.ioio == null || this.ioio.equals(ioio)) {
            this.ioio = ioio;
            try {
                motorFC = ioio.openPwmOutput(34, 200);
                motorFCC = ioio.openPwmOutput(35, 200);
                motorRC = ioio.openPwmOutput(36, 200);
                motorRCC = ioio.openPwmOutput(37, 200);
                setPulseWidth(MOTOR_ZERO_LEVEL, MOTOR_ZERO_LEVEL, MOTOR_ZERO_LEVEL, MOTOR_ZERO_LEVEL);
                Thread.sleep(1000);

                thread = new Thread(new ControllerThread());
                thread.start();


                Log.log(TAG, "IOIO is initialised");
            } catch (ConnectionLostException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException f) {
                Log.log(TAG, "IOIO was already inited");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setBearing(AndroidBearing bearing) {
        this.bearing = bearing;
    }

    /**
     * map value from one range to another, from arduino codebase
     *
     * @param value    input value
     * @param fromLow  initial range low
     * @param fromHigh initial range high
     * @param toLow    output range low
     * @param toHigh   output range high
     * @return remapped value
     */
    double map(double value, int fromLow, int fromHigh, double toLow, double toHigh) {
        return (value - fromLow) * ((toHigh - toLow) / (fromHigh - fromLow)) + toLow;
    }

    void control_update() {
        setpoint_update(0, 0, 0);
        if (bearing != null) {
            float[] data = bearing.getData();
            roll_controller.getInput(data[AdaptorBearing.ROLL]);
            pitch_controller.getInput(data[AdaptorBearing.PITCH]);
            yaw_controller.getInput(data[AdaptorBearing.YAW]);
            //Log.log(TAG, data[AdaptorBearing.ROLL] + "," + data[AdaptorBearing.PITCH] + "," + data[AdaptorBearing.YAW]);

        }

        double pid_roll_out = roll_controller.performPID();
        double pid_pitch_out = pitch_controller.performPID();
        double pid_yaw_out = yaw_controller.performPID();


        // Motors
        Double fcc, fc, rcc, rc; // Front, Right, Back, Left

        throttle = 1500; //should be replaced with actual throttle;

        // yaw control disabled for stabilization testing...
        fc = throttle - pid_pitch_out;//+ pid_yaw_out;
        fcc = throttle - pid_roll_out;//- pid_yaw_out;
        rc = throttle + pid_pitch_out;//+ pid_yaw_out;
        rcc = throttle + pid_roll_out;//- pid_yaw_out;

        //Log.log(TAG, pid_pitch_out + "," + pid_roll_out);

        fc = correctRange(fc);
        fcc = correctRange(fcc);
        rc = correctRange(rc);
        rcc = correctRange(rcc);

        try {
            setPulseWidth(fc.intValue(), fcc.intValue(), rc.intValue(), rcc.intValue());
        } catch (ConnectionLostException e) {
            e.printStackTrace();
        }
    }

    double correctRange(double val) {
        if (val > MOTOR_MAX_LEVEL) {
            return MOTOR_MAX_LEVEL;
        }
        if (val < MOTOR_ZERO_LEVEL) {
            return MOTOR_ZERO_LEVEL;
        }
        return val;
    }

    void setpoint_update(double roll, double pitch, double yaw) {
        roll_controller.setSetpoint(roll);
        pitch_controller.setSetpoint(pitch);
        yaw_controller.setSetpoint(yaw);
    }

    public void setPulseWidth(int fcRange, int fccRange, int rcRange, int rccRange) throws ConnectionLostException {
        //Log.log(TAG, fcRange+","+fccRange+","+rcRange+","+rccRange);
        try {
            if (fcRange >= 1000 && fcRange <= 2023 &&
                    fccRange >= 1000 && fccRange <= 2023 &&
                    rcRange >= 1000 && rcRange <= 2023 &&
                    rccRange >= 1000 && rccRange <= 2023) {
                motorFC.setPulseWidth(fcRange);
                motorFCC.setPulseWidth(fccRange);
                motorRC.setPulseWidth(rcRange);
                motorRCC.setPulseWidth(rccRange);

            } else
                throw new IllegalArgumentException();
        }
        catch (NullPointerException e) {

        }
    }

    private class ControllerThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                control_update();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
