package com.example.hadar.parkit.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.hadar.parkit.Logic.GPSTracker;
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
    private GPSTracker gpsTracker;
    private boolean firstAsk=false;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_my_car_avtivity);

        gpsTracker = new GPSTracker(this, firstAsk);
        if(gpsTracker.getGPSEnable()&& gpsTracker.getPosition()!=null){
            latitude=gpsTracker.getPosition().getLatitude();
            longitude=gpsTracker.getPosition().getLongitude();
            //if location wasn't enable and now it's enable
            gpsTracker.initLocation();
        }
        else if (!gpsTracker.getGPSEnable()){
            latitude=0;
            longitude=0;
            showSettingsAlert();
        }
        findViews();
        addListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isNetworkAvailable(this))
            showConnectionInternetFailed();
        else {
            showOnMap();
        }
    }

    //find views from xml
    public void findViews() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        save=(Button) findViewById(R.id.save);
        search=(Button) findViewById(R.id.search);
        reset=(Button) findViewById(R.id.reset);
        parkingLocation=(TextView)findViewById(R.id.parkinglocation);
    }

    //add buttons listeners
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

    //set marker on the map
    public void showOnMap() {
        location = new UserLocation(this, latitude, longitude);
        map = new Map(mapFragment, latitude, longitude, this.getApplicationContext(),PROXIMITY_RADIUS,ACTIVITY);
        try {
            parkingLocation.setText("Current Location:\n\n"+ map.getLocationName(latitude, longitude));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //note to the user
    public void showSettingsAlert() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. If you want to use this app you need to permit location");
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    //alert network not available
    public void showConnectionInternetFailed() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialog.setTitle("Network Connection Failed");
        alertDialog.setMessage("Network is not enabled." +
                "\n"+
                "If you want to use this app you need a connection to the network");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(FindMyCarActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        alertDialog.show();
    }

    //check network connection
    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null
                && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null
                && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        }
        else {
            return false;
        }
    }

}
