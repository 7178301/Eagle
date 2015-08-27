package eagle.sdkInterface;

import eagle.Log;

/**
 * Created by cameron on 8/27/15.
 */
public class LogAndroid implements Log.LogCallback {
    private static LogAndroid ourInstance = new LogAndroid();

    public static LogAndroid getInstance() {
        return ourInstance;
    }

    private LogAndroid() {
    }

    @Override
    public void handleMessage(String message) {
        android.util.Log.i("Eagle", message);
    }
}
