package au.edu.swin.sparrow;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import android.util.Log;
import android.widget.TextView;

import java.util.Iterator;

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

        Iterator it = drone.getSDKAdaptorList().iterator();
        Log.e(TAG, "Found SDK Adaptors: " + it.hasNext());
        String SDKs = new String();
        while(it.hasNext()){
            SDKs+=it.next()+"\n";
        }
        Log.e(TAG, "SDK Adaptors: " + SDKs);

        TextView defaultTextView = (TextView) findViewById(R.id.defaultTextView);
        defaultTextView.setText(SDKs);
    }

    /*
    Lara's initial DJI connection code

    public void start(){
        DJIDrone drone=new DJIDrone();
        drone.initWithType(this.getApplicationContext(), DJIDroneTypeDef.DJIDroneType.DJIDrone_Vision);

        try {
            drone.checkPermission(this.getApplicationContext(), new DJIGerneralListener() {
                @Override
                public void onGetPermissionResult(int result) {
                    // TODO Auto-generated method stub
                    Log.e(TAG, "onGetPermissionResult = " + result);
                    Log.e(TAG, "onGetPermissionResultDescription = "+ DJIError.getCheckPermissionErrorDescription(result));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "************\n\n"+e+"\n\n");
        }
        try{
            //drone.initWithType(this.getApplicationContext(), DJIDroneTypeDef.DJIDroneType.DJIDrone_Vision);
            drone.connectToDrone();
        }catch(Error err){
            Log.d(TAG,"************\n\n"+ err.toString()+"\n\n");
        }

    }
    */
}
