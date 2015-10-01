package eagle.network.protocolBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import eagle.Drone;
import eagle.Log;
import eagle.LogCallback;
import eagle.network.ScriptingEngine;


/**
 * Network Server Class
 *
 * @author Cameron Cross
 * @version 0.0.1
 * @since 04/09/2015
 * <p/>
 * Date Modified	04/09/2015 - Cameron
 */
public class ProtocolBufferServer implements Runnable{

    Drone drone;

    Vector<NetworkConnectionHandler> networkSessions = new Vector<NetworkConnectionHandler>();


    public ProtocolBufferServer(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(2425);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                //start new thread for session
                NetworkConnectionHandler tch = new NetworkConnectionHandler(clientSocket, drone);
                new Thread(tch).start();
                networkSessions.add(tch);
                //cleanup dead connnections.
                for (NetworkConnectionHandler networkConnectionHandler : networkSessions) {
                    if (!networkConnectionHandler.isConnected()) {
                        networkSessions.remove(networkConnectionHandler);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class NetworkConnectionHandler extends Thread {
        ScriptingEngine scriptingEngine;
        Socket socket;
        OutputStream outputStream;
        boolean connected;

        NetworkConnectionHandler(Socket socket, Drone drone) {
            this.socket = socket;
            this.scriptingEngine = drone.getScriptingEngine();
            connected = true;
        }

        @Override
        public void run() {
            try {
                outputStream = socket.getOutputStream();
                InputStream in = socket.getInputStream();


                while (true) {
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
                    }
                    catch (ScriptingEngine.InvalidInstructionException e) {
                        EagleProtoBuf.Response response = EagleProtoBuf.Response.newBuilder()
                                .setId(0)
                                .setType(EagleProtoBuf.Response.ResponseType.OTHER)
                                .addResponseStrings("Invalid Command: " + e.getMessage())
                                .build();
                        response.writeDelimitedTo(outputStream);
                        Log.log("ProtocolBufferServer", "Invalid Command: " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                connected = false;
            }
        }


        public boolean isConnected() {
            return connected;
        }
    }
}
