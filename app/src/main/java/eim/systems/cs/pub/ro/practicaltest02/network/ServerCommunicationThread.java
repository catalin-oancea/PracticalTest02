package eim.systems.cs.pub.ro.practicaltest02.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import eim.systems.cs.pub.ro.practicaltest02.general.Constants;
import eim.systems.cs.pub.ro.practicaltest02.general.Utilities;
import eim.systems.cs.pub.ro.practicaltest02.model.StockModel;

public class ServerCommunicationThread extends Thread {

    private Socket socket;
    private ServerThread st;

    public ServerCommunicationThread(Socket socket, ServerThread st) {
        if (socket != null) {
            this.socket = socket;
            Log.d(Constants.TAG, "[COMM SERVER] Created communication thread with: " + socket.getInetAddress() + ":" + socket.getLocalPort());
        }
        if (st != null) {
            this.st = st;
        }
    }

    public boolean dateDifference(Date d1, Date d2)
    {
        long currentDateMilliSec = d1.getTime();
        long updateDateMilliSec = d2.getTime();
        long diffDays = (currentDateMilliSec - updateDateMilliSec) / (24 * 60 * 60 * 1000);
        Log.d(Constants.TAG, Long.toString(diffDays));
        if (diffDays <= 1) return true;
        else return false;
    }

    public void run() {
        try {
            BufferedReader reader = Utilities.getReader(socket);
            PrintWriter writer = Utilities.getWriter(socket);

            String queryText = reader.readLine();
            Date currDate = new Date();

            if (st.dataCache.containsKey(queryText)) {
                if (dateDifference(currDate, st.dataCache.get(queryText).date)) {
                    writer.println("FROM CACHE Value: "+st.dataCache.get(queryText).value + " Time: "+st.dataCache.get(queryText).date.toString());
                    writer.flush();
                } else {
                    Log.d(Constants.TAG, "[COMM SERVER] Getting information from yahoo");
                    Log.d(Constants.TAG, "Here1");
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(Constants.API_SERVER + queryText + "&f=l1t1");
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    String pageSourceCode = httpClient.execute(httpGet, responseHandler);

                    Log.i(Constants.TAG, pageSourceCode);
                    String[] stockResult = pageSourceCode.split(",");

                    writer.println("Value: "+stockResult[0] +", Time: " +stockResult[1]);
                    writer.flush();
                    st.dataCache.remove(queryText);
                    StockModel smod = new StockModel(queryText, stockResult[0], currDate);
                    st.dataCache.put(queryText, smod);
                }
            } else {
                Log.d(Constants.TAG, "[COMM SERVER] Getting information from yahoo");
                Log.d(Constants.TAG, "Here1");
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(Constants.API_SERVER + queryText + "&f=l1t1");
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String pageSourceCode = httpClient.execute(httpGet, responseHandler);

                Log.i(Constants.TAG, pageSourceCode);
                String[] stockResult = pageSourceCode.split(",");

                writer.println("Value: "+stockResult[0] +", Time: " +stockResult[1]);
                writer.flush();
                st.dataCache.remove(queryText);
                StockModel smod = new StockModel(queryText, stockResult[0], currDate);
                st.dataCache.put(queryText, smod);
            }



        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
        finally {
            if (socket != null) {
                try {
                    socket.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
