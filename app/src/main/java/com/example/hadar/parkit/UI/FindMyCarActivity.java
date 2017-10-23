package com.example.hadar.parkit.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hadar.parkit.Logic.Map;
import com.example.hadar.parkit.Logic.UseLocation;
import com.example.hadar.parkit.R;
import com.google.android.gms.maps.SupportMapFragment;

public class FindMyCarActivity extends AppCompatActivity {
    private UseLocation currLocation;
    private SupportMapFragment mapFragment;
    private Map map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_my_car_avtivity);
        showOnMap();
    }

    public void showOnMap(){
        Bundle ex;
        double longitude, latitude;
        Intent intent = getIntent();

        if (intent != null) {
            ex = intent.getExtras();
            if(ex!=null) {
                latitude = ex.getDouble("startLocationLat");
                longitude = ex.getDouble("startLocationLong");
                mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                map = new Map(mapFragment, latitude, longitude);
            }
        }
    }
}
