package au.edu.swin.testApp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sparrow.R;
import eagle.Drone;

public class APIAdaptorActivity extends ActionBarActivity{

    Drone drone = new Drone();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apiadaptor);
        drone.setSDKAdaptor(this.getIntent().getStringExtra("drone"));
        initializeUI();
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
