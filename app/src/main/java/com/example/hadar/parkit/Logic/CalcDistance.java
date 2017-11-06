package com.example.hadar.parkit.Logic;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CalcDistance extends AsyncTask<Object,ArrayList<Street>, ArrayList<Street>> {

    private static final String TAG ="radar";
    private GoogleMap mMap;
    private ArrayList<Street> streets;
    private Activity activity;
    private String stName;
    private ArrayList<Street> streetsOnRadar;
    private MarkerOptions markerOptionsDriverLocation;
    private String[] statusTypes = {"empty","available","occupied","full"};


    @Override
    protected ArrayList<Street> doInBackground(Object... objects)
    {
        mMap = (GoogleMap) objects[0];
        streets = (ArrayList<Street>) objects[1];
        activity = (Activity) objects[2];
        stName = (String) objects[3];
        Log.d(TAG,"dest: "+stName);
        streetsOnRadar = new ArrayList<>();
        streetsOnRadar.addAll(findNearbyStreets());
        return streetsOnRadar;
    }

    @Override
    protected void onPostExecute(ArrayList<Street> streets) {
        int status=0;
        for(int i=0;i<streets.size();i++) {
            Log.d(TAG,"street: "+streets.get(i).getStreet());
            showMarker(streets.get(i), activity);
        }
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
        switch(status){
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
        mMap.addMarker(markerOptionsDriverLocation);
        Log.d(TAG,"map----done");
    }

    private ArrayList<Street> findNearbyStreets(){

        String city = "Bat Yam";
        ArrayList<Street> arr;
        arr = new ArrayList<Street>();
        for(int i=0;i<streets.size();i++){
            //streets.get(i).convertAll();
            String word = streets.get(i).getStreet();
            Log.d(TAG," "+word);
            streets.get(i).findStreetLocation(activity,word+" st "+ city);
            streets.get(i).calcWalkingDistance(stName,activity);
            if(streets.get(i).getWalking_distance()<=0.005){
                Log.d(TAG,"distance: "+streets.get(i).getWalking_distance());
                arr.add(streets.get(i));
            }
        }
        return arr;
    }
}
