package eagle.sdkInterface.sdkAdaptors.Simulator;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import eagle.Drone;
import eagle.navigation.positioning.Bearing;
import eagle.navigation.positioning.Position;
import eagle.sdkInterface.SDKAdaptor;

import static org.junit.Assert.assertEquals;

/**
 * Created by cameron on 9/6/15.
 */
public class SimulatorTest {

    Drone drone;
    SDKAdaptor sdkAdaptor;

    @Before
    public void beforeTest() {
        drone = new Drone();
        drone.setSDKAdaptor("Simulator.Simulator");
        sdkAdaptor = drone.getSDKAdaptor();
        sdkAdaptor.setHomePosition(new Position(0, 0, 0, 0, 0, new Bearing(0)));
    }

    @Test
    public void testFlyToRelative() throws Exception {
        Position pos = new Position(1, 0, 0, 0, 0, new Bearing(0));
        sdkAdaptor.flyToRelative(pos);
        assertEquals("Failed to reach position: "+pos.toPrettyString(), true, sdkAdaptor.getPositionAssigned().equals(pos));
        sdkAdaptor.goHome();
        assertEquals("Failed to return home position", true, sdkAdaptor.getPositionAssigned().equals(sdkAdaptor.getHomePosition()));


        pos = new Position(-1, 0, 0, 0, 0, new Bearing(0));
        sdkAdaptor.flyToRelative(pos);
        assertEquals("Failed to reach position: "+pos.toPrettyString(), true, sdkAdaptor.getPositionAssigned().equals(pos));
        sdkAdaptor.goHome();
        assertEquals("Failed to return home position", true, sdkAdaptor.getPositionAssigned().equals(sdkAdaptor.getHomePosition()));

        pos = new Position(0, 1, 0, 0, 0, new Bearing(0));
        sdkAdaptor.flyToRelative(pos);
        assertEquals("Failed to reach position: "+pos.toPrettyString(), true, sdkAdaptor.getPositionAssigned().equals(pos));
        sdkAdaptor.goHome();
        assertEquals("Failed to return home position", true, sdkAdaptor.getPositionAssigned().equals(sdkAdaptor.getHomePosition()));

        pos = new Position(0, -1, 0, 0, 0, new Bearing(0));
        sdkAdaptor.flyToRelative(pos);
        assertEquals("Failed to reach position: "+pos.toPrettyString(), true, sdkAdaptor.getPositionAssigned().equals(pos));
        sdkAdaptor.goHome();
        assertEquals("Failed to return home position", true, sdkAdaptor.getPositionAssigned().equals(sdkAdaptor.getHomePosition()));

        pos = new Position(0, 0, 1, 0, 0, new Bearing(0));
        sdkAdaptor.flyToRelative(pos);
        assertEquals("Failed to reach position: "+pos.toPrettyString(), true, sdkAdaptor.getPositionAssigned().equals(pos));
        sdkAdaptor.goHome();
        assertEquals("Failed to return home position", true, sdkAdaptor.getPositionAssigned().equals(sdkAdaptor.getHomePosition()));

    }
}