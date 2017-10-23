package com.example.hadar.parkit.Logic;

import android.location.Location;

import java.io.Serializable;
import java.util.Calendar;

public class UserInfo implements Serializable {
    private int arrivalTime, dayOfWeek, walk;
    private Location currentLocation, lastLocation, destLocation;

    public UserInfo(){

    }

    public UserInfo(int arrivalTime, int walk, Location destLocation,Location currentLocation, Location lastLocation){
        this.arrivalTime=arrivalTime;
        this.walk=walk;
        this.destLocation=destLocation;
        this.currentLocation=currentLocation;
        this.lastLocation=lastLocation;
    }

    public void getDayOfWeek() {
        Calendar cal=Calendar.getInstance();
        this.dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
    }

    public int getWalk() {
        return walk;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public Location getDestLocation() {
        return destLocation;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }
}
