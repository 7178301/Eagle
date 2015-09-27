package eagle.sdkInterface.sdkAdaptors.DJI;

import android.content.Context;

import au.edu.swin.eagleAPIAndroid.R;
import dji.sdk.api.DJIDrone;
import dji.sdk.api.DJIDroneTypeDef;
import dji.sdk.api.GroundStation.DJIGroundStationFlyingInfo;
import dji.sdk.api.GroundStation.DJIGroundStationTask;
import dji.sdk.api.GroundStation.DJIGroundStationTypeDef;
import dji.sdk.api.GroundStation.DJIGroundStationWaypoint;
import dji.sdk.api.MainController.DJIMainControllerSystemState;
import dji.sdk.interfaces.DJIGerneralListener;
import dji.sdk.interfaces.DJIGroundStationExecutCallBack;
import dji.sdk.interfaces.DJIGroundStationFlyingInfoCallBack;
import dji.sdk.interfaces.DJIGroundStationTakeOffCallBack;
import dji.sdk.interfaces.DJIMcuUpdateStateCallBack;
import dji.sdk.interfaces.DJIReceivedVideoDataCallBack;
import dji.sdk.widget.DjiGLSurfaceView;
import eagle.Log;
import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.PositionDisplacement;
import eagle.navigation.positioning.PositionGPS;
import eagle.navigation.positioning.PositionMetric;
import eagle.sdkInterface.AdaptorLoader;
import eagle.sdkInterface.SDKAdaptor;

/**
 * DJI SDKAdaptor
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 09/04/2015
 * <p/>
 * Date Modified	26/05/2015 - Nicholas
 */
public class Phantom2Vision extends SDKAdaptor {
    private Context context = null;

    boolean permission = false;
    DJIGroundStationTypeDef.GroundStationResult groundStation = DJIGroundStationTypeDef.GroundStationResult.GS_Result_Unknown;
    private PositionGPS initPos;


    //TODO Create method implementations

    public Phantom2Vision() {
        super("DJI", "Phantom 2 Vision", "alpha", "0.0.1");
    }

    public void loadDefaultSensorAdaptors(AdaptorLoader adaptorLoader) {

    }

