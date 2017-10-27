package com.example.hadar.parkit.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.example.hadar.parkit.Logic.Map;
import com.example.hadar.parkit.Logic.UserLocation;
import com.example.hadar.parkit.R;
import com.google.android.gms.maps.SupportMapFragment;

public class StatisticsActivity extends AppCompatActivity {
    private Spinner spinner1;
    private UserLocation currLocation;
    private SupportMapFragment mapFragment;
    private Map map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        showOnMap();
    }

    protected void onStart() {
        super.onStart();
        findViews();
    }

    public void findViews () {
        spinner1 = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
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
                map = new Map(mapFragment, latitude, longitude, this.getApplicationContext());
            }
        }
    }
}