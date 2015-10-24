package au.edu.swin.sparrow;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import au.edu.swin.sparrow.Fragment.AccelerometerFragment;
import au.edu.swin.sparrow.Fragment.BearingFragment;
import au.edu.swin.sparrow.Fragment.CameraFragment;
import au.edu.swin.sparrow.Fragment.GPSFragment;
import au.edu.swin.sparrow.Fragment.GyroscopeFragment;
import au.edu.swin.sparrow.Fragment.LIDARFragment;
import au.edu.swin.sparrow.Fragment.MagneticFragment;
import au.edu.swin.sparrow.Fragment.SensorFragment;
import au.edu.swin.sparrow.Fragment.UltrasonicFragment;
import eagle.Drone;
import eagle.Log;
import eagle.LogCallback;
import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.PositionDisplacement;
import eagle.network.protocolBuffer.ProtocolBufferServer;
import eagle.network.telnet.TelnetServer;
import eagle.sdkInterface.sensorAdaptors.AdaptorAccelerometer;
import eagle.sdkInterface.sensorAdaptors.AdaptorBearing;
import eagle.sdkInterface.sensorAdaptors.AdaptorCamera;
import eagle.sdkInterface.sensorAdaptors.AdaptorGPS;
import eagle.sdkInterface.sensorAdaptors.AdaptorGyroscope;
import eagle.sdkInterface.sensorAdaptors.AdaptorLIDAR;
import eagle.sdkInterface.sensorAdaptors.AdaptorMagnetic;
import eagle.sdkInterface.sensorAdaptors.AdaptorUltrasonic;

public class APIAdaptorActivity extends Activity implements AccelerometerFragment.OnFragmentInteractionListener, View.OnClickListener, LogCallback {


    Vector<SensorFragment> sensorFragments = new Vector<>();

    Drone drone = new Drone();
    TelnetServer telnet = null;
    ProtocolBufferServer protocolBufferServer = null;

    private Button buttonExpandControls;
    private Button buttonExpandSensors;
    private Button buttonExpandLog;

    private Button buttonControlConnect;
    private Button buttonControlForward;
    private Button buttonControlDisconnect;
    private Button buttonControlLeft;
    private Button buttonControlRight;
    private Button buttonControlBackward;
    private Button buttonControlRotateRight;
    private Button buttonControlRotateLeft;
    private Button buttonControlUp;
    private Button buttonControlGoHome;
    private Button buttonControlDown;

    private LinearLayout linearLayoutSensors;
    private GridLayout gridLayoutControls;
    private WebView webViewLog;

    private boolean isSensorsVisible = false;
    private boolean isControlsVisible = false;
    private boolean isLogVisible = false;

    private TextView textViewConnectedStatus;

