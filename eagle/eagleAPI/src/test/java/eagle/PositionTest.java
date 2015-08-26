package eagle;

import org.junit.Test;

import eagle.navigation.positioning.Bearing;
import eagle.navigation.positioning.Position;

import static org.junit.Assert.assertEquals;

public class PositionTest {

	@Test
	public void testToString(){
		Position rp = new Position(1.23, 2.34, 3.45,0,0,new Bearing(0));
		assertEquals("Longitude: 1.23, Latitude: 2.34, Altitude: 3.45, Roll: 0.0, Pitch: 0.0, Yaw: 0"+"\u00B0", rp.toString());
	}

    @Test
    public void testAdd(){
        Position rp = new Position(1.23, 2.34, 3.45,0,0,new Bearing(0));
        rp.addRelative(rp);
        assertEquals("Longitude: 2.46, Latitude: 4.68, Altitude: 6.9, Roll: 0.0, Pitch: 0.0, Yaw: 0"+"\u00B0", rp.toString());
    }

    @Test
    public void testMinus(){
        Position rp = new Position(1.23, 2.34, 3.45,0,0,new Bearing(0));
        rp.addAbsolute(rp);
        assertEquals("Longitude: 1.23, Latitude: 2.34, Altitude: 3.45, Roll: 0.0, Pitch: 0.0, Yaw: 0"+"\u00B0", rp.toString());
    }
}
