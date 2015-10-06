package eagle.sdkInterface.sdkAdaptors.Flyver;

import eagle.Log;
import eagle.sdkInterface.sensorAdaptors.accelerometerAdaptors.AndroidAccelerometer;
import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;

/**
 * Created by cameron on 10/6/15.
 */
public class Controller {

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
    static double ROLL_PID_KP = 0.250;
    static double ROLL_PID_KI = 0.950;
    static double ROLL_PID_KD = 0.011;
    static double ROLL_PID_MIN = -200.0;
    static double ROLL_PID_MAX = 200.0;

    static double PITCH_PID_KP = 0.250;
    static double PITCH_PID_KI = 0.950;
    static double PITCH_PID_KD = 0.011;
    static double PITCH_PID_MIN = -200.0;
    static double PITCH_PID_MAX = 200.0;

    static double YAW_PID_KP = 0.680;
    static double YAW_PID_KI = 0.500;
    static double YAW_PID_KD = 0.0001;
    static double YAW_PID_MIN = 100.0;
    static double YAW_PID_MAX = 100.0;

//-------------------------


    //-------RX Config----------
    static int THROTTLE_RMIN = 1000;
    static int THROTTLE_SAFE_SHUTOFF = 1120;
    static int THROTTLE_RMAX = 1900;
    static int THROTTLE_RMID = 1470;

    static int ROLL_RMIN = THROTTLE_RMIN;
    static int ROLL_RMAX = THROTTLE_RMAX;
    static int ROLL_WMIN = -30;
    static int ROLL_WMAX = 30;

    static int PITCH_RMIN = THROTTLE_RMIN;
    static int PITCH_RMAX = THROTTLE_RMAX;
    static int PITCH_WMIN = -30;
    static int PITCH_WMAX = 30;

    static int YAW_RMIN = THROTTLE_RMIN;
    static int YAW_RMAX = THROTTLE_RMAX;
    static int YAW_WMIN = -30;
    static int YAW_WMAX = 30;

    //-------Motor PWM Levels
    static double MOTOR_ZERO_LEVEL = 1000;
    static double MOTOR_MAX_LEVEL = 2023;

    private PIDController roll_controller;
    private PIDController pitch_controller;
    private PIDController yaw_controller;

    // Angles
    double angleX, angleY, angleZ = 0.0;

    // RX Signals
    double throttle = THROTTLE_RMIN;
    volatile int rx_values[] = new int[4]; // ROLL, PITCH, THROTTLE, YAW

    // PID variables
    double pid_roll_out, pid_roll_setpoint = 0;
    double pid_pitch_out, pid_pitch_setpoint = 0;
    double pid_yaw_out, pid_yaw_setpoint = 0;

    // Track loop time.
    long prev_time = 0;
    private AndroidAccelerometer accelerometer;

    //declare pid controllers

    Controller() {
        Log.log("IOIOController", "Setting up IOIO Controller");
        roll_controller = new PIDController(ROLL_PID_KP, ROLL_PID_KI, ROLL_PID_KD);
        roll_controller.setSetpoint(pid_roll_setpoint);
        pitch_controller = new PIDController(PITCH_PID_KP, PITCH_PID_KI, PITCH_PID_KD);
        pitch_controller.setSetpoint(pid_pitch_setpoint);
        yaw_controller = new PIDController(YAW_PID_KP, YAW_PID_KI, YAW_PID_KD);
        yaw_controller.setSetpoint(pid_yaw_setpoint);
    }

    void setIOIO(IOIO ioio) {
        if (this.ioio == null || this.ioio.equals(ioio)) {
            this.ioio = ioio;
            try {
                motorFC = ioio.openPwmOutput(34, 200);
                motorFCC = ioio.openPwmOutput(35, 200);
                motorRC = ioio.openPwmOutput(36, 200);
                motorRCC = ioio.openPwmOutput(37, 200);
                Thread thread = new Thread(new ControllerThread());
                thread.start();
                Log.log("IOIOController", "IOIO is initialised");
            } catch (ConnectionLostException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException f) {
                Log.log("IOIOController", "IOIO was already inited");
            }
        }
    }

