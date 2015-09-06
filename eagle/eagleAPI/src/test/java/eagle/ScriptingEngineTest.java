package eagle;

import org.junit.Before;
import org.junit.Test;

import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.PositionMetric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * ScriptingEngine Class Tester
 *
 * @author Cameron Cross
 * @version 0.0.1
 * @since 05/09/2015
 * <p/>
 * Date Modified	05/09/2015 - Cameron
 */

public class ScriptingEngineTest {
    Drone drone;
    ScriptingEngine se;

    @Before
    public void beforeTest() {
        drone = new Drone();
        drone.setSDKAdaptor("Simulator.Simulator");
        se = drone.getScriptingEngine();
    }

    @Test(timeout=5000)
    public void testExecuteInstructionInvalid() {
        try {
            se.executeInstruction("ASDF");
            fail("Exception should be thrown");
        } catch (ScriptingEngine.InvalidInstructionException e) {
        }
    }

    @Test(timeout=5000)
    public void testExecuteInstructionConnectToDrone() {
        try {
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CONNECTTODRONE"));
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }
    @Test(timeout=5000)
    public void testExecuteInstructionDisconnectToDrone() {
        try {
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("DISCONNECTFROMDRONE"));
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }

    @Test
    public void testExecuteInstructionFLYTORELATIVE() {
        try {
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("FLYTORELATIVE 1 2 3 4"));
            assertEquals("moved to wrong position", new PositionMetric(1, 2, 3, new Angle(0),new Angle(0), new Angle(4)).toString(), drone.getSDKAdaptor().getPositionAssigned().toString());
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }

    @Test
    public void testExecuteInstructionCHANGELONGITUDERELATIVE() {
        try {
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELONGITUDERELATIVE 1"));
            assertEquals("moved to wrong position", new PositionMetric(1, 0, 0, new Angle(0),new Angle(0), new Angle(0)).toString(), drone.getSDKAdaptor().getPositionAssigned().toString());
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELONGITUDERELATIVE 2 1"));
            assertEquals("moved to wrong position", new PositionMetric(3, 0, 0, new Angle(0),new Angle(0), new Angle(0)).toString(), drone.getSDKAdaptor().getPositionAssigned().toString());
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }

    @Test
    public void testExecuteInstructionCHANGELATITUDERELATIVE() {
        try {
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELATITUDERELATIVE 1"));
            assertEquals("moved to wrong position", new PositionMetric(0, 1, 0, new Angle(0),new Angle(0), new Angle(0)).toString(), drone.getSDKAdaptor().getPositionAssigned().toString());
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELATITUDERELATIVE 2 1"));
            assertEquals("moved to wrong position", new PositionMetric(0, 3, 0, new Angle(0),new Angle(0), new Angle(0)).toString(), drone.getSDKAdaptor().getPositionAssigned().toString());
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }

    @Test(timeout=5000)
    public void testExecuteInstructionCHANGEALTITUDERELATIVE() {
        try {
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGEALTITUDERELATIVE 1"));
            assertEquals("moved to wrong position", new PositionMetric(0, 0, 1, new Angle(0),new Angle(0), new Angle(0)).toString(), drone.getSDKAdaptor().getPositionAssigned().toString());
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGEALTITUDERELATIVE 2 1"));
            assertEquals("moved to wrong position", new PositionMetric(0, 0, 3, new Angle(0),new Angle(0), new Angle(0)).toString(), drone.getSDKAdaptor().getPositionAssigned().toString());
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }

//    @Test(timeout=5000)
//    public void testExecuteInstructionFLYTOGPS() {
//        try {
//            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("FLYTOGPS 1 2 3 4"));
//            assertEquals("moved to wrong position", new Position(1, 2, 3, 0, 0, new Bearing(4)).toString(), drone.getSDKAdaptor().getCurrentPositionAssigned().toString());
//        }
//        catch (Drone.InvalidInstructionException e) {
//            fail("Invalid instruction");
//        }
//    }
//
//    @Test(timeout=5000)
//    public void testExecuteInstructionCHANGELONGITUDEGPS() {
//        try {
//            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELONGITUDEGPS 1"));
//            assertEquals("moved to wrong position", new Position(1, 0, 0, 0, 0, new Bearing(0)).toString(), drone.getSDKAdaptor().getCurrentPositionAssigned().toString());
//            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELONGITUDEGPS 2 1"));
//            assertEquals("moved to wrong position", new Position(3, 0, 0, 0, 0, new Bearing(0)).toString(), drone.getSDKAdaptor().getCurrentPositionAssigned().toString());
//        }
//        catch (Drone.InvalidInstructionException e) {
//            fail("Invalid instruction");
//        }
//    }
//
//    @Test(timeout=5000)
//    public void testExecuteInstructionCHANGELATITUDEGPS() {
//        try {
//            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELATITUDEGPS 1"));
//            assertEquals("moved to wrong position", new Position(0, 1, 0, 0, 0, new Bearing(0)).toString(), drone.getSDKAdaptor().getCurrentPositionAssigned().toString());
//            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELATITUDEGPS 2 1"));
//            assertEquals("moved to wrong position", new Position(0, 3, 0, 0, 0, new Bearing(0)).toString(), drone.getSDKAdaptor().getCurrentPositionAssigned().toString());
//        }
//        catch (Drone.InvalidInstructionException e) {
//            fail("Invalid instruction");
//        }
//    }
//
//    @Test(timeout=5000)
//    public void testExecuteInstructionCHANGEALTITUDEGPS() {
//        try {
//            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGEALTITUDEGPS 1"));
//            assertEquals("moved to wrong position", new Position(0, 0, 1, 0, 0, new Bearing(0)).toString(), drone.getSDKAdaptor().getCurrentPositionAssigned().toString());
//            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGEALTITUDEGPS 2 1"));
//            assertEquals("moved to wrong position", new Position(0, 0, 3, 0, 0, new Bearing(0)).toString(), drone.getSDKAdaptor().getCurrentPositionAssigned().toString());
//        }
//        catch (Drone.InvalidInstructionException e) {
//            fail("Invalid instruction");
//        }
//    }
}
