package com.example.kousuke.stamps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements AppCompatCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ツールバーの設定
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        FloatingActionButton fab = findViewById(R.id.fab_to_change_list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });

        MyMapFragment myMapFragment = (MyMapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);


        // GPS(locationManager)の設定
        // 権限がないとき
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // TODO: ユーザに権限の説明が必要な場合
                // ユーザが権限拒否やデバイスとして使えない場合falseを返すので注意
            } else {
                final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
                // 権限要求
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }

        } else {
            // locationManagerの設定
            LocationManager locationManager= (LocationManager)getSystemService(LOCATION_SERVICE);
            String locationProvider = null;

            // GPSが利用可能かチェック
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                locationProvider = LocationManager.GPS_PROVIDER;
            }
            // GPSプロバイダが有効になっていない場合
            else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                locationProvider = LocationManager.NETWORK_PROVIDER;
            }
            // いずれも利用可能でない場合はGPSを設定する画面に遷移
            else{
                Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settingsIntent);
                return;
            }

            // 位置情報を通知するための最小時間間隔（ミリ秒）
            final long minTime = 500;
            // 位置情報を通知するための最小距離間隔（メートル）
            final long minDistance = 1;

            // 利用可能なロケーションプロバイダによる位置情報の取得の開始
            // FIXME: 本来であれば、リスナが複数回登録されないようにチェックする必要がある
            locationManager.requestLocationUpdates(locationProvider, minTime, minDistance, myMapFragment);
            // 最新の位置情報
            Location location = locationManager.getLastKnownLocation(locationProvider);

            if(location != null){
                Log.i("MainActivity", location.getLatitude() + ":" + String.valueOf(location.getLongitude()));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){

    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
        //let's leave this empty, for now
        super.onSupportActionModeStarted(mode);
    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
        // let's leave this empty, for now
        super.onSupportActionModeFinished(mode);
    }
}
