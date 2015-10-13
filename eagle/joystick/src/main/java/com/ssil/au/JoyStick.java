package com.ssil.au;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import org.lwjgl.LWJGLException;

import java.util.ArrayList;
import java.util.Scanner;

import eagle.network.protocolBuffer.ProtocolBufferClient;

public class JoyStick {
    private ProtocolBufferClient pcb;

    static void main(String[] args) throws LWJGLException {

        new JoyStick();
    }

    private ArrayList<Controller> foundControllers;

    public JoyStick() {
        System.out.println("What is the address:");
        Scanner scan = new Scanner(System.in);
        String address = scan.nextLine();

        pcb = new ProtocolBufferClient(address);
        pcb.connectToServer();

        foundControllers = new ArrayList<>();
        searchForControllers();

        // If at least one controller was found we start showing controller data on window.
        if (!foundControllers.isEmpty())
            startShowingControllerData();
        else
            System.out.println("No controller found!");
    }

    /**
     * Search (and save) for controllers of type Controller.Type.STICK,
     * Controller.Type.GAMEPAD, Controller.Type.WHEEL and Controller.Type.FINGERSTICK.
     */
    private void searchForControllers() {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        for (int i = 0; i < controllers.length; i++) {
            Controller controller = controllers[i];

            if (
                    controller.getType() == Controller.Type.STICK ||
                            controller.getType() == Controller.Type.GAMEPAD ||
                            controller.getType() == Controller.Type.WHEEL ||
                            controller.getType() == Controller.Type.FINGERSTICK
                    ) {
                // Add new controller to the list of all controllers.
                foundControllers.add(controller);

                // Add new controller to the list on the window.
                System.out.println(controller.getName() + " - " + controller.getType().toString() + " type");
            }
        }
    }

    /**
     * Starts showing controller data on the window.
     */
    private void startShowingControllerData() {
        while (true) {
            Controller controller = foundControllers.get(0);

            // Pull controller for current data, and break while loop if controller is disconnected.
            if (!controller.poll()) {
                System.out.println("Controller Disconnected");
                break;
            }

            // X axis and Y axis
            int xAxisPercentage = 0;
            int yAxisPercentage = 0;

            // Go trough all components of the controller.
            Component[] components = controller.getComponents();
            for (int i = 0; i < components.length; i++) {
                Component component = components[i];
                Identifier componentIdentifier = component.getIdentifier();

                // Axes
                if (component.isAnalog()) {
                    float axisValue = component.getPollData();

                    // X axis
                    if (componentIdentifier == Component.Identifier.Axis.X) {
                        pcb.sendMessage("SETROLL "+axisValue);
                        continue; // Go to next component.
                    }
                    // Y axis
                    if (componentIdentifier == Component.Identifier.Axis.Y) {
                        pcb.sendMessage("SETPITCH "+axisValue);
                        continue; // Go to next component.
                    }

                    //Z Axis
                    if (componentIdentifier == Component.Identifier.Axis.Y) {
                        pcb.sendMessage("SETTHROTTLE "+axisValue);
                        continue; // Go to next component.
                    }
                }
            }

            // We have to give processor some rest.
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
