package au.edu.swin.sparrow;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import au.edu.swin.sparrow.Fragment.AccelerometerFragment;
import au.edu.swin.sparrow.Fragment.GPSFragment;
import au.edu.swin.sparrow.Fragment.GyroscopeFragment;
import au.edu.swin.sparrow.Fragment.LIDARFragment;
import au.edu.swin.sparrow.Fragment.MagneticFragment;
import au.edu.swin.sparrow.Fragment.SensorFragment;
import au.edu.swin.sparrow.Fragment.UltrasonicFragment;
import eagle.Drone;
import eagle.sdkInterface.sensorAdaptors.AdaptorAccelerometer;
import eagle.sdkInterface.sensorAdaptors.AdaptorGPS;
import eagle.sdkInterface.sensorAdaptors.AdaptorGyroscope;
import eagle.sdkInterface.sensorAdaptors.AdaptorLIDAR;
import eagle.sdkInterface.sensorAdaptors.AdaptorMagnetic;
import eagle.sdkInterface.sensorAdaptors.AdaptorUltrasonic;

public class APIAdaptorActivity extends AppCompatActivity implements AccelerometerFragment.OnFragmentInteractionListener {

    Vector<SensorFragment> sensorFragments = new Vector<SensorFragment>();

    Drone drone = new Drone();

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_apiadaptor);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        drone.setSDKAdaptor(this.getIntent().getStringExtra("drone"));
        initializeUI();
        updateUI();
        MyTimerTask myTask = new MyTimerTask();
        Timer myTimer = new Timer();
        myTimer.schedule(myTask, 3000, 50);
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

        final Button ioioButton = (Button) findViewById(R.id.buttonIOIO);
        ioioButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intentIOIO = new Intent(APIAdaptorActivity.this, IOIOActivity.class);
                APIAdaptorActivity.this.startActivity(intentIOIO);
            }
        });

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
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
