package au.edu.swin.sparrow;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import eagle.Drone;
import eagle.sdkInterface.sdkAdaptors.Flyver.F450FlamewheelActivity;
import eagle.Log;
import eagle.TelnetServer;
import eagle.sdkInterface.sensorAdaptors.AdaptorAccelerometer;
import eagle.sdkInterface.sensorAdaptors.AdaptorGPS;
import eagle.sdkInterface.sensorAdaptors.AdaptorGyroscope;
import eagle.sdkInterface.sensorAdaptors.AdaptorLIDAR;
import eagle.sdkInterface.sensorAdaptors.AdaptorMagnetic;
import eagle.sdkInterface.sensorAdaptors.AdaptorUltrasonic;
import au.edu.swin.sparrow.Fragment.AccelerometerFragment;
import au.edu.swin.sparrow.Fragment.GPSFragment;
import au.edu.swin.sparrow.Fragment.GyroscopeFragment;
import au.edu.swin.sparrow.Fragment.LIDARFragment;
import au.edu.swin.sparrow.Fragment.MagneticFragment;
import au.edu.swin.sparrow.Fragment.SensorFragment;
import au.edu.swin.sparrow.Fragment.UltrasonicFragment;

public class APIAdaptorActivity extends Activity implements AccelerometerFragment.OnFragmentInteractionListener, View.OnClickListener, Log.LogCallback {

    Vector<SensorFragment> sensorFragments = new Vector<SensorFragment>();

    Drone drone = new Drone();
    TelnetServer telnet = new TelnetServer(drone);

    private Button buttonExpandSensors;
    private LinearLayout linearLayoutSensors;
    private boolean sensorsCollapsed = false;

    private Button buttonExpandLog;
    private WebView webViewLog;
    private boolean logCollapsed = false;

    private Vector<String> logMessages = new Vector<String>();
    boolean newLog = true;
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_apiadaptor);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        drone.setSDKAdaptor(this.getIntent().getStringExtra("drone"));
        drone.getSDKAdaptor().setAndroidContext(this);
        initializeUI();
        Log.addCallback(telnet);
        new Thread(telnet).start();

        MyTimerTask myTask = new MyTimerTask();
        Timer myTimer = new Timer();
        myTimer.schedule(myTask, 3000, 50);
    }

    @Override
    protected void onDestroy() {
        Log.removeCallback(telnet);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUI();
    }


    private void initializeUI() {
        TextView adaptorNameTextView = (TextView) findViewById(R.id.adaptorNameTextView);
        adaptorNameTextView.setText(drone.getSDKAdaptor().getAdaptorName());
        final Button selectAdaptorButton = (Button) findViewById(R.id.buttonConnect);
        selectAdaptorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TextView adaptorNameTextView = (TextView) findViewById(R.id.textViewConnectedStatus);
                if (drone.getSDKAdaptor().connectToDrone()) {
                    adaptorNameTextView.setText(getResources().getString(R.string.connected));
                } else {
                    adaptorNameTextView.setText(getResources().getString(R.string.not_connected));
                }
            }
        });

        buttonExpandSensors = (Button)findViewById(R.id.buttonExpandSensors);
        buttonExpandSensors.setOnClickListener(this);

        linearLayoutSensors = (LinearLayout)findViewById(R.id.scrollViewSensors);

        buttonExpandLog = (Button)findViewById(R.id.buttonExpandLog);
        buttonExpandLog.setOnClickListener(this);

        webViewLog = (WebView)findViewById(R.id.webViewLog);
        Log.addCallback(this);


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
        fragTransaction.commit();
    }

    private void updateUI() {
        for (SensorFragment sensor : sensorFragments) {
            sensor.updateData();
        }

        if (newLog && webViewLog != null) {
            newLog = false;
            StringBuilder html = new StringBuilder();
            html.append("<html>");
            html.append("<head>");

            html.append("</head>");
            html.append("<body>");
            for (String mess : logMessages) {
                html.append("<p>" + mess + "</p>");
            }
            html.append("</body></html>");

            webViewLog.loadDataWithBaseURL("file:///android_asset/", html.toString(), "text/html", "UTF-8", "");
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonExpandSensors:
                if (sensorsCollapsed) {
                    buttonExpandSensors.setText(R.string.collapseSensors);
                    linearLayoutSensors.setVisibility(View.VISIBLE);
                    sensorsCollapsed = false;
                } else {
                    buttonExpandSensors.setText(R.string.expandSensors);
                    linearLayoutSensors.setVisibility(View.GONE);
                    sensorsCollapsed = true;
                }
                break;
            case R.id.buttonExpandLog:
                if (logCollapsed) {
                    buttonExpandLog.setText(R.string.collapseLog);
                    webViewLog.setVisibility(View.VISIBLE);
                    logCollapsed = false;
                } else {
                    buttonExpandLog.setText(R.string.expandLog);
                    webViewLog.setVisibility(View.GONE);
                    logCollapsed = true;
                }
                break;


        }
    }

    @Override
    public void handleMessage(String message) {
        logMessages.add(message);
        newLog = true;
    }

    class MyTimerTask extends TimerTask {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateUI();
                }
            });
        }
    }
}
