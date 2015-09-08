package eagle.navigation.positioning;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * eagle.navigation.positioning
 *
 * @author cameron
 * @version 0.0.1
 * @since 9/8/15
 * <p/>
 */
public class PositionGPSTest {

    @Test
    public void testAdd() throws Exception {
        PositionGPS pos = new PositionGPS(144.9631, 37.7046192, 0, new Angle(0), new Angle(0), new Angle(0));
        Position output = pos.add(new PositionDisplacement(5, 5, 0, new Angle(0), new Angle(0), new Angle(0)));
        assertEquals("Wrong position", new PositionGPS(144.9631, 37.7046192, 0, new Angle(0), new Angle(0), new Angle(0)), output);
    }

    @Test
    public void testCompare() throws Exception {
        PositionGPS posGreensborough = new PositionGPS(145.1030275, 37.7046192, 0, new Angle(0), new Angle(0), new Angle(0));
        PositionGPS posEltham = new PositionGPS(145.1489162, 37.7133771, 0, new Angle(0), new Angle(0), new Angle(0));
        PositionDisplacement posDisp = posGreensborough.compare(posEltham);


        assertEquals("Wrong displacement", new PositionDisplacement(144.9631, 37.7046192, 0, new Angle(0), new Angle(0), new Angle(0)), posDisp);
    }
}