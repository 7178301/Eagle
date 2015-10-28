package eagle.sdkInterface.sdkAdaptors.Flyver;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import eagle.Log;
import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.PositionGPS;
import eagle.sdkInterface.sensorAdaptors.AdaptorBearing;
import eagle.sdkInterface.sensorAdaptors.bearingAdaptors.AndroidBearing;
import eagle.sdkInterface.sensorAdaptors.gpsAdaptors.AndroidGPS;
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

    private static Thread thread;

    private AnalogInput batteryInput;

    //-------PID Config----------

    public static final float PID_DERIV_SMOOTHING = 0.5f;
    static double ROLL_PID_KP = 2.0;
    static double ROLL_PID_KI = 0.0;
    static double ROLL_PID_KD = 0.0;

    static double PITCH_PID_KP = 2.0;
    static double PITCH_PID_KI = 0.0;
    static double PITCH_PID_KD = 0.0;

    static double YAW_PID_KP = 2.2;
    static double YAW_PID_KI = 0;
    static double YAW_PID_KD = 0.2;

    static double THROTTLE_PID_KP = 2.4;
    static double THROTTLE_PID_KI = 1.0;
    static double THROTTLE_PID_KD = 0.4;


    static double TILT_PID_KP = 0.024;
    static double TILT_PID_KI = 0.0;
    static double TILT_PID_KD = 0.004;

    //-------Motor PWM Levels
    static int MOTOR_ZERO_LEVEL = 1000;
    static int MOTOR_DESCENT_LEVEL = 1500;
    static int MOTOR_MAX_LEVEL = 2023;

    //declare pid controllers
    private PIDAngleController rollController;
    private PIDAngleController pitchController;
    private PIDAngleController yawController;
    private ThrottleController throttleController;
    private PIDPercentageController tiltController;

    // PID variables
    double yawAngleTarget = 0;
    double pitchAngleTarget = 0;
    double rollAngleTarget = 0;
    double altitudeTarget = 0;

    private AndroidBearing bearing;
    private AndroidGPS position;

    private static final String TAG = "IOIOController";
    private static long previousTime;

    private Position desiredPosition;
    private double maxTilt = 10;
    double throttlePercentage = -1;
    //private Sequencer seq;
//    private Thread batteryThread;