    private Timer myTimer;

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_apiadaptor);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        drone.setSDKAdaptor(this.getIntent().getStringExtra("drone"));
        drone.getSDKAdaptor().setAndroidContext(this);
        initializeUI();
        telnet = new TelnetServer(drone.getSDKAdaptor().scriptingEngine, 2323);
        protocolBufferServer = new ProtocolBufferServer(drone.getSDKAdaptor().scriptingEngine, 2324);
        Log.addVerboseCallback(this);

        buttonExpandControls.setOnClickListener(this);
        buttonExpandSensors.setOnClickListener(this);
        buttonExpandLog.setOnClickListener(this);

        buttonControlConnect.setOnClickListener(this);
        buttonControlForward.setOnClickListener(this);
        buttonControlDisconnect.setOnClickListener(this);
        buttonControlLeft.setOnClickListener(this);
        buttonControlRight.setOnClickListener(this);
        buttonControlBackward.setOnClickListener(this);
        buttonControlRotateRight.setOnClickListener(this);
        buttonControlRotateLeft.setOnClickListener(this);
        buttonControlUp.setOnClickListener(this);
        buttonControlGoHome.setOnClickListener(this);
        buttonControlDown.setOnClickListener(this);

        MyTimerTask myTask = new MyTimerTask();
        myTimer = new Timer();
        myTimer.schedule(myTask, 3000, 1000);

        if(drone.getSDKAdaptor().connectToDrone())
            textViewConnectedStatus.setText(getResources().getString(R.string.connected));
    }

    @Override
    protected void onDestroy() {
        myTimer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Calendar.getInstance().getTime());
            Log.writeLogToFile(Environment.getExternalStorageDirectory().getPath() + "/sparrow/log-" + timeStamp + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUISensors();
    }

    private void initializeUI() {
        TextView adaptorNameTextView = (TextView) findViewById(R.id.adaptorNameTextView);
        adaptorNameTextView.setText(drone.getSDKAdaptor().getAdaptorName());

        textViewConnectedStatus = (TextView) findViewById(R.id.textViewConnectedStatus);

        buttonExpandControls = (Button) findViewById(R.id.buttonExpandControls);
        gridLayoutControls = (GridLayout) findViewById(R.id.gridLayoutControls);

        buttonControlConnect = (Button) findViewById(R.id.buttonControlConnect);
        buttonControlForward = (Button) findViewById(R.id.buttonControlForward);
        buttonControlDisconnect = (Button) findViewById(R.id.buttonControlDisconnect);
        buttonControlLeft = (Button) findViewById(R.id.buttonControlLeft);
        buttonControlRight = (Button) findViewById(R.id.buttonControlRight);
        buttonControlBackward = (Button) findViewById(R.id.buttonControlBackward);
        buttonControlRotateRight = (Button) findViewById(R.id.buttonControlRotateRight);
        buttonControlRotateLeft = (Button) findViewById(R.id.buttonControlRotateLeft);
        buttonControlUp = (Button) findViewById(R.id.buttonControlUp);
        buttonControlDown = (Button) findViewById(R.id.buttonControlDown);
        buttonControlGoHome = (Button) findViewById(R.id.buttonControlGoHome);

        buttonExpandSensors = (Button) findViewById(R.id.buttonExpandSensors);
        linearLayoutSensors = (LinearLayout) findViewById(R.id.scrollViewSensors);

        buttonExpandLog = (Button) findViewById(R.id.buttonExpandLog);
        webViewLog = (WebView) findViewById(R.id.webViewLog);
        updateLogUI();

        FragmentManager fragMan = getFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        ArrayList<AdaptorAccelerometer> accelerometerAdaptors = drone.getSDKAdaptor().getAccelerometers();
        for (AdaptorAccelerometer accelerometer : accelerometerAdaptors) {
            AccelerometerFragment fragment = AccelerometerFragment.newInstance();
            accelerometer.setAndroidContext(this);
            accelerometer.connectToSensor();
            fragment.setAccelerometerAdaptor(accelerometer);
            sensorFragments.add(fragment);
            fragTransaction.add(R.id.scrollViewSensors, fragment);
        }
        ArrayList<AdaptorCamera> adaptorCameras = drone.getSDKAdaptor().getCameras();
        for (AdaptorCamera camera : adaptorCameras) {
            CameraFragment fragment = CameraFragment.newInstance();
            camera.connectToSensor();
            fragment.setCameraAdaptor(camera);
            sensorFragments.add(fragment);
            fragTransaction.add(R.id.scrollViewSensors, fragment);
        }
        ArrayList<AdaptorUltrasonic> ultrasonicsAdaptors = drone.getSDKAdaptor().getUltrasonics();
        for (AdaptorUltrasonic ultrasonic : ultrasonicsAdaptors) {
            UltrasonicFragment fragment = UltrasonicFragment.newInstance();
            ultrasonic.setAndroidContext(this);
            ultrasonic.connectToSensor();
            fragment.setUltrasonicAdaptor(ultrasonic);
            sensorFragments.add(fragment);
            fragTransaction.add(R.id.scrollViewSensors, fragment);
        }
        ArrayList<AdaptorLIDAR> lidarAdaptors = drone.getSDKAdaptor().getLidars();
        for (AdaptorLIDAR lidar : lidarAdaptors) {
            LIDARFragment fragment = LIDARFragment.newInstance();
            lidar.setAndroidContext(this);
            lidar.connectToSensor();
            fragment.setLIDARAdaptor(lidar);
            sensorFragments.add(fragment);
            fragTransaction.add(R.id.scrollViewSensors, fragment);
        }
        ArrayList<AdaptorGyroscope> gyroscopeAdaptors = drone.getSDKAdaptor().getGyroscopes();
        for (AdaptorGyroscope gyroscope : gyroscopeAdaptors) {
            GyroscopeFragment fragment = GyroscopeFragment.newInstance();
            gyroscope.setAndroidContext(this);
            gyroscope.connectToSensor();
            fragment.setGyroscopeAdaptor(gyroscope);
            sensorFragments.add(fragment);
            fragTransaction.add(R.id.scrollViewSensors, fragment);
        }
        ArrayList<AdaptorMagnetic> magneticAdaptors = drone.getSDKAdaptor().getMagnetics();
        for (AdaptorMagnetic magnetic : magneticAdaptors) {
            MagneticFragment fragment = MagneticFragment.newInstance();
            magnetic.setAndroidContext(this);
            magnetic.connectToSensor();
            fragment.setMagneticAdaptor(magnetic);
            sensorFragments.add(fragment);
            fragTransaction.add(R.id.scrollViewSensors, fragment);
        }
        ArrayList<AdaptorGPS> gpsAdaptors = drone.getSDKAdaptor().getGPSs();
        for (AdaptorGPS gps : gpsAdaptors) {
            GPSFragment fragment = GPSFragment.newInstance();
            gps.setAndroidContext(this);
            gps.connectToSensor();
            fragment.setGPSAdaptor(gps);
            sensorFragments.add(fragment);
            fragTransaction.add(R.id.scrollViewSensors, fragment);
        }
        ArrayList<AdaptorBearing> bearingAdaptors = drone.getSDKAdaptor().getBearings();
        for (AdaptorBearing adaptorBearing : bearingAdaptors) {
            BearingFragment fragment = BearingFragment.newInstance();
            adaptorBearing.setAndroidContext(this);
            adaptorBearing.connectToSensor();
            fragment.setMagneticAccelerometerAdaptors(adaptorBearing);
            sensorFragments.add(fragment);
            fragTransaction.add(R.id.scrollViewSensors, fragment);
        }
        fragTransaction.commit();
    }

    private void updateUISensors() {
        for (SensorFragment sensor : sensorFragments) {
            sensor.updateData();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonExpandControls:
                if (isControlsVisible) {
                    buttonExpandControls.setText(R.string.expandControls);
                    gridLayoutControls.setVisibility(View.GONE);
                    isControlsVisible = false;
                } else {
                    buttonExpandControls.setText(R.string.collapseControls);
                    gridLayoutControls.setVisibility(View.VISIBLE);
                    isControlsVisible = true;
                }
                break;
            case R.id.buttonExpandSensors:
                if (isSensorsVisible) {
                    buttonExpandSensors.setText(R.string.expandSensors);
                    linearLayoutSensors.setVisibility(View.GONE);
                    isSensorsVisible = false;
                } else {
                    buttonExpandSensors.setText(R.string.collapseSensors);
                    linearLayoutSensors.setVisibility(View.VISIBLE);
                    isSensorsVisible = true;
                }
                break;
            case R.id.buttonExpandLog:
                if (isLogVisible) {
                    buttonExpandLog.setText(R.string.expandLog);
                    webViewLog.setVisibility(View.GONE);
                    isLogVisible = false;
                } else {
                    buttonExpandLog.setText(R.string.collapseLog);
                    webViewLog.setVisibility(View.VISIBLE);
                    isLogVisible = true;
                }
                break;
            case R.id.buttonControlConnect:
                if (drone.getSDKAdaptor().connectToDrone())
                    textViewConnectedStatus.setText(getResources().getString(R.string.connected));
                else
                    textViewConnectedStatus.setText(getResources().getString(R.string.not_connected));
                break;
            case R.id.buttonControlDisconnect:
                if (!drone.getSDKAdaptor().disconnectFromDrone())
                    textViewConnectedStatus.setText(getResources().getString(R.string.connected));
                else
                    textViewConnectedStatus.setText(getResources().getString(R.string.not_connected));
                break;
            case R.id.buttonControlForward:
                if (drone != null && drone.getSDKAdaptor() != null && drone.getSDKAdaptor().getPositionInFlight() != null) {
                    double bearingAngle = drone.getSDKAdaptor().getPositionInFlight().getYaw().getDegrees();
                    drone.getSDKAdaptor().flyTo(null, new PositionDisplacement(5 * Math.cos(Math.toRadians(bearingAngle)), 5 * Math.sin(Math.toRadians(bearingAngle)), 0, new Angle(0)));
                }
                break;
            case R.id.buttonControlBackward:
                if (drone != null && drone.getSDKAdaptor() != null && drone.getSDKAdaptor().getPositionInFlight() != null) {
                    double bearingAngle = drone.getSDKAdaptor().getPositionInFlight().getYaw().getDegrees();
                    drone.getSDKAdaptor().flyTo(null, new PositionDisplacement(-5 * Math.cos(Math.toRadians(bearingAngle)), -5 * Math.sin(Math.toRadians(bearingAngle)), 0, new Angle(0)));
                }
                break;
            case R.id.buttonControlLeft:
                if (drone != null && drone.getSDKAdaptor() != null && drone.getSDKAdaptor().getPositionInFlight() != null) {
                    double bearingAngle = drone.getSDKAdaptor().getPositionInFlight().getYaw().getDegrees();
                    drone.getSDKAdaptor().flyTo(null, new PositionDisplacement(5 * Math.sin(Math.toRadians(bearingAngle)), -5 * Math.cos(Math.toRadians(bearingAngle)), 0, new Angle(0)));
                }
                break;
            case R.id.buttonControlRight:
                if (drone != null && drone.getSDKAdaptor() != null && drone.getSDKAdaptor().getPositionInFlight() != null) {
                    double bearingAngle = drone.getSDKAdaptor().getPositionInFlight().getYaw().getDegrees();
                    drone.getSDKAdaptor().flyTo(null, new PositionDisplacement(-5 * Math.sin(Math.toRadians(bearingAngle)), 5 * Math.cos(Math.toRadians(bearingAngle)), 0, new Angle(0)));
                }
                break;
            case R.id.buttonControlRotateLeft:
                if (drone != null && drone.getSDKAdaptor() != null)
                    drone.getSDKAdaptor().changeYawDisplacement(null, new Angle(315));
                break;
            case R.id.buttonControlRotateRight:
                if (drone != null && drone.getSDKAdaptor() != null)
                    drone.getSDKAdaptor().changeYawDisplacement(null, new Angle(45));
                break;
            case R.id.buttonControlUp:
                if (drone != null && drone.getSDKAdaptor() != null)
                    drone.getSDKAdaptor().changeAltitudeDisplacement(null, 2);
                break;
            case R.id.buttonControlDown:
                if (drone != null && drone.getSDKAdaptor() != null)
                    drone.getSDKAdaptor().changeAltitudeDisplacement(null, -2);
                break;
            case R.id.buttonControlGoHome:
                if (drone != null && drone.getSDKAdaptor() != null)
                    drone.getSDKAdaptor().goHome(null);
                break;
        }
    }

    @Override
    public void onLogEntry(String tag, String message) {
        if (webViewLog != null)
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateLogUI();
                }
            });
    }

    public synchronized void updateLogUI() {
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<head>");

        html.append("</head>");
        html.append("<body>");
        ArrayList<String> tempLog = Log.getLog();

        for (int i = tempLog.size(); i > 0; i--) {
            html.append(tempLog.get(i - 1) + "<br>");
        }
        html.append("</body></html>");
        webViewLog.loadDataWithBaseURL("file:///android_asset/", html.toString(), "text/html", "UTF-8", "");
    }

    class MyTimerTask extends TimerTask {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateUISensors();
                }
            });
        }
    }
}
