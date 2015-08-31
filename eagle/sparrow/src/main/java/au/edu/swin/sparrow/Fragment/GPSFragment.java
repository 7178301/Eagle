package au.edu.swin.sparrow.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import au.edu.swin.sparrow.R;
import eagle.sdkInterface.sensorAdaptors.AdaptorGPS;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GPSFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GPSFragment extends SensorFragment {

    AdaptorGPS gps = null;

    public static GPSFragment newInstance() {
        GPSFragment fragment = new GPSFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public GPSFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sensor_5_output, container, false);

        TextView sensorOutputTitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutputTitle);
        TextView sensorOutput1TitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Title);
        TextView sensorOutput2TitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutput2Title);
        TextView sensorOutput3TitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutput3Title);
        TextView sensorOutput4TitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutput4Title);
        TextView sensorOutput5TitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutput5Title);
        sensorOutputTitleTextView.setText(getResources().getString(R.string.gps));
        sensorOutput1TitleTextView.setText(getResources().getString(R.string.longatude));
        sensorOutput2TitleTextView.setText(getResources().getString(R.string.latitude));
        sensorOutput3TitleTextView.setText(getResources().getString(R.string.altitude));
        sensorOutput4TitleTextView.setText(getResources().getString(R.string.bearing));
        sensorOutput5TitleTextView.setText(getResources().getString(R.string.accuracy));

        return view;
    }

    public void setGPSAdaptor(AdaptorGPS gps) {
        this.gps = gps;
    }

    public void updateData() {
        if (view != null) {
            TextView sensorOutput1DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput1Data);
            TextView sensorOutput2DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput2Data);
            TextView sensorOutput3DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput3Data);
            TextView sensorOutput4DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput4Data);
            TextView sensorOutput5DataTextView = (TextView) view.findViewById(R.id.textViewSensorOutput5Data);

            if (gps.isConnectedToSensor() && gps.isDataReady()) {
                sensorOutput1DataTextView.setText(String.valueOf(gps.getData().getLongitude()));
                sensorOutput2DataTextView.setText(String.valueOf(gps.getData().getLatitude()));
                sensorOutput3DataTextView.setText(String.valueOf(gps.getData().getAltitude()));
                sensorOutput4DataTextView.setText(String.valueOf(gps.getData().getYaw().getDegrees()));
                sensorOutput5DataTextView.setText(String.valueOf(gps.getGPSAccuracy()));
            } else {
                sensorOutput1DataTextView.setText("");
                sensorOutput2DataTextView.setText("");
                sensorOutput3DataTextView.setText("");
                sensorOutput4DataTextView.setText("");
                sensorOutput5DataTextView.setText("");
            }
        }
    }
}
