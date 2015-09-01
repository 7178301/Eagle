package eagle;

import org.junit.Before;
import org.junit.Test;

import eagle.navigation.positioning.Bearing;
import eagle.navigation.positioning.Position;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Drone Class Tester
 *
 * @author Cameron Cross
 * @version 0.0.1
 * @since 27/08/2015
 * <p/>
 * Date Modified	27/08/2015 - Cameron
 */

public class DroneTest {
    Drone drone;

    @Before
    public void beforeTest() {
        drone = new Drone();
        drone.setSDKAdaptor("Simulator.Simulator");
    }

    @Test
    public void testExecuteInstructionInvalid() {
        try {
            drone.executeInstruction("ASDF");
            fail("Exception should be thrown");
        } catch (Drone.InvalidInstructionException e) {
        }
    }

    @Test
    public void testExecuteInstructionConnectToDrone() {
        try {
            assertEquals("return wrong value", "SUCCESS", drone.executeInstruction("CONNECTTODRONE"));
        }
        catch (Drone.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }
    @Test
    public void testExecuteInstructionDisconnectToDrone() {
        try {
            assertEquals("return wrong value", "SUCCESS", drone.executeInstruction("DISCONNECTFROMDRONE"));
        }
        catch (Drone.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }

    @Test
    public void testExecuteInstructionFLYTORELATIVE() {
        try {
            assertEquals("return wrong value", "SUCCESS", drone.executeInstruction("FLYTORELATIVE 1 2 3 4"));
            assertEquals("moved to wrong position", drone.getSDKAdaptor().getPositionAssigned().toString(), new Position(1, 2, 3, 0, 0, new Bearing(4)).toString());
        }
        catch (Drone.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }

    @Test
    public void testExecuteInstructionCHANGELONGITUDERELATIVE() {
        try {
            assertEquals("return wrong value", "SUCCESS", drone.executeInstruction("CHANGELONGITUDERELATIVE 1"));
            assertEquals("moved to wrong position", drone.getSDKAdaptor().getPositionAssigned().toString(), new Position(1, 0, 0, 0, 0, new Bearing(0)).toString());
            assertEquals("return wrong value", "SUCCESS", drone.executeInstruction("CHANGELONGITUDERELATIVE 2 1"));
            assertEquals("moved to wrong position", drone.getSDKAdaptor().getPositionAssigned().toString(), new Position(3, 0, 0, 0, 0, new Bearing(0)).toString());
        }
        catch (Drone.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }

    @Test
    public void testExecuteInstructionCHANGELATITUDERELATIVE() {
        try {
            assertEquals("return wrong value", "SUCCESS", drone.executeInstruction("CHANGELATITUDERELATIVE 1"));
            assertEquals("moved to wrong position", drone.getSDKAdaptor().getPositionAssigned().toString(), new Position(0, 1, 0, 0, 0, new Bearing(0)).toString());
            assertEquals("return wrong value", "SUCCESS", drone.executeInstruction("CHANGELATITUDERELATIVE 2 1"));
            assertEquals("moved to wrong position", drone.getSDKAdaptor().getPositionAssigned().toString(), new Position(0, 3, 0, 0, 0, new Bearing(0)).toString());
        }
        catch (Drone.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }

    @Test
    public void testExecuteInstructionCHANGEALTITUDERELATIVE() {
        try {
            assertEquals("return wrong value", "SUCCESS", drone.executeInstruction("CHANGEALTITUDERELATIVE 1"));
            assertEquals("moved to wrong position", drone.getSDKAdaptor().getPositionAssigned().toString(), new Position(0, 0, 1, 0, 0, new Bearing(0)).toString());
            assertEquals("return wrong value", "SUCCESS", drone.executeInstruction("CHANGEALTITUDERELATIVE 2 1"));
            assertEquals("moved to wrong position", drone.getSDKAdaptor().getPositionAssigned().toString(), new Position(0, 0, 3, 0, 0, new Bearing(0)).toString());
        }
        catch (Drone.InvalidInstructionException e) {
            fail("Invalid instruction");
        }
    }
}
