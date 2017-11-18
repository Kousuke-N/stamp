package com.example.kousuke.stamps;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    static MainActivity instance = null;

    private GoogleApiClient myGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        // google api serviceとの接続
        myGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart(){
        super.onStart();
        myGoogleApiClient.connect();
    }

    @Override
    protected void onStop(){
        myGoogleApiClient.disconnect();
        super.onStop();
    }

    // どこからでもMainActivityを参照できるようにするためのメソッド
    static MainActivity getInstance(){
        return instance;
    }

    // グーグルAPIサービスへの接続成功
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    // グーグルAPIサービスとの接続中断
    @Override
    public void onConnectionSuspended(int i) {
        Log.d("GoogleApiService", "中断");
    }

    // グーグルAPIサービスとの接続失敗
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("GoogleApiService", "失敗");
    }
}
