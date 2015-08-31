package au.edu.swin.sparrow.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import au.edu.swin.sparrow.R;
import eagle.sdkInterface.sensorAdaptors.AdaptorLIDAR;
import eagle.sdkInterface.sensorAdaptors.AdaptorUltrasonic;

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

        view = inflater.inflate(R.layout.fragment_basicsensor, container, false);
        TextView title = (TextView) view.findViewById(R.id.editTextSensorName);
        title.setText("LIDAR");
        return view;
    }

    public void setLIDARAdaptor(AdaptorLIDAR Lidar) {
        this.lidar = Lidar;
    }

    @Override
    public void updateData() {
        if (view != null && lidar.isConnectedToSensor()) {
            TextView UltrasonicTextViewX = (TextView) view.findViewById(R.id.editTextSensorData);
            UltrasonicTextViewX.setText(String.valueOf(lidar.getData()));
        }
    }
}
