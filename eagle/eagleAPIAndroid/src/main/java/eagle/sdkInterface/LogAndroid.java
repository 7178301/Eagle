package eagle.sdkInterface;

import eagle.Log;

/**
 * Adaptor Loader
 *
 * @author Cameron Cross [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 8/13/15
 * <p/>
 * Date Modified	8/13/15 - Nicholas
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
