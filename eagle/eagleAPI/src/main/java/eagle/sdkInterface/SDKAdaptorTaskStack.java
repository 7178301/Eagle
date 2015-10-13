package eagle.sdkInterface;

import java.util.HashMap;
import java.util.Stack;

import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.PositionDisplacement;
import eagle.navigation.positioning.PositionGPS;
import eagle.navigation.positioning.PositionMetric;
import eagle.sdkInterface.sensorAdaptors.AdaptorAccelerometer;
import eagle.sdkInterface.sensorAdaptors.AdaptorBarometer;
import eagle.sdkInterface.sensorAdaptors.AdaptorBearing;
import eagle.sdkInterface.sensorAdaptors.AdaptorCamera;
import eagle.sdkInterface.sensorAdaptors.AdaptorGPS;
import eagle.sdkInterface.sensorAdaptors.AdaptorGyroscope;
import eagle.sdkInterface.sensorAdaptors.AdaptorLIDAR;
import eagle.sdkInterface.sensorAdaptors.AdaptorMagnetic;
import eagle.sdkInterface.sensorAdaptors.AdaptorRPLIDAR;
import eagle.sdkInterface.sensorAdaptors.AdaptorUltrasonic;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.AdaptorAccelerometerEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.AdaptorBarometerEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.AdaptorBearingEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.AdaptorCameraEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.AdaptorGPSEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.AdaptorGyroscopeEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.AdaptorLIDAREventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.AdaptorMagneticEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.AdaptorRPLIDAREventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.AdaptorUltrasonicEventCallback;

/**
 * SDK Adaptor Flight Stack Class
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 30/09/2015
 * <p/>
 * Date Modified	30/09/2015 - Nicholas
 */
public class SDKAdaptorTaskStack {
    private Stack<SDKAdaptorTask> sdkAdaptorTasks;
    private SDKAdaptor sdkAdaptor;
    private Thread sdkAdaptorTaskStackThread;

    public SDKAdaptorTaskStack(SDKAdaptor sdkAdaptor) {
        this.sdkAdaptor = sdkAdaptor;
        sdkAdaptorTasks = new Stack<SDKAdaptorTask>();
    }

    public void push(SDKAdaptorTask sdkAdaptorTask) {
        sdkAdaptorTasks.push(sdkAdaptorTask);
    }

    public void push(Stack<SDKAdaptorTask> sdkAdaptorTasks){
        if(sdkAdaptorTasks!=null&&!sdkAdaptorTasks.empty())
        for(SDKAdaptorTask sdkAdaptorTask :sdkAdaptorTasks)
            this.sdkAdaptorTasks.push(sdkAdaptorTask);
    }

    public void push(int delay) {
        sdkAdaptorTasks.push(new SDKAdaptorTask().Delay(delay));
    }

    public void push(Position position) {
        sdkAdaptorTasks.push(new SDKAdaptorTask().Position(position).Speed(sdkAdaptor.getMaxSpeed()));
    }

    public void push(Position position, double metersPerSecond) {
        sdkAdaptorTasks.push(new SDKAdaptorTask().Position(position).Speed(metersPerSecond));
    }

