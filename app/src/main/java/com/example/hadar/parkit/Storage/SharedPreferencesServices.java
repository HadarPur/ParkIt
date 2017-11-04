package com.example.hadar.parkit.Storage;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesServices{
    private final Activity activity;

    //c'tor
    public SharedPreferencesServices (Activity activity) {
        this.activity = activity;
    }

    // save location into shared preferences
    public void saveLocationByKey(int key, double loc) {
        SharedPreferences sharedPreferences=activity.getSharedPreferences("lastLocation", Context.MODE_PRIVATE);
        String sharedPreferencesKey=String.valueOf(key);
        sharedPreferences.edit().putFloat(sharedPreferencesKey, (float) loc).apply();
    }

    // save location into shared preferences
    public double readLocationByKey(int key) {
        SharedPreferences sharedPreferences=activity.getSharedPreferences("lastLocation", Context.MODE_PRIVATE);
        String sharedPreferencesKey=String.valueOf(key);
        double longitude=sharedPreferences.getFloat(sharedPreferencesKey, 0);
        return longitude;
    }

}
