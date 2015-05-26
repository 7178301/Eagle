package eagle;

import eagle.sdkInterface.positioning.RelativePosition;

import static org.junit.Assert.*;
import org.junit.Test;

public class RelativePositionTest {

	@Test
	public void testToString() {
		RelativePosition rp = new RelativePosition(1.23, 2.34, 3.45);
		assertEquals("X: 1.23, Y: 2.34, Z: 3.45", rp.toString());
	}
	
	@Test
	public void testAdd() {
		RelativePosition rp = new RelativePosition(1.23, 2.34, 3.45);
		RelativePosition out = rp.add(rp);
		assertEquals("X: 2.46, Y: 4.68, Z: 6.9", out.toString());
	}
	
	@Test
	public void testMinus() {
		RelativePosition rp = new RelativePosition(1.23, 2.34, 3.45);
		RelativePosition out = rp.minus(rp);
		assertEquals("X: 0.0, Y: 0.0, Z: 0.0", out.toString());
	}

}
