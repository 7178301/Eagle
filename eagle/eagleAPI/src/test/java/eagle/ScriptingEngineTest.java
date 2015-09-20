package eagle;

import org.junit.Before;
import org.junit.Test;

import eagle.navigation.positioning.Angle;
import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.PositionGPS;
import eagle.navigation.positioning.PositionDisplacement;
import eagle.navigation.positioning.PositionMetric;
import eagle.sdkInterface.SDKAdaptor;

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
        drone.getSDKAdaptor().connectToDrone();
    }

    @Test
    public void testExecuteInstructionInvalid() {
        try {
            se.executeInstruction("ASDF");
            fail("Exception should be thrown");
        } catch (ScriptingEngine.InvalidInstructionException e) {
        }
    }

    @Test
    public void testExecuteInstructionConnectToDrone() {
        try {
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CONNECTTODRONE"));
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }
    @Test
    public void testExecuteInstructionDisconnectToDrone() {
        try {
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("DISCONNECTFROMDRONE"));
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }

    @Test
    public void testExecuteInstructionFLYTODISPLACEMENT() {
        try {
            se.adaptor.setHomePosition(new PositionMetric(0, 0, 0, new Angle(0),new Angle(0), new Angle(0)));
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("FLYTO DISPLACEMENT 1 2 3 4"));
            assertEquals("moved to wrong position", new PositionMetric(1, 2, 3, new Angle(0),new Angle(0), new Angle(4)), drone.getSDKAdaptor().getPositionAssigned());
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
        catch (SDKAdaptor.InvalidPositionException e) {
            fail("Failed to set home position");
        }
    }

    @Test
    public void testExecuteInstructionCHANGELONGITUDEDISPLACEMENT() {
        try {
            se.adaptor.setHomePosition(new PositionMetric(0, 0, 0, new Angle(0),new Angle(0), new Angle(0)));
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELONGITUDE DISPLACEMENT 1"));
            assertEquals("moved to wrong position", new PositionMetric(0, 1, 0, new Angle(0),new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELONGITUDE DISPLACEMENT 2 1"));
            assertEquals("moved to wrong position", new PositionMetric(0, 3, 0, new Angle(0), new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
        catch (SDKAdaptor.InvalidPositionException e) {
            fail("Failed to set home position");
        }
    }

    @Test
    public void testExecuteInstructionCHANGELATITUDEDISPLACEMENT() {
        try {
            se.adaptor.setHomePosition(new PositionMetric(0, 0, 0, new Angle(0),new Angle(0), new Angle(0)));
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELATITUDE DISPLACEMENT 1"));
            assertEquals("moved to wrong position", new PositionMetric(1, 0, 0, new Angle(0),new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELATITUDE DISPLACEMENT 2 1"));
            assertEquals("moved to wrong position", new PositionMetric(3, 0, 0, new Angle(0),new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
        catch (SDKAdaptor.InvalidPositionException e) {
            fail("Failed to set home position");
        }
    }

    @Test
    public void testExecuteInstructionCHANGEALTITUDEDISPLACEMENT() {
        try {
            se.adaptor.setHomePosition(new PositionMetric(0, 0, 0, new Angle(0),new Angle(0), new Angle(0)));
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGEALTITUDE DISPLACEMENT 1"));
            assertEquals("moved to wrong position", new PositionMetric(0, 0, 1, new Angle(0),new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGEALTITUDE DISPLACEMENT 2 1"));
            assertEquals("moved to wrong position", new PositionMetric(0, 0, 3, new Angle(0),new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
        catch (SDKAdaptor.InvalidPositionException e) {
            fail("Failed to set home position");
        }
    }

    @Test
    public void testExecuteInstructionFLYTOMETRIC() {
        try {
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("FLYTO METRIC 1 2 3 4"));
            assertEquals("moved to wrong position", new PositionMetric(1, 2, 3, new Angle(0),new Angle(0), new Angle(4)), drone.getSDKAdaptor().getPositionAssigned());
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }

    @Test
    public void testExecuteInstructionCHANGELONGITUDEMETRIC() {
        try {
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELONGITUDE METRIC 1"));
            assertEquals("moved to wrong position", new PositionMetric(0, 1, 0, new Angle(0),new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELONGITUDE METRIC 2 1"));
            assertEquals("moved to wrong position", new PositionMetric(0, 2, 0, new Angle(0), new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }

    @Test
    public void testExecuteInstructionCHANGELATITUDEMETRIC() {
        try {
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELATITUDE METRIC 1"));
            assertEquals("moved to wrong position", new PositionMetric(1, 0, 0, new Angle(0),new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELATITUDE METRIC 2 1"));
            assertEquals("moved to wrong position", new PositionMetric(2, 0, 0, new Angle(0),new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }

    @Test
    public void testExecuteInstructionCHANGEALTITUDEMETRIC() {
        try {
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGEALTITUDE METRIC 1"));
            assertEquals("moved to wrong position", new PositionMetric(0, 0, 1, new Angle(0),new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGEALTITUDE METRIC 2 1"));
            assertEquals("moved to wrong position", new PositionMetric(0, 0, 2, new Angle(0),new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }


    @Test
    public void testExecuteInstructionFLYTOGPS() {
        try {
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("FLYTO GPS 1 2 3 4"));
            assertEquals("moved to wrong position", new PositionGPS(1, 2, 3, new Angle(0),new Angle(0), new Angle(4)), drone.getSDKAdaptor().getPositionAssigned());
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }

    @Test
    public void testExecuteInstructionCHANGELONGITUDEGPS() {
        try {
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELONGITUDE GPS 1"));
            assertEquals("moved to wrong position", new PositionGPS(0, 1, 0, new Angle(0),new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELONGITUDE GPS 2 1"));
            assertEquals("moved to wrong position", new PositionGPS(0, 2, 0, new Angle(0),new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }

    @Test
    public void testExecuteInstructionCHANGELATITUDEGPS() {
        try {
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELATITUDE GPS 1"));
            assertEquals("moved to wrong position", new PositionGPS(1, 0, 0, new Angle(0),new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGELATITUDE GPS 2 1"));
            assertEquals("moved to wrong position", new PositionGPS(2, 0, 0, new Angle(0),new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }

    @Test
    public void testExecuteInstructionCHANGEALTITUDEGPS() {
        try {
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGEALTITUDE GPS 1"));
            assertEquals("moved to wrong position", new PositionGPS(0, 0, 1, new Angle(0),new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
            assertEquals("return wrong value", "SUCCESS", se.executeInstruction("CHANGEALTITUDE GPS 2 1"));
            assertEquals("moved to wrong position", new PositionGPS(0, 0, 2, new Angle(0),new Angle(0), new Angle(0)), drone.getSDKAdaptor().getPositionAssigned());
        }
        catch (ScriptingEngine.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }
}
