package eagle.sdkInterface.sdkAdaptors.DJI;

import android.content.Context;
import android.os.Looper;

import dji.sdk.api.Battery.DJIBattery;
import dji.sdk.api.Battery.DJIBatteryProperty;
import dji.sdk.api.DJIDrone;
import dji.sdk.api.DJIDroneTypeDef;
import dji.sdk.api.GroundStation.DJIGroundStation;
import dji.sdk.api.GroundStation.DJIGroundStationFlyingInfo;
import dji.sdk.api.GroundStation.DJIGroundStationTask;
import dji.sdk.api.GroundStation.DJIGroundStationTypeDef;
import dji.sdk.api.GroundStation.DJIGroundStationWaypoint;
import dji.sdk.api.MainController.DJIMainControllerSystemState;
import dji.sdk.api.MainController.DJIMainControllerTypeDef;
import dji.sdk.interfaces.DJIBatteryUpdateInfoCallBack;
import dji.sdk.interfaces.DJIGerneralListener;
import dji.sdk.interfaces.DJIGroundStationExecutCallBack;
import dji.sdk.interfaces.DJIGroundStationFlyingInfoCallBack;
import dji.sdk.interfaces.DJIGroundStationGoHomeCallBack;
import dji.sdk.interfaces.DJIGroundStationTakeOffCallBack;
import dji.sdk.interfaces.DJIMcuUpdateStateCallBack;
import eagle.Log;
import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.PositionDisplacement;
import eagle.navigation.positioning.PositionGPS;
import eagle.navigation.positioning.PositionMetric;
import eagle.sdkInterface.AdaptorLoader;
import eagle.sdkInterface.SDKAdaptor;
import eagle.sdkInterface.SDKAdaptorCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorCallback;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorGPSEventCallback;

/**
 * DJI SDKAdaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */
public class Phantom2Vision extends SDKAdaptor implements DJIMcuUpdateStateCallBack, DJIBatteryUpdateInfoCallBack {

    private Context context = null;

    private DJIMainControllerSystemState djiMainControllerSystemState = null;
    private DJIBatteryProperty djiBatteryProperty = null;
    //TODO Create method implementations

    public Phantom2Vision() {
        super("DJI", "Phantom 2 Vision", "2.4.0", "0.0.1", 15, 6, 2, 200);
    }

    public void loadDefaultSensorAdaptors(AdaptorLoader adaptorLoader) {
        addSensorAdaptorCamera(adaptorLoader.getSensorAdaptorCamera("DJICamera"));
        addSensorAdaptorGPS(adaptorLoader.getSensorAdaptorGPS("DJIGPS"));
        addSensorAdaptorAccelerometer(adaptorLoader.getSensorAdaptorAccelerometer("DJIAccelerometer"));
        if (getGPSs().size() > 0 && getGPSs().get(0).getAdaptorName().equals("DJI GPS") && getAccelerometers().size() > 0 && getAccelerometers().get(0).getAdaptorName().equals("DJI Accelerometer"))
            getAccelerometers().get(0).setController(getGPSs().get(0));
    }

