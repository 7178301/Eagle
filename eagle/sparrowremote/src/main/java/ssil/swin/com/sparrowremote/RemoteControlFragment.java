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


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static RemoteControlFragment newInstance() {
        RemoteControlFragment fragment = new RemoteControlFragment();
        return fragment;
    }

    public RemoteControlFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_remote_control, container, false);
        buttonUp = (Button) view.findViewById(R.id.buttonUp);
        buttonDown = (Button) view.findViewById(R.id.buttonDown);
        buttonForward = (Button) view.findViewById(R.id.buttonForward);
        buttonBackward = (Button) view.findViewById(R.id.buttonBackward);
        buttonLeft = (Button) view.findViewById(R.id.buttonLeft);
        buttonRight = (Button) view.findViewById(R.id.buttonRight);
        buttonRotateLeft = (Button) view.findViewById(R.id.buttonRotateLeft);
        buttonRotateRight = (Button) view.findViewById(R.id.buttonRotateRight);
        buttonGoHome = (Button) view.findViewById(R.id.buttonGoHome);

        buttonUp.setOnClickListener(this);
        buttonDown.setOnClickListener(this);
        buttonForward.setOnClickListener(this);
        buttonBackward.setOnClickListener(this);
        buttonLeft.setOnClickListener(this);
        buttonRight.setOnClickListener(this);
        buttonRotateLeft.setOnClickListener(this);
        buttonRotateRight.setOnClickListener(this);
        buttonGoHome.setOnClickListener(this);

        editTextLongitude = (EditText) view.findViewById(R.id.editTextLongitude);
        editTextLatitude = (EditText) view.findViewById(R.id.editTextLatitude);
        editTextAltitude = (EditText) view.findViewById(R.id.editTextAltitude);
        editTextBearing = (EditText) view.findViewById(R.id.editTextBearing);
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
        Uri uri = null;
        switch (v.getId()) {
            case R.id.buttonUp:
                uri = new Uri.Builder().appendPath("buttonUp").build();
                break;
            case R.id.buttonDown:
                uri = new Uri.Builder().appendPath("buttonDown").build();
                break;
            case R.id.buttonRotateLeft:
                uri = new Uri.Builder().appendPath("buttonRotateLeft").build();
                break;
            case R.id.buttonRotateRight:
                uri = new Uri.Builder().appendPath("buttonRotateRight").build();
                break;
            case R.id.buttonLeft:
                uri = new Uri.Builder().appendPath("buttonLeft").build();
                break;
            case R.id.buttonRight:
                uri = new Uri.Builder().appendPath("buttonRight").build();
                break;
            case R.id.buttonForward:
                uri = new Uri.Builder().appendPath("buttonForward").build();
                break;
            case R.id.buttonBackward:
                uri = new Uri.Builder().appendPath("buttonBackward").build();
                break;
            case R.id.buttonGoHome:
                uri = new Uri.Builder().appendPath("buttonGoHome").build();
                break;
        }
        if (uri != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setPosition(final String lat, final String lon, final String alt, final String bear) {
        if (isAdded()) {
            getActivity().runOnUiThread(
                    new Runnable() {

                        @Override
                        public void run() {

                            editTextLongitude.setText(lat);
                            editTextLatitude.setText(lon);
                            editTextAltitude.setText(alt);
                            editTextBearing.setText(bear);
                        }
                    }
            );
        }
    }


}
