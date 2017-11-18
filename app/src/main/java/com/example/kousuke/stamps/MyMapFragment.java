package com.example.kousuke.stamps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by kousuke nezu on 2017/11/18.
 */

public class MyMapFragment extends MapFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        // Map情報取得
        this.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (ContextCompat.checkSelfPermission(MainActivity.getInstance().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    // 地図を有効化する
                    googleMap.setMyLocationEnabled(true);
                } else {
                    // TODO: エラー時の挙動を実装
                    // Show rationale and request permission.
                }
            }
        });
    }
}
