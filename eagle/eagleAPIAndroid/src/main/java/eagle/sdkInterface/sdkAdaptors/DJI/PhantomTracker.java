package eagle.sdkInterface.sdkAdaptors.DJI;
import dji.sdk.api.Battery.DJIBatteryProperty;
import dji.sdk.api.Camera.DJICameraSystemState;
import dji.sdk.api.DJIDrone;
import dji.sdk.api.Gimbal.DJIGimbalAttitude;
import dji.sdk.api.GroundStation.DJIGroundStationFlyingInfo;
import dji.sdk.api.MainController.DJIMainControllerSystemState;
import dji.sdk.interfaces.DJIBatteryUpdateInfoCallBack;
import dji.sdk.interfaces.DJICameraSystemStateCallBack;
import dji.sdk.interfaces.DJIGimbalUpdateAttitudeCallBack;
import dji.sdk.interfaces.DJIGroundStationFlyingInfoCallBack;
import dji.sdk.interfaces.DJIMcuUpdateStateCallBack;
import eagle.Log;

@SuppressWarnings("unused")
public class PhantomTracker extends Thread implements DJIGroundStationFlyingInfoCallBack,
        DJIGimbalUpdateAttitudeCallBack, DJIMcuUpdateStateCallBack, DJIBatteryUpdateInfoCallBack,
        DJICameraSystemStateCallBack
{
    private static final String TAG = "SSIL-DJI-SDK | Tracker";
    public static final PhantomTracker INSTANCE = new PhantomTracker();

    private DJIGimbalAttitude gimbalAttitude;
    private DJIGroundStationFlyingInfo flyingInfo;
    private DJIMainControllerSystemState controllerSystemState;
    private DJIBatteryProperty batteryProperty;
    private DJICameraSystemState cameraSystemState;
    private boolean running = true;

    private PhantomTracker() {
        DJIDrone.getDjiGroundStation().setGroundStationFlyingInfoCallBack(this);
        DJIDrone.getDjiMC().setMcuUpdateStateCallBack(this);
        DJIDrone.getDjiBattery().setBatteryUpdateInfoCallBack(this);
        DJIDrone.getDjiCamera().setDjiCameraSystemStateCallBack(this);
        DJIDrone.getDjiGimbal().setGimbalUpdateAttitudeCallBack(this);
    }

    @Override
    public void run() {
        Log.log("Phantom Drone tracking thread starting");
        DJIDrone.getDjiGroundStation().startUpdateTimer(1000);
        DJIDrone.getDjiMC().startUpdateTimer(1000);
        DJIDrone.getDjiBattery().startUpdateTimer(1000);
        DJIDrone.getDjiCamera().startUpdateTimer(1000);
        DJIDrone.getDjiGimbal().startUpdateTimer(1000);

        try {
            while (running) {
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        running = false;
    }

    public DJIGimbalAttitude getGimbalAttitude() {

        return gimbalAttitude;
    }

    public DJIGroundStationFlyingInfo getFlyingInfo() {
        return flyingInfo;
    }

    public DJIMainControllerSystemState getControllerSystemState() {
        return controllerSystemState;
    }

    public DJIBatteryProperty getBatteryProperty() {
        return batteryProperty;
    }

    public DJICameraSystemState getCameraSystemState() {
        return cameraSystemState;
    }

    @Override
    public void onResult(DJIGimbalAttitude newGimbalAttitude) {
        gimbalAttitude = newGimbalAttitude;
    }

    @Override
    public void onResult(DJIGroundStationFlyingInfo newFlyingInfo) {
        flyingInfo = newFlyingInfo;
    }

    @Override
    public void onResult(DJIMainControllerSystemState newControllerSystemState) {
        controllerSystemState = newControllerSystemState;
    }

    @Override
    public void onResult(DJIBatteryProperty newBatteryProperty) {
        batteryProperty = newBatteryProperty;
    }

    @Override
    public void onResult(DJICameraSystemState newCameraSystemState) {
        cameraSystemState = newCameraSystemState;
    }
}
