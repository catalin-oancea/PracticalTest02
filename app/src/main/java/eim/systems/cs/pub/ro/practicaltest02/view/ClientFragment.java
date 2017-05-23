package eim.systems.cs.pub.ro.practicaltest02.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import eim.systems.cs.pub.ro.practicaltest02.R;
import eim.systems.cs.pub.ro.practicaltest02.network.ClientCommunicationThread;

public class ClientFragment extends Fragment {

    private EditText addressEditText;
    private EditText portEditText;
    private EditText queryEditText;
    private Button queryButton;
    private TextView clientTextView;

    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String address = addressEditText.getText().toString();
            String port = portEditText.getText().toString();
            String queryText = queryEditText.getText().toString();
            ClientCommunicationThread clientCommunicationThread = new ClientCommunicationThread(address, port, queryText, clientTextView);
            clientCommunicationThread.start();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        return inflater.inflate(R.layout.fragment_client, parent, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        addressEditText = (EditText)getActivity().findViewById(R.id.server_address_edit_text);
        portEditText = (EditText)getActivity().findViewById(R.id.server_port_edit_text);
        queryEditText = (EditText)getActivity().findViewById(R.id.query_edit_text);
        clientTextView = (TextView)getActivity().findViewById(R.id.client_text_view);
        queryButton = (Button)getActivity().findViewById(R.id.query_button);
        queryButton.setOnClickListener(buttonClickListener);
    }

}
