package eagle.sdkInterface;

import android.os.AsyncTask;

import eagle.Drone;
import eagle.Log;
import eagle.TelnetServer;

/**
 * Created by cameron on 8/27/15.
 */
public class AndroidTelnetServer extends AsyncTask implements Log.LogCallback {

    Drone drone;
    TelnetServer ts;


    public AndroidTelnetServer(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void handleMessage(String s) {
        if (ts != null) {
            ts.handleMessage(s);
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        TelnetServer ts = new TelnetServer(drone);
        ts.run();
        return null;
    }
}
