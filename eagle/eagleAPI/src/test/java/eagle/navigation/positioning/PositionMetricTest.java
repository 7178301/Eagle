package eagle.navigation.positioning;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * eagle.navigation.positioning
 *
 * @author cameron
 * @version 0.0.1
 * @since 9/8/15
 * <p/>
 */
public class PositionMetricTest {

    @Test
    public void testAddAndCompare() throws Exception {
        //these results were checked by eye.
        PositionMetric pos = new PositionMetric(-37.82111100000000, 145.0383330000000, 0, new Angle(0), new Angle(0), new Angle(0));
        Position output = pos.add(new PositionDisplacement(0, 1000, 0, new Angle(0), new Angle(0), new Angle(0)));
        if (!(output instanceof PositionMetric)) {
            fail("Wrong type");
        }
        assertEquals("Wrong position", new PositionMetric(-37.82111100000000, 1145.0383330000000, 0, new Angle(0), new Angle(0), new Angle(0)), output);
        PositionDisplacement posDisp = ((PositionMetric)output).compare(pos);
        assertEquals("Wrong displacement", true, Math.abs(posDisp.getLongitude() - 1000) < 1);



        output = pos.add(new PositionDisplacement(0, -1000, 0, new Angle(0), new Angle(0), new Angle(0)));
        assertEquals("Wrong position", new PositionMetric(-37.821111, -854.961667, 0, new Angle(0), new Angle(0), new Angle(0)), output);
        posDisp = ((PositionMetric)output).compare(pos);
        assertEquals("Wrong displacement", true, Math.abs(posDisp.getLongitude() + 1000) < 1);

        output = pos.add(new PositionDisplacement(1000, 0, 0, new Angle(0), new Angle(0), new Angle(0)));
        assertEquals("Wrong position", new PositionMetric(962.178889, 145.0383330000000, 0, new Angle(0), new Angle(0), new Angle(0)), output);
        posDisp = ((PositionMetric)output).compare(pos);
        assertEquals("Wrong displacement", true, Math.abs(posDisp.getLatitude() - 1000)<1);

        output = pos.add(new PositionDisplacement(-1000, 0, 0, new Angle(0), new Angle(0), new Angle(0)));
        assertEquals("Wrong position", new PositionMetric(-1037.821111, 145.0383330000000, 0, new Angle(0), new Angle(0), new Angle(0)), output);
        posDisp = ((PositionMetric)output).compare(pos);
        assertEquals("Wrong displacement", true, Math.abs(posDisp.getLatitude() + 1000)<1);
    }

    @Test
    public void testCompare() throws Exception {
        PositionMetric pos1 = new PositionMetric(37.7046192, 144.9631, 0, new Angle(0), new Angle(0), new Angle(0));
        PositionMetric pos2 = new PositionMetric(37.7046192, 144.9631, 0, new Angle(0), new Angle(0), new Angle(0));
        PositionDisplacement posDisp = pos1.compare(pos2);


        assertEquals("Wrong displacement", new PositionDisplacement(0, 0, 0, new Angle(0), new Angle(0), new Angle(0)), posDisp);
    }


    @Test
    public void testAdd(){
        PositionMetric rp = new PositionMetric(1.23, 2.34, 3.45,new Angle(0),new Angle(0),new Angle(0));
        PositionDisplacement dp = new PositionDisplacement(1.23, 2.34, 3.45,new Angle(0),new Angle(0),new Angle(0));
        Position output = rp.add(dp);
        if (!(output instanceof PositionMetric)) {
            fail("Returned wrong type");
        }
        assertEquals("Latitude: 2.46, Longitude: 4.68, Altitude: 6.9, Roll: 0.0, Pitch: 0.0, Yaw: 0"+"\u00B0", output.toStringLong());
    }
}