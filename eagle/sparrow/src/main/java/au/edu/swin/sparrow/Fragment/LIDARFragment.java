package au.edu.swin.sparrow.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import au.edu.swin.sparrow.R;
import eagle.sdkInterface.sensorAdaptors.AdaptorLIDAR;

/**
 * Created by cameron on 8/29/15.
 */
public class LIDARFragment extends SensorFragment {

    AdaptorLIDAR lidar = null;

    public static LIDARFragment newInstance() {
        LIDARFragment fragment = new LIDARFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LIDARFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sensor_1_output, container, false);

        TextView sensorOutputTitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutputTitle);
        TextView sensorOutput1TitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Title);
        sensorOutputTitleTextView.setText(getResources().getString(R.string.lidar));
        sensorOutput1TitleTextView.setText(getResources().getString(R.string.distance_cm_));

        return view;
    }

    public void setLIDARAdaptor(AdaptorLIDAR Lidar) {
        this.lidar = Lidar;
    }

    @Override
    public void updateData() {
        if (view != null) {
            TextView sensorOutput1DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Data);
            if (lidar.isConnectedToSensor()) {
                sensorOutput1DataTextView.setText(String.valueOf(lidar.getData()));
            } else {
                sensorOutput1DataTextView.setText("");
            }
        }
    }
}
