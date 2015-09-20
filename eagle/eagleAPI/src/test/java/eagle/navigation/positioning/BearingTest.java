package eagle.navigation.positioning;

import eagle.navigation.positioning.Angle;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Bearing Class Tester
 *
 * @author Cameron Cross
 * @version 0.0.1
 * @since 27/08/2015
 * <p/>
 * Date Modified	27/08/2015 - Cameron
 */
public class BearingTest
{
	@Test
	public void testBearingToString(){
		Angle temp = new Angle(40.34722);
		assertEquals("40\u00B020'50\"", temp.toStringLong());
		
		temp = new Angle(30);
		assertEquals("30\u00B0", temp.toStringLong());
		
		temp = new Angle(-30);
		assertEquals("330\u00B0", temp.toStringLong());
		
		temp = new Angle(1050);
		assertEquals("330\u00B0", temp.toStringLong());
	}
}
