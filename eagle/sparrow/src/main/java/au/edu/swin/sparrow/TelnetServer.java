package au.edu.swin.sparrow;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import eagle.Drone;
import eagle.Log;

/**
 * Created by cameron on 8/27/15.
 */
public class TelnetServer extends AsyncTask implements Log.LogCallback {

    Drone drone;
    PrintWriter out;

    public TelnetServer(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void handleMessage(String s) {
        if (out != null) {
            out.println(s);
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(2424);

            Socket clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                try {
                    out.println(drone.executeInstruction(inputLine));
                }
                catch (Drone.InvalidInstructionException e) {
                    out.println(e.getMessage());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
