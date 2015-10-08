package au.edu.swin.sparrow.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import au.edu.swin.sparrow.R;
import eagle.sdkInterface.sensorAdaptors.AdaptorBearing;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorCallback;

/**
 * Created by cameron on 8/29/15.
 */
public class BearingFragment extends SensorFragment {

    AdaptorBearing adaptorBearing = null;
    private TextView sensorOutput3DataTextView;
    private TextView sensorOutput2DataTextView;
    private TextView sensorOutput1DataTextView;

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

        view = inflater.inflate(R.layout.fragment_sensor_3_output, container, false);

        TextView sensorOutputTitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutputTitle);
        TextView sensorOutput1TitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Title);
        TextView sensorOutput2TitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutput2Title);
        TextView sensorOutput3TitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutput3Title);

        sensorOutput1DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Data);
        sensorOutput2DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput2Data);
        sensorOutput3DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput3Data);
        sensorOutputTitleTextView.setText(getResources().getString(R.string.bearing));
        sensorOutput1TitleTextView.setText(R.string.bearing);
        sensorOutput2TitleTextView.setText(R.string.pitch);
        sensorOutput3TitleTextView.setText(R.string.roll);
        return view;
    }

    public void setBearingAdaptor(AdaptorBearing adaptorBearing) {
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
            if (adaptorBearing.isConnectedToSensor()) {
                sensorOutput1DataTextView.setText(String.valueOf(adaptorBearing.getData()[0]));
                sensorOutput2DataTextView.setText(String.valueOf(adaptorBearing.getData()[1]));
                sensorOutput3DataTextView.setText(String.valueOf(adaptorBearing.getData()[2]));
            } else {
                sensorOutput1DataTextView.setText("");
                sensorOutput2DataTextView.setText("");
                sensorOutput3DataTextView.setText("");
            }
        }
    }
}
