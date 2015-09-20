package eagle.network;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cameron on 9/2/15.
 */
public class ConnectProtoBuf {
    private Socket pingSocket = null;
    private OutputStream out = null;
    private InputStream in = null;
    private String serverName;
    private int id = 0;
    private Map<Integer, String> responseMap = new HashMap<>();

    public ConnectProtoBuf(String servername) {
        this.serverName = servername;
    }

    public void connectToServer() throws NotConnectedException {
        if (out == null || in == null || pingSocket == null || pingSocket.isClosed()) {

            try {
                pingSocket = new Socket(serverName, 2424);
                out = pingSocket.getOutputStream();
                in = pingSocket.getInputStream();
            } catch (IOException e) {
                throw new NotConnectedException();
            }
        }
    }

    public synchronized String sendMessage(String message) throws NotConnectedException {
        try {
            id++;
            EagleProtoBuf.Request request = EagleProtoBuf.Request.newBuilder()
                    .setId(id)
                    .addRequestStrings(message)
                    .build();

            request.writeTo(out);
            while (true) {
                if (responseMap.containsKey(id)) {
                    String responseString = responseMap.get(id);
                    responseMap.remove(id);
                    return responseString;
                }
                EagleProtoBuf.Response response = EagleProtoBuf.Response.parseFrom(in);
                if (response.getId() == id) {
                    return response.getResponseStrings(0);
                } else {
                    responseMap.put(response.getId(), response.getResponseStrings(0));
                }
            }
        } catch (IOException e) {
            throw new NotConnectedException();
        }
    }

    public class NotConnectedException extends Exception {

    }
}
