package com.example.hadar.parkit.Logic;

import android.app.Activity;
import android.widget.TextView;

import com.example.hadar.parkit.Storage.SharedPreferencesServices;

import java.io.IOException;

public class UserLocation {
    private final int LOCATION_LAT_CODE=1212;
    private final int LOCATION_LGT_CODE=1213;
    private double longitude;
    private double latitude;
    private double parkingLongitude;
    private double parkingLatitude;
    private Activity activity;
    private SharedPreferencesServices service;

    public UserLocation(Activity activity, double latitude, double longitude){
        this.activity=activity;
        this.latitude = latitude;
        this.longitude = longitude;
        service=new SharedPreferencesServices(activity);
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getParkingLongitude() {
        return parkingLongitude;
    }

    public double getParkingLatitude() {
        return parkingLatitude;
    }

    public void saveParkingLocation(double lng, double lat) {
        service.saveLocationByKey(LOCATION_LGT_CODE, lng);
        service.saveLocationByKey(LOCATION_LAT_CODE, lat);
    }

    public String readParkingLocation(Map map) throws IOException {
        String street="";
        this.parkingLongitude=service.readLocationByKey(LOCATION_LGT_CODE);
        this.parkingLatitude=service.readLocationByKey(LOCATION_LAT_CODE);
        if (parkingLongitude!=0&&parkingLatitude!=0) {
            street = map.setCarMarkersOnMap(parkingLongitude,parkingLatitude);
        }
        return street;
    }
}
