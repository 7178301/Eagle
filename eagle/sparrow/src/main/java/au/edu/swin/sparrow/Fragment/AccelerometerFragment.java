package au.edu.swin.sparrow.Fragment;

import android.app.Activity;
import android.net.Uri;
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

        view = inflater.inflate(R.layout.fragment_accelerometer, container, false);
        return view;
    }

    public void setAccelerometerAdaptor(AdaptorAccelerometer accelerometer) {
        this.accelerometer = accelerometer;
    }

    public void updateData() {
        if (view != null && accelerometer.isConnectedToSensor()) {
            TextView accelerometerTextViewX = (TextView) view.findViewById(R.id.editTextAccelerometerDataX);
            TextView accelerometerTextViewY = (TextView) view.findViewById(R.id.editTextAccelerometerDataY);
            TextView accelerometerTextViewZ = (TextView) view.findViewById(R.id.editTextAccelerometerDataZ);
            accelerometerTextViewX.setText(String.valueOf(accelerometer.getData()[0]));
            accelerometerTextViewY.setText(String.valueOf(accelerometer.getData()[1]));
            accelerometerTextViewZ.setText(String.valueOf(accelerometer.getData()[2]));
        }
    }
}
