package eagle.logging;

/**
 * Java Log Callback
 *
 * @author Cameron Cross
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 8/13/15
 * <p/>
 * Date Modified	27/08/2015 - Cameron
 */
public interface LogCallback {
    public void onLogEntry(String tag, String message);
}