    public void run(final SDKAdaptorCallback sdkAdaptorCallback) {
        sdkAdaptorTaskStackThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final Boolean[] onResult = {null, null};
                while (!sdkAdaptorTasks.empty()) {
                    onResult[0] = null;
                    onResult[1] = null;
                    SDKAdaptorTask sdkAdaptorTask = sdkAdaptorTasks.pop();
                    if (sdkAdaptorTask.position != null) {
                        if (sdkAdaptorTask.position instanceof PositionGPS) {
                            onResult[0] = false;
                            sdkAdaptor.flyTo(new SDKAdaptorCallback() {
                                @Override
                                public void onResult(boolean booleanResult, String stringResult) {
                                    onResult[0] = true;
                                    onResult[1] = booleanResult;
                                    if (sdkAdaptorCallback != null && !booleanResult) {
                                        sdkAdaptorCallback.onResult(booleanResult, stringResult);
                                    }
                                }
                            }, (PositionGPS) sdkAdaptorTask.position);
                        } else if (sdkAdaptorTask.position instanceof PositionDisplacement) {
                            onResult[0] = false;
                            sdkAdaptor.flyTo(new SDKAdaptorCallback() {
                                @Override
                                public void onResult(boolean booleanResult, String stringResult) {
                                    onResult[0] = true;
                                    onResult[1] = booleanResult;
                                    if (sdkAdaptorCallback != null && !booleanResult) {
                                        sdkAdaptorCallback.onResult(booleanResult, stringResult);
                                    }
                                }
                            }, (PositionDisplacement) sdkAdaptorTask.position);
                        } else if (sdkAdaptorTask.position instanceof PositionMetric) {
                            onResult[0] = false;
                            sdkAdaptor.flyTo(new SDKAdaptorCallback() {
                                @Override
                                public void onResult(boolean booleanResult, String stringResult) {
                                    onResult[0] = true;
                                    onResult[1] = booleanResult;
                                    if (sdkAdaptorCallback != null && !booleanResult) {
                                        sdkAdaptorCallback.onResult(booleanResult, stringResult);
                                    }
                                }
                            }, (PositionMetric) sdkAdaptorTask.position);
                        }
                    }
                    if (sdkAdaptorTask.delay > 0) {
                        try {
                            Thread.sleep(sdkAdaptorTask.delay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (sdkAdaptorTask.sensorEventAccelerometer != null) {
                        for (AdaptorAccelerometer adaptorAccelerometer : sdkAdaptorTask.sensorEventAccelerometer.keySet())
                            sdkAdaptorTask.sensorEventAccelerometer.get(adaptorAccelerometer).onSensorEvent(adaptorAccelerometer.getCalibratedData());
                    }
                    if (sdkAdaptorTask.sensorEventBearing != null) {
                        for (AdaptorBearing adaptorBearing : sdkAdaptorTask.sensorEventBearing.keySet())
                            sdkAdaptorTask.sensorEventBearing.get(adaptorBearing).onSensorEvent(adaptorBearing.getCalibratedData());
                    }
                    if (sdkAdaptorTask.sensorEventCamera != null) {
                        for (AdaptorCamera adaptorCamera : sdkAdaptorTask.sensorEventCamera.keySet())
                            sdkAdaptorTask.sensorEventCamera.get(adaptorCamera).onSensorEvent(adaptorCamera.getData());
                    }
                    if (sdkAdaptorTask.sensorEventGPS != null) {
                        for (AdaptorGPS adaptorGPS : sdkAdaptorTask.sensorEventGPS.keySet())
                            sdkAdaptorTask.sensorEventGPS.get(adaptorGPS).onSensorEvent(adaptorGPS.getCalibratedData());
                    }
                    if (sdkAdaptorTask.sensorEventGyroscope != null) {
                        for (AdaptorGyroscope adaptorGyroscope : sdkAdaptorTask.sensorEventGyroscope.keySet())
                            sdkAdaptorTask.sensorEventGyroscope.get(adaptorGyroscope).onSensorEvent(adaptorGyroscope.getCalibratedData());
                    }
                    if (sdkAdaptorTask.sensorEventLIDAR != null) {
                        for (AdaptorLIDAR adaptorLIDAR : sdkAdaptorTask.sensorEventLIDAR.keySet())
                            sdkAdaptorTask.sensorEventLIDAR.get(adaptorLIDAR).onSensorEvent(adaptorLIDAR.getCalibratedData());
                    }
                    if (sdkAdaptorTask.sensorEventMagnetic != null) {
                        for (AdaptorMagnetic adaptorMagnetic : sdkAdaptorTask.sensorEventMagnetic.keySet())
                            sdkAdaptorTask.sensorEventMagnetic.get(adaptorMagnetic).onSensorEvent(adaptorMagnetic.getCalibratedData());
                    }
                    if (sdkAdaptorTask.sensorEventRPLIDAR != null) {
                        for (AdaptorRPLIDAR adaptorRPLIDAR : sdkAdaptorTask.sensorEventRPLIDAR.keySet())
                            sdkAdaptorTask.sensorEventRPLIDAR.get(adaptorRPLIDAR).onSensorEvent(adaptorRPLIDAR.getCalibratedData());
                    }
                    if (sdkAdaptorTask.sensorEventUltrasonic != null) {
                        for (AdaptorUltrasonic adaptorUltrasonic : sdkAdaptorTask.sensorEventUltrasonic.keySet()) {
                            sdkAdaptorTask.sensorEventUltrasonic.get(adaptorUltrasonic).onSensorEvent(adaptorUltrasonic.getCalibratedData());
                        }
                    }

                    while (onResult[0] != null) {
                        if (!onResult[0]) {
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            onResult[0] = null;
                            if (!onResult[1]) ;
                            interupt();
                        }
                    }
                }
                if (sdkAdaptorCallback != null)
                    sdkAdaptorCallback.onResult(true, "Stack Execution Successful");
            }
        });
        sdkAdaptorTaskStackThread.start();

    }

