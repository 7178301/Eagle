package eagle;

import eagle.sdkInterface.positioning.GPSPosition;

import static org.junit.Assert.*;
import org.junit.Test;

public class GPSPositionTest{

	@Test
	public void testToString() {
		GPSPosition rp = new GPSPosition(1.23, 2.34, 3.45);
		assertEquals("Longitude: 1.23, Latitude: 2.34, Altitude: 3.45", rp.toString());
	}
	
}
