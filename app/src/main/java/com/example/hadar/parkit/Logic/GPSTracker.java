package com.example.hadar.parkit.Logic;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {
    private static final String TAG =GPSTracker.class.getSimpleName();
    private final int PERMISSION_LOCATION_CODE=1234;
    private final Activity activity;
    private boolean isGPSEnabled =false;
    private boolean isNetworkEnabled =false;
    private boolean isGPSPermited=true;
    private Location location;
    protected LocationManager locationManager;

    public GPSTracker(Activity activity, boolean firstAction) {
        this.activity = activity;
        if (firstAction==true)
            setFirstCounter();
        initLocation();
    }

    //Create a GetLocation Method //
    public  void initLocation() {
        try {
            locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
            isNetworkEnabled=locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // for new versions
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {

                    requestLocation();
                }
                else {
                    boolean alreadyAsked = getCounter(PERMISSION_LOCATION_CODE) > 0;
                    if (!alreadyAsked) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_CODE);
                        incrementCounter(PERMISSION_LOCATION_CODE);
                    }
                    else {
                        isGPSPermited=false;
                        activity.finish();
                    }
                }
            }
            else {
                // for older versions
                requestLocation();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void setFirstCounter() {
        SharedPreferences sharedPreferences=activity.getSharedPreferences("permissionCounters", Context.MODE_PRIVATE);
        String sharedPreferencesKey=String.valueOf(PERMISSION_LOCATION_CODE);
        int permissionCounter=0;
        sharedPreferences.edit().putInt(sharedPreferencesKey, permissionCounter).apply();
    }

    private int getCounter(int permissionCode) {
        SharedPreferences sharedPreferences=activity.getSharedPreferences("permissionCounters", Context.MODE_PRIVATE);
        String sharedPreferencesKey=String.valueOf(permissionCode);
        int permissionCounter=sharedPreferences.getInt(sharedPreferencesKey, 0);
        Log.d(TAG, "counter value (get counter): " +permissionCounter);
        return permissionCounter;
    }

    public void incrementCounter(int permissionCode) {
        SharedPreferences sharedPreferences=activity.getSharedPreferences("permissionCounters", Context.MODE_PRIVATE);
        String sharedPreferencesKey=String.valueOf(permissionCode);
        int permissionCounter=sharedPreferences.getInt(sharedPreferencesKey, 0);
        Log.d(TAG, "counter value (increment): " +permissionCounter);
        sharedPreferences.edit().putInt(sharedPreferencesKey, permissionCounter+1).apply();
    }

    /**
     * Already permitted
     */
    @SuppressWarnings("MissingPermission")
    private void requestLocation() {
        if (isGPSEnabled) {
            if (location == null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
                if (locationManager != null)
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        // if lcoation is not found from GPS than it will found from network //
        if (location == null) {
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, this);
                if (locationManager != null)
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
    }

    public boolean isGPSPermited() {
        return isGPSPermited;
    }

    public boolean getGPSEnable(){
        return isGPSEnabled;
    }

    public Location getPosition(){
        return location;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}
}