    public void interupt() {
        if (sdkAdaptorTaskStackThread != null && !sdkAdaptorTaskStackThread.isInterrupted())
            sdkAdaptorTaskStackThread.interrupt();
    }

    public void removeAll(){
        sdkAdaptorTasks.removeAllElements();
    }

    public boolean isAlive() {
        return sdkAdaptorTaskStackThread.isAlive();
    }

    public Stack<SDKAdaptorTask> getSdkAdaptorTasks(){
        Stack<SDKAdaptorTask> temp = new Stack<SDKAdaptorTask>();
        temp.addAll(this.sdkAdaptorTasks);
        return temp;
    }

    public class SDKAdaptorTask {
        Position position = null;
        double speed = 0;
        int delay = 0;
        HashMap<AdaptorAccelerometer, AdaptorAccelerometerEventCallback> sensorEventAccelerometer = null;
        HashMap<AdaptorBearing, AdaptorBearingEventCallback> sensorEventBearing = null;
        HashMap<AdaptorBarometer, AdaptorBarometerEventCallback> sensorEventBarometer = null;
        HashMap<AdaptorCamera, AdaptorCameraEventCallback> sensorEventCamera = null;
        HashMap<AdaptorGPS, AdaptorGPSEventCallback> sensorEventGPS = null;
        HashMap<AdaptorGyroscope, AdaptorGyroscopeEventCallback> sensorEventGyroscope = null;
        HashMap<AdaptorLIDAR, AdaptorLIDAREventCallback> sensorEventLIDAR = null;
        HashMap<AdaptorMagnetic, AdaptorMagneticEventCallback> sensorEventMagnetic = null;
        HashMap<AdaptorRPLIDAR, AdaptorRPLIDAREventCallback> sensorEventRPLIDAR = null;
        HashMap<AdaptorUltrasonic, AdaptorUltrasonicEventCallback> sensorEventUltrasonic = null;

        public SDKAdaptorTask() {
            ;
        }

        public SDKAdaptorTask Position(Position position) {
            this.position = position;
            return this;
        }

        public SDKAdaptorTask Delay(int seconds) {
            this.delay = seconds;
            return this;
        }

        public SDKAdaptorTask Speed(double metersPerSecond) {
            this.speed = metersPerSecond;
            return this;
        }

        public SDKAdaptorTask SensorEvent(final AdaptorAccelerometerEventCallback adaptorAccelerometerCallback, AdaptorAccelerometer adaptorAccelerometer) {
            if (adaptorAccelerometerCallback == null)
                throw new IllegalArgumentException("AdaptorAccelerometerEventCallback Must Not Be Null");
            else {
                if (sensorEventAccelerometer == null)
                    sensorEventAccelerometer = new HashMap<>();
                sensorEventAccelerometer.put(adaptorAccelerometer, adaptorAccelerometerCallback);
                return this;
            }
        }

        public SDKAdaptorTask SensorEvent(final AdaptorBarometerEventCallback adaptorBarometerEventCallback, AdaptorBarometer adaptorBarometer) {
            if (adaptorBarometerEventCallback == null)
                throw new IllegalArgumentException("AdaptorBarometerEventCallback Must Not Be Null");
            else {
                if (sensorEventBearing == null)
                    sensorEventBearing = new HashMap<>();
                sensorEventBarometer.put(adaptorBarometer, adaptorBarometerEventCallback);
                return this;
            }
        }

