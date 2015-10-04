package au.edu.swin.sparrow.Fragment;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import au.edu.swin.sparrow.R;
import eagle.sdkInterface.sensorAdaptors.AdaptorBearing;
import eagle.sdkInterface.sensorAdaptors.SensorAdaptorCallback;

/**
 * Created by cameron on 8/29/15.
 */
public class BearingFragment extends SensorFragment {

    AdaptorBearing adaptorBearing = null;

    public static BearingFragment newInstance() {
        BearingFragment fragment = new BearingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BearingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_sensor_1_output, container, false);

        TextView sensorOutputTitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutputTitle);
        TextView sensorOutput1TitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Title);
        sensorOutputTitleTextView.setText(getResources().getString(R.string.bearing));
        sensorOutput1TitleTextView.setText(getResources().getString(R.string.bearing));
        return view;
    }

    public void setMagneticAccelerometerAdaptors(AdaptorBearing adaptorBearing) {
        this.adaptorBearing = adaptorBearing;
        this.adaptorBearing.addSensorAdaptorCallback(new SensorAdaptorCallback() {
            @Override
            public void onSensorChanged() {
                updateData();
            }
        });
    }

    @Override
    public void updateData() {
        if (view != null) {
            TextView sensorOutput1DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Data);
            if (adaptorBearing.isConnectedToSensor()&&adaptorBearing.isDataReady())
                sensorOutput1DataTextView.setText(String.valueOf(adaptorBearing.getData()));
            else
                sensorOutput1DataTextView.setText("");
        }
    }
}
