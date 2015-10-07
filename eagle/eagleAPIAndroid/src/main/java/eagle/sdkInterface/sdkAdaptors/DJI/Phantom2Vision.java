package eagle.sdkInterface.sdkAdaptors.DJI;

import android.content.Context;

import dji.sdk.api.Battery.DJIBatteryProperty;
import dji.sdk.api.DJIDrone;
import dji.sdk.api.DJIDroneTypeDef;
import dji.sdk.api.GroundStation.DJIGroundStationFlyingInfo;
import dji.sdk.api.GroundStation.DJIGroundStationTask;
import dji.sdk.api.GroundStation.DJIGroundStationTypeDef;
import dji.sdk.api.GroundStation.DJIGroundStationWaypoint;
import dji.sdk.api.MainController.DJIMainControllerSystemState;
import dji.sdk.interfaces.DJIBatteryUpdateInfoCallBack;
import dji.sdk.interfaces.DJIGerneralListener;
import dji.sdk.interfaces.DJIGroundStationExecutCallBack;
import dji.sdk.interfaces.DJIGroundStationFlyingInfoCallBack;
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

/**
 * DJI SDKAdaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */
public class Phantom2Vision extends SDKAdaptor implements DJIGroundStationFlyingInfoCallBack, DJIMcuUpdateStateCallBack, DJIBatteryUpdateInfoCallBack{

    private Context context = null;
    boolean permissionCheck = false;

    private DJIGroundStationFlyingInfo djiGroundStationFlyingInfo = null;
    private DJIMainControllerSystemState djiMainControllerSystemState = null;
    private DJIBatteryProperty djiBatteryProperty = null;
    //TODO Create method implementations

    public Phantom2Vision() {
        super("DJI", "Phantom 2 Vision", "1.0.6", "0.0.1", 0, 0);
    }

    public void loadDefaultSensorAdaptors(AdaptorLoader adaptorLoader) {
        addSensorAdaptorCamera(adaptorLoader.getSensorAdaptorCamera("DJICamera"));
    }

