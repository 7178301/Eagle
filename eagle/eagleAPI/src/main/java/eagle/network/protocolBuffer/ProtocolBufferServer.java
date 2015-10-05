package eagle.network.protocolBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;

import eagle.Drone;
import eagle.Log;
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
public class ProtocolBufferServer{

    private Drone drone;
    private Thread serverThread = null;
    private int incomingPort;

    public ProtocolBufferServer(Drone drone, int incomingPort) {
        this.drone = drone;
        this.incomingPort=incomingPort;
        serverThread = new Thread(new ProtocolBufferServerThread());
        serverThread.start();
    }

    public void stop(){
        serverThread.interrupt();
    }

    public class ProtocolBufferServerThread implements Runnable {

        public ServerSocket serverSocket = null;
        Thread clientThread=null;
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(incomingPort);
                while (!serverSocket.isClosed()) {

                    clientThread = new Thread(new ProtocolBufferServerCommunicationsThread(serverSocket, drone));
                    clientThread.start();
                    while (clientThread!=null&&clientThread.getState()!= Thread.State.TERMINATED){
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

    class ProtocolBufferServerCommunicationsThread extends Thread {
        ScriptingEngine scriptingEngine;
        ServerSocket serverSocket;

        ProtocolBufferServerCommunicationsThread(ServerSocket serverSocket, Drone drone) {
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
            while(!serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    OutputStream outputStream = clientSocket.getOutputStream();
                    InputStream in = clientSocket.getInputStream();

                    while (!serverSocket.isClosed()) {
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
                    if(clientSocket!=null&&!clientSocket.isClosed())
                        clientSocket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
