package com.example.hadar.parkit.Logic;

import android.app.Activity;
import com.example.hadar.parkit.Storage.SharedPreferencesServices;
import java.io.IOException;

public class UserLocation {
    private final int LOCATION_LAT_CODE=1212, LOCATION_LGT_CODE=1213;
    private double longitude, latitude, parkingLongitude, parkingLatitude;
    private Activity activity;
    private SharedPreferencesServices service;

    //c'tor
    public UserLocation(Activity activity, double latitude, double longitude){
        this.activity=activity;
        this.latitude = latitude;
        this.longitude = longitude;
        service=new SharedPreferencesServices(activity);
    }

    //getter
    public double getLongitude() {
        return longitude;
    }

    //getter
    public double getLatitude() {
        return latitude;
    }

    public double getParkingLongitude() {
        return parkingLongitude;
    }

    public double getParkingLatitude() {
        return parkingLatitude;
    }

    //save into share preference
    public void saveParkingLocation(double lng, double lat) {
        service.saveLocationByKey(LOCATION_LGT_CODE, lng);
        service.saveLocationByKey(LOCATION_LAT_CODE, lat);
    }

    //read from share preference
    public String readParkingLocation(Map map) throws IOException {
        String street="Location doesn't save";
        this.parkingLongitude=service.readLocationByKey(LOCATION_LGT_CODE);
        this.parkingLatitude=service.readLocationByKey(LOCATION_LAT_CODE);
        if (parkingLongitude!=0&&parkingLatitude!=0) {
            street = map.setCarMarkersOnMap(parkingLongitude,parkingLatitude);
        }
        return street;
    }
}
