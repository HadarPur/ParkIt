package com.example.hadar.parkit.Logic;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import com.example.hadar.parkit.R;
import com.example.hadar.parkit.Storage.GetNearbyPlacesData;
import com.example.hadar.parkit.UI.FindMyCarActivity;
import com.example.hadar.parkit.UI.StatisticsActivity;
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
    private static final int FIND_PARKING_SPACE =1, FIND_CAR =2, STATISTICS =3;
    private boolean start;
    private double latitude, longitude, destLatitude, destLongitude;
    private int radius,activity;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Geocoder gc;
    private MarkerOptions markerOptionsMyLocation, markerOptionsDriverLocation;


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
            Log.d(TAG,"set my location");
            switch (activity) {
                case FIND_CAR:
                    setMyLocationOnTheMap();
                    break;
                case STATISTICS:
                    setMyLocationOnTheMap();
                    break;
                case FIND_PARKING_SPACE:
                    setMyLocationOnTheMap();
                    String url = getUrl(latitude, longitude, "parking");
                    Object[] DataTransfer = new Object[2];
                    DataTransfer[0] = mMap;
                    DataTransfer[1] = url;
                    Log.d("url: ", url);
                    GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                    getNearbyPlacesData.execute(DataTransfer);
                    break;
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //show markers statistics on the map
    public void showStatistics(ArrayList<Street> streets, StatisticsActivity act, double latitude, double longitude, String name) {
//        this.destLatitude=latitude;
//        this.destLongitude=longitude;
//        try {
//            setDestLocationOnTheMap();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
        Log.d(TAG,"markers : "+streets.size());

        CalcDistance calcOnRadius = new CalcDistance(mMap,streets,act,name);
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
        if (activity== FIND_CAR || activity==STATISTICS)
            markerOptionsMyLocation.title("Current Position");
        else if (activity==FIND_PARKING_SPACE)
            markerOptionsMyLocation.title("Your Destination");


        markerOptionsMyLocation.snippet("Location: " + street);
        mMap.addMarker(markerOptionsMyLocation);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

//    //set user location on the map
//    public void setDestLocationOnTheMap() throws IOException{
//        setMyLocationOnTheMap();
//        //Place current location marker
//        LatLng latLng = new LatLng(destLatitude, destLongitude);
//        markerOptionsMyLocation = new MarkerOptions();
//
//        //get street name
//        String street= getLocationName(destLatitude, destLongitude);
//
//        //Place current location marker
//        markerOptionsMyLocation.position(latLng);
//        markerOptionsMyLocation.title("Your Destination");
//
//        markerOptionsMyLocation.snippet("Location: " + street);
//        markerOptionsMyLocation.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
//
//        mMap.addMarker(markerOptionsMyLocation);
//
//        //move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//    }


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


    //set marker with car's location on the map
    public String setMarkersOnMap(double lgt, double lat) throws IOException {
        //place users markers
        String street;
        mMap.clear();
        setMyLocationOnTheMap();
        String url = getUrl(latitude, longitude, "parking");
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        Log.d("url: ", url);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);
        //to specific location
        LatLng PlayerLatLng = new LatLng(lat, lgt);
        markerOptionsDriverLocation = new MarkerOptions();
        //get street name
        street = getLocationName(lat, lgt);

        //set markers on the map
        markerOptionsDriverLocation.position(PlayerLatLng);
        markerOptionsDriverLocation.title("On Street Parking");
        markerOptionsDriverLocation.snippet("Location: " + street);
        markerOptionsDriverLocation.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        mMap.addMarker(markerOptionsDriverLocation);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(PlayerLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        return street;
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