package eagle.network.telnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import eagle.Drone;
import eagle.Log;
import eagle.network.ScriptingEngine;


/**
 * Telnet Server Class
 *
 * @author Cameron Cross
 * @version 0.0.1
 * @since 04/09/2015
 * <p/>
 * Date Modified	04/09/2015 - Cameron
 */
public class TelnetServer {

    private Drone drone;
    private Thread serverThread = null;
    private int incomingPort;

    public TelnetServer(Drone drone, int incomingPort) {
        this.drone = drone;
        this.incomingPort=incomingPort;
        serverThread = new Thread(new TelnetServerThread());
        serverThread.start();
    }

    public void stop() {
        serverThread.interrupt();
    }

    public class TelnetServerThread implements Runnable {

        public ServerSocket serverSocket = null;
        Thread clientThread = null;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(incomingPort);
                while (!serverSocket.isClosed()) {

                    clientThread = new Thread(new TelnetServerCommunicationThread(serverSocket, drone));
                    clientThread.start();
                    while (clientThread != null && clientThread.getState() != Thread.State.TERMINATED) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            serverSocket.close();
                            clientThread.interrupt();
                        }
                    }
                }
            } catch (IOException e) {
            }
        }
    }

    class TelnetServerCommunicationThread extends Thread {
        ScriptingEngine scriptingEngine;
        ServerSocket serverSocket;

        TelnetServerCommunicationThread(ServerSocket serverSocket, Drone drone) {
            this.serverSocket = serverSocket;
            this.scriptingEngine = drone.getScriptingEngine();
            try {
                serverSocket.setSoTimeout(100);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (!serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        try {
                            ScriptingEngine scriptingEngine = drone.getScriptingEngine();
                            if (scriptingEngine != null) {
                                String result = scriptingEngine.executeInstruction(inputLine);
                                out.println(result);
                                Log.log("TelnetServer", result);
                            }
                        } catch (ScriptingEngine.InvalidInstructionException e) {
                            out.println("Invalid Command: " + e.getMessage());
                            Log.log("TelnetServer", "Invalid Command: " + e.getMessage());
                        }
                    }
                    if (clientSocket != null && !clientSocket.isClosed())
                        clientSocket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