    public boolean connectToDrone() {
        Log.log("Phantom2Vision", "Checking Android Context");
        if (context == null) {
            return false;        }
        //must be run threaded (Networking Code)
        Log.log("Phantom2Vision", "Checking DJI Application Key");
        Thread authThread = new Thread() {
            @Override
            public void run() {
                try {
                    DJIDrone.checkPermission(context, new DJIGerneralListener() {
                        @Override
                        public void onGetPermissionResult(int result) {
                            if (result == 0)
                                permissionCheck = true;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        authThread.start();
        try {
            authThread.join(30000);
        } catch (InterruptedException e) {
            return false;
        }
        if (!permissionCheck)
            return false;
        Log.log("Phantom2Vision", "DJI Application Key Level = " + DJIDrone.getLevel());

        Log.log("Phantom2Vision", "Initializing Drone Type");
        if(!DJIDrone.initWithType(context, DJIDroneTypeDef.DJIDroneType.DJIDrone_Vision))
            return false;

        Log.log("Phantom2Vision", "Checking Drone Communication Status");
        if(!DJIDrone.connectToDrone())
            return false;
        Log.log("Phantom2Vision", "Drone Communication Status = True");

        Log.log("Phantom2Vision", "Registering DJI Battery");
        DJIDrone.getDjiBattery().setBatteryUpdateInfoCallBack(this);
        if(!DJIDrone.getDjiBattery().startUpdateTimer(1000))
            return false;

        Log.log("Phantom2Vision", "Registering DJI GroundStation");
        DJIDrone.getDjiGroundStation().setGroundStationFlyingInfoCallBack(this);
        if(!DJIDrone.getDjiGroundStation().startUpdateTimer(1000))
            return false;

        Log.log("Phantom2Vision", "Registering  DJI MainController");
        DJIDrone.getDjiMC().setMcuUpdateStateCallBack(this);
        return DJIDrone.getDjiMC().startUpdateTimer(1000);
    }

    //TODO: workout what to do for these functions
    public boolean disconnectFromDrone() {
        return false;
    }

    public boolean turnOnMotors(){
        Log.log("Phantom2Vision", "Starting  DJI GroundStation Communications");
        final DJIGroundStationTypeDef.GroundStationResult[] groundStationResult = {DJIGroundStationTypeDef.GroundStationResult.GS_Result_Unknown};
        DJIDrone.getDjiGroundStation().openGroundStation(new DJIGroundStationExecutCallBack() {
            @Override
            public void onResult(DJIGroundStationTypeDef.GroundStationResult groundStationResult1) {
                groundStationResult[0] = groundStationResult1;
            }
        });
        while (groundStationResult[0] == DJIGroundStationTypeDef.GroundStationResult.GS_Result_Unknown) {
            try {
                Log.log("Phantom2Vision", "Thread Sleeping "+groundStationResult[0]);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return false;
            }
        }
        Log.log("Phantom2Vision", "DJI GroundStation Communications SUCCESS");
        return (groundStationResult[0]==DJIGroundStationTypeDef.GroundStationResult.GS_Result_Successed);
    }

    public boolean isConnectedToDrone() {
        return false;
    }

    public boolean standbyDrone() {
        return false;
    }

    public Position getPositionInFlight() {
        if (djiGroundStationFlyingInfo!=null) {
            PositionGPS pos = new PositionGPS(djiGroundStationFlyingInfo.phantomLocationLatitude, djiGroundStationFlyingInfo.phantomLocationLongitude, djiGroundStationFlyingInfo.altitude, new Angle(djiGroundStationFlyingInfo.roll), new Angle(djiGroundStationFlyingInfo.pitch), new Angle(djiGroundStationFlyingInfo.yaw));
            return pos;
        }else return null;
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
        if (positionMetric == null){
            if (sdkAdaptorCallback!=null)
                sdkAdaptorCallback.onResult(false,"Arguments must not be null");
            throw new IllegalArgumentException("Arguments must not be null");
        } else if(speed<=0) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Speed Must Be Positive");
            throw new IllegalArgumentException("Speed Must Be Positive");
        }else if(getPositionInFlight()==null) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Current Position Not Available");
            throw new NullPointerException("Current Position Not Available");
        } else {
            PositionGPS positionGPS = (PositionGPS)getPositionInFlight().add(new PositionDisplacement(positionMetric));
            flyTo(sdkAdaptorCallback,positionGPS, speed);
        }
    }

    @Override
    public void flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionMetric positionMetric) {
        if (positionMetric == null){
            if (sdkAdaptorCallback!=null)
                sdkAdaptorCallback.onResult(false,"Arguments must not be null");
            throw new IllegalArgumentException("Arguments must not be null");
        } else if(getPositionInFlight()==null) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Current Position Not Available");
            throw new NullPointerException("Current Position Not Available");
        } else{
            PositionGPS positionGPS = (PositionGPS)getPositionInFlight().add(new PositionDisplacement(positionMetric));
            flyTo(sdkAdaptorCallback,positionGPS);
        }
    }

    @Override
    public void flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionGPS positionGPS, double speed) {
        if (positionGPS == null){
            if (sdkAdaptorCallback!=null)
                sdkAdaptorCallback.onResult(false,"Arguments must not be null");
            throw new IllegalArgumentException("Arguments must not be null");
        } else if(speed<=0) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Speed Must Be Positive");
            throw new IllegalArgumentException("Speed Must Be Positive");
        } else {
            DJIGroundStationTask djiGroundStationTask = new DJIGroundStationTask();
            DJIGroundStationWaypoint djiGroundStationWaypoint = new DJIGroundStationWaypoint(positionGPS.getLatitude(), positionGPS.getLongitude());
            djiGroundStationWaypoint.altitude = new Float(positionGPS.getAltitude());
            if (positionGPS.getAltitude() < 180)
                djiGroundStationWaypoint.heading = new Float(positionGPS.getAltitude());
            else
                djiGroundStationWaypoint.heading = new Float(-(360 - positionGPS.getAltitude()));

            djiGroundStationWaypoint.speed = new Float(speed * 10);
            djiGroundStationTask.addWaypoint(djiGroundStationWaypoint);

            final DJIGroundStationTypeDef.GroundStationResult[] groundStationResults = {null};

            Log.log("Phantom2Vision", "Uploading Ground Station Task");
            DJIDrone.getDjiGroundStation().uploadGroundStationTask(djiGroundStationTask, new DJIGroundStationExecutCallBack() {
                @Override
                public void onResult(final DJIGroundStationTypeDef.GroundStationResult groundStationResult) {
                    Log.log("Phantom2Vision", "Upload Of Ground Station Task = " + groundStationResult);
                    groundStationResults[0] = groundStationResult;
                    if (groundStationResult == DJIGroundStationTypeDef.GroundStationResult.GS_Result_Successed) {
                        Log.log("Phantom2Vision", "Starting Ground Station Task");
                        DJIDrone.getDjiGroundStation().startGroundStationTask(new DJIGroundStationTakeOffCallBack() {
                            @Override
                            public void onResult(DJIGroundStationTypeDef.GroundStationTakeOffResult groundStationTakeOffResult) {
                                Log.log("Phantom2Vision", "Ground Station Task" + groundStationTakeOffResult);
                                if (sdkAdaptorCallback != null && groundStationTakeOffResult == DJIGroundStationTypeDef.GroundStationTakeOffResult.GS_Takeoff_Successed)
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
        if (positionGPS == null){
            if (sdkAdaptorCallback!=null)
                sdkAdaptorCallback.onResult(false,"Arguments must not be null");
            throw new IllegalArgumentException("Arguments must not be null");
        } else {
            DJIGroundStationTask djiGroundStationTask = new DJIGroundStationTask();
            DJIGroundStationWaypoint djiGroundStationWaypoint = new DJIGroundStationWaypoint(positionGPS.getLatitude(), positionGPS.getLongitude());
            djiGroundStationWaypoint.altitude = new Float(positionGPS.getAltitude());
            if (positionGPS.getAltitude() < 180)
                djiGroundStationWaypoint.heading = new Float(positionGPS.getAltitude());
            else
                djiGroundStationWaypoint.heading = new Float(-(360 - positionGPS.getAltitude()));
            djiGroundStationTask.addWaypoint(djiGroundStationWaypoint);

            final DJIGroundStationTypeDef.GroundStationResult[] groundStationResults = {null};

            Log.log("Phantom2Vision", "Uploading Ground Station Task");
            DJIDrone.getDjiGroundStation().uploadGroundStationTask(djiGroundStationTask, new DJIGroundStationExecutCallBack() {
                @Override
                public void onResult(final DJIGroundStationTypeDef.GroundStationResult groundStationResult) {
                    Log.log("Phantom2Vision", "Upload Of Ground Station Task = " + groundStationResult);
                    groundStationResults[0] = groundStationResult;
                    if (groundStationResult == DJIGroundStationTypeDef.GroundStationResult.GS_Result_Successed) {
                        Log.log("Phantom2Vision", "Starting Ground Station Task");
                        DJIDrone.getDjiGroundStation().startGroundStationTask(new DJIGroundStationTakeOffCallBack() {
                            @Override
                            public void onResult(DJIGroundStationTypeDef.GroundStationTakeOffResult groundStationTakeOffResult) {
                                Log.log("Phantom2Vision", "Ground Station Task" + groundStationTakeOffResult);
                                if (sdkAdaptorCallback != null && groundStationTakeOffResult == DJIGroundStationTypeDef.GroundStationTakeOffResult.GS_Takeoff_Successed)
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
        if (positionDisplacement == null){
            if (sdkAdaptorCallback!=null)
                sdkAdaptorCallback.onResult(false,"Arguments must not be null");
            throw new IllegalArgumentException("Arguments must not be null");
        } else if(speed<=0) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Speed Must Be Positive");
            throw new IllegalArgumentException("Speed Must Be Positive");
        }else if(getPositionInFlight()==null) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Current Position Not Available");
            throw new NullPointerException("Current Position Not Available");
        } else {
            PositionGPS positionGPS = (PositionGPS) getPositionInFlight().add(positionDisplacement);
            flyTo(sdkAdaptorCallback,positionGPS, speed);
        }
    }

    @Override
    public void flyTo(final SDKAdaptorCallback sdkAdaptorCallback, PositionDisplacement positionDisplacement) {
        if (positionDisplacement == null){
            if (sdkAdaptorCallback!=null)
                sdkAdaptorCallback.onResult(false,"Arguments must not be null");
            throw new IllegalArgumentException("Arguments must not be null");
        } else if(getPositionInFlight()==null) {
            if (sdkAdaptorCallback != null)
                sdkAdaptorCallback.onResult(false, "Current Position Not Available");
            throw new NullPointerException("Current Position Not Available");
        } else{
            PositionGPS positionGPS = (PositionGPS) getPositionInFlight().add(positionDisplacement);
            flyTo(sdkAdaptorCallback, positionGPS);
        }
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
