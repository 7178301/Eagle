package eagle.network.protocolBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import eagle.Log;
import eagle.network.ScriptingEngine;


/**
 * Protocol Buffer Client Class
 * Uses protocol buffers to wrap commands and responses between the drone and a client. Much more reliable.
 *
 * @author Cameron Cross
 * @version 0.0.1
 * @since 04/09/2015
 * <p/>
 * Date Modified	04/09/2015 - Cameron
 */
public class ProtocolBufferServer {

    private ScriptingEngine scriptingEngine;
    private Thread serverThread = null;
    private int incomingPort;

    public ProtocolBufferServer(ScriptingEngine scriptingEngine, int incomingPort) {
        this.scriptingEngine = scriptingEngine;
        this.incomingPort = incomingPort;
        serverThread = new Thread(new ProtocolBufferServerThread());
        serverThread.start();
    }

    public class ProtocolBufferServerThread implements Runnable {

        public ServerSocket serverSocket = null;
        Thread clientThread = null;

        @Override
        public void run() {
            while (true) {
                try {
                    serverSocket = new ServerSocket(incomingPort);
                    Log.log("ProtocolBufferServer", "Server Started");
                    while (!serverSocket.isClosed()) {

                        clientThread = new Thread(new ProtocolBufferServerCommunicationsThread(serverSocket, scriptingEngine));
                        clientThread.start();
                        while (clientThread != null && clientThread.getState() != Thread.State.TERMINATED) {
                            Thread.sleep(10);
                        }
                    }
                    serverSocket = new ServerSocket(incomingPort);
                    Log.log("ProtocolBufferServer", "Server Re-Started");
                } catch (IOException | InterruptedException e) {
                }
            }
        }
    }

    class ProtocolBufferServerCommunicationsThread extends Thread {
        ScriptingEngine scriptingEngine;
        ServerSocket serverSocket;

        ProtocolBufferServerCommunicationsThread(ServerSocket serverSocket, ScriptingEngine scriptingEngine) {
            this.serverSocket = serverSocket;
            this.scriptingEngine = scriptingEngine;
            try {
                serverSocket.setSoTimeout(100);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                Socket clientSocket = serverSocket.accept();
                Log.log("ProtocolBufferServer", "Client Connected: " + clientSocket.getRemoteSocketAddress().toString());
                OutputStream outputStream = clientSocket.getOutputStream();
                InputStream in = clientSocket.getInputStream();

                while (clientSocket != null && !clientSocket.isClosed()) {
                    EagleProtoBuf.Request request = EagleProtoBuf.Request.parseDelimitedFrom(in);
                    try {
                        if (scriptingEngine != null && request.getRequestStringsCount() > 0) {
                            EagleProtoBuf.Response response = EagleProtoBuf.Response.newBuilder()
                                    .setId(request.getId())
                                    .setType(EagleProtoBuf.Response.ResponseType.COMMAND)
                                    .addResponseStrings(scriptingEngine.executeInstruction(request.getRequestStrings(0)))
                                    .build();
                            response.writeDelimitedTo(outputStream);
                        } else {
                            EagleProtoBuf.Response response = EagleProtoBuf.Response.newBuilder()
                                    .setId(request.getId())
                                    .setType(EagleProtoBuf.Response.ResponseType.OTHER)
                                    .addResponseStrings("Could not execute command")
                                    .build();
                            response.writeDelimitedTo(outputStream);
                            Log.log("ProtocolBufferServer", "Could not execute command");
                        }
                    } catch (ScriptingEngine.InvalidInstructionException e) {
                        EagleProtoBuf.Response response = EagleProtoBuf.Response.newBuilder()
                                .setId(0)
                                .setType(EagleProtoBuf.Response.ResponseType.OTHER)
                                .addResponseStrings("Invalid Command: " + e.getMessage())
                                .build();
                        response.writeDelimitedTo(outputStream);
                        Log.log("ProtocolBufferServer", "Invalid Command: " + e.getMessage());
                    }
                }
                Log.log("ProtocolBufferServer", "Client Disconnected: " + clientSocket.getRemoteSocketAddress().toString());
            } catch (IOException e) {
            }
        }
    }
}
