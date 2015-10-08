package eagle.sdkInterface.sdkAdaptors.DJI;

import android.content.Context;
import android.os.Looper;

import dji.sdk.api.Battery.DJIBatteryProperty;
import dji.sdk.api.DJIDrone;
import dji.sdk.api.DJIDroneTypeDef;
import dji.sdk.api.GroundStation.DJIGroundStationFlyingInfo;
import dji.sdk.api.GroundStation.DJIGroundStationTask;
import dji.sdk.api.GroundStation.DJIGroundStationTypeDef;
import dji.sdk.api.GroundStation.DJIGroundStationWaypoint;
import dji.sdk.api.MainController.DJIMainControllerSystemState;
import dji.sdk.interfaces.DJIBatteryUpdateInfoCallBack;
import dji.sdk.interfaces.DJIGeneralListener;
import dji.sdk.interfaces.DJIGroundStationExecuteCallBack;
import dji.sdk.interfaces.DJIGroundStationFlyingInfoCallBack;
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

/**
 * DJI SDKAdaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */
public class Phantom2Vision extends SDKAdaptor implements DJIGroundStationFlyingInfoCallBack, DJIMcuUpdateStateCallBack, DJIBatteryUpdateInfoCallBack {

    private Context context = null;
    boolean permissionCheck = false;

    private DJIGroundStationFlyingInfo djiGroundStationFlyingInfo = null;
    private DJIMainControllerSystemState djiMainControllerSystemState = null;
    private DJIBatteryProperty djiBatteryProperty = null;
    //TODO Create method implementations

    public Phantom2Vision() {
        super("DJI", "Phantom 2 Vision", "2.4.0", "0.0.1", 0, 0);
    }

    public void loadDefaultSensorAdaptors(AdaptorLoader adaptorLoader) {
        addSensorAdaptorCamera(adaptorLoader.getSensorAdaptorCamera("DJICamera"));
    }

