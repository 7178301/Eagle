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
    public void testAddAndCompare() throws Exception {
        //these results were checked by eye.
        PositionGPS pos = new PositionGPS(-37.82111100000000, 145.0383330000000, 0, new Angle(0), new Angle(0), new Angle(0));
        PositionGPS home = (PositionGPS) pos.copy();
        pos.add(new PositionDisplacement(0, 1000, 0, new Angle(0), new Angle(0), new Angle(0)));
        assertEquals("Wrong position", new PositionGPS(-37.82111045211409, 145.0497178390728, 0, new Angle(0), new Angle(0), new Angle(0)), pos);
        PositionDisplacement posDisp = pos.compare(home);
        assertEquals("Wrong displacement", true, Math.abs(posDisp.getLongitude() - 1000) < 1);



        pos.add(new PositionDisplacement(0, -1000, 0, new Angle(0), new Angle(0), new Angle(0)));
        assertEquals("Wrong position", new PositionGPS(-37.8211099042282, 145.03833300008446, 0, new Angle(0), new Angle(0), new Angle(0)), pos);
        posDisp = pos.compare(home);
        assertEquals("Wrong displacement", true, Math.abs(posDisp.getLongitude())<1);

        pos.add(new PositionDisplacement(1000, 0, 0, new Angle(0), new Angle(0), new Angle(0)));
        assertEquals("Wrong position", new PositionGPS(-37.81211668816901, 145.03833300008446, 0, new Angle(0), new Angle(0), new Angle(0)), pos);
        posDisp = pos.compare(home);
        assertEquals("Wrong displacement", true, Math.abs(posDisp.getLatitude()-1000)<1);

        pos.add(new PositionDisplacement(-1000, 0, 0, new Angle(0), new Angle(0), new Angle(0)));
        assertEquals("Wrong position", new PositionGPS(-37.821109904228194, 145.03833300008446, 0, new Angle(0), new Angle(0), new Angle(0)), pos);
        posDisp = pos.compare(home);
        assertEquals("Wrong displacement", true, Math.abs(posDisp.getLatitude())<1);
    }

    @Test
    public void testCompare() throws Exception {
        PositionGPS pos1 = new PositionGPS(37.7046192, 144.9631, 0, new Angle(0), new Angle(0), new Angle(0));
        PositionGPS pos2 = new PositionGPS(37.7046192, 144.9631, 0, new Angle(0), new Angle(0), new Angle(0));
        PositionDisplacement posDisp = pos1.compare(pos2);


        assertEquals("Wrong displacement", new PositionDisplacement(0, 0, 0, new Angle(0), new Angle(0), new Angle(0)), posDisp);
    }
}