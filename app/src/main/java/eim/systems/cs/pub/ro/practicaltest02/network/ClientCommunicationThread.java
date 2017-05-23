package eim.systems.cs.pub.ro.practicaltest02.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import eim.systems.cs.pub.ro.practicaltest02.general.Constants;
import eim.systems.cs.pub.ro.practicaltest02.general.Utilities;

public class ClientCommunicationThread extends Thread {

    private Socket socket = null;

    private String address;
    private String port;
    private String queryText;
    private TextView clientTextView;

    public ClientCommunicationThread(String address, String port, String queryText, TextView clientTextView) {
        this.address = address;
        this.port = port;
        this.queryText = queryText;
        this.clientTextView = clientTextView;
    }

    public void run() {
        if (socket == null) {
            try {
                socket = new Socket(address, Integer.parseInt(port));
                Log.d(Constants.TAG, "[CLIENT] Created communication thread with: " + socket.getInetAddress() + ":" + socket.getLocalPort());
            } catch (UnknownHostException unknownHostException) {
                Log.e(Constants.TAG, "An exception has occurred: " + unknownHostException.getMessage());
                if (Constants.DEBUG) {
                    unknownHostException.printStackTrace();
                }
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
        try {
            BufferedReader reader = Utilities.getReader(socket);
            PrintWriter writer = Utilities.getWriter(socket);

            if (queryText == null || queryText.isEmpty()) {
                Log.d(Constants.TAG, "[CLIENT] Should not sent empty message!");
            }

            writer.println(queryText);
            writer.flush();

            final String result = reader.readLine();

            if (result.equals("NO.")) {
                clientTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        clientTextView.setText("Too many request from this client in 1 minute timeframe.");
                    }
                });
            } else {
                clientTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        clientTextView.setText(result);
                    }
                });
            }

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
        finally {
            try {
                socket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}