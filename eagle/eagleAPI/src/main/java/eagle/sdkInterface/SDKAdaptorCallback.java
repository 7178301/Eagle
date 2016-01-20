package eagle.sdkInterface;

/**
 * SDK Adaptor Callback Class
 * Callback to return the result of a SDK adaptor event
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 30/09/2015
 * <p/>
 * Date Modified	30/09/2015 - Nicholas
 */
public interface SDKAdaptorCallback {
    /**
     * Used to retrieve the details of a SDK adaptor event
     * @param booleanResult boolean success result
     * @param stringResult Error/Success description
     * */
    void onResult(boolean booleanResult, String stringResult);
}
