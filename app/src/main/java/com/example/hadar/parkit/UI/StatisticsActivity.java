package com.example.hadar.parkit.UI;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import com.example.hadar.parkit.Logic.Map;
import com.example.hadar.parkit.Logic.UserLocation;
import com.example.hadar.parkit.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.maps.SupportMapFragment;

public class StatisticsActivity extends AppCompatActivity {
    private Spinner spinner1;
    private UserLocation currLocation;
    private SupportMapFragment mapFragment;
    private Map map;
    private RelativeLayout loadingBack;
    private SpinKitView loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        showOnMap();
    }

    protected void onStart() {
        super.onStart();
        findViews();
        //loadingPage(); for the callback
    }

    public void findViews () {
        spinner1 = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        loadingBack=(RelativeLayout)findViewById(R.id.load);
        loadingBack.setBackgroundColor(Color.argb(200, 165,205,253));
        loader=(SpinKitView)findViewById(R.id.spin_kit);
    }

    public void loadingPage() {
        loadingBack.setVisibility(View.VISIBLE);
        loader.setVisibility(View.VISIBLE);
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