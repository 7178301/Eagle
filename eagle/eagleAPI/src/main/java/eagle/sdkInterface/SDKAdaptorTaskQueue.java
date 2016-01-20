package eagle.sdkInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.PositionMetricDisplacement;
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

import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorAccelerometerEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorBearingEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorCameraEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorGPSEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorGyroscopeEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorLIDAREventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorMagneticEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorRPLIDAREventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorUltrasonicEventCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorBarometerEventCallback;

/**
 * SDK Adaptor Flight Queue Class
 * A queue of tasks to be performed for a particular SDK adaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 30/09/2015
 * <p/>
 * Date Modified	06/01/2016 - Nicholas
 */
public class SDKAdaptorTaskQueue {
    private ArrayList<SDKAdaptorTask> sdkAdaptorTasks;
    private SDKAdaptor sdkAdaptor;
    private Thread sdkAdaptorTaskStackThread;

    /**
     * Initializes the defaults for the queue
     * @param sdkAdaptor The SDK Adaptor this queue is associated to.
     */
    public SDKAdaptorTaskQueue(SDKAdaptor sdkAdaptor) {
        this.sdkAdaptor = sdkAdaptor;
        sdkAdaptorTasks = new ArrayList<SDKAdaptorTask>();
    }

    /**
     * Add a task to the queue of tasks
     * @param sdkAdaptorTask Task to add to the queue
     */
    public void add(SDKAdaptorTask sdkAdaptorTask) {
        sdkAdaptorTasks.add(sdkAdaptorTask);
    }

    /**
     * Add a List of tasks to the queue of tasks
     * @param sdkAdaptorTasks
     */
    public void add(List<SDKAdaptorTask> sdkAdaptorTasks) {
        if (sdkAdaptorTasks != null && !sdkAdaptorTasks.isEmpty()) {
            for (SDKAdaptorTask sdkAdaptorTask : sdkAdaptorTasks)
                this.sdkAdaptorTasks.add(sdkAdaptorTask);
        }
    }

    /**
     * Add a delay task to the queue of tasks
     * @param delay
     */
    public void add(int delay) {
        sdkAdaptorTasks.add(new SDKAdaptorTask().Delay(delay));
    }

    /**
     * Add a Position task to the queue of tasks
     * @param position Destination position for the task
     */
    public void add(Position position) {
        sdkAdaptorTasks.add(new SDKAdaptorTask().Position(position).Speed(sdkAdaptor.getMaxFlightSpeed()));
    }

    /**
     * Add a Position tasks with speed to the queue of tasks
     * @param position        Destination position for the task
     * @param metersPerSecond Speed from previous position to the current one
     */
    public void add(Position position, double metersPerSecond) {
        sdkAdaptorTasks.add(new SDKAdaptorTask().Position(position).Speed(metersPerSecond));
    }

