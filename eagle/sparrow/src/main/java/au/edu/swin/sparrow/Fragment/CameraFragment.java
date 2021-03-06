package au.edu.swin.sparrow.Fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import au.edu.swin.sparrow.R;
import dji.sdk.widget.DjiGLSurfaceView;
import eagle.Log;
import eagle.sdkInterface.SDKAdaptorCallback;
import eagle.sdkInterface.sensorAdaptors.AdaptorCamera;
import eagle.sdkInterface.sensorAdaptors.cameraAdaptors.DJICamera;
import eagle.sdkInterface.sensorAdaptors.sensorAdaptorCallbacks.SensorAdaptorCallback;
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

    private AdaptorCamera camera = null;

    private Button connectCameraButton = null;
    private Button upButton = null;
    private Button downButton = null;
    private Button captureButton = null;
    private Button startRecordButton = null;
    private Button stopRecordButton = null;

    private TextView sensorCameraAttitudeX = null;
    private TextView sensorCameraAttitudeXData = null;
    private TextView sensorCameraAttitudeY = null;
    private TextView sensorCameraAttitudeYData = null;
    private TextView sensorCameraAttitudeZ = null;
    private TextView sensorCameraAttitudeZData = null;

    private DjiGLSurfaceView djiGLSurfaceView = null;
    private SurfaceView surfaceView = null;
    private SensorAdaptorCameraLiveFeedCallback sensorAdaptorCameraLiveFeedCallback = null;

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
    public void onDestroy() {
        if (camera != null) {
            if(sensorAdaptorCameraLiveFeedCallback!=null)
                camera.removeSensorAdaptorCameraLiveFeedbackCallback(sensorAdaptorCameraLiveFeedCallback);
            if (camera instanceof DJICamera)
                djiGLSurfaceView.destroy();
            camera.disconnectFromSensor();
        }
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sensor_camera, container, false);

        activity = getActivity();

        TextView sensorOutputTitleTextView = (TextView) view.findViewById(R.id.textViewSensorOutputTitle);
        sensorOutputTitleTextView.setText(getResources().getString(R.string.camera));

        connectCameraButton = (Button) view.findViewById(R.id.buttonCameraConnect);
        upButton = (Button) view.findViewById(R.id.buttonCameraUp);
        downButton = (Button) view.findViewById(R.id.buttonCameraDown);
        captureButton = (Button) view.findViewById(R.id.buttonCapture);
        startRecordButton = (Button) view.findViewById(R.id.buttonStartRecord);
        stopRecordButton = (Button) view.findViewById(R.id.buttonStopRecord);

        sensorCameraAttitudeX = (TextView) view.findViewById(R.id.textViewSensorCameraAttitudeX);
        sensorCameraAttitudeXData = (TextView) view.findViewById(R.id.textViewSensorCameraAttitudeXData);
        sensorCameraAttitudeY = (TextView) view.findViewById(R.id.textViewSensorCameraAttitudeY);
        sensorCameraAttitudeYData = (TextView) view.findViewById(R.id.textViewSensorCameraAttitudeYData);
        sensorCameraAttitudeZ = (TextView) view.findViewById(R.id.textViewSensorCameraAttitudeZ);
        sensorCameraAttitudeZData = (TextView) view.findViewById(R.id.textViewSensorCameraAttitudeZData);

        djiGLSurfaceView = (DjiGLSurfaceView) this.view.findViewById(R.id.djiSurfaceView);
        surfaceView = (SurfaceView) this.view.findViewById(R.id.surfaceView);

        if (camera instanceof DJICamera) {
            sensorAdaptorCameraLiveFeedCallback = new SensorAdaptorCameraLiveFeedCallback() {
                @Override
                public void onSensorEvent(final byte[] getData, final int getSize) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            djiGLSurfaceView.setDataToDecoder(getData, getSize);
                        }
                    });
                }
            };
            camera.addSensorAdaptorCameraLiveFeedallback(sensorAdaptorCameraLiveFeedCallback);
            djiGLSurfaceView.start();
        }

        if (camera != null) {
            if (camera.isConnectedToSensor())
                displayCameraHUD();
            else if (camera.connectToSensor())
                displayCameraHUD();
        }

        connectCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (camera != null && !camera.isConnectedToSensor() && camera.connectToSensor()) {
                    displayCameraHUD();
                    Log.log("APIAdaptorActivity", "Connect To Camera SUCCESS");
                } else if (camera != null && camera.isConnectedToSensor()) {
                    displayCameraHUD();
                    Log.log("CameraFragment", "Already Connected To Camera");
                } else
                    Log.log("CameraFragment", "Connect To Camera FAIL");

            }
        });

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera != null && camera.isConnectedToSensor())
                    camera.takePicture(new SDKAdaptorCallback() {
                        @Override
                        public void onResult(boolean booleanResult, String stringResult) {
                        }
                    });
            }
        });

        startRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera != null && camera.isConnectedToSensor())
                    camera.startRecord(new SDKAdaptorCallback() {
                        @Override
                        public void onResult(boolean booleanResult, String stringResult) {
                        }
                    });
            }
        });

        stopRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera != null && camera.isConnectedToSensor())
                    camera.stopRecord(new SDKAdaptorCallback() {
                        @Override
                        public void onResult(boolean booleanResult, String stringResult) {
                        }
                    });
            }
        });

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera != null && camera.isConnectedToSensor())
                    camera.updateCameraAttitude(new SDKAdaptorCallback() {
                        @Override
                        public void onResult(boolean booleanResult, String stringResult) {
                        }
                    }, 0, camera.getCameraAttitude()[1] - 200, 0);
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera != null && camera.isConnectedToSensor())
                    camera.updateCameraAttitude(new SDKAdaptorCallback() {
                        @Override
                        public void onResult(boolean booleanResult, String stringResult) {
                        }
                    }, 0, camera.getCameraAttitude()[1] + 200, 0);
            }
        });

        return view;
    }

    public void setCameraAdaptor(final AdaptorCamera camera) {
        this.camera = camera;
        camera.addSensorAdaptorCallback(new SensorAdaptorCallback() {
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
                    if (camera != null && camera.isConnectedToSensor() && camera.getCameraAttitude()!=null) {
                        sensorCameraAttitudeXData.setText(String.valueOf(camera.getCameraAttitude()[0]));
                        sensorCameraAttitudeYData.setText(String.valueOf(camera.getCameraAttitude()[1]));
                        sensorCameraAttitudeZData.setText(String.valueOf(camera.getCameraAttitude()[2]));
                    } else {
                        sensorCameraAttitudeXData.setText("");
                        sensorCameraAttitudeYData.setText("");
                        sensorCameraAttitudeZData.setText("");
                    }
                }
            });
        }
    }

    private void displayCameraHUD() {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int screenWidth = size.x;
        final int screenHeight = size.y;
        int surfaceViewWidthHeight = 0;
        if (screenWidth < screenHeight)
            surfaceViewWidthHeight = (int) (screenWidth - (screenWidth * 0.2));
        else
            surfaceViewWidthHeight = (int) (screenHeight - (screenHeight * 0.2));
        //Set The Surface View

        if (camera instanceof DJICamera) {
            djiGLSurfaceView.setVisibility(View.VISIBLE);
            djiGLSurfaceView.getHolder().setFixedSize(surfaceViewWidthHeight, surfaceViewWidthHeight);
            djiGLSurfaceView.start();
        } else {
            surfaceView.setVisibility(View.VISIBLE);
            surfaceView.getHolder().setFixedSize(surfaceViewWidthHeight, surfaceViewWidthHeight);
        }

        //Set Adaptor Specific Buttons
        if (camera instanceof DJICamera) {
            captureButton.setVisibility(View.VISIBLE);
            downButton.setVisibility(View.VISIBLE);
            upButton.setVisibility(View.VISIBLE);
            startRecordButton.setVisibility(View.VISIBLE);
            stopRecordButton.setVisibility(View.VISIBLE);
            sensorCameraAttitudeX.setVisibility(View.VISIBLE);
            sensorCameraAttitudeXData.setVisibility(View.VISIBLE);
            sensorCameraAttitudeY.setVisibility(View.VISIBLE);
            sensorCameraAttitudeYData.setVisibility(View.VISIBLE);
            sensorCameraAttitudeZ.setVisibility(View.VISIBLE);
            sensorCameraAttitudeZData.setVisibility(View.VISIBLE);
        }
    }
}
