package com.example.hadar.parkit.Logic;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import com.example.hadar.parkit.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;

public class Map implements OnMapReadyCallback {
    private static final String TAG ="map";
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Geocoder gc;
    private Marker mCurrLocationMarker;
    private double latitude, longitude;
    private MarkerOptions markerOptionsMyLocation, markerOptionsDriverLocation;


    public Map(SupportMapFragment mapFragment, double latitude, double longitude, Context context){
        this.mapFragment=mapFragment;
        this.latitude=latitude;
        this.longitude=longitude;
        mapFragment.getMapAsync(this);
        gc=new Geocoder(context);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            Log.d(TAG,"set my location");
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            setMyLocationOnTheMap();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMyLocationOnTheMap() throws IOException {
        //place users markers
        mMap.clear();

        //Place current location marker
        LatLng latLng = new LatLng(latitude, longitude);
        markerOptionsMyLocation = new MarkerOptions();

        //get street name
        String street=getLocationName(latitude, longitude);

        //Place current location marker
        markerOptionsMyLocation.position(latLng);
        markerOptionsMyLocation.title("Current Position");
        markerOptionsMyLocation.snippet("Location: " + street);
        mMap.addMarker(markerOptionsMyLocation);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    //set marker with player's location on the map
    public String setMarkersOnMap(double lgt, double lat) throws IOException {
        //place users markers
        mMap.clear();

        //to specific location
        LatLng PlayerLatLng = new LatLng(lat, lgt);
        markerOptionsDriverLocation = new MarkerOptions();

        //get street name
        String street=getLocationName(lat, lgt);

        //set markers on the map
        markerOptionsDriverLocation.position(PlayerLatLng);
        markerOptionsDriverLocation.title("Parking Location");
        markerOptionsDriverLocation.snippet("Location: "+street);
        markerOptionsDriverLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.carmarker));
        mMap.addMarker(markerOptionsDriverLocation);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(PlayerLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        return street;
    }

    public String getLocationName(double latitude, double longitude) throws IOException {
        //to specific address
        List<Address> ls=gc.getFromLocation(latitude, longitude,1);
        android.location.Address address=ls.get(0);
        //get current province/City
        String street=address.getAddressLine(0);

        return street;
    }
}