package com.example.kousuke.stamps;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity
        implements AppCompatCallback, LocationListener, OnMapReadyCallback {

    private String TAG = "MainActivity";

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    private MapFragment mapFragment;
    private GoogleMap map;

    private FusedLocationProviderClient mFusedLocationClient;

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

        // 地図のフラグメントを取得（設定は先）
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        // mapFragmentの設定
        mapFragment.getMapAsync(this);


        // GPS(locationManager)の設定
        // 権限がないとき
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                requestPermission();
            } else {
                // 権限要求
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        } else {
            // locationManagerの設定
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            String locationProvider = null;

            // GPSが利用可能かチェック
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationProvider = LocationManager.GPS_PROVIDER;
            }
            // GPSプロバイダが有効になっていない場合
            else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationProvider = LocationManager.NETWORK_PROVIDER;
            }
            // いずれも利用可能でない場合はGPSを設定する画面に遷移
            else {
                Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settingsIntent);
                return;
            }


            // 位置情報を通知するための最小時間間隔（ミリ秒）
            final int fastestInterval = 500;
            final int interval = 1000;
            // 位置情報を通知するための最小距離間隔（メートル）
            final long minDistance = 1;

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {

                            }
                        }
                    });
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(interval);
            mLocationRequest.setFastestInterval(fastestInterval);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
        }
    }

    // Permission handling for Android 6.0
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            Log.d("MainActivity", "shouldShowRequestPermissionRationale:追加説明");
            // 権限チェックした結果、持っていない場合はダイアログを出す
            new AlertDialog.Builder(this)
                    .setTitle("パーミッションの追加説明")
                    .setMessage("このアプリを使用するにはパーミッションが必要です")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                        }
                    })
                    .create()
                    .show();
            return;
        }

        // 権限を取得する
        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        return;
    }

    @Override
    public void onResume(){
        super.onResume();
    }



// OnMapReadyCallback ******************************************************************************

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                requestPermission();
            } else {
                // 権限要求
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        } else {
            map.setMyLocationEnabled(true);
        }
    }

// *************************************************************************************************

// LocationListener ********************************************************************************

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
// *************************************************************************************************

// AppCompatActivity *******************************************************************************

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_FINE_LOCATION) {
            if (grantResults.length != 1 ||
                    grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult:DENYED");

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) ) {
                    Log.d(TAG, "[show error]");
                    new AlertDialog.Builder(this)
                            .setTitle("パーミッション取得エラー")
                            .setMessage("再試行する場合は、再度Requestボタンを押してください")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermission();
                                }
                            })
                            .create()
                            .show();

                } else {
                    Log.d(TAG, "[show app settings guide]");
                    new AlertDialog.Builder(this)
                            .setTitle("パーミッション取得エラー")
                            .setMessage("今後は許可しないが選択されました。アプリ設定＞権限をチェックしてください（権限をON/OFFすることで状態はリセットされます）")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    openSettings();
                                }
                            })
                            .create()
                            .show();
                }
            } else {
                Log.d(TAG, "onRequestPermissionsResult:GRANTED");
                // TODO 許可された
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        //Fragmentの場合はgetContext().getPackageName()
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
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
// *************************************************************************************************
}
