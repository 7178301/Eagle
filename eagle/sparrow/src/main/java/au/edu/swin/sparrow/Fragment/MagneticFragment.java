package au.edu.swin.sparrow.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import au.edu.swin.sparrow.R;
import eagle.sdkInterface.sensorAdaptors.AdaptorMagnetic;

/**
 * Created by cameron on 8/29/15.
 */
public class MagneticFragment extends SensorFragment {

    AdaptorMagnetic magnetic = null;

    public static MagneticFragment newInstance() {
        MagneticFragment fragment = new MagneticFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MagneticFragment() {
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
        sensorOutputTitleTextView.setText(getResources().getString(R.string.magnetic));
        sensorOutput1TitleTextView.setText(getResources().getString(R.string.x_axis_));
        sensorOutput2TitleTextView.setText(getResources().getString(R.string.y_axis_));
        sensorOutput3TitleTextView.setText(getResources().getString(R.string.z_axis_));

        return view;
    }

    public void setMagneticAdaptor(AdaptorMagnetic magnetic) {
        this.magnetic = magnetic;
    }

    @Override
    public void updateData() {
        if (view != null) {
            TextView sensorOutput1DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Data);
            TextView sensorOutput2DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput2Data);
            TextView sensorOutput3DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput3Data);
            if (magnetic.isConnectedToSensor()) {
                sensorOutput1DataTextView.setText(String.valueOf(magnetic.getData()[0]));
                sensorOutput2DataTextView.setText(String.valueOf(magnetic.getData()[1]));
                sensorOutput3DataTextView.setText(String.valueOf(magnetic.getData()[2]));
            } else {
                sensorOutput1DataTextView.setText("");
                sensorOutput2DataTextView.setText("");
                sensorOutput3DataTextView.setText("");
            }
        }
    }
}