    void pid_initialize() {
        roll_controller.setOutputRange(ROLL_PID_MIN, ROLL_PID_MAX);
        pitch_controller.setOutputRange(PITCH_PID_MIN, PITCH_PID_MAX);
        yaw_controller.setOutputRange(YAW_PID_MIN, YAW_PID_MAX);
//    roll_controller.setMode(AUTOMATIC);
//    pitch_controller.SetMode(AUTOMATIC);
//    yaw_controller.SetMode(AUTOMATIC);
//    roll_controller.SetSampleTime(10);
//    pitch_controller.SetSampleTime(10);
//    yaw_controller.SetSampleTime(10);
    }

    public void setAccelerometer(AndroidAccelerometer accelerometer) {
        this.accelerometer = accelerometer;
    }

    void pid_update() {
        //TODO
        roll_controller.setSetpoint(0); //desired roll
        pitch_controller.setSetpoint(0); //desired pitch
        yaw_controller.setSetpoint(0); //desired yaw
        if (accelerometer != null) {
            float[] data = accelerometer.getData();
            roll_controller.getInput(data[0]);
            pitch_controller.getInput(data[1]);
            yaw_controller.getInput(data[2]);
        }
    }

    void pid_compute() {
        pid_roll_out = roll_controller.performPID();
        pid_pitch_out = pitch_controller.performPID();
        pid_yaw_out = yaw_controller.performPID();
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
    double map(int value, int fromLow, int fromHigh, double toLow, double toHigh) {
        return (value - fromLow) * ((toHigh - toLow) / (fromHigh - fromLow)) + toLow;
    }

    void control_update() {


        // Motors
        Double m0, m1, m2, m3; // Front, Right, Back, Left

        throttle = map(rx_values[2], THROTTLE_RMIN, THROTTLE_RMAX, MOTOR_ZERO_LEVEL, MOTOR_MAX_LEVEL);

        setpoint_update();
        pid_update();
        pid_compute();

        // yaw control disabled for stabilization testing...
        m0 = throttle + pid_pitch_out;//+ pid_yaw_out;
        m1 = throttle + pid_roll_out;//- pid_yaw_out;
        m2 = throttle - pid_pitch_out;//+ pid_yaw_out;
        m3 = throttle - pid_roll_out;//- pid_yaw_out;

        if (throttle < THROTTLE_SAFE_SHUTOFF) {
            m0 = MOTOR_ZERO_LEVEL;
            m1 = MOTOR_ZERO_LEVEL;
            m2 = MOTOR_ZERO_LEVEL;
            m3 = MOTOR_ZERO_LEVEL;
        }

        try {
            setPulseWidth(m0.intValue(), m1.intValue(), m2.intValue(), m3.intValue());
        } catch (ConnectionLostException e) {
            e.printStackTrace();
        }
    }

    void setpoint_update() {
        // here we allow +- 20 for noise and sensitivity on the RX controls...
        // ROLL rx at mid level?
        if (rx_values[0] > THROTTLE_RMID - 20 && rx_values[0] < THROTTLE_RMID + 20)
            pid_roll_setpoint = 0;
        else
            pid_roll_setpoint = map(rx_values[0], ROLL_RMIN, ROLL_RMAX, ROLL_WMIN, ROLL_WMAX);
        //PITCH rx at mid level?
        if (rx_values[1] > THROTTLE_RMID - 20 && rx_values[1] < THROTTLE_RMID + 20)
            pid_pitch_setpoint = 0;
        else
            pid_pitch_setpoint = map(rx_values[1], PITCH_RMIN, PITCH_RMAX, PITCH_WMIN, PITCH_WMAX);
        //YAW rx at mid level?
        if (rx_values[3] > THROTTLE_RMID - 20 && rx_values[3] < THROTTLE_RMID + 20)
            pid_yaw_setpoint = 0;
        else
            pid_yaw_setpoint = map(rx_values[3], YAW_RMIN, YAW_RMAX, YAW_WMIN, YAW_WMAX);
    }

    public void setPulseWidth(int fcRange, int fccRange, int rcRange, int rccRange) throws ConnectionLostException {
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
                pid_update();
                setpoint_update();
                pid_compute();
                control_update();
            }
        }
    }
}