//    private Sequencer.ChannelCuePwmSpeed motorFCcue = new Sequencer.ChannelCuePwmSpeed();
//    private Sequencer.ChannelCuePwmSpeed motorFCCcue = new Sequencer.ChannelCuePwmSpeed();
//    private Sequencer.ChannelCuePwmSpeed motorRCcue = new Sequencer.ChannelCuePwmSpeed();
//    private Sequencer.ChannelCuePwmSpeed motorRCCcue = new Sequencer.ChannelCuePwmSpeed();
//
//    private Sequencer.ChannelCue[] cue = new Sequencer.ChannelCue[] {motorFCCcue, motorFCcue, motorRCCcue, motorRCcue};

    IOIOController() {
        Log.log(TAG, "Loading config file");
        String filename = Environment.getExternalStorageDirectory().getPath() + "/sparrow/f450config.txt";
        loadConfig(filename);
        Log.log(TAG, "Setting up IOIO IOIOController");
        rollController = new PIDAngleController(ROLL_PID_KP, ROLL_PID_KI, ROLL_PID_KD, PID_DERIV_SMOOTHING);
        pitchController = new PIDAngleController(PITCH_PID_KP, PITCH_PID_KI, PITCH_PID_KD, PID_DERIV_SMOOTHING);
        yawController = new PIDAngleController(YAW_PID_KP, YAW_PID_KI, YAW_PID_KD, PID_DERIV_SMOOTHING);
        throttleController = new ThrottleController(MOTOR_ZERO_LEVEL, MOTOR_DESCENT_LEVEL, MOTOR_MAX_LEVEL);
//        tiltController = new PIDPercentageController(TILT_PID_KP, TILT_PID_KI, TILT_PID_KD, PID_DERIV_SMOOTHING);
    }

    private void loadConfig(String filename) {
        //echo "MOTOR_ZERO_LEVEL,MOTOR_DESCENT_LEVEL,MOTOR_MAX_LEVEL,ROLL_PID_KP,ROLL_PID_KI,ROLL_PID_KD,PITCH_PID_KP,PITCH_PID_KI,PITCH_PID_KD\n1000,1500,2023,2.0,0,0,2.0,0,0" > sparrow/f450config.txt
        File file = new File(filename);
        if (file.exists()) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
                br.readLine(); //discard first line, its comments anyway
                String[] values = br.readLine().split(",");
                if (values.length != 9) {
                    return;
                }
                MOTOR_ZERO_LEVEL = Integer.parseInt(values[0]);
                MOTOR_DESCENT_LEVEL = Integer.parseInt(values[1]);
                MOTOR_MAX_LEVEL = Integer.parseInt(values[2]);
                ROLL_PID_KP = Double.parseDouble(values[3]);
                ROLL_PID_KI = Double.parseDouble(values[4]);
                ROLL_PID_KD = Double.parseDouble(values[5]);
                PITCH_PID_KP = Double.parseDouble(values[6]);
                PITCH_PID_KI = Double.parseDouble(values[7]);
                PITCH_PID_KD = Double.parseDouble(values[8]);
                Log.log(TAG, "Successfully loaded parameters from file: "+filename);

            } catch (IOException e) {
                Log.log(TAG, "Failed to load parameters from file: "+filename);
                e.printStackTrace();
            }
        }
    }

    void setIOIO(IOIO ioio) {
        if (this.ioio == null || this.ioio.equals(ioio)) {
            this.ioio = ioio;
            try {
//                Sequencer.ChannelConfigPwmSpeed motorFCconfig = new Sequencer.ChannelConfigPwmSpeed(Sequencer.Clock.CLK_62K5, 312, MOTOR_ZERO_LEVEL,  new DigitalOutput.Spec(34));
//                Sequencer.ChannelConfigPwmSpeed motorFCCconfig = new Sequencer.ChannelConfigPwmSpeed(Sequencer.Clock.CLK_62K5, 312, MOTOR_ZERO_LEVEL,  new DigitalOutput.Spec(35));
//                Sequencer.ChannelConfigPwmSpeed motorRCconfig = new Sequencer.ChannelConfigPwmSpeed(Sequencer.Clock.CLK_62K5, 312, MOTOR_ZERO_LEVEL,  new DigitalOutput.Spec(36));
//                Sequencer.ChannelConfigPwmSpeed motorRCCconfig = new Sequencer.ChannelConfigPwmSpeed(Sequencer.Clock.CLK_62K5, 312, MOTOR_ZERO_LEVEL,  new DigitalOutput.Spec(37));
//
//                Sequencer.ChannelConfig[] config = new Sequencer.ChannelConfig[] {motorFCCconfig, motorFCconfig, motorRCCconfig, motorRCconfig};
//                seq = ioio.openSequencer(config);
//
//                motorFCcue.pulseWidth = MOTOR_ZERO_LEVEL;
//                motorFCCcue.pulseWidth = MOTOR_ZERO_LEVEL;
//                motorRCcue.pulseWidth = MOTOR_ZERO_LEVEL;
//                motorRCCcue.pulseWidth = MOTOR_ZERO_LEVEL;
//
//
//                seq.manualStart(cue);


                motorFC = ioio.openPwmOutput(34, 200);
                motorFCC = ioio.openPwmOutput(35, 200);
                motorRC = ioio.openPwmOutput(36, 200);
                motorRCC = ioio.openPwmOutput(37, 200);

//                batteryInput = ioio.openAnalogInput(46);
                motorFC.setPulseWidth(MOTOR_ZERO_LEVEL);
                motorFCC.setPulseWidth(MOTOR_ZERO_LEVEL);
                motorRC.setPulseWidth(MOTOR_ZERO_LEVEL);
                motorRCC.setPulseWidth(MOTOR_ZERO_LEVEL);

                Thread.sleep(1000);

                if (thread != null) {
                    thread.interrupt();
                }

                previousTime = System.nanoTime();
                thread = new Thread(new ControllerThread());
                thread.start();
//                batteryThread = new Thread(new BatteryThread());
//                batteryThread.start();


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

    public void setBearingAdaptor(AndroidBearing bearing) {
        this.bearing = bearing;
    }

    public void setPositionAdaptor(AndroidGPS position) {
        this.position = position;
    }


    public void setDesiredPosition(Position desiredPosition) {
        this.desiredPosition = desiredPosition;
    }

    void control_update() {
        if (bearing != null && position != null) {
            float[] data = bearing.getData();
            PositionGPS posData = position.getData();


            // Compute the power of each motor.
            int tempPowerFCW = MOTOR_ZERO_LEVEL;
            int tempPowerFCCW = MOTOR_ZERO_LEVEL;
            int tempPowerRCW = MOTOR_ZERO_LEVEL;
            int tempPowerRCCW = MOTOR_ZERO_LEVEL;

            long currentTime = System.nanoTime();
            float dt = ((float) (currentTime - previousTime)) / 1000000000.0f; // [s].
            previousTime = currentTime;

//            if (desiredPosition != null) {
//                PositionDisplacement disp = ((PositionGPS)desiredPosition).compare(posData);
//                double displacement = disp.getDisplacement();
//                double angle = disp.getAngle();
//                double tiltAngle = maxTilt*tiltController.getInput(displacement, 0, dt);
////
////                pitchAngleTarget = tiltAngle*Math.cos(data[AndroidBearing.YAW]+angle);
////                yawAngleTarget = tiltAngle*Math.sin(data[AndroidBearing.YAW]+angle);
//
//            }

            double yawForce = yawController.getInput(yawAngleTarget, data[AdaptorBearing.YAW], dt);
            double pitchForce = pitchController.getInput(pitchAngleTarget, -data[AdaptorBearing.PITCH], dt);
            double rollForce = rollController.getInput(rollAngleTarget, data[AdaptorBearing.ROLL], dt);

            double altitudeForce = throttleController.getInput(throttlePercentage, dt);


//            //todo:
//            int altitudeForce = 200;

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

//            tempPowerFCW += yawForce; // Yaw "force".
//            tempPowerFCCW -= yawForce; //
//            tempPowerRCW += yawForce; //
//            tempPowerRCCW -= yawForce; //

            Log.log(TAG, currentTime + "," + pitchAngleTarget + "," + rollAngleTarget + "," + data[AdaptorBearing.PITCH] + "," + data[AdaptorBearing.ROLL] + "," + pitchForce + "," + rollForce + "," + altitudeForce + "," + tempPowerFCCW + "," + tempPowerFCW + "," + tempPowerRCCW + "," + tempPowerRCW);


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
                motorFC.setPulseWidth(MOTOR_ZERO_LEVEL);
                motorFCC.setPulseWidth(MOTOR_ZERO_LEVEL);
                motorRC.setPulseWidth(MOTOR_ZERO_LEVEL);
                motorRCC.setPulseWidth(MOTOR_ZERO_LEVEL);

            } else
                throw new IllegalArgumentException();
        } catch (NullPointerException ignored) {

        }
    }

    public boolean kill() {
        thread.interrupt();
        try {
            motorFC.setPulseWidth(MOTOR_ZERO_LEVEL);
            motorFCC.setPulseWidth(MOTOR_ZERO_LEVEL);
            motorRC.setPulseWidth(MOTOR_ZERO_LEVEL);
            motorRCC.setPulseWidth(MOTOR_ZERO_LEVEL);
            return true;
        } catch (ConnectionLostException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setThrottle(double percentage) {
        throttlePercentage = percentage;
    }

    public void setYaw(double angle) {

    }

    public void setPitch(double angle) {
        pitchAngleTarget = angle * 15;
    }

    public void setRoll(double angle) {
        rollAngleTarget = angle * 15;

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
