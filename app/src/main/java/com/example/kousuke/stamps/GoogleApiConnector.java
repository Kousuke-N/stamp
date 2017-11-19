package com.example.kousuke.stamps;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kousuke nezu on 2017/11/19.
 */

public class GoogleApiConnector
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    static GoogleApiClient googleApiClient;
    private boolean isConnected = false;

    public void build(MainActivity mainActivity) {
        googleApiClient = new GoogleApiClient
                .Builder(mainActivity)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void connect() {
        googleApiClient.connect();
        isConnected = true;
    }

    public void disconnect() {
        googleApiClient.disconnect();
        isConnected = false;
    }

    // GoogleApiClientをどこでも取得できるようにするメソッド
    static GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    // グーグルAPIサービスとの接続成功
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        isConnected = true;

        if (ContextCompat.checkSelfPermission(MainActivity.getInstance().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // 現在地の取得
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                    .getCurrentPlace(googleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(@NonNull PlaceLikelihoodBuffer placeLikelihoods) {
                    for ( PlaceLikelihood placeLikelihood : placeLikelihoods ) {
                        Log.i("PickerTest", String.format( "Place '%s' has likelihood: %g",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()) );

                    }
                    placeLikelihoods.release();
                }
            });
        } else {
            // TODO: エラー時の挙動を実装
            // Show rationale and request permission.
        }

    }

    // グーグルAPIサービスとの接続中断
    @Override
    public void onConnectionSuspended(int i) {
        isConnected = false;
        Log.d("GoogleApiService", "中断");
    }

    // グーグルAPIサービスとの接続失敗
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        isConnected = false;
        Log.d("GoogleApiService", "失敗");
    }
}
