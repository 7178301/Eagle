package au.edu.swin.sparrow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import au.edu.swin.sparrow.R;
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
        adaptorNameTextView.setText(drone.getAdaptor().getAdaptorName());
        final Button selectAdaptorButton = (Button) findViewById(R.id.buttonConnect);
        selectAdaptorButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                TextView adaptorNameTextView = (TextView) findViewById(R.id.textViewConnectedStatus);
                if(drone.getAdaptor().connectToDrone())
                    adaptorNameTextView.setText(getResources().getString(R.string.connected));
                else
                    adaptorNameTextView.setText(getResources().getString(R.string.not_connected));
            }
        });

    }


}
