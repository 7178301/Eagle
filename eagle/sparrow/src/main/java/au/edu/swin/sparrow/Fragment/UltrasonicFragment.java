package au.edu.swin.sparrow.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import au.edu.swin.sparrow.R;
import eagle.sdkInterface.sensorAdaptors.AdaptorUltrasonic;

/**
 * Created by cameron on 8/29/15.
 */
public class UltrasonicFragment extends SensorFragment {

    AdaptorUltrasonic ultrasonic = null;

    public static UltrasonicFragment newInstance() {
        UltrasonicFragment fragment = new UltrasonicFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public UltrasonicFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_sensor_1_output, container, false);

        TextView sensorOutputTitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutputTitle);
        TextView sensorOutput1TitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Title);
        sensorOutputTitleTextView.setText(getResources().getString(R.string.ultrasonic));
        sensorOutput1TitleTextView.setText(getResources().getString(R.string.distance_cm_));

        return view;
    }

    public void setUltrasonicAdaptor(AdaptorUltrasonic Ultrasonic) {
        this.ultrasonic = Ultrasonic;
    }

    @Override
    public void updateData() {
        if (view != null) {
            TextView sensorOutput1DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Data);
            if (ultrasonic.isConnectedToSensor())
                sensorOutput1DataTextView.setText(String.valueOf(ultrasonic.getData()));
            else
                sensorOutput1DataTextView.setText("");
        }
    }
}
