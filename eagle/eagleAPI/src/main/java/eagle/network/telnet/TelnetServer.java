package eagle.network.telnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import eagle.logging.Log;
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

    private ScriptingEngine scriptingEngine;
    private Thread serverThread = null;
    private int incomingPort;

    public TelnetServer(ScriptingEngine scriptingEngine, int incomingPort) {
        this.scriptingEngine = scriptingEngine;
        this.incomingPort = incomingPort;
        serverThread = new Thread(new TelnetServerThread());
        serverThread.start();
    }

    public class TelnetServerThread implements Runnable {

        public ServerSocket serverSocket = null;
        Thread clientThread = null;

        @Override
        public void run() {
            while (true) {
                try {
                    serverSocket = new ServerSocket(incomingPort);
                    Log.log("TelnetServer", "Server Started");
                    while (!serverSocket.isClosed()) {

                        clientThread = new Thread(new TelnetServerCommunicationThread(serverSocket, scriptingEngine));
                        clientThread.start();
                        while (clientThread != null && clientThread.getState() != Thread.State.TERMINATED) {
                            Thread.sleep(10);
                        }
                    }
                    serverSocket = new ServerSocket(incomingPort);
                    Log.log("TelnetServer", "Server Re-Started");
                } catch (IOException | InterruptedException e) {
                }
            }
        }
    }

    class TelnetServerCommunicationThread extends Thread {
        ScriptingEngine scriptingEngine;
        ServerSocket serverSocket;

        TelnetServerCommunicationThread(ServerSocket serverSocket, ScriptingEngine scriptingEngine) {
            this.serverSocket = serverSocket;
            this.scriptingEngine = scriptingEngine;
        }

        @Override
        public void run() {
            try {
                Socket clientSocket = serverSocket.accept();
                Log.log("TelnetServer", "Client Connected: " + clientSocket.getRemoteSocketAddress().toString());
                PrintWriter outgoing = new PrintWriter(clientSocket.getOutputStream(), true);
                outgoing.println("CONNECTED");
                BufferedReader incoming = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = incoming.readLine()) != null && !clientSocket.isClosed()) {
                    try {
                        if (scriptingEngine != null) {
                            String result = scriptingEngine.executeInstruction(inputLine);
                            outgoing.println(result);
                            Log.log("TelnetServer", "REQUEST: " + inputLine + " RESULT: " + result);
                        }
                    } catch (ScriptingEngine.InvalidInstructionException e) {
                        outgoing.println("Invalid Command: " + e.getMessage());
                        Log.log("TelnetServer", "REQUEST: " + inputLine + " RESULT: " + e.getMessage());
                    }
                }
                Log.log("TelnetServer", "Client Disconnected: " + clientSocket.getRemoteSocketAddress().toString());

            } catch (IOException e) {
            }
        }
    }
}
