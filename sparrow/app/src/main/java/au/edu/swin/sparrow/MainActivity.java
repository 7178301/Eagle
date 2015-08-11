package au.edu.swin.sparrow;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import dji.sdk.api.DJIDrone;
import dji.sdk.api.DJIDroneTypeDef;
import dji.sdk.api.DJIError;
import dji.sdk.interfaces.DJIGerneralListener;
import eagle.Drone;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static final String TAG = "Sparrow Debug";

    private void initializeUI(){
        Drone drone = new Drone();
        Log.e(TAG, "EagleAPI Version: " + drone.getAPIVersion());
        TextView tv = (TextView)findViewById(R.id.textviewVersion);
        tv.setText(drone.getAPIVersion());

        HashSet<String> sdkAdaptors = drone.getSDKAdaptorList();
        Log.e(TAG, "SDK Adaptors: " + sdkAdaptors.toString());

        Spinner sp = (Spinner)findViewById(R.id.spinnerSDKs);
        List<String> arrayOfAdaptors = new ArrayList<String>();
        arrayOfAdaptors.addAll(sdkAdaptors);

        sp.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayOfAdaptors));
    }
}