        public SDKAdaptorTask SensorEvent(final AdaptorBearingEventCallback adaptorBearingEventCallback, AdaptorBearing adaptorBearing) {
            if (adaptorBearingEventCallback == null)
                throw new IllegalArgumentException("AdaptorBearingEventCallback Must Not Be Null");
            else {
                if (sensorEventBearing == null)
                    sensorEventBearing = new HashMap<>();
                sensorEventBearing.put(adaptorBearing, adaptorBearingEventCallback);
                return this;
            }
        }

        public SDKAdaptorTask SensorEvent(final AdaptorCameraEventCallback adaptorCameraEventCallback, AdaptorCamera adaptorCamera) {
            if (adaptorCameraEventCallback == null)
                throw new IllegalArgumentException("AdaptorCameraEventCallback Must Not Be Null");
            else {
                if (sensorEventCamera == null)
                    sensorEventCamera = new HashMap<>();
                sensorEventCamera.put(adaptorCamera, adaptorCameraEventCallback);
                return this;
            }
        }

        public SDKAdaptorTask SensorEvent(final AdaptorGPSEventCallback adaptorGPSEventCallback, AdaptorGPS adaptorGPS) {
            if (adaptorGPSEventCallback == null)
                throw new IllegalArgumentException("AdaptorGPSEventCallback Must Not Be Null");
            else {
                if (sensorEventGPS == null)
                    sensorEventGPS = new HashMap<>();
                sensorEventGPS.put(adaptorGPS, adaptorGPSEventCallback);
                return this;
            }
        }

        public SDKAdaptorTask SensorEvent(final AdaptorGyroscopeEventCallback adaptorGyroscopeEventCallback, AdaptorGyroscope adaptorGyroscope) {
            if (adaptorGyroscopeEventCallback == null)
                throw new IllegalArgumentException("AdaptorGyroscopeEventCallback Must Not Be Null");
            else {
                if (sensorEventGyroscope == null)
                    sensorEventGyroscope = new HashMap<>();
                sensorEventGyroscope.put(adaptorGyroscope, adaptorGyroscopeEventCallback);
                return this;
            }
        }

        public SDKAdaptorTask SensorEvent(final AdaptorLIDAREventCallback adaptorLIDAREventCallback, AdaptorLIDAR adaptorLIDAR) {
            if (adaptorLIDAREventCallback == null)
                throw new IllegalArgumentException("AdaptorLIDAREventCallback Must Not Be Null");
            else {
                if (sensorEventLIDAR == null)
                    sensorEventLIDAR = new HashMap<>();
                sensorEventLIDAR.put(adaptorLIDAR, adaptorLIDAREventCallback);
                return this;
            }
        }

        public SDKAdaptorTask SensorEvent(final AdaptorMagneticEventCallback adaptorMagneticEventCallback, AdaptorMagnetic adaptorMagnetic) {
            if (adaptorMagneticEventCallback == null)
                throw new IllegalArgumentException("AdaptorMagneticEventCallback Must Not Be Null");
            else {
                if (sensorEventMagnetic == null)
                    sensorEventMagnetic = new HashMap<>();
                sensorEventMagnetic.put(adaptorMagnetic, adaptorMagneticEventCallback);
                return this;
            }
        }

        public SDKAdaptorTask SensorEvent(final AdaptorRPLIDAREventCallback adaptorRPLIDAREventCallback, AdaptorRPLIDAR adaptorRPLIDAR) {
            if (adaptorRPLIDAREventCallback == null)
                throw new IllegalArgumentException("AdaptorRPLIDAREventCallback Must Not Be Null");
            else {
                if (sensorEventRPLIDAR == null)
                    sensorEventRPLIDAR = new HashMap<>();
                sensorEventRPLIDAR.put(adaptorRPLIDAR, adaptorRPLIDAREventCallback);
                return this;
            }
        }

        public SDKAdaptorTask SensorEvent(final AdaptorUltrasonicEventCallback adaptorUltrasonicTaskCallback, AdaptorUltrasonic adaptorUltrasonic) {
            if (adaptorUltrasonicTaskCallback == null)
                throw new IllegalArgumentException("AdaptorUltrasonicEventCallback Must Not Be Null");
            else {
                if (sensorEventUltrasonic == null)
                    sensorEventUltrasonic = new HashMap<>();
                sensorEventUltrasonic.put(adaptorUltrasonic, adaptorUltrasonicTaskCallback);
                return this;
            }
        }

    }
}

