package eagle.network;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import eagle.Log;

/**
 * Created by cameron on 9/2/15.
 */
public class ConnectDroneServer {
    private Socket pingSocket = null;
    private OutputStream out = null;
    private InputStream in = null;
    private String serverName;
    private int id = 0;
    private Map<Integer, ResponseCallBack> responseMap = new HashMap<>();
    private Thread read;
    private boolean connected = false;

    public ConnectDroneServer(String servername) {
        this.serverName = servername;
    }

    public void connectToServer() {
        Thread connect = new Thread(new connectThread());
        connect.start();
        try {
            connect.join(3000);
            if (read != null && read.isAlive()) {
                read.interrupt();
            }
            read = new Thread(new readThread());
            read.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public synchronized boolean sendMessage(String message) {
        return sendMessage(message, null);
    }

    public synchronized boolean sendMessage(String message, ResponseCallBack rcb) {
        if (connected == false) {
            return false;
        }
        id++;
        if (rcb != null) {
            responseMap.put(id, rcb);
        }
        new Thread(new writeThread(message)).start();
        return true;
    }

    public boolean isConnected() {
        return connected;
    }

    public interface ResponseCallBack {
        void handleResponse(String response);
    }

    private class connectThread implements Runnable {

        @Override
        public void run() {
            connected = false;
            if (pingSocket != null) {
                try {
                    pingSocket.close();
                } catch (IOException e) {
                }
            }
            try {
                pingSocket = new Socket(serverName, 2425);
                out = pingSocket.getOutputStream();
                in = pingSocket.getInputStream();
                connected = true;
            } catch (IOException e) {
                connected = false;
            }
        }
    }

    private class readThread implements Runnable {

        @Override
        public void run() {
            if (out == null || in == null || pingSocket == null || pingSocket.isClosed()) {

                try {
                    pingSocket = new Socket(serverName, 2425);
                    out = pingSocket.getOutputStream();
                    in = pingSocket.getInputStream();
                } catch (IOException e) {
                    connected = false;
                    return;
                }
            }
            while (true) {
                EagleProtoBuf.Response response = null;
                try {
                    response = EagleProtoBuf.Response.parseDelimitedFrom(in);
                    if (responseMap.containsKey(response.getId())) {
                        ResponseCallBack rcb = responseMap.get(response.getId());
                        rcb.handleResponse(response.getResponseStrings(0));
                        responseMap.remove(response.getId());
                    } else if (response.getType() == EagleProtoBuf.Response.ResponseType.LOG) {
                        Log.log(response.getResponseStrings(0));
                    } else {
                        Log.log(response.toString());
                    }
                } catch (IOException e) {
                    connected = false;
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
                request.writeDelimitedTo(out);
            } catch (IOException e) {
                connected = false;
            }
        }
    }
}
