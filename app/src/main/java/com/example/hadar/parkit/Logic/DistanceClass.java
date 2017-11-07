package com.example.hadar.parkit.Logic;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

public class DistanceClass implements Runnable{

    private static final String TAG = "thread";
    private ArrayList<Street> streetsOld;
    private ArrayList<Street> newStreets;
    private int num_of_threads;
    private String st_name;
    private Activity activity;
    private CallableArr callArr;

    public DistanceClass(ArrayList<Street> streetsOld , int num_of_threads, String st_name, Activity activity,CallableArr callArr){

        this.streetsOld = new ArrayList<>();
        this.newStreets = new ArrayList<>();
        this.num_of_threads = num_of_threads;
        this.st_name = st_name;
        this.activity = activity;
        this.streetsOld.addAll(streetsOld);
        this.newStreets.addAll(newStreets);
        this.callArr = callArr;
    }

    @Override
    public void run() {

        int section;
        Street street;
        String city = "Bat Yam";
        int tNum = (int) (Thread.currentThread().getId() % num_of_threads + 1);
        if (tNum < num_of_threads) {
            section = streetsOld.size() / num_of_threads;
        }
        else {
            section = streetsOld.size() % num_of_threads;
        }
        for (int i = (tNum - 1) * section; i < tNum * section; i++) {
            streetsOld.get(i).convertAll();
            street = streetsOld.get(i);
            String word = street.getStreet();
            street.findStreetLocation(activity, word + " st " + city);
            street.calcWalkingDistance(st_name, activity);
            if (street.getWalking_distance() <= 0.005) {
                Log.d(TAG, "distance: " + street.getWalking_distance());
                newStreets.add(street);
            }
        }

        callArr.filterDistance(newStreets);

        Log.d(TAG,"size: "+newStreets.size());
    }

    public ArrayList<Street> getNewStreets() {
        return newStreets;
    }

}
