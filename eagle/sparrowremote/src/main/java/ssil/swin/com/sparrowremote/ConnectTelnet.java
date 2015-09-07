package ssil.swin.com.sparrowremote;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.TimerTask;

/**
 * Created by cameron on 9/2/15.
 */
public class ConnectTelnet {
    Socket pingSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    String serverName;

    ConnectTelnet(String servername) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        this.serverName = servername;
    }

    void connectToServer() throws NotConnectedException {
        if (out == null || in == null || pingSocket == null || pingSocket.isClosed()) {

            try {
                pingSocket = new Socket(serverName, 2424);
                out = new PrintWriter(pingSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(pingSocket.getInputStream()));
            } catch (IOException e) {
                throw new NotConnectedException();
            }
        }
    }

    synchronized String sendMessage(String message) throws NotConnectedException {
        out.println(message);
        try {
            if (in.ready()) {
                return in.readLine();
            }
        } catch (IOException e) {
            throw new NotConnectedException();
        }

        return null;
    }

    class NotConnectedException extends Exception {

    }
}
