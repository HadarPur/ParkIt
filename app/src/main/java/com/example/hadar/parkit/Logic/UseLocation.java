package com.example.hadar.parkit.Logic;


public class UseLocation {

    private double longitude;
    private double latitude;

    public UseLocation(double latitude,double longitude){

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}