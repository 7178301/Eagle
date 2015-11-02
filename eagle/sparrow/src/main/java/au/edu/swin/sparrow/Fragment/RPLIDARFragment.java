package au.edu.swin.sparrow.Fragment;

import android.app.Activity;
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
public class RPLIDARFragment extends SensorFragment {

    AdaptorLIDAR rplidar = null;

    private Activity activity;

    private TextView sensorOutput1DataTextView = null;
    private TextView sensorOutput2DataTextView = null;

    public static RPLIDARFragment newInstance() {
        RPLIDARFragment fragment = new RPLIDARFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public RPLIDARFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sensor_2_output, container, false);

        activity = getActivity();

        TextView sensorOutputTitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutputTitle);
        TextView sensorOutput1TitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Title);
        TextView sensorOutput2TitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutput2Title);

        sensorOutputTitleTextView.setText(getResources().getString(R.string.rplidar));
        sensorOutput1TitleTextView.setText(getResources().getString(R.string.angle_degrees_));
        sensorOutput2TitleTextView.setText(getResources().getString(R.string.distance_cm_));

        sensorOutput1DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Data);
        sensorOutput2DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput2Data);

        return view;
    }

    public void setRPLIDARAdaptor(AdaptorLIDAR Lidar) {
        this.rplidar = Lidar;
    }

    @Override
    public void updateData() {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (rplidar != null && rplidar.isConnectedToSensor() && rplidar.isDataReady()) {
                        sensorOutput1DataTextView.setText(String.valueOf(rplidar.getData()));
                        sensorOutput2DataTextView.setText(String.valueOf(rplidar.getData()));
                    } else {
                        sensorOutput1DataTextView.setText("");
                        sensorOutput2DataTextView.setText("");
                    }
                }
            });
        }
    }
}
