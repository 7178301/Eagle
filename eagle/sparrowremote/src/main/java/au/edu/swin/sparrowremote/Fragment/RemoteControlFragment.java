package au.edu.swin.sparrowremote.Fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import au.edu.swin.sparrowremote.R;


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

    private Button buttonControlConnect;
    private Button buttonControlForward;
    private Button buttonControlDisconnect;
    private Button buttonControlLeft;
    private Button buttonControlRight;
    private Button buttonControlBackward;
    private Button buttonControlRotateRight;
    private Button buttonControlRotateLeft;
    private Button buttonControlUp;
    private Button buttonControlGoHome;
    private Button buttonControlDown;

    private static EditText editTextDistanceLatitudeLongitude;
    private static EditText editTextDistanceAltitude;
    private static EditText editTextAngleYaw;

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

        buttonControlConnect = (Button) view.findViewById(R.id.buttonControlConnect);
        buttonControlForward = (Button) view.findViewById(R.id.buttonControlForward);
        buttonControlDisconnect = (Button) view.findViewById(R.id.buttonControlDisconnect);
        buttonControlLeft = (Button) view.findViewById(R.id.buttonControlLeft);
        buttonControlRight = (Button) view.findViewById(R.id.buttonControlRight);
        buttonControlBackward = (Button) view.findViewById(R.id.buttonControlBackward);
        buttonControlRotateRight = (Button) view.findViewById(R.id.buttonControlRotateRight);
        buttonControlRotateLeft = (Button) view.findViewById(R.id.buttonControlRotateLeft);
        buttonControlUp = (Button) view.findViewById(R.id.buttonControlUp);
        buttonControlDown = (Button) view.findViewById(R.id.buttonControlDown);
        buttonControlGoHome = (Button) view.findViewById(R.id.buttonControlGoHome);

        editTextAngleYaw = (EditText) view.findViewById(R.id.editTextAngleYaw);
        editTextDistanceAltitude = (EditText) view.findViewById(R.id.editTextDistanceAltitude);
        editTextDistanceLatitudeLongitude = (EditText) view.findViewById(R.id.editTextDistanceLatitudeLongitude);

        editTextAngleYaw.setText(String.valueOf(10));
        editTextDistanceAltitude.setText(String.valueOf(1));
        editTextDistanceLatitudeLongitude.setText(String.valueOf(1));

        buttonControlConnect.setOnClickListener(this);
        buttonControlForward.setOnClickListener(this);
        buttonControlDisconnect.setOnClickListener(this);
        buttonControlLeft.setOnClickListener(this);
        buttonControlRight.setOnClickListener(this);
        buttonControlBackward.setOnClickListener(this);
        buttonControlRotateRight.setOnClickListener(this);
        buttonControlRotateLeft.setOnClickListener(this);
        buttonControlUp.setOnClickListener(this);
        buttonControlGoHome.setOnClickListener(this);
        buttonControlDown.setOnClickListener(this);

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

    public String[] getEditTextStrings(){
        return new String[]{editTextDistanceLatitudeLongitude.getText().toString(),editTextDistanceAltitude.getText().toString(),editTextAngleYaw.getText().toString()};
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
            case R.id.buttonControlConnect:
                uri = new Uri.Builder().appendPath("buttonConnect").build();
                break;
            case R.id.buttonControlDisconnect:
                uri = new Uri.Builder().appendPath("buttonDisconnect").build();
                break;
            case R.id.buttonControlUp:
                uri = new Uri.Builder().appendPath("buttonUp").build();
                break;
            case R.id.buttonControlDown:
                uri = new Uri.Builder().appendPath("buttonDown").build();
                break;
            case R.id.buttonControlRotateLeft:
                uri = new Uri.Builder().appendPath("buttonRotateLeft").build();
                break;
            case R.id.buttonControlRotateRight:
                uri = new Uri.Builder().appendPath("buttonRotateRight").build();
                break;
            case R.id.buttonControlLeft:
                uri = new Uri.Builder().appendPath("buttonLeft").build();
                break;
            case R.id.buttonControlRight:
                uri = new Uri.Builder().appendPath("buttonRight").build();
                break;
            case R.id.buttonControlForward:
                uri = new Uri.Builder().appendPath("buttonForward").build();
                break;
            case R.id.buttonControlBackward:
                uri = new Uri.Builder().appendPath("buttonBackward").build();
                break;
            case R.id.buttonControlGoHome:
                uri = new Uri.Builder().appendPath("buttonGoHome").build();
                break;
        }
        if (uri != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setPosition(final String latitude, final String longitude, final String altitude, final String bearing) {
        if (isAdded()) {
            getActivity().runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            editTextLatitude.setText(latitude);
                            editTextLongitude.setText(longitude);
                            editTextAltitude.setText(altitude);
                            editTextBearing.setText(bearing);
                        }
                    }
            );
        }
    }


}
