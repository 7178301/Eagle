package au.edu.swin.sparrow.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import au.edu.swin.sparrow.R;
import eagle.sdkInterface.sensorAdaptors.AdaptorAccelerometer;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorCallback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccelerometerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccelerometerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccelerometerFragment extends SensorFragment {

    private AdaptorAccelerometer accelerometer = null;

    private Activity activity = null;

    private TextView sensorOutput1DataTextView = null;
    private TextView sensorOutput2DataTextView = null;
    private TextView sensorOutput3DataTextView = null;

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

        activity = getActivity();


        TextView sensorOutputTitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutputTitle);
        TextView sensorOutput1TitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Title);
        TextView sensorOutput2TitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutput2Title);
        TextView sensorOutput3TitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutput3Title);

        sensorOutputTitleTextView.setText(getResources().getString(R.string.accelerometer));
        sensorOutput1TitleTextView.setText(getResources().getString(R.string.x_axis_));
        sensorOutput2TitleTextView.setText(getResources().getString(R.string.y_axis_));
        sensorOutput3TitleTextView.setText(getResources().getString(R.string.z_axis_));

        sensorOutput1DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Data);
        sensorOutput2DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput2Data);
        sensorOutput3DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput3Data);

        return view;
    }

    public void setAccelerometerAdaptor(AdaptorAccelerometer accelerometer) {
        this.accelerometer = accelerometer;
        accelerometer.addSensorAdaptorCallback(new SensorAdaptorCallback() {
            @Override
            public void onSensorChanged() {
                updateData();
            }
        });
    }

    public void updateData() {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (accelerometer != null && accelerometer.isConnectedToSensor() && accelerometer.isDataReady()) {
                        sensorOutput1DataTextView.setText(String.valueOf(accelerometer.getData()[0]));
                        sensorOutput2DataTextView.setText(String.valueOf(accelerometer.getData()[1]));
                        sensorOutput3DataTextView.setText(String.valueOf(accelerometer.getData()[2]));
                    } else {
                        sensorOutput1DataTextView.setText("");
                        sensorOutput2DataTextView.setText("");
                        sensorOutput3DataTextView.setText("");
                    }
                }
            });
        }
    }
}