    public boolean connectToDrone() {
        final boolean[] returnValue = {true};

        Log.log("Phantom2VisionConnectToDrone", "Checking Android Context");
        if (context == null) {
            Log.log("Phantom2VisionConnectToDrone", "Checking Android Context FAIL: Not Set");
            return false;
        }

        //must be run threaded (Networking Code)
        Log.log("Phantom2VisionConnectToDrone", "Checking DJI Application Key");
        Thread checkPermissionThread = new Thread() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    DJIDrone.checkPermission(context, new DJIGerneralListener() {
                        @Override
                        public void onGetPermissionResult(int result) {
                            if (result == 0) {
                                Log.log("Phantom2Vision", "Initializing Drone Type");
                                if (!DJIDrone.initWithType(context, DJIDroneTypeDef.DJIDroneType.DJIDrone_Vision))
                                    Log.log("Phantom2VisionConnectToDrone", "Initializing Drone Type FAIL: Already Initialized");
                            } else {
                                Log.log("Phantom2Vision", "Application Key Check: FAIL");
                                returnValue[0] = false;
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        checkPermissionThread.start();
        try {
            checkPermissionThread.join(20000);
        } catch (InterruptedException e) {
            Log.log("Phantom2VisionConnectToDrone", "Checking DJI Application Key FAIL: Timed Out");
            returnValue[0] = false;
        }
        if (!returnValue[0])
            return false;

        Log.log("Phantom2VisionConnectToDrone", "DJI Application Key Level = " + DJIDrone.getLevel());

        Log.log("Phantom2VisionConnectToDrone", "Initializing Communication Protocols");
        if (!DJIDrone.connectToDrone())
            Log.log("Phantom2VisionConnectToDrone", "Initializing Communication Protocols Already Initialized");

        if (getGPSs().size() > 0 && getGPSs().get(0).getAdaptorName().equals("DJI GPS")) {
            getGPSs().get(0).addSensorAdaptorCallback(new SensorAdaptorCallback() {
                @Override
                public void onSensorChanged() {
                    if (getPositionAssigned() == null && getGPSs().get(0).getGPSAccuracy() == 3) {
                        try {
                            setPositionAssigned(new PositionGPS(getPositionInFlight()));
                        } catch (SDKAdaptor.InvalidPositionTypeException e) {
                            Log.log("Phantom2Vision", e.getMessage());
                        }
                    }
                    if (getHomePosition() == null && getGPSs().get(0).getGPSAccuracy() == 3) {
                        try {
                            setHomePosition(new PositionGPS(getPositionInFlight()));
                        } catch (SDKAdaptor.InvalidPositionTypeException e) {
                            Log.log("Phantom2Vision", e.getMessage());
                        }
                    }

                }
            });

        }

        if (getGPSs().size() > 0 && getGPSs().get(0).getAdaptorName().equals("DJI GPS")) {
            if (!getGPSs().get(0).connectToSensor())
                returnValue[0] = false;
        }

        Log.log("Phantom2VisionConnectToDrone", "Registering DJI Battery Polling Interval");
        DJIDrone.getDjiBattery().setBatteryUpdateInfoCallBack(this);
        DJIDrone.getDjiBattery().stopUpdateTimer();
        if (!DJIDrone.getDjiBattery().startUpdateTimer(100)) {
            Log.log("Phantom2VisionConnectToDrone", "Registering DJI Battery Polling Interval FAIL");
            returnValue[0] = false;
        }

        Log.log("Phantom2VisionConnectToDrone", "Registering  DJI MainController PollingInterval");
        DJIDrone.getDjiMC().setMcuUpdateStateCallBack(this);
        DJIDrone.getDjiMC().stopUpdateTimer();
        if (!DJIDrone.getDjiMC().startUpdateTimer(100)) {
            Log.log("Phantom2VisionConnectToDrone", "Registering DJI MainController Polling Interval FAIL");
            returnValue[0] = false;
        }
        if (!returnValue[0])
            return false;

        Log.log("Phantom2VisionConnectToDrone", "Starting  DJI GroundStation Communications");
        DJIDrone.getDjiGroundStation().openGroundStation(new DJIGroundStationExecutCallBack() {
            @Override
            public void onResult(DJIGroundStationTypeDef.GroundStationResult groundStationResult) {
                Log.log("Phantom2VisionConnectToDrone", "DJI GroundStation Communications " + groundStationResult);
            }
        });

        if (!returnValue[0])
            return returnValue[0];
        else if (isConnectedToDrone())
            return true;
        else {
            delay(1000);
            return isConnectedToDrone();
        }
    }

    //TODO: workout what to do for these functions
    public boolean disconnectFromDrone() {
        if(DJIDrone.getDjiGroundStation()!=null) {
            Log.log("Phantom2VisionDissconectFromDrone", "Stopping  DJI GroundStation Communications");
            DJIDrone.getDjiGroundStation().closeGroundStation(new DJIGroundStationExecutCallBack() {
                @Override
                public void onResult(DJIGroundStationTypeDef.GroundStationResult groundStationResult) {
                    Log.log("Phantom2VisionDissconectFromDrone", "DJI GroundStation Communications " + groundStationResult);
                }
            });
            Log.log("Phantom2VisionDissconectFromDrone", "Stopping  DJI Battery Polling Interval");
            DJIDrone.getDjiBattery().stopUpdateTimer();
            DJIDrone.getDjiBattery().setBatteryUpdateInfoCallBack(null);
            djiBatteryProperty = null;
            Log.log("Phantom2VisionDissconectFromDrone", "Stopping  DJI MainController Polling Interval");
            DJIDrone.getDjiMC().stopUpdateTimer();
            DJIDrone.getDjiMC().setMcuUpdateStateCallBack(null);
            djiMainControllerSystemState = null;

            if (getGPSs().size() > 0 && getGPSs().get(0).getAdaptorName().equals("DJI GPS")) {
                if (!getGPSs().get(0).disconnectFromSensor())
                    return false;
            }

            Log.log("Phantom2VisionDissconectFromDrone", "Destroying All Initialized Values/Variables");
            return DJIDrone.disconnectToDrone();
        }
        return false;
    }

    public boolean isConnectedToDrone() {
        return DJIDrone.getDjiMC() != null && djiMainControllerSystemState != null;
    }

    public boolean standbyDrone() {
        return false;
    }

    public Position getPositionInFlight() {
        if (getGPSs().size() > 0 && getGPSs().get(0).getAdaptorName().equals("DJI GPS") && getGPSs().get(0).isDataReady())
            return getGPSs().get(0).getData();
        else
            return null;
    }

    //TODO: workout what to do for these functions
    public boolean resumeDrone() {
        return false;
    }

    public boolean shutdownDrone() {
        return false;
    }

    @Override
    public void flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionMetric positionMetric, double speed) {
        if (positionMetric == null) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Arguments must not be null");
        } else if (speed <= 0) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Speed Must Be Positive");
        } else if (getPositionInFlight() == null) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Current Position Not Available");
        } else {
            PositionGPS positionGPS = (PositionGPS) getPositionInFlight().add(new PositionDisplacement(positionMetric));
            flyTo(sdkAdaptorCallback, positionGPS, speed);
        }
    }

