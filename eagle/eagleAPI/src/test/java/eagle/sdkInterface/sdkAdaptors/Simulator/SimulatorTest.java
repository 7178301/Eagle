package eagle.sdkInterface.sdkAdaptors.Simulator;

import org.junit.Before;
import org.junit.Test;

import eagle.Drone;
import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.PositionGPS;
import eagle.navigation.positioning.PositionMetric;
import eagle.sdkInterface.SDKAdaptor;
import eagle.sdkInterface.SDKAdaptorCallback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
        drone.getSDKAdaptor().connectToDrone();
    }

    @Test
    public void testFlyToMetric() throws Exception {
        try {
            sdkAdaptor.setHomePosition(new PositionMetric(0, 0, 0, new Angle(0),new Angle(0), new Angle(0)));
        } catch (SDKAdaptor.InvalidPositionTypeException e) {
            fail("Failed to set home position correctly");
        }

        PositionMetric pos = new PositionMetric(1, 0, 0, new Angle(0),new Angle(0), new Angle(0));
        sdkAdaptor.flyTo(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        },pos);
        assertEquals("Failed to reach position: "+pos.toStringLong(), pos, sdkAdaptor.getPositionAssigned());
        sdkAdaptor.goHome(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        });
        assertEquals("Failed to return home position", sdkAdaptor.getHomePosition(), sdkAdaptor.getPositionAssigned());


        pos = new PositionMetric(-1, 0, 0, new Angle(0),new Angle(0), new Angle(0));
        sdkAdaptor.flyTo(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        },pos);
        assertEquals("Failed to reach position: "+pos.toStringLong(), pos, sdkAdaptor.getPositionAssigned());
        sdkAdaptor.goHome(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        });
        assertEquals("Failed to return home position", sdkAdaptor.getHomePosition(), sdkAdaptor.getPositionAssigned());

        pos = new PositionMetric(0, 1, 0, new Angle(0),new Angle(0), new Angle(0));
        sdkAdaptor.flyTo(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        },pos);
        assertEquals("Failed to reach position: "+pos.toStringLong(), pos, sdkAdaptor.getPositionAssigned());
        sdkAdaptor.goHome(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        });
        assertEquals("Failed to return home position", sdkAdaptor.getHomePosition(), sdkAdaptor.getPositionAssigned());

        pos = new PositionMetric(0, -1, 0, new Angle(0),new Angle(0), new Angle(0));
        sdkAdaptor.flyTo(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        },pos);
        assertEquals("Failed to reach position: "+pos.toStringLong(), pos, sdkAdaptor.getPositionAssigned());
        sdkAdaptor.goHome(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        });
        assertEquals("Failed to return home position", sdkAdaptor.getHomePosition(), sdkAdaptor.getPositionAssigned());

        pos = new PositionMetric(0, 0, 1, new Angle(0),new Angle(0), new Angle(0));
        sdkAdaptor.flyTo(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        },pos);
        assertEquals("Failed to reach position: "+pos.toStringLong(), pos, sdkAdaptor.getPositionAssigned());
        sdkAdaptor.goHome(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        });
        assertEquals("Failed to return home position", sdkAdaptor.getHomePosition(), sdkAdaptor.getPositionAssigned());

    }

    @Test
    public void testFlyToGPS() throws Exception {
        try {
            sdkAdaptor.setHomePosition(new PositionGPS(0, 0, 0, new Angle(0),new Angle(0), new Angle(0)));
        } catch (SDKAdaptor.InvalidPositionTypeException e) {
            fail("Failed to set home position correctly");
        }

        PositionGPS pos = new PositionGPS(1, 0, 0, new Angle(0),new Angle(0), new Angle(0));
        sdkAdaptor.flyTo(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        },pos);
        assertEquals("Failed to reach position: "+pos.toStringLong(), pos, sdkAdaptor.getPositionAssigned());
        sdkAdaptor.goHome(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        });
        assertEquals("Failed to return home position", sdkAdaptor.getHomePosition(), sdkAdaptor.getPositionAssigned());


        pos = new PositionGPS(-1, 0, 0, new Angle(0),new Angle(0), new Angle(0));
        sdkAdaptor.flyTo(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        },pos);
        assertEquals("Failed to reach position: "+pos.toStringLong(), pos, sdkAdaptor.getPositionAssigned());
        sdkAdaptor.goHome(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        });
        assertEquals("Failed to return home position", sdkAdaptor.getHomePosition(), sdkAdaptor.getPositionAssigned());

        pos = new PositionGPS(0, 1, 0, new Angle(0),new Angle(0), new Angle(0));
        sdkAdaptor.flyTo(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        },pos);
        assertEquals("Failed to reach position: "+pos.toStringLong(), pos, sdkAdaptor.getPositionAssigned());
        sdkAdaptor.goHome(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        });
        assertEquals("Failed to return home position", sdkAdaptor.getHomePosition(), sdkAdaptor.getPositionAssigned());

        pos = new PositionGPS(0, -1, 0, new Angle(0),new Angle(0), new Angle(0));
        sdkAdaptor.flyTo(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        },pos);
        assertEquals("Failed to reach position: "+pos.toStringLong(), pos, sdkAdaptor.getPositionAssigned());
        sdkAdaptor.goHome(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        });
        assertEquals("Failed to return home position", sdkAdaptor.getHomePosition(), sdkAdaptor.getPositionAssigned());

        pos = new PositionGPS(0, 0, 1, new Angle(0),new Angle(0), new Angle(0));
        sdkAdaptor.flyTo(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        },pos);
        assertEquals("Failed to reach position: "+pos.toStringLong(), pos, sdkAdaptor.getPositionAssigned());
        sdkAdaptor.goHome(new SDKAdaptorCallback() {
            @Override
            public void onResult(boolean booleanResult, String stringResult) {
            }
        });
        assertEquals("Failed to return home position", sdkAdaptor.getHomePosition(), sdkAdaptor.getPositionAssigned());

    }


}