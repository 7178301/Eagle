package eagle.sdkInterface;

import eagle.Log;
import eagle.LogCallback;


/**
 * Android Logger
 *
 * @author Cameron Cross [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 27/08/15
 * <p/>
 * Date Modified	27/08/2015 - Cameron
 */

public class LogAndroid {

    public LogAndroid() {
        Log.addVerboseCallback(new LogCallback() {
            @Override
            public void onLogEntry(String tag, String message) {
                android.util.Log.e("EagleAPI", tag + ": " + message);
            }
        });
    }
}
