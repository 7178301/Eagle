package eagle;

import eagle.navigation.positioning.RelativePosition;

import static org.junit.Assert.*;
import org.junit.Test;

public class RelativePositionTest {

	@Test
	public void testToString() {
		RelativePosition rp = new RelativePosition(1.23, 2.34, 3.45,0,0,0);
		assertEquals("Longitude: 1.23, Latitude: 2.34, Altitude: 3.45, Roll: 0.0, Pitch: 0.0, Yaw: 0.0", rp.toString());
	}
	
	@Test
	public void testAdd() {
		RelativePosition rp = new RelativePosition(1.23, 2.34, 3.45,0,0,0);
		RelativePosition out = rp.add(rp);
		assertEquals("Longitude: 2.46, Latitude: 4.68, Altitude: 6.9, Roll: 0.0, Pitch: 0.0, Yaw: 0.0", out.toString());
	}
	
	@Test
	public void testMinus() {
		RelativePosition rp = new RelativePosition(1.23, 2.34, 3.45,0,0,0);
		RelativePosition out = rp.minus(rp);
		assertEquals("Longitude: 0.0, Latitude: 0.0, Altitude: 0.0, Roll: 0.0, Pitch: 0.0, Yaw: 0.0", out.toString());
	}

}
