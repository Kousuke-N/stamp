package com.example.kousuke.stamps;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity{

    static MainActivity instance = null;

    private GoogleApiConnector googleApiConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);

        googleApiConnector = new GoogleApiConnector();
        googleApiConnector.build(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        googleApiConnector.connect();
    }

    @Override
    protected void onStop(){
        googleApiConnector.disconnect();
        super.onStop();
    }

    // どこからでもMainActivityを参照できるようにするためのメソッド
    static MainActivity getInstance(){
        return instance;
    }
}
