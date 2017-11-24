package com.example.kousuke.stamps;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;

public class MyMapFragment extends MapFragment
        implements LocationListener{

    // LocationListenerによるimplements ************************************************************

    // 位置情報更新時
    @Override
    public void onLocationChanged(Location location) {

    }

    // ロケーションプロバイダが利用可能になった時
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    // ロケーションプロバイダが利用不可になった時
    @Override
    public void onProviderEnabled(String provider) {

    }

    // ロケーションステータスが変化時
    @Override
    public void onProviderDisabled(String provider) {

    }

    // *********************************************************************************************
}
