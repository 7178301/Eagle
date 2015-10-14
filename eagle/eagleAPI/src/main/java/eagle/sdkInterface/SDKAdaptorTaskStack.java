package eagle.sdkInterface;

import java.util.HashMap;
import java.util.Stack;

import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.PositionDisplacement;
import eagle.navigation.positioning.PositionGPS;
import eagle.navigation.positioning.PositionMetric;
import eagle.sdkInterface.sensorAdaptors.AdaptorAccelerometer;
import eagle.sdkInterface.sensorAdaptors.AdaptorBearing;
import eagle.sdkInterface.sensorAdaptors.AdaptorCamera;
import eagle.sdkInterface.sensorAdaptors.AdaptorGPS;
import eagle.sdkInterface.sensorAdaptors.AdaptorGyroscope;
import eagle.sdkInterface.sensorAdaptors.AdaptorLIDAR;
import eagle.sdkInterface.sensorAdaptors.AdaptorMagnetic;
import eagle.sdkInterface.sensorAdaptors.AdaptorRPLIDAR;
import eagle.sdkInterface.sensorAdaptors.AdaptorUltrasonic;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorAccelerometerEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorBearingEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorCameraEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorGPSEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorGyroscopeEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorLIDAREventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorMagneticEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorRPLIDAREventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorUltrasonicEventCallback;

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
        sdkAdaptorTasks.push(new SDKAdaptorTask().Position(position).Speed(sdkAdaptor.getMaxFlightSpeed()));
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
        HashMap<AdaptorAccelerometer, SensorAdaptorAccelerometerEventCallback> sensorEventAccelerometer = null;
        HashMap<AdaptorBearing, SensorAdaptorBearingEventCallback> sensorEventBearing = null;
        HashMap<AdaptorCamera, SensorAdaptorCameraEventCallback> sensorEventCamera = null;
        HashMap<AdaptorGPS, SensorAdaptorGPSEventCallback> sensorEventGPS = null;
        HashMap<AdaptorGyroscope, SensorAdaptorGyroscopeEventCallback> sensorEventGyroscope = null;
        HashMap<AdaptorLIDAR, SensorAdaptorLIDAREventCallback> sensorEventLIDAR = null;
        HashMap<AdaptorMagnetic, SensorAdaptorMagneticEventCallback> sensorEventMagnetic = null;
        HashMap<AdaptorRPLIDAR, SensorAdaptorRPLIDAREventCallback> sensorEventRPLIDAR = null;
        HashMap<AdaptorUltrasonic, SensorAdaptorUltrasonicEventCallback> sensorEventUltrasonic = null;

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

        public SDKAdaptorTask SensorEvent(final SensorAdaptorAccelerometerEventCallback adaptorAccelerometerCallback, AdaptorAccelerometer adaptorAccelerometer) {
            if (adaptorAccelerometerCallback == null)
                throw new IllegalArgumentException("SensorAdaptorAccelerometerEventCallback Must Not Be Null");
            else {
                if (sensorEventAccelerometer == null)
                    sensorEventAccelerometer = new HashMap<>();
                sensorEventAccelerometer.put(adaptorAccelerometer, adaptorAccelerometerCallback);
                return this;
            }
        }

        public SDKAdaptorTask SensorEvent(final SensorAdaptorBearingEventCallback sensorAdaptorBearingEventCallback, AdaptorBearing adaptorBearing) {
            if (sensorAdaptorBearingEventCallback == null)
                throw new IllegalArgumentException("SensorAdaptorBearingEventCallback Must Not Be Null");
            else {
                if (sensorEventBearing == null)
                    sensorEventBearing = new HashMap<>();
                sensorEventBearing.put(adaptorBearing, sensorAdaptorBearingEventCallback);
                return this;
            }
        }

        public SDKAdaptorTask SensorEvent(final SensorAdaptorCameraEventCallback sensorAdaptorCameraEventCallback, AdaptorCamera adaptorCamera) {
            if (sensorAdaptorCameraEventCallback == null)
                throw new IllegalArgumentException("SensorAdaptorCameraEventCallback Must Not Be Null");
            else {
                if (sensorEventCamera == null)
                    sensorEventCamera = new HashMap<>();
                sensorEventCamera.put(adaptorCamera, sensorAdaptorCameraEventCallback);
                return this;
            }
        }

        public SDKAdaptorTask SensorEvent(final SensorAdaptorGPSEventCallback sensorAdaptorGPSEventCallback, AdaptorGPS adaptorGPS) {
            if (sensorAdaptorGPSEventCallback == null)
                throw new IllegalArgumentException("SensorAdaptorGPSEventCallback Must Not Be Null");
            else {
                if (sensorEventGPS == null)
                    sensorEventGPS = new HashMap<>();
                sensorEventGPS.put(adaptorGPS, sensorAdaptorGPSEventCallback);
                return this;
            }
        }

        public SDKAdaptorTask SensorEvent(final SensorAdaptorGyroscopeEventCallback sensorAdaptorGyroscopeEventCallback, AdaptorGyroscope adaptorGyroscope) {
            if (sensorAdaptorGyroscopeEventCallback == null)
                throw new IllegalArgumentException("SensorAdaptorGyroscopeEventCallback Must Not Be Null");
            else {
                if (sensorEventGyroscope == null)
                    sensorEventGyroscope = new HashMap<>();
                sensorEventGyroscope.put(adaptorGyroscope, sensorAdaptorGyroscopeEventCallback);
                return this;
            }
        }

        public SDKAdaptorTask SensorEvent(final SensorAdaptorLIDAREventCallback sensorAdaptorLIDAREventCallback, AdaptorLIDAR adaptorLIDAR) {
            if (sensorAdaptorLIDAREventCallback == null)
                throw new IllegalArgumentException("SensorAdaptorLIDAREventCallback Must Not Be Null");
            else {
                if (sensorEventLIDAR == null)
                    sensorEventLIDAR = new HashMap<>();
                sensorEventLIDAR.put(adaptorLIDAR, sensorAdaptorLIDAREventCallback);
                return this;
            }
        }

        public SDKAdaptorTask SensorEvent(final SensorAdaptorMagneticEventCallback sensorAdaptorMagneticEventCallback, AdaptorMagnetic adaptorMagnetic) {
            if (sensorAdaptorMagneticEventCallback == null)
                throw new IllegalArgumentException("SensorAdaptorMagneticEventCallback Must Not Be Null");
            else {
                if (sensorEventMagnetic == null)
                    sensorEventMagnetic = new HashMap<>();
                sensorEventMagnetic.put(adaptorMagnetic, sensorAdaptorMagneticEventCallback);
                return this;
            }
        }

        public SDKAdaptorTask SensorEvent(final SensorAdaptorRPLIDAREventCallback sensorAdaptorRPLIDAREventCallback, AdaptorRPLIDAR adaptorRPLIDAR) {
            if (sensorAdaptorRPLIDAREventCallback == null)
                throw new IllegalArgumentException("SensorAdaptorRPLIDAREventCallback Must Not Be Null");
            else {
                if (sensorEventRPLIDAR == null)
                    sensorEventRPLIDAR = new HashMap<>();
                sensorEventRPLIDAR.put(adaptorRPLIDAR, sensorAdaptorRPLIDAREventCallback);
                return this;
            }
        }

        public SDKAdaptorTask SensorEvent(final SensorAdaptorUltrasonicEventCallback adaptorUltrasonicTaskCallback, AdaptorUltrasonic adaptorUltrasonic) {
            if (adaptorUltrasonicTaskCallback == null)
                throw new IllegalArgumentException("SensorAdaptorUltrasonicEventCallback Must Not Be Null");
            else {
                if (sensorEventUltrasonic == null)
                    sensorEventUltrasonic = new HashMap<>();
                sensorEventUltrasonic.put(adaptorUltrasonic, adaptorUltrasonicTaskCallback);
                return this;
            }
        }

    }
}

