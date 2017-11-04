package com.example.hadar.parkit.Logic;


import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.example.hadar.parkit.R;
import com.example.hadar.parkit.Storage.GetNearbyPlacesData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map implements OnMapReadyCallback {
    private static final String TAG ="map";
    private static final int FIND_PARKING_SPACE =1;
    private static final int FIND_CAR =2;
    private static final int STATISTICS =3;
    private boolean start;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private double latitude, longitude;
    private Geocoder gc;
    private MarkerOptions markerOptionsMyLocation, markerOptionsDriverLocation;
    private String[] statusTypes = {"empty","available","occupied","full"};
    private int radius,activity;

    public Map(SupportMapFragment mapFragment, double latitude, double longitude, Context context,
               int proximity_radius,int activity_type){
        this.mapFragment=mapFragment;
        this.latitude=latitude;
        this.longitude=longitude;
        this.radius = proximity_radius;
        this.activity = activity_type;
        start = true;
        mapFragment.getMapAsync(this);
        gc=new Geocoder(context);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            mMap.clear();
            Log.d(TAG,"set my location");
            switch (activity){
                case FIND_CAR:
                    setMyLocationOnTheMap();
                    break;
                case STATISTICS:
                    String url = getUrl(latitude, longitude, "parking");
                    Object[] DataTransfer = new Object[2];
                    DataTransfer[0] = mMap;
                    DataTransfer[1] = url;
                    Log.d("url: ", url);
                    GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                    getNearbyPlacesData.execute(DataTransfer);
                    setMyLocationOnTheMap();
                    break;
                }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //show markers statistics on the map
    public void showStatistics(ArrayList<Street> streets,Activity activity){
        for(int i=0;i<streets.size();i++){
            Log.d("TAG"," street name: "+streets.get(i).getStreet());
            showMarker(streets.get(i),activity);
        }
    }

    //set user location on the map
    public void setMyLocationOnTheMap() throws IOException{
        //place users markers
        mMap.clear();

        //Place current location marker
        LatLng latLng = new LatLng(latitude, longitude);
        markerOptionsMyLocation = new MarkerOptions();

        //get street name
        String street= getLocationName(latitude, longitude);

        //Place current location marker
        markerOptionsMyLocation.position(latLng);
        markerOptionsMyLocation.title("Current Position");
        markerOptionsMyLocation.snippet("Location: " + street);
        mMap.addMarker(markerOptionsMyLocation);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }


    //set marker with car's location on the map
    public String setCarMarkersOnMap(double lgt, double lat) throws IOException {
        //place users markers
        String street;
        mMap.clear();
        //to specific location
        LatLng PlayerLatLng = new LatLng(lat, lgt);
        markerOptionsDriverLocation = new MarkerOptions();
        //get street name
        street = getLocationName(lat, lgt);

        //set markers on the map
        markerOptionsDriverLocation.position(PlayerLatLng);
        markerOptionsDriverLocation.title("Parking Location");
        markerOptionsDriverLocation.snippet("Location: " + street);
        markerOptionsDriverLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.carmarker));
        mMap.addMarker(markerOptionsDriverLocation);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(PlayerLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        return street;
    }

    //get occupation status
    public int getStatus(Street st){
        int status = 0;
        if(st.getOccupacy()>=0 && st.getOccupacy()<25){
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

    //show on street parking places
    public void showMarker(Street st, Activity activity){
        int status=0;

        st.findStreetLocation(activity,st.getStreet());
        status = getStatus(st);
        LatLng PlayerLatLng = new LatLng(st.getStreetLocation().getLatitude(), st.getStreetLocation().getLongitude());
        markerOptionsDriverLocation = new MarkerOptions();
        markerOptionsDriverLocation.position(PlayerLatLng);
        markerOptionsDriverLocation.title(statusTypes[status]);
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
    }

    //geocode function return street name
    public String getLocationName(double latitude, double longitude) throws IOException {
        //to specific address
        List<Address> ls=gc.getFromLocation(latitude, longitude,1);
        android.location.Address address=ls.get(0);
        //get current province/City
        String street=address.getAddressLine(0);

        return street;
    }

    //sends info near by places URL
    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + radius);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyCdv0EMKx3KLjPWbDX_GpuE3UbniIh0a1o");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

}