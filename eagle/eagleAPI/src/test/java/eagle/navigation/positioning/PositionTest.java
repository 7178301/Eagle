package eagle.navigation.positioning;

import org.junit.Test;

import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.PositionDisplacement;
import eagle.navigation.positioning.PositionMetric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Position Class Tester
 *
 * @author Cameron Cross
 * @version 0.0.1
 * @since 27/08/2015
 * <p/>
 * Date Modified	27/08/2015 - Cameron
 */
public class PositionTest {

	@Test
	public void testToString() {
		Position rp = new PositionMetric(1.23, 2.34, 3.45,new Angle(0),new Angle(0),new Angle(0));
		assertEquals("Latitude: 1.23, Longitude: 2.34, Altitude: 3.45, Roll: 0.0, Pitch: 0.0, Yaw: 0"+"\u00B0", rp.toStringLong());
	}
}
