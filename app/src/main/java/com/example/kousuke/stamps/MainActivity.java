package com.example.kousuke.stamps;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class MainActivity extends AppCompatActivity {

    MapFragment mf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //        mf= MapFragment.newInstance();
        //
        //        FragmentManager fm = getFragmentManager();
        //        FragmentTransaction ft = fm.beginTransaction();
        //        ft.add(android.R.id.content, mf);
        //        ft.commit();
    }
}
