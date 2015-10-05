package eagle;

import java.util.HashMap;
import java.util.Vector;

/**
 * Java Logger
 *
 * @author Cameron Cross
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 8/13/15
 * <p/>
 * Date Modified	27/08/2015 - Cameron
 */
public class Log {

    private static HashMap<String,Vector<String>> log = new HashMap<>();
    private static HashMap<String,Vector<LogCallback>> logCallBack = new HashMap<>();
    private static Vector<LogCallback> verboseLogCallback = new Vector<>();


    public synchronized static void log(String tag, String message) {
        if (log.containsKey(tag))
            log.get(tag).add(message);
        else{
            Vector<String> initial = new Vector<>();
            initial.add(message);
            log.put(tag, initial);
        }
        for (LogCallback vLogCallback : verboseLogCallback)
            vLogCallback.onLogEntry(tag, message);
        if(logCallBack.containsKey(tag)) {
            for (LogCallback logcallback : logCallBack.get(tag))
                logcallback.onLogEntry(tag, message);
        }
    }
    public synchronized static String lastMessage(String tag){
        if (log.containsKey(tag))
            return log.get(tag).lastElement();
        else
            return "TAG DOES NOT EXIST";
    }

    public synchronized static void addCallback(String tag, LogCallback callback) {
        if(log.containsKey(tag)&& logCallBack.containsKey(tag))
            logCallBack.get(tag).add(callback);
        else if(log.containsKey(tag)&&!logCallBack.containsKey(tag)){
            Vector<LogCallback> initial = new Vector<>();
            initial.add(callback);
            logCallBack.put(tag, initial);
        }else if(!log.containsKey(tag)&&!logCallBack.containsKey(tag)){
            Vector<String> initialTag = new Vector<>();
            log.put(tag, initialTag);
            Vector<LogCallback> initialLogCallback = new Vector<>();
            initialLogCallback.add(callback);
            logCallBack.put(tag,initialLogCallback);
        }
        Log.log("LogCallback","NEW CALLBACK ADDED TO "+tag);
    }

    public synchronized static void addVerboseCallback(LogCallback callback){
        verboseLogCallback.add(callback);
    }

    public synchronized static void removeCallback(String tag, LogCallback callback) {
        if(logCallBack.containsValue(tag)){
            logCallBack.get(tag).remove(callback);
        Log.log("LogCallback","CALLBACK REMOVED FROM "+tag);
        }

    }
}
