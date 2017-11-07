package com.example.hadar.parkit.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.hadar.parkit.Logic.Map;
import com.example.hadar.parkit.Logic.UserLocation;
import com.example.hadar.parkit.R;
import com.google.android.gms.maps.SupportMapFragment;
import java.io.IOException;

public class FindMyCarActivity extends AppCompatActivity {
    private static final int PROXIMITY_RADIUS = 0, ACTIVITY = 2;
    private SupportMapFragment mapFragment;
    private Map map;
    private UserLocation location;
    private TextView parkingLocation;
    private Button save, search, reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_my_car_avtivity);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        findViews();
        addListeners();
        showOnMap();
    }

    public void findViews() {
        save=(Button) findViewById(R.id.save);
        search=(Button) findViewById(R.id.search);
        reset=(Button) findViewById(R.id.reset);
        parkingLocation=(TextView)findViewById(R.id.parkinglocation);
    }

    public void addListeners() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location!=null) {
                    location.saveParkingLocation(location.getLongitude(), location.getLatitude());
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String street = location.readParkingLocation(map);
                    parkingLocation.setText("Parking Location:\n\n"+street);
                }
                catch (IOException ex1) {
                    ex1.printStackTrace();
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOnMap();
            }
        });
    }


    public void showOnMap() {
        Bundle ex;
        double longitude, latitude;
        Intent intent = getIntent();
        try {
            if (intent != null) {
                ex = intent.getExtras();
                if (ex != null) {
                    latitude = ex.getDouble("startLocationLat");
                    longitude = ex.getDouble("startLocationLong");
                    location = new UserLocation(this, latitude, longitude);
                    map = new Map(mapFragment, latitude, longitude, this.getApplicationContext(),PROXIMITY_RADIUS,ACTIVITY);
                    parkingLocation.setText("Current Location:\n\n"+ map.getLocationName(latitude, longitude));
                }
            }
        }
        catch (IOException ex2) {
            ex2.printStackTrace();
        }
    }
}
