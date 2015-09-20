package au.edu.swin.sparrow.Fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import au.edu.swin.sparrow.R;
import eagle.sdkInterface.sensorAdaptors.AdaptorAccelerometer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccelerometerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccelerometerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccelerometerFragment extends SensorFragment {

    AdaptorAccelerometer accelerometer = null;

    public static AccelerometerFragment newInstance() {
        AccelerometerFragment fragment = new AccelerometerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AccelerometerFragment() {
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
        sensorOutputTitleTextView.setText(getResources().getString(R.string.accelerometer));
        sensorOutput1TitleTextView.setText(getResources().getString(R.string.x_axis_));
        sensorOutput2TitleTextView.setText(getResources().getString(R.string.y_axis_));
        sensorOutput3TitleTextView.setText(getResources().getString(R.string.z_axis_));

        return view;
    }

    public void setAccelerometerAdaptor(AdaptorAccelerometer accelerometer) {
        this.accelerometer = accelerometer;
    }

    public void updateData() {
        if (view != null) {
            TextView sensorOutput1DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Data);
            TextView sensorOutput2DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput2Data);
            TextView sensorOutput3DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput3Data);
            if (accelerometer.isConnectedToSensor()) {
                sensorOutput1DataTextView.setText(String.valueOf(accelerometer.getData()[0]));
                sensorOutput2DataTextView.setText(String.valueOf(accelerometer.getData()[1]));
                sensorOutput3DataTextView.setText(String.valueOf(accelerometer.getData()[2]));
            } else {
                sensorOutput1DataTextView.setText("");
                sensorOutput2DataTextView.setText("");
                sensorOutput3DataTextView.setText("");
            }
        }
    }
}
