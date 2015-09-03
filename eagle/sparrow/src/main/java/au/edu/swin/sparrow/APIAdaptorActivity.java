package au.edu.swin.sparrow;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import eagle.Drone;
import eagle.Log;
import eagle.sdkInterface.AndroidTelnetServer;

public class APIAdaptorActivity extends ActionBarActivity{

    Drone drone = new Drone();
    AndroidTelnetServer telnet = new AndroidTelnetServer(drone);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apiadaptor);
        drone.setSDKAdaptor(this.getIntent().getStringExtra("drone"));
        initializeUI();
        Log.addCallback(telnet);
        telnet.execute();
    }

    @Override
    protected void onDestroy() {
        Log.removeCallback(telnet);
        super.onDestroy();
    }


    private void initializeSocket() {

    }


    private void initializeUI() {
        TextView adaptorNameTextView = (TextView) findViewById(R.id.adaptorNameTextView);
        adaptorNameTextView.setText(drone.getSDKAdaptor().getAdaptorName());
        final Button selectAdaptorButton = (Button) findViewById(R.id.buttonConnect);
        selectAdaptorButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                TextView adaptorNameTextView = (TextView) findViewById(R.id.textViewConnectedStatus);
                if(drone.getSDKAdaptor().connectToDrone())
                    adaptorNameTextView.setText(getResources().getString(R.string.connected));
                else
                    adaptorNameTextView.setText(getResources().getString(R.string.not_connected));
            }
        });

    }


}
