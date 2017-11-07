package com.example.hadar.parkit.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.hadar.parkit.Logic.Map;
import com.example.hadar.parkit.Logic.Street;
import com.example.hadar.parkit.Logic.StreetsData;
import com.example.hadar.parkit.R;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

public class Top5ParkingSpaceActivity extends AppCompatActivity {
    private static final int PROXIMITY_RADIUS = 1000;
    private static final int ACTIVITY = 1;
    private boolean isFirst=true;
    private int radius;
    private String streetName;
    private StreetsData streetsInfo;
    private Street st;
    private ArrayList<Street>[] stData;
    private double longitude, latitude;
    private SupportMapFragment mapFragment;
    private Map map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top5_parking_space);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        streetsInfo = StreetsData.getInstance();
        stData = streetsInfo.getData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent != null) {
            Bundle ex = intent.getExtras();
            if (ex != null) {
                radius=ex.getInt("Radius");
                streetName=ex.getString("Street");
                latitude = ex.getDouble("startLocationLat");
                longitude = ex.getDouble("startLocationLong");
                showOnMap();
            }
        }
    }

    public void showOnMap() {
        int type;
        String city = "Bat Yam";
        map = new Map(mapFragment, latitude, longitude, this, PROXIMITY_RADIUS, ACTIVITY);
    }

    public void convertStreet() {

    }

}
