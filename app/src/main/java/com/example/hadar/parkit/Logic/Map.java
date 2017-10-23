package com.example.hadar.parkit.Logic;

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

public class Map implements OnMapReadyCallback {
    private static final String TAG ="map";
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Marker mCurrLocationMarker;
    private double latitude, longitude;

    public Map(SupportMapFragment mapFragment,double latitude,double longitude){
        this.mapFragment=mapFragment;
        this.latitude=latitude;
        this.longitude=longitude;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        Log.d(TAG,"set my location");
        setMyLocationOnTheMap();
    }

    public void setMyLocationOnTheMap() {
        //Place current location marker
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    public void setMarkersOnMap(UserInfo userInfo) {
        //place users markers
        mMap.clear();
        setMyLocationOnTheMap();
        MarkerOptions markerOptions = new MarkerOptions();
        Log.d(TAG,"set Markers array");

        //LatLng latLng = new LatLng(userInfo.getLatitude(), userInfo.getLongitude());
        //markerOptions.position(latLng);
        //markerOptions.snippet("location:" + userInfo.getLatitude() + "," + userInfo.getLongitude());
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        mMap.addMarker(markerOptions);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }
}