package au.edu.swin.sparrow;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import eagle.Drone;
import eagle.sdkInterface.sensorAdaptors.AdaptorAccelerometer;

public class APIAdaptorActivity extends AppCompatActivity{


    Drone drone = new Drone();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apiadaptor);
        drone.setSDKAdaptor(this.getIntent().getStringExtra("drone"));
        initializeUI();
        MyTimerTask myTask = new MyTimerTask();
        Timer myTimer = new Timer();
        myTimer.schedule(myTask, 3000, 500);
    }


    private void initializeUI() {
        TextView adaptorNameTextView = (TextView) findViewById(R.id.adaptorNameTextView);
        adaptorNameTextView.setText(drone.getSDKAdaptor().getAdaptorName());
        final Button selectAdaptorButton = (Button) findViewById(R.id.buttonConnect);
        selectAdaptorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TextView adaptorNameTextView = (TextView) findViewById(R.id.textViewConnectedStatus);
                if (drone.getSDKAdaptor().connectToDrone())
                    adaptorNameTextView.setText(getResources().getString(R.string.connected));
                else
                    adaptorNameTextView.setText(getResources().getString(R.string.not_connected));
            }
        });
        ArrayList<AdaptorAccelerometer> accelerometerAdaptors = drone.getSDKAdaptor().getAccelerometers();
        AdaptorAccelerometer first;
        if(accelerometerAdaptors!=null&&accelerometerAdaptors.size()>0){
            first = accelerometerAdaptors.get(0);
            first.setAndroidContext(this);
            first.connectToSensor();
        }
    }

    private void updateUI(){
        ArrayList<AdaptorAccelerometer> accelerometerAdaptors = drone.getSDKAdaptor().getAccelerometers();
        AdaptorAccelerometer first;
        if(accelerometerAdaptors!=null&&accelerometerAdaptors.size()>0) {
            first = accelerometerAdaptors.get(0);
            if (first.isConnectedToSensor()) {
                TextView accelerometerTextViewX = (TextView) findViewById(R.id.textViewAccelerometerDataX);
                TextView accelerometerTextViewY = (TextView) findViewById(R.id.textViewAccelerometerDataY);
                TextView accelerometerTextViewZ = (TextView) findViewById(R.id.textViewAccelerometerDataZ);
                accelerometerTextViewX.setText(String.valueOf(first.getData()[0]));
                accelerometerTextViewY.setText(String.valueOf(first.getData()[1]));
                accelerometerTextViewZ.setText(String.valueOf(first.getData()[2]));
            }
        }
    }

    class MyTimerTask extends TimerTask{
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateUI();
                }
            });
        }
    }
}
