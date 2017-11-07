package com.example.hadar.parkit.Logic;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.hadar.parkit.UI.StatisticsActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CalcDistance  implements CallableArr {

    private static final String TAG ="radar";
    private static final int NUM_OF_THREADS =14;
    private GoogleMap mMap;
    private ArrayList<Street> streets;
    private StatisticsActivity activity;
    private String stName;
    private ArrayList<Street> streetsOnRadar;
    private MarkerOptions markerOptionsDriverLocation;
    private String[] statusTypes = {"empty","available","occupied","full"};
    private DistanceClass[] tasks;
    private int count;

    public  CalcDistance(GoogleMap mMap,ArrayList<Street> streets,StatisticsActivity activity,String stName) {
       this.mMap = mMap;
       this.streets = streets;
       this.activity = activity;
       this.stName = stName;
       Log.d(TAG,"dest: "+stName);
       streetsOnRadar = new ArrayList<>();
       count = 0;
       tasks = new DistanceClass[NUM_OF_THREADS];
       findNearbyStreets(stName,activity);
    }


    public int getStatus(Street st){
        int status = 0;
        Log.d(TAG,"status rate: "+st.getOccupacy());
        if(st.getOccupacy()>=0 && st.getOccupacy()*100<25){
            status = 0;
        }
        if(st.getOccupacy()>=25 && st.getOccupacy()<50){
            status = 1;
        }
        if(st.getOccupacy()>=50 && st.getOccupacy()<75){
            status = 2;
        }
        if(st.getOccupacy()>=75 && st.getOccupacy()<=100){
            status = 3;
        }
        return status;
    }

    private void showMarker(Street st, Activity activity){
        int status=0;
        st.convertAll();
        //st.findStreetLocation(activity,st.getStreet());
        status = getStatus(st);
        Log.d(TAG,"status : "+status);
        LatLng PlayerLatLng = new LatLng(st.getStreetLocation().getLatitude(), st.getStreetLocation().getLongitude());
        markerOptionsDriverLocation = new MarkerOptions();
        markerOptionsDriverLocation.position(PlayerLatLng);
        markerOptionsDriverLocation.title(statusTypes[status]+" : "+st.getRate()+" %");
        markerOptionsDriverLocation.snippet("Location: " + st.getStreet());
        if(st.getStreet().equals(stName)==true){
            markerOptionsDriverLocation.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

        else {
            switch (status) {
                case 0:
                    markerOptionsDriverLocation.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    break;
                case 1:
                    markerOptionsDriverLocation.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    break;
                case 2:
                    markerOptionsDriverLocation.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    break;
                case 3:
                    markerOptionsDriverLocation.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    break;
            }
        }
        mMap.addMarker(markerOptionsDriverLocation);
        Log.d(TAG,"map----done");
    }

    private void findNearbyStreets(String stName,Activity activity){
        Street street;
        String city = "Bat Yam";
        ArrayList<Street> arr;
        arr = new ArrayList<Street>();

        ExecutorService ex = Executors.newFixedThreadPool(NUM_OF_THREADS);
        for(int i=0; i<NUM_OF_THREADS; i++) {
            tasks[i] = new DistanceClass(streets,NUM_OF_THREADS,stName,activity,this);
            ex.execute(tasks[i]);
        }
        if(ex.isTerminated()) {
            ex.shutdown();
            Log.d(TAG,"all done");
        }

    }

    @Override
    public synchronized void filterDistance(ArrayList<Street> thArr) {
        ArrayList<Street> threadArr;
        threadArr = new ArrayList<>();
        streetsOnRadar.addAll(thArr);
        count++;
        if(count == NUM_OF_THREADS){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<streetsOnRadar.size();i++) {
                        showMarker(streetsOnRadar.get(i), activity);
                    }
                    activity.doneLoadingPage();
                }
            });
        }
    }
}

