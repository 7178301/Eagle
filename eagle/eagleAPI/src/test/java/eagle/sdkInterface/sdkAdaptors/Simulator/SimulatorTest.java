package eagle.sdkInterface.sdkAdaptors.Simulator;

import org.junit.Before;
import org.junit.Test;

import eagle.Drone;
import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.PositionMetric;
import eagle.sdkInterface.SDKAdaptor;

import static org.junit.Assert.assertEquals;

/**
 * Simulator Class Tester
 *
 * @author Cameron Cross
 * @version 0.0.1
 * @since 05/09/2015
 * <p/>
 * Date Modified	05/09/2015 - Cameron
 */
public class SimulatorTest {

    Drone drone;
    SDKAdaptor sdkAdaptor;

    @Before
    public void beforeTest() {
        drone = new Drone();
        drone.setSDKAdaptor("Simulator.Simulator");
        sdkAdaptor = drone.getSDKAdaptor();
    }

    @Test
    public void testFlyToRelative() throws Exception {
        PositionMetric pos = new PositionMetric(1, 0, 0, new Angle(0),new Angle(0), new Angle(0));
        sdkAdaptor.flyToRelative(pos);
        assertEquals("Failed to reach position: "+pos.toStringLong(), true, sdkAdaptor.getPositionAssigned().equals(pos));
        sdkAdaptor.goHome();
        assertEquals("Failed to return home position", true, sdkAdaptor.getPositionAssigned().equals(sdkAdaptor.getHomePosition()));


        pos = new PositionMetric(-1, 0, 0, new Angle(0),new Angle(0), new Angle(0));
        sdkAdaptor.flyToRelative(pos);
        assertEquals("Failed to reach position: "+pos.toStringLong(), true, sdkAdaptor.getPositionAssigned().equals(pos));
        sdkAdaptor.goHome();
        assertEquals("Failed to return home position", true, sdkAdaptor.getPositionAssigned().equals(sdkAdaptor.getHomePosition()));

        pos = new PositionMetric(0, 1, 0, new Angle(0),new Angle(0), new Angle(0));
        sdkAdaptor.flyToRelative(pos);
        assertEquals("Failed to reach position: "+pos.toStringLong(), true, sdkAdaptor.getPositionAssigned().equals(pos));
        sdkAdaptor.goHome();
        assertEquals("Failed to return home position", true, sdkAdaptor.getPositionAssigned().equals(sdkAdaptor.getHomePosition()));

        pos = new PositionMetric(0, -1, 0, new Angle(0),new Angle(0), new Angle(0));
        sdkAdaptor.flyToRelative(pos);
        assertEquals("Failed to reach position: "+pos.toStringLong(), true, sdkAdaptor.getPositionAssigned().equals(pos));
        sdkAdaptor.goHome();
        assertEquals("Failed to return home position", true, sdkAdaptor.getPositionAssigned().equals(sdkAdaptor.getHomePosition()));

        pos = new PositionMetric(0, 0, 1, new Angle(0),new Angle(0), new Angle(0));
        sdkAdaptor.flyToRelative(pos);
        assertEquals("Failed to reach position: "+pos.toStringLong(), true, sdkAdaptor.getPositionAssigned().equals(pos));
        sdkAdaptor.goHome();
        assertEquals("Failed to return home position", true, sdkAdaptor.getPositionAssigned().equals(sdkAdaptor.getHomePosition()));

    }
}