package au.edu.swin.sparrow;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import eagle.Drone;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

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
        //fill dropdown with adaptors
        HashSet<String> sdkAdaptors = drone.getSDKAdaptorList();
        Log.e(TAG, "SDK Adaptors: " + sdkAdaptors.toString());

        Spinner sp = (Spinner)findViewById(R.id.spinnerSDKs);
        List<String> arrayOfAdaptors = new ArrayList<String>();
        arrayOfAdaptors.addAll(sdkAdaptors);

        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayOfAdaptors);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(ad);

        //fill test dropdown with tests
        List<String> arrayOfTests = new ArrayList<String>();
        arrayOfTests.add("FlyUpFlyDown");
        arrayOfTests.add("All Tests");

        Spinner sp2 = (Spinner)findViewById(R.id.spinnerTests);
        ArrayAdapter<String> ad2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayOfTests);
        ad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(ad2);




        Button bt = (Button)findViewById(R.id.buttonRunTest);
        bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Spinner sp = (Spinner)findViewById(R.id.spinnerSDKs);
        String sdk = sp.getSelectedItem().toString();

        Spinner sp2 = (Spinner)findViewById(R.id.spinnerTests);
        String test = sp2.getSelectedItem().toString();

        Intent myIntent = new Intent(this, RunningTest.class);
        myIntent.putExtra("sdk", sdk); //Optional parameters
        myIntent.putExtra("test", test); //Optional parameters

        startActivity(myIntent);



    }
}
