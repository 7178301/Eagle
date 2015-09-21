package eagle;

import java.util.Vector;

/**
 * Java Logger
 *
 * @author Cameron Cross [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 8/13/15
 * <p/>
 * Date Modified	27/08/2015 - Cameron
 */
public class Log {
    static Vector<LogCallback> callbacks = new Vector<LogCallback>();

    public interface LogCallback {
        public void handleMessage(String message);
    }

    public static void log(String message) {
        for (LogCallback callback : callbacks) {
            callback.handleMessage(message);
        }
    }

    public static void addCallback(LogCallback callback) {
        callbacks.add(callback);
    }

    public static void removeCallback(LogCallback callback) {
        callbacks.remove(callback);
    }
}