    /**
     * Run the queue tasks.
     * All task info will be reported back through the callback.
     * The queue will automatically be paused if a problem is internally recognized.
     * @param sdkAdaptorCallback Callback for the responses from the queue tasks
     */
    public void run(final SDKAdaptorCallback sdkAdaptorCallback) {
        if (sdkAdaptorTaskStackThread != null && !sdkAdaptorTaskStackThread.isAlive()) {
            sdkAdaptorTaskStackThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    final Boolean[] onResult = {null, null};
                    while (!sdkAdaptorTasks.isEmpty()) {
                        onResult[0] = null;
                        onResult[1] = null;
                        SDKAdaptorTask sdkAdaptorTask = sdkAdaptorTasks.get(0);
                        sdkAdaptorTasks.remove(sdkAdaptorTask);
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
                            } else if (sdkAdaptorTask.position instanceof PositionMetricDisplacement) {
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
                                }, (PositionMetricDisplacement) sdkAdaptorTask.position);
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

                        while (onResult[0] != null) {
                            if (!onResult[0]) {
                                try {
                                    Thread.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                onResult[0] = null;
                                if (!onResult[1])
                                    pause();
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

                    }
                    if (sdkAdaptorCallback != null)
                        sdkAdaptorCallback.onResult(true, "Stack Execution Successful");
                }
            });
            sdkAdaptorTaskStackThread.start();
        }
    }

    /**
     * Pause the running tasks
     */
    public void pause() {
        if (sdkAdaptorTaskStackThread != null && !sdkAdaptorTaskStackThread.isInterrupted())
            sdkAdaptorTaskStackThread.interrupt();
    }

    /**
     * Remove all queued tasks
     */
    public void removeAll() {
        sdkAdaptorTasks.clear();
    }

    /**
     * Check if the queue is empty
     * @return Status of empty queue
     */
    public boolean isEmpty() {
        return sdkAdaptorTasks.isEmpty();
    }

    /**
     * Check if the queue is paused
     * @return Status of the queue
     */
    public boolean isPaused() {
        return sdkAdaptorTaskStackThread.isAlive();
    }

    /**
     * Returns an ArrayList of all the queued tasks
     * @return ArrayList of all queued tasks
     */
    public ArrayList<SDKAdaptorTask> getSdkAdaptorTasks() {
        ArrayList<SDKAdaptorTask> temp = new ArrayList<SDKAdaptorTask>();
        temp.addAll(this.sdkAdaptorTasks);
        return temp;
    }

    /**
     * SDK Adaptor Task Class
     * Defines what a Task can be comprised of and can do.
     */
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
        HashMap<AdaptorBarometer, SensorAdaptorBarometerEventCallback> sensorEventBarometer = null;

        /**
         * Define a position for the task
         * @param position Destination position for the task
         * @return The current tasks
         */
        public SDKAdaptorTask Position(Position position) {
            this.position = position;
            return this;
        }

        /**
         * Define a delay for the task
         * @param seconds Post delay for the task
         * @return The current tasks
         */
        public SDKAdaptorTask Delay(int seconds) {
            this.delay = seconds;
            return this;
        }

        /**
         * Define the travel speed for the task
         * @param metersPerSecond travel speed in m/s for the task
         * @return The current tasks
         */
        public SDKAdaptorTask Speed(double metersPerSecond) {
            this.speed = metersPerSecond;
            return this;
        }

        /**
         * Post accelerometer sensor data retrieval at the end of the task
         * @param adaptorAccelerometerCallback Callback to send the data back
         * @param adaptorAccelerometer The accelerometer adapter to get data from.
         * @return The current tasks
         */
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

        /**
         * Post barometer sensor data retrieval at the end of the task
         * @param adaptorBarometerEventCallback Callback to send the data back
         * @param adaptorBarometer The barometer adapter to get data from.
         * @return The current tasks
         */
        public SDKAdaptorTask SensorEvent(final SensorAdaptorBarometerEventCallback adaptorBarometerEventCallback, AdaptorBarometer adaptorBarometer) {
            if (adaptorBarometerEventCallback == null)
                throw new IllegalArgumentException("AdaptorBarometerEventCallback Must Not Be Null");
            else {
                if (sensorEventBarometer == null)
                    sensorEventBarometer = new HashMap<>();
                sensorEventBarometer.put(adaptorBarometer, adaptorBarometerEventCallback);
                return this;
            }
        }

        /**
         * Post bearing sensor data retrieval at the end of the task
         * @param sensorAdaptorBearingEventCallback Callback to send the data back
         * @param adaptorBearing The bearing adaptor to get the data from
         * @return The current task
         */
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

        /**
         * Post camera sensor data retrieval at the end of the task
         * @param sensorAdaptorCameraEventCallback callback to send the data back
         * @param adaptorCamera The bearing adaptor to get the data from
         * @return The current task
         */
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

        /**
         * Post GPS sensor data retrieval at the end of the task
         * @param sensorAdaptorGPSEventCallback Callback to send the data back
         * @param adaptorGPS The GPS adaptor to get the data from
         * @return The current task
         */
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

        /**
         * Post Gyroscope data retrieval at the end of the task
         * @param sensorAdaptorGyroscopeEventCallback Callback to send the data back
         * @param adaptorGyroscope The Gyroscope adaptor to get the data from
         * @return The current task
         */
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

        /**
         * Post LIDAR data retrieval at the end of the task
         * @param sensorAdaptorLIDAREventCallback Callback to send the data back
         * @param adaptorLIDAR The LIDAR adaptor to get the data from
         * @return The current task
         */
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

        /**
         * Post Magnetic data retrieval at the end of the task
         * @param sensorAdaptorMagneticEventCallback Callback to send the data back
         * @param adaptorMagnetic The Magnetic adaptor to get the data from
         * @return The current task
         */
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

        /**
         * Post RPLIDAR data retrieval at the end of the task
         * @param sensorAdaptorRPLIDAREventCallback Callback to send the data back
         * @param adaptorRPLIDAR The RPLIDAR adaptor to get the data from
         * @return The current task
         */
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

        /**
         * Post Ultrasonic data retrieval at the end of the task
         * @param adaptorUltrasonicTaskCallback Callback to send the data back
         * @param adaptorUltrasonic The Ultrasonic adaptor to get the data from
         * @return The current task
         */
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

