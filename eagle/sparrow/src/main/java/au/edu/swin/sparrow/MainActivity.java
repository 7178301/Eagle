package au.edu.swin.sparrow;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;

import eagle.Drone;
import eagle.sdkInterface.LogAndroid;
import eagle.sdkInterface.SDKAdaptor;

public class MainActivity extends ListActivity {
    Drone drone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eagle.Log.addCallback(LogAndroid.getInstance());
        initializeUI();
    }

    private static final String TAG = "Sparrow Debug";

    private void initializeUI() {
        drone = new Drone();
        TextView tv = (TextView) findViewById(R.id.textviewVersion);
        tv.setText(getResources().getString(R.string.api_version) + ": " + drone.getAPIVersion());

        @SuppressWarnings("unchecked")
        HashMap<String, SDKAdaptor> sdkAdaptors = drone.getSDKAdaptorMap();


        Log.e(TAG, "EagleAPI Version: " + drone.getAPIVersion());
        Log.e(TAG, "SDK Adaptors: " + sdkAdaptors.keySet().toString());

        ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sdkAdaptors.keySet().toArray(new String[sdkAdaptors.size()]));
        setListAdapter(ad);

        final Button selectAdaptorButton = (Button) findViewById(R.id.buttonSelectAdaptor);
        selectAdaptorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intentApiAdaptor = new Intent(MainActivity.this, APIAdaptorActivity.class).putExtra("drone", getIntent().getStringExtra("drone"));
                MainActivity.this.startActivity(intentApiAdaptor);
            }
        });
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        String selectedItem = (String) getListView().getItemAtPosition(position);
        drone.setSDKAdaptor(selectedItem);

        Button selectAdaptorButton = (Button) findViewById(R.id.buttonSelectAdaptor);

        this.getIntent().putExtra("drone", selectedItem);

        if (selectAdaptorButton.getVisibility() == View.INVISIBLE)
            makeSettingsVisible();

        TextView adaptorNameTextView = (TextView) findViewById(R.id.textViewAdaptorName);
        TextView sdkVersionTextView = (TextView) findViewById(R.id.textViewSDKVersion);
        TextView adaptorVersionTextView = (TextView) findViewById(R.id.textViewAdaptorVersion);

        adaptorNameTextView.setText(getResources().getString(R.string.adaptor_name) + ": " + drone.getSDKAdaptor().getAdaptorName());
        sdkVersionTextView.setText(getResources().getString(R.string.sdk_version) + ": " + drone.getSDKAdaptor().getSdkVersion());
        adaptorVersionTextView.setText(getResources().getString(R.string.adaptor_version) + ": " + drone.getSDKAdaptor().getAdaptorVersion());

        Log.e(TAG, "Selected Adaptor " + selectedItem);
    }

    private void makeSettingsVisible() {

        TextView adaptorNameTextView = (TextView) findViewById(R.id.textViewAdaptorName);
        TextView sdkVersionTextView = (TextView) findViewById(R.id.textViewSDKVersion);
        TextView adaptorVersionTextView = (TextView) findViewById(R.id.textViewAdaptorVersion);
        Button selectAdaptorButton = (Button) findViewById(R.id.buttonSelectAdaptor);

        adaptorNameTextView.setVisibility(View.VISIBLE);
        sdkVersionTextView.setVisibility(View.VISIBLE);
        adaptorVersionTextView.setVisibility(View.VISIBLE);
        selectAdaptorButton.setVisibility(View.VISIBLE);
    }
}
