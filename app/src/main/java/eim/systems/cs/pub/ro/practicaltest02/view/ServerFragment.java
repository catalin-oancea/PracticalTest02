package eim.systems.cs.pub.ro.practicaltest02.view;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import eim.systems.cs.pub.ro.practicaltest02.R;
import eim.systems.cs.pub.ro.practicaltest02.general.Constants;
import eim.systems.cs.pub.ro.practicaltest02.network.ServerThread;

public class ServerFragment extends Fragment {

    private ServerThread serverThread;

    private EditText serverPortEditText;
    private Button startButton;

    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {

            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Log.d(Constants.TAG, "[MAIN] Port should be specified for server!");
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.d(Constants.TAG, "[MAIN] Could not open server socket!");
                return;
            }
            serverThread.start();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        return inflater.inflate(R.layout.fragment_server, parent, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        serverPortEditText = (EditText)getActivity().findViewById(R.id.port_edit_text);
        startButton = (Button)getActivity().findViewById(R.id.start_button);

        startButton.setOnClickListener(buttonClickListener);
    }

    @Override
    public void onDestroy() {
        if (serverThread != null) {
            serverThread.stopServer();
        }
        super.onDestroy();
    }

}
