package com.example.kousuke.stamps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

/**
 * Mapを表示するフラグメント
 * Created by kousuke nezu on 2017/11/18.
 */

public class MapExample extends MapFragment
        implements LocationListener, OnMapReadyCallback {

    // MainActivityのコンテキスト
    private Context context;

    // グーグルマップ
    private GoogleMap googleMap;

    // 現在地を監視するマネージャ
    private LocationManager locationManager;

    private static final int MIN_TIME = 1000;
    private static final float MIN_DISTANCE = 1;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        context = MainActivity.getInstance().getApplicationContext();

        // locationManagerインスタンス作成
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // GPSの監視開始
        startGPS();

        // Map情報取得
        this.getMapAsync(this);
    }

    @Override
    public void onPause() {
        if (locationManager != null) {
            stopGPS();
        } else {
            // TODO:locationManagerがnullの時の挙動
        }
        super.onPause();
    }

    private void stopGPS() {
        if (locationManager != null) {
            // update を止める
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(this);
        }
    }

    private void startGPS() {
        Log.d("MapExample", "GPS観測開始");
        final boolean gpsEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            // TODO:GPSを設定するように促す
            // enableLocationSettings();
        }
        if (locationManager != null) {
            try {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(context,
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME, MIN_DISTANCE, this);
            } catch (Exception e) {
                e.printStackTrace();

                Toast toast = Toast.makeText(context,
                        "例外が発生位置情報のPermisssionを許可していますか？", Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            //TODO: LocationManagerがnullの時の挙動
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("Main", String.format("Current Locatin is %s,%s",
                String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude())));
        // 現在地の表示を変える処理
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), R.integer.map_scale_rate);
        googleMap.moveCamera(cameraUpdate);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    // 地図の準備ができた時に呼ばれるコールバック関数
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // パーミッションの確認
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // ローカル変数に入れる
            this.googleMap = googleMap;

            // 地図の表示を変えるクラス
            CameraUpdate cameraUpdate;

            // 前回の位置を判定
            if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()),
                        R.integer.map_scale_rate);
            } else {
                cameraUpdate = CameraUpdateFactory.zoomBy(R.integer.map_scale_rate);
            }
            googleMap.moveCamera(cameraUpdate);
            // 地図を有効化する
            googleMap.setMyLocationEnabled(true);
        } else {
            // TODO: エラー時の挙動を実装
            // Show rationale and request permission.
        }
    }
}