    public boolean connectToDrone() {
        if (context == null) {
            return false;
        }
        //must be run threaded (Networking Code)
        Thread authThread = new Thread() {
            @Override
            public void run() {
                try {
                    //Your code goes here
                    DJIDrone.checkPermission(context, new DJIGerneralListener() {
                        @Override
                        public void onGetPermissionResult(int result) {
                            //Log.log(, "checkPermission = " + result);
                            if (result == 0)
                                permission = true;
                            //else exit
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        authThread.start();
        try {
            authThread.join(10000);
        } catch (InterruptedException e) {
            return false;
        }
        if (!permission) {
            return false;
        }
        return onInitSDK();
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
        DJIGroundStationFlyingInfo fi = PhantomTracker.INSTANCE.getFlyingInfo();
        PositionGPS pos = new PositionGPS(fi.phantomLocationLatitude, fi.phantomLocationLongitude, fi.altitude, new Angle(fi.roll), new Angle(fi.pitch), new Angle(fi.yaw));
        return pos;
    }

    //TODO: workout what to do for these functions
    public boolean resumeDrone() {
        return false;
    }

    public boolean shutdownDrone() {
        return false;
    }

    @Override
    public boolean flyTo(PositionMetric positionMetric, double speed) {
        //casting metric position to displacement doesnt work, but creating a displacement does.
        PositionDisplacement disp = new PositionDisplacement(positionMetric);
        PositionGPS newPos = initPos.add(disp);
        return flyTo(newPos, speed);
    }

    @Override
    public boolean flyTo(PositionMetric positionMetric) {
        //casting metric position to displacement doesnt work, but creating a displacement does.
        PositionDisplacement disp = new PositionDisplacement(positionMetric);
        PositionGPS newPos = initPos.add(disp);
        return flyTo(newPos);
    }

    @Override
    public boolean flyTo(PositionGPS position, double speed) {
        return flyTo(position);
    }

    @Override
    public boolean flyTo(PositionGPS position) {
        DJIGroundStationTask task1 = new DJIGroundStationTask();
        DJIGroundStationWaypoint way1 = new DJIGroundStationWaypoint(position.getLatitude(), position.getLongitude());
        way1.altitude = new Float(position.getAltitude());
        task1.addWaypoint(way1);


        final DJIGroundStationTypeDef.GroundStationResult[] stationResult = {null};
        final DJIGroundStationTypeDef.GroundStationTakeOffResult[] takeOffResult = {null};

        DJIDrone.getDjiGroundStation().uploadGroundStationTask(task1, new DJIGroundStationExecutCallBack() {

            @Override
            public void onResult(final DJIGroundStationTypeDef.GroundStationResult groundStationResult) {
                Log.log("uploadGroundStationTask = " + groundStationResult);
                stationResult[0] = groundStationResult;
                if (groundStationResult == DJIGroundStationTypeDef.GroundStationResult.GS_Result_Successed) {
                    DJIDrone.getDjiGroundStation().startGroundStationTask(new DJIGroundStationTakeOffCallBack() {
                        @Override
                        public void onResult(DJIGroundStationTypeDef.GroundStationTakeOffResult groundStationTakeOffResult) {
                            //Log.log(, "Uselessly hanging out here.......");
                            Log.log("startGroundStationTask = " + groundStationTakeOffResult);
                            takeOffResult[0] = groundStationTakeOffResult;

                        }
                    });
                }
            }
        });
        try {
            while (true) {
                if (stationResult[0] != null && stationResult[0] == DJIGroundStationTypeDef.GroundStationResult.GS_Result_Successed
                        && takeOffResult[0] != null && takeOffResult[0] == DJIGroundStationTypeDef.GroundStationTakeOffResult.GS_Takeoff_Successed) {
                    return true;
                }
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            return false;
        }


    }


    @Override
    public boolean flyTo(PositionDisplacement position, double speed) {
        PositionGPS current = (PositionGPS) getPositionInFlight();
        PositionGPS newPos = current.add(position);
        return flyTo(newPos, speed);
    }

    @Override
    public boolean flyTo(PositionDisplacement position) {
        PositionGPS current = (PositionGPS) getPositionInFlight();
        PositionGPS newPos = current.add(position);
        return flyTo(newPos);
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


    private boolean onInitSDK() {
        //Log.log(, "init with type = " + DJIDrone.initWithType(getApplicationContext(), DJIDroneTypeDef.DJIDroneType.DJIDrone_Vision));
        boolean result = DJIDrone.initWithType(context, DJIDroneTypeDef.DJIDroneType.DJIDrone_Vision);
        if (!result) {
            return false;
        }
        //Log.log(, "GetLevel = " + DJIDrone.getLevel());
        //Log.log(, "connectToDrone = " + DJIDrone.connectToDrone());
        result = DJIDrone.connectToDrone();
        if (!result) {
            return false;
        }
        result = GroundStationGo();
        if (!result) {
            return false;
        }
        result = MControllerGo();
        return result;

    }

    private boolean MControllerGo() {
        //DJIDrone.getDjiMC().
        //Log.log(, "DJIDrone.getDjiMC.getClass = " + DJIDrone.getDjiMC().getClass()); //class dji.sdk.api.MainController.DJIPhantomMainController

        boolean result = DJIDrone.getDjiMC().startUpdateTimer(1000);
        if (!result) {
            return false;
        }
        DJIDrone.getDjiMC().setMcuUpdateStateCallBack(new DJIMcuUpdateStateCallBack() {
            public void onResult(DJIMainControllerSystemState state) {
                //Log.log(, "DJIMainControllerSystemState altitude = " + state.altitude);
            }
        });
        return true;
        //if (drone.getDjiMC().getClass().equals(DJIPhantomMainController.class)) Log.log(, "DJIPhantomMainController");


    }

    private boolean GroundStationGo() {
        PhantomTracker.INSTANCE.start();
        initPos = (PositionGPS) getPositionInFlight();

        boolean result = DJIDrone.getDjiGroundStation().startUpdateTimer(1000);
        DJIDrone.getDjiGroundStation().setGroundStationFlyingInfoCallBack(new DJIGroundStationFlyingInfoCallBack() {
            @Override
            public void onResult(DJIGroundStationFlyingInfo djiGroundStationFlyingInfo) {
                //Log.log(, "altitude = " + djiGroundStationFlyingInfo.altitude); //the results are rubbish
            }
        });
        DJIDrone.getDjiGroundStation().openGroundStation(new DJIGroundStationExecutCallBack() {
            @Override
            public void onResult(DJIGroundStationTypeDef.GroundStationResult result) {
                groundStation = result;
            }
        });
        while (groundStation != DJIGroundStationTypeDef.GroundStationResult.GS_Result_Successed) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return false;
            }
        }
        return true;
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
