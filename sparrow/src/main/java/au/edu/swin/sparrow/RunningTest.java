package au.edu.swin.sparrow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import au.edu.swin.sparrow.FlightTests.FlyUpFlyDown;
import eagle.Drone;
import eagle.sdkInterface.SDKAdaptor;
import eagle.sdkInterface.sdkAdaptors.DJIPhantom.*;
import eagle.sdkInterface.sdkAdaptors.Flyver.*;

public class RunningTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_test);

        Intent intent = getIntent();
        String sdk = intent.getStringExtra("sdk");
        String test = intent.getStringExtra("test");
        runTest(sdk, test);
        finish();
    }

    void runTest(String sdk, String test) {
        SDKAdaptor droneSDK = null;
        FlightTest flightTest = null;
        boolean allTests = false;
        switch (sdk) {
            case "sdkAdaptors.Flyver":
                droneSDK = new Flyver();
                break;
            case "sdkAdaptors.DJIPhantom2Vision":
                droneSDK = new DJIPhantom2Vision();
                break;
            default:
                Log.w("Sparrow", "Did not find appropriate adaptor");
                finish();
                return;
        }

        if (test.compareTo("All Tests") == 0) {
            allTests = true;
        }
        if (allTests || test.compareTo("FlyUpFlyDown") == 0) {
            flightTest = new FlyUpFlyDown(droneSDK);
            flightTest.runTest();
        }




    }


}


