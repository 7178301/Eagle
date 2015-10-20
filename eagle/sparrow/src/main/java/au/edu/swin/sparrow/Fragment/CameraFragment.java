package au.edu.swin.sparrow.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import au.edu.swin.sparrow.R;
import dji.sdk.widget.DjiGLSurfaceView;
import eagle.Log;
import eagle.sdkInterface.SDKAdaptorCallback;
import eagle.sdkInterface.sensorAdaptors.AdaptorCamera;
import eagle.sdkInterface.sensorAdaptors.cameraAdaptors.DJICamera;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorCameraLiveFeedCallback;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends SensorFragment {

    AdaptorCamera camera = null;

    Button connectCameraButton = null;
    Button upButton = null;
    Button downButton = null;
    Button captureButton = null;

    private DjiGLSurfaceView djiGLSurfaceView = null;
    private Activity activity;

    public static CameraFragment newInstance() {
        CameraFragment cameraFragment = new CameraFragment();
        Bundle args = new Bundle();
        cameraFragment.setArguments(args);
        return cameraFragment;
    }

    public CameraFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sensor_camera, container, false);

        activity=getActivity();

        TextView sensorOutputTitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutputTitle);
        sensorOutputTitleTextView.setText(getResources().getString(R.string.camera));

        if (camera instanceof DJICamera) {
            upButton = (Button) view.findViewById(R.id.buttonCameraUp);
            upButton.setVisibility(View.VISIBLE);
            upButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (camera.isConnectedToSensor())
                        camera.updateCameraAttitude(new SDKAdaptorCallback() {
                            @Override
                            public void onResult(boolean booleanResult, String stringResult) {

                            }
                        }, 0, camera.getCameraAttitude()[1] - 100, 0);
                }
            });
            downButton = (Button) view.findViewById(R.id.buttonCameraDown);
            downButton.setVisibility(View.VISIBLE);
            downButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (camera.isConnectedToSensor())

                        camera.updateCameraAttitude(new SDKAdaptorCallback() {
                            @Override
                            public void onResult(boolean booleanResult, String stringResult) {

                            }
                        }, 0, camera.getCameraAttitude()[1] + 100, 0);
                }
            });
        }

        djiGLSurfaceView = (DjiGLSurfaceView) this.view.findViewById(R.id.djiSurfaceView);
        connectCameraButton = (Button) view.findViewById(R.id.buttonCameraConnect);
        connectCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (camera instanceof DJICamera) {
                    if (camera.connectToSensor()) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                djiGLSurfaceView.setVisibility(View.VISIBLE);
                                camera.addSensorAdaptorCameraLiveFeedallback(new SensorAdaptorCameraLiveFeedCallback() {
                                    @Override
                                    public void onSensorEvent(final byte[] getData, final int getSize) {
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                djiGLSurfaceView.setDataToDecoder(getData, getSize);
                                            }
                                        });
                                    }
                                });
                                djiGLSurfaceView.start();
                            }
                        });
                        Log.log("APIAdaptorActivity", "Connect To Camera SUCCESS");
                    } else
                        Log.log("CameraFragment", "Connect To Camera FAIL");
                }
            }
        });

        captureButton = (Button) view.findViewById(R.id.buttonCapture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera.isConnectedToSensor())
                    camera.takePicture(new SDKAdaptorCallback() {
                        @Override
                        public void onResult(boolean booleanResult, String stringResult) {
                        }
                    });
            }
        });


        return view;
    }

    public void setCameraAdaptor(final AdaptorCamera camera) {
        this.camera = camera;
    }

    public void updateData() {
    }
}
