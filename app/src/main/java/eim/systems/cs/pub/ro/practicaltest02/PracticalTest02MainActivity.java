package eim.systems.cs.pub.ro.practicaltest02;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import eim.systems.cs.pub.ro.practicaltest02.view.ClientFragment;
import eim.systems.cs.pub.ro.practicaltest02.view.ServerFragment;

public class PracticalTest02MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.server_frame_layout, new ServerFragment());
        fragmentTransaction.add(R.id.client_frame_layout, new ClientFragment());
        fragmentTransaction.commit();
    }
}
