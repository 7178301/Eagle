package eagle.sdkInterface.sdkAdaptors.DJI;

import android.content.Context;

import dji.sdk.api.GroundStation.DJIGroundStationFlyingInfo;
import dji.sdk.interfaces.DJIBatteryUpdateInfoCallBack;
import dji.sdk.interfaces.DJIGroundStationFlyingInfoCallBack;
import dji.sdk.interfaces.DJIMcuUpdateStateCallBack;
import eagle.logging.Log;
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

    private DJIGroundStationFlyingInfo djiGroundStationFlyingInfo = null;
    private DJIMainControllerSystemState djiMainControllerSystemState = null;
    private DJIBatteryProperty djiBatteryProperty = null;
    //TODO Create method implementations

    public Phantom2Vision() {
        super("DJI", "Phantom 2 Vision", "2.4.0", "0.0.1", 15, 6, 2, 200);
    }

    public void loadDefaultSensorAdaptors(AdaptorLoader adaptorLoader) {
        addSensorAdaptorCamera(adaptorLoader.getSensorAdaptorCamera("DJICamera"));
    }

    public boolean connectToDrone() {
        Log.log("Phantom2VisionConnectToDrone", "Checking Android Context");
        if (context == null)
            return false;
        //must be run threaded (Networking Code)


        final Boolean[] permissionCheck = {null};
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
                                permissionCheck[0] = true;
                                Log.log("Phantom2Vision", "Initializing Drone Type");
                                if (!DJIDrone.initWithType(context, DJIDroneTypeDef.DJIDroneType.DJIDrone_Vision))
                                    permissionCheck[0] = false;
                            }else {
                                Log.log("Phantom2Vision", "Application Key Check: FAIL");
                                permissionCheck[0] = false;
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
            checkPermissionThread.join(30000);
        } catch (InterruptedException e) {
            return false;
        }
        if (!permissionCheck[0])
            return false;
        Log.log("Phantom2VisionConnectToDrone", "DJI Application Key Level = " + DJIDrone.getLevel());

        Log.log("Phantom2VisionConnectToDrone", "Checking Drone Communication Status");
        if (!DJIDrone.connectToDrone())
            return false;
        Log.log("Phantom2VisionConnectToDrone", "Drone Communication Status = True");

        Log.log("Phantom2VisionConnectToDrone", "Registering DJI Battery");
        DJIDrone.getDjiBattery().setBatteryUpdateInfoCallBack(this);
        DJIDrone.getDjiBattery().stopUpdateTimer();
        if (!DJIDrone.getDjiBattery().startUpdateTimer(100))
            return false;

        Log.log("Phantom2VisionConnectToDrone", "Registering DJI GroundStation");
        DJIDrone.getDjiGroundStation().setGroundStationFlyingInfoCallBack(this);
        DJIDrone.getDjiGroundStation().stopUpdateTimer();
        if (!DJIDrone.getDjiGroundStation().startUpdateTimer(100))
            return false;

        Log.log("Phantom2VisionConnectToDrone", "Registering  DJI MainController");
        DJIDrone.getDjiMC().setMcuUpdateStateCallBack(this);
        DJIDrone.getDjiMC().stopUpdateTimer();
        if (!DJIDrone.getDjiMC().startUpdateTimer(100))
            return false;

        final Boolean[] openGroundStation = {null};
        Log.log("Phantom2VisionConnectToDrone", "Starting  DJI GroundStation Communications");
        Thread openGroundStationThread = new Thread() {
            @Override
            public void run() {
                DJIDrone.getDjiGroundStation().openGroundStation(new DJIGroundStationExecutCallBack() {
                    @Override
                    public void onResult(DJIGroundStationTypeDef.GroundStationResult groundStationResult) {
                        Log.log("Phantom2VisionConnectToDrone", "DJI GroundStation Communications " + groundStationResult);
                        if (groundStationResult == DJIGroundStationTypeDef.GroundStationResult.GS_Result_Successed)
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
        final Boolean[] closeGroundStation = {null};
        Log.log("Phantom2VisionDissconectFromDrone", "Stopping  DJI GroundStation Communications");
        Thread openGroundStationThread = new Thread() {
            @Override
            public void run() {
                if(DJIDrone.disconnectToDrone()) {
                    DJIDrone.getDjiGroundStation().closeGroundStation(new DJIGroundStationExecutCallBack() {
                        @Override
                        public void onResult(DJIGroundStationTypeDef.GroundStationResult groundStationResult) {
                            Log.log("Phantom2VisionDissconectFromDrone", "DJI GroundStation Communications " + groundStationResult);
                            if (groundStationResult == DJIGroundStationTypeDef.GroundStationResult.GS_Result_Successed)
                                closeGroundStation[0] = true;
                            else
                                closeGroundStation[0] = false;
                        }
                    });
                    DJIDrone.getDjiBattery().stopUpdateTimer();
                    DJIDrone.getDjiMC().stopUpdateTimer();
                    DJIDrone.getDjiGroundStation().stopUpdateTimer();
                }
            }
        };
        openGroundStationThread.start();
        try {
            openGroundStationThread.join(10000);
        } catch (InterruptedException e) {
            return false;
        }
        if (closeGroundStation[0] == null)
            return false;
        else
            return closeGroundStation[0];
    }

    public boolean isConnectedToDrone() {
        return DJIDrone.getDjiGroundStation()!=null;
    }

    public boolean standbyDrone() {
        return false;
    }

    public Position getPositionInFlight() {
        if (djiGroundStationFlyingInfo != null&&djiGroundStationFlyingInfo.gpsStatus!=DJIGroundStationTypeDef.GroundStationGpsStatus.GS_GPS_Unknown) {
            double roll360, pitch360, yaw360;
            if (djiGroundStationFlyingInfo.roll >= 0)
                roll360 = Float.valueOf(djiGroundStationFlyingInfo.roll).doubleValue();
            else
                roll360 = Float.valueOf(360 - Math.abs(djiGroundStationFlyingInfo.roll)).doubleValue();
            if (djiGroundStationFlyingInfo.pitch >= 0)
                pitch360 = Float.valueOf(djiGroundStationFlyingInfo.pitch).doubleValue();
            else
                pitch360 = Float.valueOf(360 - Math.abs(djiGroundStationFlyingInfo.pitch)).doubleValue();
            if (djiGroundStationFlyingInfo.yaw >=  0)
                yaw360 = Float.valueOf(djiGroundStationFlyingInfo.yaw).doubleValue();
            else
                yaw360 = Float.valueOf(360 - Math.abs(djiGroundStationFlyingInfo.yaw)).doubleValue();

            PositionGPS positionGPS = new PositionGPS(djiGroundStationFlyingInfo.phantomLocationLatitude,
                    djiGroundStationFlyingInfo.phantomLocationLongitude,
                    Float.valueOf(djiGroundStationFlyingInfo.altitude).doubleValue(),
                    new Angle(roll360),
                    new Angle(pitch360),
                    new Angle(yaw360));
            return positionGPS;
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
            Log.log("Phantom2VisionFlyToDisplacement","FlyToDisplacement: GPS Position Current: "+getPositionInFlight().toString());
            Log.log("Phantom2VisionFlyToDisplacement","FlyToDisplacement: GPS Position: "+positionGPS.toString());
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
            Log.log("Phantom2VisionFlyToDisplacement", "FlyToDisplacement: DisplacementPosition: " + positionDisplacement.toString());
            PositionGPS positionGPS = (PositionGPS) getPositionInFlight().add(positionDisplacement);
            Log.log("Phantom2VisionFlyToDisplacement","FlyToDisplacement: GPS Position Current: "+getPositionInFlight().toString());
            Log.log("Phantom2VisionFlyToDisplacement","FlyToDisplacement: GPS Position: "+positionGPS.toString());
            flyTo(sdkAdaptorCallback, positionGPS);
        }
    }

    @Override
    public void goHome(final SDKAdaptorCallback sdkAdaptorCallback) {
        DJIDrone.getDjiGroundStation().goHome(new DJIGroundStationGoHomeCallBack() {
            @Override
            public void onResult(DJIGroundStationTypeDef.GroundStationGoHomeResult groundStationResult) {
                if(sdkAdaptorCallback!=null) {
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
    public void onResult(DJIGroundStationFlyingInfo djiGroundStationFlyingInfo) {
        this.djiGroundStationFlyingInfo = djiGroundStationFlyingInfo;
        if (getPositionAssigned() == null&&djiGroundStationFlyingInfo.gpsStatus==DJIGroundStationTypeDef.GroundStationGpsStatus.GS_GPS_Excellent) {
            try {
                setPositionAssigned(new PositionGPS(getPositionInFlight()));
            } catch (InvalidPositionTypeException e) {
                Log.log("Phantom2Vision", e.getMessage());
            }
        }
        if (getHomePosition() == null&&djiGroundStationFlyingInfo.gpsStatus==DJIGroundStationTypeDef.GroundStationGpsStatus.GS_GPS_Excellent) {
            try {
                setHomePosition(new PositionGPS(getPositionInFlight()));
            } catch (InvalidPositionTypeException e) {
                Log.log("Phantom2Vision", e.getMessage());
            }
        }
    }

    @Override
    public void onResult(DJIMainControllerSystemState djiMainControllerSystemState) {
        this.djiMainControllerSystemState = djiMainControllerSystemState;
    }

    @Override
    public void onResult(DJIBatteryProperty djiBatteryProperty) {
        this.djiBatteryProperty = djiBatteryProperty;
    }

    @Override
    public void setThrottle(double percentage) {

    }

    @Override
    public void setYaw(double angle) {

    }

    @Override
    public void setPitch(double angle) {

    }

    @Override
    public void setRoll(double angle) {

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
