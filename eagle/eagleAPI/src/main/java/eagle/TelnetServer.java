package eagle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


/**
 * Telnet Server Class
 *
 * @author Cameron Cross
 * @version 0.0.1
 * @since 04/09/2015
 * <p/>
 * Date Modified	04/09/2015 - Cameron
 */
public class TelnetServer implements Runnable, Log.LogCallback {

    Drone drone;

    Vector<TelnetConnectionHandler> telnetSessions = new Vector<TelnetConnectionHandler>();


    public TelnetServer(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(2424);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                //start new thread for session
                TelnetConnectionHandler tch = new TelnetConnectionHandler(clientSocket, drone);
                new Thread(tch).start();
                telnetSessions.add(tch);
                //cleanup dead connnections.
                for (TelnetConnectionHandler telnetConnectionHandler : telnetSessions) {
                    if (!telnetConnectionHandler.isConnected()) {
                        telnetSessions.remove(telnetConnectionHandler);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(String message) {
        for (TelnetConnectionHandler tch : telnetSessions) {
            if (tch.isConnected()) {
                tch.handleMessage(message);
            } else {
                telnetSessions.remove(tch);
            }
        }
    }

    class TelnetConnectionHandler extends Thread {
        Drone drone;
        Socket socket;
        PrintWriter out;
        boolean connected;

        TelnetConnectionHandler(Socket socket, Drone drone) {
            this.socket = socket;
            this.drone = drone;
            connected = true;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    try {
                        ScriptingEngine scriptingEngine = drone.getScriptingEngine();
                        if (scriptingEngine != null) {
                            out.println(scriptingEngine.executeInstruction(inputLine));
                        }
                    } catch (ScriptingEngine.InvalidInstructionException e) {
                        out.println("Invalid Command: " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                connected = false;
            }
        }

        public void handleMessage(String message) {
            if (out != null) {
                out.println(message);
            }
        }

        public boolean isConnected() {
            return connected;
        }
    }
}
