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
        PositionGPS pos = new PositionGPS(37.7046192, 144.9631, 0, new Angle(0), new Angle(0), new Angle(0));
        Position output = pos.add(new PositionDisplacement(0, 1000, 0, new Angle(0), new Angle(0), new Angle(0)));
        assertEquals("Wrong position", new PositionGPS(37.7046186544102, 144.97446692233666, 0, new Angle(0), new Angle(0), new Angle(0)), output);
        PositionDisplacement posDisp = pos.compare((PositionGPS)output);
        assertEquals("Wrong displacement", true, Math.abs(posDisp.getLongitude()-1000)>1);



        output = pos.add(new PositionDisplacement(0, -1000, 0, new Angle(0), new Angle(0), new Angle(0)));
        assertEquals("Wrong position", new PositionGPS(37.7046186544102, 144.9517330776634, 0, new Angle(0), new Angle(0), new Angle(0)), output);
        posDisp = pos.compare((PositionGPS)output);
        assertEquals("Wrong displacement", true, Math.abs(posDisp.getLongitude()-1000)>1);

        output = pos.add(new PositionDisplacement(1000, 0, 0, new Angle(0), new Angle(0), new Angle(0)));
        assertEquals("Wrong position", new PositionGPS(37.71361241605919, 144.96310000000003, 0, new Angle(0), new Angle(0), new Angle(0)), output);
        posDisp = pos.compare((PositionGPS)output);
        assertEquals("Wrong displacement", true, Math.abs(posDisp.getLatitude()-1000)>1);

        output = pos.add(new PositionDisplacement(-1000, 0, 0, new Angle(0), new Angle(0), new Angle(0)));
        assertEquals("Wrong position", new PositionGPS(37.71361241605919, 144.96310000000003, 0, new Angle(0), new Angle(0), new Angle(0)), output);
        posDisp = pos.compare((PositionGPS)output);
        assertEquals("Wrong displacement", true, Math.abs(posDisp.getLatitude()-1000)>1);
    }

    @Test
    public void testCompare() throws Exception {
        PositionGPS pos1 = new PositionGPS(37.7046192, 144.9631, 0, new Angle(0), new Angle(0), new Angle(0));
        PositionGPS pos2 = new PositionGPS(37.7046192, 144.9631, 0, new Angle(0), new Angle(0), new Angle(0));
        PositionDisplacement posDisp = pos1.compare(pos2);


        assertEquals("Wrong displacement", new PositionDisplacement(0, 0, 0, new Angle(0), new Angle(0), new Angle(0)), posDisp);
    }
}