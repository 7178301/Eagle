package au.edu.swin.sparrow.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import au.edu.swin.sparrow.R;
import eagle.sdkInterface.sensorAdaptors.AdaptorBarometer;

/**
 * Created by cameron on 8/29/15.
 */
public class BarometerFragment extends SensorFragment {

    AdaptorBarometer barometer = null;

    private Activity activity;

    private TextView sensorOutput1DataTextView = null;

    public static BarometerFragment newInstance() {
        BarometerFragment fragment = new BarometerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BarometerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_sensor_1_output, container, false);

        activity = getActivity();

        TextView sensorOutputTitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutputTitle);
        TextView sensorOutput1TitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Title);

        sensorOutputTitleTextView.setText(R.string.barometer);
        sensorOutput1TitleTextView.setText(R.string.pressure);

        sensorOutput1DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Data);

        return view;
    }

    public void setBarometerAdaptor(AdaptorBarometer barometer) {
        this.barometer = barometer;
    }

    @Override
    public void updateData() {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (barometer != null && barometer.isConnectedToSensor() && barometer.isDataReady())
                        sensorOutput1DataTextView.setText(String.valueOf(barometer.getData()));
                    else
                        sensorOutput1DataTextView.setText("");
                }
            });
        }
    }
}