    public boolean connectToDrone() {
        Log.log("Phantom2Vision", "Checking Android Context");
        if (context == null)
            return false;
        //must be run threaded (Networking Code)

        Log.log("Phantom2Vision", "Checking DJI Application Key");
        Thread checkPermissionThread = new Thread() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    DJIDrone.checkPermission(context, new DJIGeneralListener() {
                        @Override
                        public void onGetPermissionResult(int result) {
                            if (result == 0)
                                permissionCheck = true;
                            Log.log("Phantom2Vision", "Initializing Drone Type");
                            if (!DJIDrone.initWithType(context, DJIDroneTypeDef.DJIDroneType.DJIDrone_Vision))
                                permissionCheck = false;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        checkPermissionThread.start();
        try {
            checkPermissionThread.join(30000);
        } catch (InterruptedException e) {
            return false;
        }
        if (!permissionCheck)
            return false;
        Log.log("Phantom2Vision", "DJI Application Key Level = " + DJIDrone.getLevel());

        Log.log("Phantom2Vision", "Checking Drone Communication Status");
        if (!DJIDrone.connectToDrone())
            return false;
        Log.log("Phantom2Vision", "Drone Communication Status = True");

        Log.log("Phantom2Vision", "Registering DJI Battery");
        DJIDrone.getDjiBattery().setBatteryUpdateInfoCallBack(this);
        DJIDrone.getDjiBattery().stopUpdateTimer();
        if (!DJIDrone.getDjiBattery().startUpdateTimer(100))
            return false;

        Log.log("Phantom2Vision", "Registering DJI GroundStation");
        DJIDrone.getDjiGroundStation().setGroundStationFlyingInfoCallBack(this);
        DJIDrone.getDjiGroundStation().stopUpdateTimer();
        if (!DJIDrone.getDjiGroundStation().startUpdateTimer(100))
            return false;

        Log.log("Phantom2Vision", "Registering  DJI MainController");
        DJIDrone.getDjiMC().setMcuUpdateStateCallBack(this);
        DJIDrone.getDjiMC().stopUpdateTimer();
        if (!DJIDrone.getDjiMC().startUpdateTimer(100))
            return false;

        final Boolean[] openGroundStation = {null};
        Log.log("Phantom2Vision", "Starting  DJI GroundStation Communications");
        Thread openGroundStationThread = new Thread() {
            @Override
            public void run() {
                DJIDrone.getDjiGroundStation().openGroundStation(new DJIGroundStationExecuteCallBack() {
                    @Override
                    public void onResult(DJIGroundStationTypeDef.GroundStationResult groundStationResult) {
                        Log.log("Phantom2Vision", "DJI GroundStation Communications " + groundStationResult);
                        if (groundStationResult == DJIGroundStationTypeDef.GroundStationResult.GS_Result_Success)
                            openGroundStation[0] = true;
                        else
                            openGroundStation[0] = false;
                    }
                });
            }
        };
        openGroundStationThread.start();
        try {
            openGroundStationThread.join(10000);
        } catch (InterruptedException e) {
            return false;
        }
        if (openGroundStation[0] == null)
            return false;
        else
            return openGroundStation[0];
    }

    //TODO: workout what to do for these functions
    public boolean disconnectFromDrone() {
        return false;
    }

    public boolean isConnectedToDrone() {
        return false;
    }

    public boolean standbyDrone() {
        return false;
    }

    public Position getPositionInFlight() {
        if (djiGroundStationFlyingInfo != null&&djiGroundStationFlyingInfo.gpsStatus!=DJIGroundStationTypeDef.GroundStationGpsStatus.GS_GPS_Unknown) {
            PositionGPS pos = new PositionGPS(djiGroundStationFlyingInfo.droneLocationLatitude, djiGroundStationFlyingInfo.droneLocationLongitude, djiGroundStationFlyingInfo.altitude, new Angle(djiGroundStationFlyingInfo.roll), new Angle(djiGroundStationFlyingInfo.pitch), new Angle(djiGroundStationFlyingInfo.yaw));
            return pos;
        } else
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
            DJIGroundStationTask djiGroundStationTask = new DJIGroundStationTask();
            DJIGroundStationWaypoint djiGroundStationWaypoint = new DJIGroundStationWaypoint(positionGPS.getLatitude(), positionGPS.getLongitude());
            djiGroundStationWaypoint.altitude = new Float(positionGPS.getAltitude());
            if (positionGPS.getYaw().getDegrees() < 180)
                djiGroundStationWaypoint.heading = new Double(positionGPS.getYaw().getDegrees()).shortValue();
            else
                djiGroundStationWaypoint.heading = new Double(-(360 - positionGPS.getYaw().getDegrees())).shortValue();

            djiGroundStationWaypoint.speed = new Float(speed * 10);
            djiGroundStationTask.addWaypoint(djiGroundStationWaypoint);

            Log.log("Phantom2Vision", "Uploading Ground Station Task");
            DJIDrone.getDjiGroundStation().uploadGroundStationTask(djiGroundStationTask, new DJIGroundStationExecuteCallBack() {
                @Override
                public void onResult(final DJIGroundStationTypeDef.GroundStationResult groundStationResult) {
                    Log.log("Phantom2Vision", "Upload Of Ground Station Task = " + groundStationResult);
                    if (groundStationResult == DJIGroundStationTypeDef.GroundStationResult.GS_Result_Success) {
                        Log.log("Phantom2Vision", "Starting Ground Station Task ");
                        DJIDrone.getDjiGroundStation().startGroundStationTask(new DJIGroundStationExecuteCallBack() {
                            @Override
                            public void onResult(DJIGroundStationTypeDef.GroundStationResult groundStationResult) {
                                Log.log("Phantom2Vision", "Ground Station Task " + groundStationResult);
                                if (sdkAdaptorCallback != null && groundStationResult == DJIGroundStationTypeDef.GroundStationResult.GS_Result_Success)
                                    sdkAdaptorCallback.onResult(false, groundStationResult.toString());
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
            DJIGroundStationTask djiGroundStationTask = new DJIGroundStationTask();
            DJIGroundStationWaypoint djiGroundStationWaypoint = new DJIGroundStationWaypoint(positionGPS.getLatitude(), positionGPS.getLongitude());
            djiGroundStationWaypoint.altitude = new Float(positionGPS.getAltitude());
            if (positionGPS.getYaw().getDegrees() < 180)
                djiGroundStationWaypoint.heading = new Double(positionGPS.getYaw().getDegrees()).shortValue();
            else
                djiGroundStationWaypoint.heading = new Double(-(360 - positionGPS.getYaw().getDegrees())).shortValue();
            djiGroundStationTask.addWaypoint(djiGroundStationWaypoint);

            final DJIGroundStationTypeDef.GroundStationResult[] groundStationResults = {null};

            Log.log("Phantom2Vision", "Uploading Ground Station Task");
            DJIDrone.getDjiGroundStation().uploadGroundStationTask(djiGroundStationTask, new DJIGroundStationExecuteCallBack() {
                @Override
                public void onResult(final DJIGroundStationTypeDef.GroundStationResult groundStationResult) {
                    Log.log("Phantom2Vision", "Upload Of Ground Station Task = " + groundStationResult);
                    groundStationResults[0] = groundStationResult;
                    if (groundStationResult == DJIGroundStationTypeDef.GroundStationResult.GS_Result_Success) {
                        Log.log("Phantom2Vision", "Starting Ground Station Task ");
                        DJIDrone.getDjiGroundStation().startGroundStationTask(new DJIGroundStationExecuteCallBack() {
                            @Override
                            public void onResult(DJIGroundStationTypeDef.GroundStationResult groundStationResult) {
                                Log.log("Phantom2Vision", "Ground Station Task " + groundStationResult);
                                if (sdkAdaptorCallback != null && groundStationResult == DJIGroundStationTypeDef.GroundStationResult.GS_Result_Success)
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
            PositionGPS positionGPS = (PositionGPS) getPositionInFlight().add(positionDisplacement);
            flyTo(sdkAdaptorCallback, positionGPS, speed);
        }
    }

    @Override
    public void flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionDisplacement positionDisplacement){
        if (positionDisplacement == null) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Arguments must not be null");
        } else if (getPositionInFlight() == null) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Current Position Not Available");
        } else {
            PositionGPS positionGPS = (PositionGPS) getPositionInFlight().add(positionDisplacement);
            flyTo(sdkAdaptorCallback, positionGPS);
        }
    }

    @Override
    public void goHome(final SDKAdaptorCallback sdkAdaptorCallback) {
        DJIDrone.getDjiGroundStation().goHome(new DJIGroundStationExecuteCallBack() {
            @Override
            public void onResult(DJIGroundStationTypeDef.GroundStationResult groundStationResult) {
                if(sdkAdaptorCallback!=null) {
                    if (groundStationResult == DJIGroundStationTypeDef.GroundStationResult.GS_Result_Success)
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
    public void onResult(DJIGroundStationFlyingInfo djiGroundStationFlyingInfo) {
        if (getPositionAssigned() == null&&djiGroundStationFlyingInfo.gpsStatus!=DJIGroundStationTypeDef.GroundStationGpsStatus.GS_GPS_Unknown) {
            try {
                setPositionAssigned(new PositionGPS(djiGroundStationFlyingInfo.droneLocationLatitude, djiGroundStationFlyingInfo.droneLocationLongitude, djiGroundStationFlyingInfo.altitude, new Angle(djiGroundStationFlyingInfo.roll), new Angle(djiGroundStationFlyingInfo.pitch), new Angle(djiGroundStationFlyingInfo.yaw)));
            } catch (InvalidPositionTypeException e) {
                Log.log("Phantom2Vision", e.getMessage());
            }
        }
        if (getHomePosition() == null&&djiGroundStationFlyingInfo.gpsStatus!=DJIGroundStationTypeDef.GroundStationGpsStatus.GS_GPS_Unknown) {
            try {
                setHomePosition(new PositionGPS(djiGroundStationFlyingInfo.droneLocationLatitude, djiGroundStationFlyingInfo.droneLocationLongitude, djiGroundStationFlyingInfo.altitude, new Angle(djiGroundStationFlyingInfo.roll), new Angle(djiGroundStationFlyingInfo.pitch), new Angle(djiGroundStationFlyingInfo.yaw)));
            } catch (InvalidPositionTypeException e) {
                Log.log("Phantom2Vision", e.getMessage());
            }
        }
        this.djiGroundStationFlyingInfo = djiGroundStationFlyingInfo;
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


//The following are example implementations
/*
    public boolean flyTo(Position destination, double speed){//default speed?
        if(drone.ready==true){
            if(destination.getAltitude()!=drone.position.getAltitude())
                drone.changeAltitude(destination.getAltitude());//callback?
            drone.linearFlyTo(destination.getLatitude(), destination.getLongitude());
            return true;
        }
        else
            return false;
    }

    public Boolean takeOff(double altitude) {
        if (drone.ready==true){
            drone.changeAltitude(altitude,drone.minSpeed);
            return true;
        }
        else
            return false;
    }

    public Boolean land(double speed) {
        hover(2000);
        //change the altitude for the following
        drone.changeAltitude(home.getAltitude(),drone.minSpeed);//lands in the same altitude as it took off from.
        drone.shutDown();
        return null;
    }

    public Boolean hover(long milsec){
        try{
            wait(milsec);
            //Can we somehow bring the speed to zero?
            return true;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

*/