    @Override
    public void flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionMetric positionMetric) {
        if (positionMetric == null) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Arguments must not be null");
        } else if (getPositionInFlight() == null) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Current Position Not Available");
        } else {
            PositionGPS positionGPS = (PositionGPS) getPositionInFlight().add(new PositionDisplacement(positionMetric));
            flyTo(sdkAdaptorCallback, positionGPS);
        }
    }

    @Override
    public void flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionGPS positionGPS, double speed) {
        if (positionGPS == null) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Arguments must not be null");
        } else if (speed <= 0) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Speed Must Be Positive");
        } else {
            try {
                setPositionAssigned(positionGPS);
            } catch (InvalidPositionTypeException e) {
                e.printStackTrace();
            }
            DJIGroundStationTask djiGroundStationTask = new DJIGroundStationTask();
            DJIGroundStationWaypoint djiGroundStationWaypoint = new DJIGroundStationWaypoint(positionGPS.getLatitude(), positionGPS.getLongitude());
            djiGroundStationWaypoint.altitude = Double.valueOf(positionGPS.getAltitude()).floatValue();
            djiGroundStationWaypoint.heading = Double.valueOf(Math.toRadians(positionGPS.getYaw().getDegrees180())).floatValue();

            djiGroundStationWaypoint.speed = Double.valueOf(speed).floatValue() * 10;
            djiGroundStationWaypoint.stayTime = 1;

            djiGroundStationTask.addWaypoint(djiGroundStationWaypoint);
            Log.log("Phantom2VisionFlyToGPS", "Waypoint Added: " + djiGroundStationWaypoint.latitude + " " + djiGroundStationWaypoint.lontitude + " " + djiGroundStationWaypoint.altitude + " * * " + djiGroundStationWaypoint.heading);


            Log.log("Phantom2VisionFlyToGPS", "Uploading Ground Station Task");
            DJIDrone.getDjiGroundStation().uploadGroundStationTask(djiGroundStationTask, new DJIGroundStationExecutCallBack() {
                @Override
                public void onResult(final DJIGroundStationTypeDef.GroundStationResult groundStationResult) {
                    Log.log("Phantom2VisionFlyToGPS", "Upload Of Ground Station Task = " + groundStationResult);
                    if (groundStationResult == DJIGroundStationTypeDef.GroundStationResult.GS_Result_Successed) {
                        Log.log("Phantom2VisionFlyToGPS", "Starting Ground Station Task ");
                        DJIDrone.getDjiGroundStation().startGroundStationTask(new DJIGroundStationTakeOffCallBack() {
                            @Override
                            public void onResult(DJIGroundStationTypeDef.GroundStationTakeOffResult groundStationResult) {
                                Log.log("Phantom2VisionFlyToGPS", "Ground Station Task " + groundStationResult);
                                if (sdkAdaptorCallback != null && groundStationResult == DJIGroundStationTypeDef.GroundStationTakeOffResult.GS_Takeoff_Successed)
                                    sdkAdaptorCallback.onResult(true, groundStationResult.toString());
                                else if (sdkAdaptorCallback != null)
                                    sdkAdaptorCallback.onResult(false, groundStationResult.toString());
                            }
                        });
                    } else if (sdkAdaptorCallback != null) {
                        sdkAdaptorCallback.onResult(false, groundStationResult.toString());
                    }
                }
            });
        }
    }

    @Override
    public void flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionGPS positionGPS) {
        if (positionGPS == null) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Arguments must not be null");
        } else {
            try {
                setPositionAssigned(positionGPS);
            } catch (InvalidPositionTypeException e) {
                e.printStackTrace();
            }
            DJIGroundStationTask djiGroundStationTask = new DJIGroundStationTask();
            DJIGroundStationWaypoint djiGroundStationWaypoint = new DJIGroundStationWaypoint(positionGPS.getLatitude(), positionGPS.getLongitude());
            djiGroundStationWaypoint.altitude = Double.valueOf(positionGPS.getAltitude()).floatValue();
            djiGroundStationWaypoint.heading = Double.valueOf(Math.toRadians(positionGPS.getYaw().getDegrees180())).floatValue();

            djiGroundStationWaypoint.speed = Double.valueOf(getMaxFlightSpeed()).floatValue() * 10;
            djiGroundStationWaypoint.stayTime = 1;

            djiGroundStationTask.addWaypoint(djiGroundStationWaypoint);
            Log.log("Phantom2VisionFlyToGPS", "Waypoint Added: " + djiGroundStationWaypoint.latitude + " " + djiGroundStationWaypoint.lontitude + " " + djiGroundStationWaypoint.altitude + " * * " + djiGroundStationWaypoint.heading);

            Log.log("Phantom2VisionFlyToGPS", "Uploading Ground Station Task");
            DJIDrone.getDjiGroundStation().uploadGroundStationTask(djiGroundStationTask, new DJIGroundStationExecutCallBack() {
                @Override
                public void onResult(final DJIGroundStationTypeDef.GroundStationResult groundStationResult) {
                    Log.log("Phantom2VisionFlyToGPS", "Upload Of Ground Station Task = " + groundStationResult);
                    if (groundStationResult == DJIGroundStationTypeDef.GroundStationResult.GS_Result_Successed) {
                        Log.log("Phantom2VisionFlyToGPS", "Starting Ground Station Task ");
                        DJIDrone.getDjiGroundStation().startGroundStationTask(new DJIGroundStationTakeOffCallBack() {
                            @Override
                            public void onResult(DJIGroundStationTypeDef.GroundStationTakeOffResult groundStationResult) {
                                Log.log("Phantom2VisionFlyToGPS", "Start Ground Station Task " + groundStationResult);
                                if (sdkAdaptorCallback != null && groundStationResult == DJIGroundStationTypeDef.GroundStationTakeOffResult.GS_Takeoff_Successed)
                                    sdkAdaptorCallback.onResult(true, groundStationResult.toString());
                                else if (sdkAdaptorCallback != null)
                                    sdkAdaptorCallback.onResult(false, groundStationResult.toString());
                            }
                        });
                    } else if (sdkAdaptorCallback != null) {
                        sdkAdaptorCallback.onResult(false, groundStationResult.toString());
                    }
                }
            });
        }
    }

    @Override
    public void flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionDisplacement positionDisplacement, double speed) {
        if (positionDisplacement == null) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Arguments must not be null");
        } else if (speed <= 0) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Speed Must Be Positive");
        } else if (getPositionInFlight() == null) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Current Position Not Available");
        } else {
            Log.log("Phantom2VisionFlyToDisplacement", "FlyToDisplacement: DisplacementPosition: " + positionDisplacement.toString());
            PositionGPS positionGPS = (PositionGPS) getPositionInFlight().add(positionDisplacement);
            Log.log("Phantom2VisionFlyToDisplacement", "FlyToDisplacement: GPS Position Current: " + getPositionInFlight().toString());
            Log.log("Phantom2VisionFlyToDisplacement", "FlyToDisplacement: GPS Position: " + positionGPS.toString());
            flyTo(sdkAdaptorCallback, positionGPS, speed);
        }
    }

    @Override
    public void flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionDisplacement positionDisplacement) {
        if (positionDisplacement == null) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Arguments must not be null");
        } else if (getPositionInFlight() == null) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Current Position Not Available");
        } else {
            Log.log("Phantom2VisionFlyToDisplacement", "FlyToDisplacement: DisplacementPosition: " + positionDisplacement.toString());
            PositionGPS positionGPS = (PositionGPS) getPositionInFlight().add(positionDisplacement);
            Log.log("Phantom2VisionFlyToDisplacement", "FlyToDisplacement: GPS Position Current: " + getPositionInFlight().toString());
            Log.log("Phantom2VisionFlyToDisplacement", "FlyToDisplacement: GPS Position: " + positionGPS.toString());
            flyTo(sdkAdaptorCallback, positionGPS);
        }
    }

    @Override
    public void goHome(final SDKAdaptorCallback sdkAdaptorCallback) {
        Log.log("Phantom2VisionGoHome", "Go Home");
        DJIDrone.getDjiGroundStation().goHome(new DJIGroundStationGoHomeCallBack() {
            @Override
            public void onResult(DJIGroundStationTypeDef.GroundStationGoHomeResult groundStationResult) {
                if (sdkAdaptorCallback != null) {
                    Log.log("Phantom2VisionGoHome", "Go Home: " + groundStationResult);
                    if (groundStationResult == DJIGroundStationTypeDef.GroundStationGoHomeResult.GS_GoHome_Successed)
                        sdkAdaptorCallback.onResult(true, groundStationResult.toString());
                    else
                        sdkAdaptorCallback.onResult(false, groundStationResult.toString());
                }
            }
        });
    }

    @Override
    public void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean setAndroidContext(Object object) {
        if (object instanceof Context) {
            context = (Context) object;
            return true;
        }
        return false;
    }

    @Override
    public void onResult(DJIMainControllerSystemState djiMainControllerSystemState) {
        this.djiMainControllerSystemState = djiMainControllerSystemState;
    }

    @Override
    public void onResult(DJIBatteryProperty djiBatteryProperty) {
        this.djiBatteryProperty = djiBatteryProperty;
    }
}