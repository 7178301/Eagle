package eagle;

/**
 * Java Log Callback Interface
 * Callback for when a log entry is made *
 * @author Cameron Cross
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 8/13/15
 * <p/>
 * Date Modified	27/08/2015 - Cameron
 */
public interface LogCallback {
    /**
     * Used to retrieve the details of the log entry
     * @param tag Log entry tag
     * @param message Log entry message
     */
    public void onLogEntry(String tag, String message);
}
