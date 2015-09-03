package eagle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by cameron on 9/2/15.
 */
public class TelnetServer implements Runnable, Log.LogCallback {

    Drone drone;
    PrintWriter out;

    public TelnetServer(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(2424);

            Socket clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                try {
                    ScriptingEngine scriptingEngine = drone.getScriptingEngine();
                    if (scriptingEngine != null) {
                        out.println(scriptingEngine.executeInstruction(inputLine));
                    }
                }
                catch (ScriptingEngine.InvalidInstructionException e) {
                    out.println("Invalid Command: "+e.getMessage());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
}
