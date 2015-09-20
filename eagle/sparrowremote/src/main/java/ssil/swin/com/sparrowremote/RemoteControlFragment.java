package ssil.swin.com.sparrowremote;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import eagle.network.ConnectProtoBuf;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RemoteControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemoteControlFragment extends Fragment implements View.OnClickListener {
    private String serveraddr;

    private View view;

    private Button buttonUp;
    private Button buttonDown;
    private Button buttonForward;
    private Button buttonBackward;
    private Button buttonLeft;
    private Button buttonRight;
    private Button buttonRotateLeft;
    private Button buttonRotateRight;
    private Button buttonGoHome;

    private EditText editTextLongitude;
    private EditText editTextLatitude;
    private EditText editTextAltitude;
    private EditText editTextBearing;

    private OnFragmentInteractionListener mListener;

    private ConnectProtoBuf statusConnection;
    private ConnectProtoBuf commandConnection;

    private double bearingAngle;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param serveraddr Address of the server.
     * @return A new instance of fragment RemoteControlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RemoteControlFragment newInstance(String serveraddr) {
        RemoteControlFragment fragment = new RemoteControlFragment();
        Bundle args = new Bundle();
        args.putString("serveraddr", serveraddr);
        fragment.setArguments(args);
        return fragment;
    }

    public RemoteControlFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            serveraddr = getArguments().getString("serveraddr");
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        statusConnection = new ConnectProtoBuf(serveraddr);
        commandConnection = new ConnectProtoBuf(serveraddr);
        try {
            statusConnection.connectToServer();
            commandConnection.connectToServer();

            MyTimerTask myTask = new MyTimerTask();
            Timer myTimer = new Timer();
            myTimer.schedule(myTask, 1000, 100);
        } catch (ConnectProtoBuf.NotConnectedException e) {
            Toast toast = Toast.makeText(getContext(), "Failed to connect to drone", Toast.LENGTH_LONG);
            toast.show();

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_remote_control, container, false);
        buttonUp = (Button)view.findViewById(R.id.buttonUp);
        buttonDown = (Button)view.findViewById(R.id.buttonDown);
        buttonForward = (Button)view.findViewById(R.id.buttonForward);
        buttonBackward = (Button)view.findViewById(R.id.buttonBackward);
        buttonLeft = (Button)view.findViewById(R.id.buttonLeft);
        buttonRight = (Button)view.findViewById(R.id.buttonRight);
        buttonRotateLeft = (Button)view.findViewById(R.id.buttonRotateLeft);
        buttonRotateRight = (Button)view.findViewById(R.id.buttonRotateRight);
        buttonGoHome = (Button)view.findViewById(R.id.buttonGoHome);

        buttonUp.setOnClickListener(this);
        buttonDown.setOnClickListener(this);
        buttonForward.setOnClickListener(this);
        buttonBackward.setOnClickListener(this);
        buttonLeft.setOnClickListener(this);
        buttonRight.setOnClickListener(this);
        buttonRotateLeft.setOnClickListener(this);
        buttonRotateRight.setOnClickListener(this);
        buttonGoHome.setOnClickListener(this);

        editTextLongitude = (EditText)view.findViewById(R.id.editTextLongitude);
        editTextLatitude = (EditText)view.findViewById(R.id.editTextLatitude);
        editTextAltitude = (EditText)view.findViewById(R.id.editTextAltitude);
        editTextBearing = (EditText)view.findViewById(R.id.editTextBearing);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.buttonUp:
                    commandConnection.sendMessage("CHANGEALTITUDERELATIVE 1");
                    break;
                case R.id.buttonDown:
                    commandConnection.sendMessage("CHANGEALTITUDERELATIVE -1");
                    break;
                case R.id.buttonRotateLeft:
                    commandConnection.sendMessage("CHANGEYAWRELATIVE -1");
                    break;
                case R.id.buttonRotateRight:
                    commandConnection.sendMessage("CHANGEYAWRELATIVE 1");
                    break;
                case R.id.buttonLeft:
                    double longitude = -1 * Math.cos(bearingAngle * Math.PI / 180);
                    double latitude = -1 * Math.sin(bearingAngle * Math.PI / 180);
                    commandConnection.sendMessage("FLYTORELATIVE " + longitude + " " + latitude + " 0 0");
                    break;
                case R.id.buttonRight:
                    longitude = 1 * Math.cos(bearingAngle * Math.PI / 180);
                    latitude = 1 * Math.sin(bearingAngle * Math.PI / 180);
                    commandConnection.sendMessage("FLYTORELATIVE " + longitude + " " + latitude + " 0 0");
                    break;
                case R.id.buttonForward:
                    longitude = 1 * Math.sin(bearingAngle * Math.PI / 180);
                    latitude = 1 * Math.cos(bearingAngle * Math.PI / 180);
                    commandConnection.sendMessage("FLYTORELATIVE " + longitude + " " + latitude + " 0 0");
                    break;
                case R.id.buttonBackward:
                    longitude = -1 * Math.sin(bearingAngle * Math.PI / 180);
                    latitude = -1 * Math.cos(bearingAngle * Math.PI / 180);
                    commandConnection.sendMessage("FLYTORELATIVE " + longitude + " " + latitude + " 0 0");
                    break;
                case R.id.buttonGoHome:
                    commandConnection.sendMessage("GOHOME");
                    break;

            }
        } catch (ConnectProtoBuf.NotConnectedException e) {
            Toast toast = Toast.makeText(getContext(), "Accidentally disconnected from drone", Toast.LENGTH_LONG);
            toast.show();
            Uri uri = new Uri.Builder().appendPath("finish").build();
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setPosition() {
        try {
            String position = statusConnection.sendMessage("GETPOSITIONASSIGNED");
            if (position == null) {
                return;
            }
            String parts[] = position.split(" ");

            editTextLongitude.setText(parts[0]);
            editTextLatitude.setText(parts[1]);
            editTextAltitude.setText(parts[2]);
            editTextBearing.setText(parts[5]);
            bearingAngle = Double.parseDouble(parts[5]);

        }
        catch (ConnectProtoBuf.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    class MyTimerTask extends TimerTask {
        public void run() {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    setPosition();
                }
            };
            new Thread (runnable).run();
        }
    }

}
