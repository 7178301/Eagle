package eagle.sdkInterface;

import eagle.Log;


/**
 * Android Logger
 * @author Cameron Cross [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 27/08/15
 * <p/>
 * Date Modified	27/08/2015 - Cameron
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
