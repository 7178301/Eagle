package eagle;

import eagle.navigation.positioning.AbsolutePosition;

import static org.junit.Assert.*;
import org.junit.Test;

public class AbsolutePositionTest {

	@Test
	public void testToString() {
		AbsolutePosition rp = new AbsolutePosition(1.23, 2.34, 3.45,0,0,0);
		assertEquals("Longitude: 1.23, Latitude: 2.34, Altitude: 3.45, Roll: 0.0, Pitch: 0.0, Yaw: 0.0", rp.toString());
	}
	
}
