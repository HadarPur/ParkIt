package com.example.hadar.parkit.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.hadar.parkit.Logic.GPSTracker;
import com.example.hadar.parkit.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="Main";
    private Button statistics, findMyCar, findParkingSpace, aboutAs;
    private boolean firstAsk=true;
    private GPSTracker gpsTracker ;
    private Location startLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gpsTracker = new GPSTracker(this, firstAsk);
        if (gpsTracker!=null)
            startLocation = gpsTracker.getPosition();
        Log.d(TAG,"location: ("+startLocation.getLatitude()+" , "+startLocation.getLongitude()+" )");

    }

    @Override
    protected void onStart() {
        super.onStart();
        findViews ();
        addListenersToButtons();
        if (!gpsTracker.getGPSEnable())
            showSettingsAlert();
    }

    public void findViews () {
        findParkingSpace=(Button) findViewById(R.id.button1);
        findMyCar=(Button) findViewById(R.id.button2);
        statistics=(Button) findViewById(R.id.button3);
        aboutAs=(Button)findViewById(R.id.button4);

    }

    public void addListenersToButtons() {
        findParkingSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FindParkingSpaceActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        findMyCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FindMyCarActivity.class);
                intent.putExtra("startLocationLat", startLocation.getLatitude());
                intent.putExtra("startLocationLong", startLocation.getLongitude());
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
                intent.putExtra("startLocationLat", startLocation.getLatitude());
                intent.putExtra("startLocationLong", startLocation.getLongitude());
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        aboutAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        alertDialog.show();
    }

}