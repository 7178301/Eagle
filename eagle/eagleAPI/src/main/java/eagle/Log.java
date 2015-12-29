package eagle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Java Logger
 *
 * @author Cameron Cross
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @author Cameron Cross
 * @version 0.0.1
 * @since 8/13/15
 * <p/>
 * Date Modified	02/10/15 - Nicholas
 */
public class Log {

    private static List<String> log = new ArrayList<>();
    private static HashMap<String, Vector<LogCallback>> logCallBack = new HashMap<>();
    private static Vector<LogCallback> verboseLogCallback = new Vector<>();
    public static int logLimit = 5000;


    public synchronized static void log(String tag, String message) {
        if (tag != null && message != null) {
            while (log.size() >= logLimit) {
                log.remove(0);
            }
            log.add(tag + ": " + message);
            for (LogCallback vLogCallback : verboseLogCallback)
                vLogCallback.onLogEntry(tag, message);
            if (logCallBack.containsKey(tag)) {
                for (LogCallback logcallback : logCallBack.get(tag))
                    logcallback.onLogEntry(tag, message);
            }
        }
    }

    public synchronized static void addCallback(String tag, LogCallback logCallback) {
        if (logCallBack.containsKey(tag))
            logCallBack.get(tag).add(logCallback);
        else if (!logCallBack.containsKey(tag)) {
            Vector<LogCallback> initial = new Vector<>();
            initial.add(logCallback);
            logCallBack.put(tag, initial);
        }
        Log.log("LogCallback", "NEW CALLBACK ADDED TO " + tag);
    }

    public synchronized static void addVerboseCallback(LogCallback logCallback) {
        verboseLogCallback.add(logCallback);
    }

    public synchronized static void removeCallback(String tag, LogCallback logCallback) {
        if (logCallBack.containsKey(tag)) {
            logCallBack.get(tag).remove(logCallback);
            Log.log("LogCallback", "CALLBACK REMOVED FROM " + tag);
        }
    }

    public synchronized static void removeVerboseCallback(LogCallback logCallback) {
        if (verboseLogCallback.contains(logCallback))
            verboseLogCallback.remove(logCallback);
    }

    public synchronized static List<String> getLog() {
        if (log instanceof ArrayList)
            return new ArrayList<>(log);
        else
            return new LinkedList<>(log);

    }

    public synchronized static boolean setLogLimit(int logLimit) {
        if (logLimit > 0) {
            if (logLimit >= 50000 && Log.logLimit < 50000) {
                LinkedList<String> newLog = new LinkedList<>(log);
                log = newLog;
            } else if (logLimit < 50000 && Log.logLimit >= 50000) {
                ArrayList<String> newLog = new ArrayList<>(log);
                log = newLog;
            }
            Log.logLimit = logLimit;
            while (log.size() >= logLimit) {
                log.remove(0);
            }
            return true;
        } else
            return false;
    }

    public synchronized static void writeLogToFile(String filename) throws IOException {
        PrintWriter output = null;
        try {
            File file = new File(filename);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    if (!file.getParentFile().mkdirs())
                        throw new IOException("Failed To Create Folder");
                }
                if (!file.createNewFile())
                    throw new IOException("Failed To Create File");
            }
            output = new PrintWriter(new FileOutputStream(file));
            for (String message : log) {
                output.println(message);
            }
        } finally {
            if (output != null)
                output.close();
        }
    }
}
