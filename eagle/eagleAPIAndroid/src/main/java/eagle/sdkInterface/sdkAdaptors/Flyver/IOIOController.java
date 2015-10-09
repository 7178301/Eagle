package eagle.sdkInterface.sdkAdaptors.Flyver;

import eagle.logging.Log;
import eagle.sdkInterface.sensorAdaptors.AdaptorBearing;
import eagle.sdkInterface.sensorAdaptors.bearingAdaptors.AndroidBearing;
import ioio.lib.api.AnalogInput;
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

    private AnalogInput batteryInput;

    //-------PID Config----------

    public static final float PID_DERIV_SMOOTHING = 0.5f;
    static double ROLL_PID_KP = 2.4;
    static double ROLL_PID_KI = 0.0;
    static double ROLL_PID_KD = 0.4;

    static double PITCH_PID_KP = 2.4;
    static double PITCH_PID_KI = 0.0;
    static double PITCH_PID_KD = 0.4;

    static double YAW_PID_KP = 2.2;
    static double YAW_PID_KI = 0;
    static double YAW_PID_KD = 0.2;

    //-------Motor PWM Levels
    static int MOTOR_ZERO_LEVEL = 1000;
    static int MOTOR_MAX_LEVEL = 2023;

    //declare pid controllers
    private PIDAngleController rollController;
    private PIDAngleController pitchController;
    private PIDAngleController yawController;

    // PID variables
    double yawAngleTarget = 0;
    double pitchAngleTarget = 0;
    double rollAngleTarget = 0;

    private AndroidBearing bearing;

    private static final String TAG = "IOIOController";
    private static long previousTime;
    private Thread thread;
//    private Thread batteryThread;

    IOIOController() {
        Log.log(TAG, "Setting up IOIO IOIOController");
        rollController = new PIDAngleController(ROLL_PID_KP, ROLL_PID_KI, ROLL_PID_KD, PID_DERIV_SMOOTHING);
        pitchController = new PIDAngleController(PITCH_PID_KP, PITCH_PID_KI, PITCH_PID_KD, PID_DERIV_SMOOTHING);
        yawController = new PIDAngleController(YAW_PID_KP, YAW_PID_KI, YAW_PID_KD, PID_DERIV_SMOOTHING);
    }

    void setIOIO(IOIO ioio) {
        if (this.ioio == null || this.ioio.equals(ioio)) {
            this.ioio = ioio;
            try {
                motorFC = ioio.openPwmOutput(34, 200);
                motorFCC = ioio.openPwmOutput(35, 200);
                motorRC = ioio.openPwmOutput(36, 200);
                motorRCC = ioio.openPwmOutput(37, 200);

//                batteryInput = ioio.openAnalogInput(46);
                motorFC.setPulseWidth(MOTOR_ZERO_LEVEL);
                motorFCC.setPulseWidth(MOTOR_ZERO_LEVEL);
                motorRC.setPulseWidth(MOTOR_ZERO_LEVEL);
                motorRCC.setPulseWidth(MOTOR_ZERO_LEVEL);
//
                Thread.sleep(1000);

                if (thread != null) {
                    thread.interrupt();
                }
                thread = new Thread(new ControllerThread());
                thread.start();
//                batteryThread = new Thread(new BatteryThread());
//                batteryThread.start();


                Log.log(TAG, "IOIO is initialised");
            } catch (ConnectionLostException | InterruptedException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException f) {
                Log.log(TAG, "IOIO was already inited");
            }
        }
    }

    public void setBearing(AndroidBearing bearing) {
        this.bearing = bearing;
    }

    void control_update() {
        if (bearing != null) {
            float[] data = bearing.getData();

            // Compute the power of each motor.
            int tempPowerFCW = MOTOR_ZERO_LEVEL;
            int tempPowerFCCW = MOTOR_ZERO_LEVEL;
            int tempPowerRCW = MOTOR_ZERO_LEVEL;
            int tempPowerRCCW = MOTOR_ZERO_LEVEL;

            long currentTime = System.nanoTime();
            float dt = ((float) (currentTime - previousTime)) / 1000000000.0f; // [s].
            previousTime = currentTime;

            double yawForce = yawController.getInput(yawAngleTarget, data[AdaptorBearing.YAW], dt);
            double pitchForce = pitchController.getInput(pitchAngleTarget, -data[AdaptorBearing.PITCH], dt);
            double rollForce = rollController.getInput(rollAngleTarget, data[AdaptorBearing.ROLL], dt);

            //todo:
            int altitudeForce = 200;


            tempPowerFCW += altitudeForce; // Vertical "force".
            tempPowerFCCW += altitudeForce; //
            tempPowerRCW += altitudeForce; //
            tempPowerRCCW += altitudeForce; //

            tempPowerFCW += pitchForce; // Pitch "force".
            tempPowerFCCW += pitchForce; //
            tempPowerRCW -= pitchForce; //
            tempPowerRCCW -= pitchForce; //

            tempPowerFCW += rollForce; // Roll "force".
            tempPowerFCCW -= rollForce; //
            tempPowerRCW -= rollForce; //
            tempPowerRCCW += rollForce; //

            Log.log(TAG, data[AdaptorBearing.ROLL]+","+rollAngleTarget+","+rollForce);
            //Log.log(TAG, tempPowerFCW+","+tempPowerFCCW+","+tempPowerRCW+","+tempPowerRCCW);
//
            tempPowerFCW += yawForce; // Yaw "force".
            tempPowerFCCW -= yawForce; //
            tempPowerRCW += yawForce; //
            tempPowerRCCW -= yawForce; //

            //Log.log(TAG, pid_pitch_out + "," + pid_roll_out);

            tempPowerFCW = correctRange(tempPowerFCW);
            tempPowerFCCW = correctRange(tempPowerFCCW);
            tempPowerRCW = correctRange(tempPowerRCW);
            tempPowerRCCW = correctRange(tempPowerRCCW);

            try {
                setPulseWidth(tempPowerFCW, tempPowerFCCW, tempPowerRCW, tempPowerRCCW);
            } catch (ConnectionLostException e) {
                thread.interrupt();
            }
        }
    }

    int correctRange(int val) {
        if (val > MOTOR_MAX_LEVEL) {
            return MOTOR_MAX_LEVEL;
        }
        if (val < MOTOR_ZERO_LEVEL) {
            return MOTOR_ZERO_LEVEL;
        }
        return val;
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
        } catch (NullPointerException ignored) {

        }
    }

    private class ControllerThread implements Runnable {
        @Override
        public void run() {
            try {
                //noinspection InfiniteLoopStatement
                while (true) {
                    Thread.sleep(1);
                    control_update();

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    private class BatteryThread implements Runnable {
//        @Override
//        public void run() {
//            try {
//                //noinspection InfiniteLoopStatement
//                while (true) {
//                    Log.log(TAG, "Battery Voltage is: " +batteryInput.getVoltage());
//                    Thread.sleep(10000);
//
//                }
//            } catch (InterruptedException | ConnectionLostException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
