package eagle.network.protocolBuffer;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import eagle.Log;

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
public class ProtocolBufferClient {
    private Socket socket = null;
    private OutputStream outputStream = null;
    private InputStream inputStream = null;
    private String serverAddress;
    private int id = 0;
    private Map<Integer, ResponseCallBack> responseCallBackMap = new HashMap<>();
    private Thread readThread;

    public ProtocolBufferClient(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void connectToServer() {
        Thread connect = new Thread(new connectThread());
        connect.start();
        try {
            connect.join(3000);
            if (readThread != null && readThread.isAlive()) {
                readThread.interrupt();
            }
            readThread = new Thread(new readThread());
            readThread.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Send a message to the client, disregard the response.
     * @param message
     * @return successfully sent message
     */
    public synchronized boolean sendMessage(String message) {
        return sendMessage(message, null);
    }

    /**
     * Send a message to the client.
     * @param message message to be sent
     * @param rcb callback to contain the response
     * @return successfully sent message
     */
    public synchronized boolean sendMessage(String message, ResponseCallBack rcb) {
        if (!isConnected()) {
            return false;
        }
        id++;
        if (rcb != null) {
            responseCallBackMap.put(id, rcb);
        }
        new Thread(new writeThread(message)).start();
        return true;
    }

    /**
     * Check if connected
     * @return
     */
    public boolean isConnected() {
        if (socket != null && !socket.isClosed())
            return true;
        return false;
    }

    public interface ResponseCallBack {
        void handleResponse(String response);
    }

    private class connectThread implements Runnable {

        @Override
        public void run() {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
            try {
                socket = new Socket(serverAddress, 2324);
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
            } catch (IOException e) {
            }
        }
    }

    private class readThread implements Runnable {

        @Override
        public void run() {
            if (outputStream == null || inputStream == null || socket == null || socket.isClosed()) {

                try {
                    socket = new Socket(serverAddress, 2425);
                    outputStream = socket.getOutputStream();
                    inputStream = socket.getInputStream();
                } catch (IOException e) {
                    return;
                }
            }
            while (true) {
                EagleProtoBuf.Response response = null;
                try {
                    response = EagleProtoBuf.Response.parseDelimitedFrom(inputStream);
                    if (responseCallBackMap.containsKey(response.getId())) {
                        ResponseCallBack rcb = responseCallBackMap.get(response.getId());
                        rcb.handleResponse(response.getResponseStrings(0));
                        responseCallBackMap.remove(response.getId());
                    } else if (response.getType() == EagleProtoBuf.Response.ResponseType.LOG) {
                        Log.log("ProtocolBufferClient", response.getResponseStrings(0));
                    } else {
                        Log.log("ProtocolBufferClient", response.toString());
                    }
                } catch (IOException e) {
                    return;
                }

            }
        }
    }

    private class writeThread implements Runnable {

        String message;

        writeThread(String mess) {
            message = mess;
        }

        @Override
        public void run() {
            EagleProtoBuf.Request request = EagleProtoBuf.Request.newBuilder()
                    .setId(id)
                    .addRequestStrings(message)
                    .build();

            try {
                request.writeDelimitedTo(outputStream);
            } catch (IOException e) {
            }
        }
    }
}
