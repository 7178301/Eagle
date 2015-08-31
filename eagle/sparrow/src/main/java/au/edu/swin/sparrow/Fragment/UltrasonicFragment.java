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

        view = inflater.inflate(R.layout.fragment_basicsensor, container, false);
        TextView title = (TextView) view.findViewById(R.id.editTextSensorName);
        title.setText("Ultrasonic");
        return view;
    }

    public void setUltrasonicAdaptor(AdaptorUltrasonic Ultrasonic) {
        this.ultrasonic = Ultrasonic;
    }

    @Override
    public void updateData() {
        if (view != null && ultrasonic.isConnectedToSensor()) {
            TextView UltrasonicTextViewX = (TextView) view.findViewById(R.id.editTextSensorData);
            UltrasonicTextViewX.setText(String.valueOf(ultrasonic.getData()));
        }
    }
}
