package ssil.swin.com.sparrowremote;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class RemoteControl extends ActionBarActivity implements View.OnClickListener {

    Button buttonUp;
    Button buttonDown;
    Button buttonForward;
    Button buttonBackward;
    Button buttonLeft;
    Button buttonRight;
    Button buttonRotateLeft;
    Button buttonRotateRight;
    Button buttonGoHome;

    EditText editTextLongitude;
    EditText editTextLatitude;
    EditText editTextAltitude;
    EditText editTextBearing;

    ConnectTelnet statusConnection;
    ConnectTelnet commandConnection;

    double bearingAngle;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_control);

        buttonUp = (Button)findViewById(R.id.buttonUp);
        buttonDown = (Button)findViewById(R.id.buttonDown);
        buttonForward = (Button)findViewById(R.id.buttonForward);
        buttonBackward = (Button)findViewById(R.id.buttonBackward);
        buttonLeft = (Button)findViewById(R.id.buttonLeft);
        buttonRight = (Button)findViewById(R.id.buttonRight);
        buttonRotateLeft = (Button)findViewById(R.id.buttonRotateLeft);
        buttonRotateRight = (Button)findViewById(R.id.buttonRotateRight);
        buttonGoHome = (Button)findViewById(R.id.buttonGoHome);

        buttonUp.setOnClickListener(this);
        buttonDown.setOnClickListener(this);
        buttonForward.setOnClickListener(this);
        buttonBackward.setOnClickListener(this);
        buttonLeft.setOnClickListener(this);
        buttonRight.setOnClickListener(this);
        buttonRotateLeft.setOnClickListener(this);
        buttonRotateRight.setOnClickListener(this);
        buttonGoHome.setOnClickListener(this);

        editTextLongitude = (EditText)findViewById(R.id.editTextLongitude);
        editTextLatitude = (EditText)findViewById(R.id.editTextLatitude);
        editTextAltitude = (EditText)findViewById(R.id.editTextAltitude);
        editTextBearing = (EditText)findViewById(R.id.editTextBearing);

        String serveraddr = getIntent().getStringExtra("serveraddr");
        statusConnection = new ConnectTelnet(serveraddr);
        commandConnection = new ConnectTelnet(serveraddr);
        try {
            statusConnection.connectToServer();
            commandConnection.connectToServer();
        } catch (ConnectTelnet.NotConnectedException e) {
            Toast toast = Toast.makeText(this, "Failed to connect to drone", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        MyTimerTask myTask = new MyTimerTask();
        Timer myTimer = new Timer();
        myTimer.schedule(myTask, 1000, 100);




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
                    double longitude = -1*Math.sin(bearingAngle);
                    double latitude = -1*Math.cos(bearingAngle);
                    commandConnection.sendMessage("FLYTORELATIVE "+longitude+" "+latitude+" 0 0");
                    break;
                case R.id.buttonRight:
                    longitude = 1*Math.sin(bearingAngle);
                    latitude = 1*Math.cos(bearingAngle);
                    commandConnection.sendMessage("FLYTORELATIVE "+longitude+" "+latitude+" 0 0");
                    break;
                case R.id.buttonForward:
                    longitude = 1*Math.cos(bearingAngle);
                    latitude = 1*Math.sin(bearingAngle);
                    commandConnection.sendMessage("FLYTORELATIVE "+longitude+" "+latitude+" 0 0");
                    break;
                case R.id.buttonBackward:
                    longitude = -1*Math.cos(bearingAngle);
                    latitude = -1*Math.sin(bearingAngle);
                    commandConnection.sendMessage("FLYTORELATIVE "+longitude+" "+latitude+" 0 0");
                    break;
                case R.id.buttonGoHome:
                    commandConnection.sendMessage("GOHOME");
                    break;

            }
        } catch (ConnectTelnet.NotConnectedException e) {
            Toast toast = Toast.makeText(this, "Accidentally disconnected from drone", Toast.LENGTH_LONG);
            toast.show();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
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
        catch (ConnectTelnet.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    class MyTimerTask extends TimerTask {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setPosition();
                }
            });
        }
    }
}
