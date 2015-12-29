package eagle;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Log Class Tester
 *
 * @author Nicholas Alards
 * @version 0.0.1
 * @since 01/11/2015
 * <p/>
 * Date Modified	01/11/2015 - Cameron
 */
public class LogTest {

    @Test
    public void testLogNullElements() {
        //Get Log Pre Size
        Log.setLogLimit(Log.logLimit + 1);
        int preSize = Log.getLog().size();
        Log.log(null, null);
        //Check log size didn't increase
        assertEquals("", Log.getLog().size(), preSize);
    }

    @Test
    public void testLogLimit() {
        //Fill Log to Limit
        for (int i = Log.getLog().size(); i < Log.logLimit; i++) {
            Log.log("a", "a");
        }

        //Add an additional log
        Log.log("a", "a");
        int postCheck = Log.getLog().size();

        assertEquals("Log Limit Failure", Log.logLimit, postCheck);
    }

    @Test
    public void testSetLogLimit() {
        int preLimit = Log.logLimit;
        Log.setLogLimit(preLimit + 5000);
        assertEquals("Set Log Limit Failure", Log.logLimit, preLimit + 5000);
    }

    @Test
    public void testLogLimitListType() {
        Log.setLogLimit(5000);
        assertThat(Log.getLog(), instanceOf(ArrayList.class));
        Log.setLogLimit(50000);
        assertThat(Log.getLog(), instanceOf(LinkedList.class));
    }
}
