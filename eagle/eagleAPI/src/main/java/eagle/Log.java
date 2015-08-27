package eagle;

import java.util.Vector;

/**
 * Created by cameron on 8/27/15.
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
