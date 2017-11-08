package com.example.hadar.parkit.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.example.hadar.parkit.Logic.GPSTracker;
import com.example.hadar.parkit.Logic.Street;
import com.example.hadar.parkit.Logic.StreetsData;
import com.example.hadar.parkit.R;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final String TAG ="Main";
    private boolean firstAsk=true, isLoading;
    private Button statistics, findMyCar, findParkingSpace, aboutAs;
    private GPSTracker gpsTracker ;
    private StreetsData streetsInfo;
    private RelativeLayout loadingBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews ();
        showOnSettingsAlert();
        gpsTracker = new GPSTracker(this, firstAsk);
        if(!gpsTracker.getGPSEnable()){
            showSettingsAlert();
        }
        if(!isNetworkAvailable(this)) {
            showConnectionInternetFailed();
        }
        else {
            streetsInfo = StreetsData.getInstance();
            loadingPage();
            streetsInfo.readData(new StreetsData.Callback() {
                @Override
                public void onCallback(ArrayList<Street>[] cloudData) {
                    streetsInfo.setData(cloudData);
                    Log.d(TAG, "array size: " + streetsInfo.getStreets(0).size());
                    doneLoadingPage();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        addListenersToButtons();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //find views from xml
    public void findViews () {
        findParkingSpace=(Button) findViewById(R.id.button1);
        findMyCar=(Button) findViewById(R.id.button2);
        statistics=(Button) findViewById(R.id.button3);
        aboutAs=(Button)findViewById(R.id.button4);
        loadingBack=(RelativeLayout)findViewById(R.id.load);
        loadingBack.setBackgroundColor(Color.argb(200, 165,205,253));
    }

    //add buttons listeners
    public void addListenersToButtons() {
        findParkingSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoading==false) {
                    Intent intent = new Intent(MainActivity.this, FindParkingSpaceActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        });

        findMyCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoading==false) {
                    Intent intent = new Intent(MainActivity.this, FindMyCarActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        });

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoading==false) {
                    /** transformation from object to Json string **/
                    Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
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

    //alert for GPS connection
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                finish();
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

    //alert network not available
    public void showConnectionInternetFailed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Network Connection Failed");
        alertDialog.setMessage("Network is not enabled." +
                "\n"+
                "If you want to use this app you need a connection to the network");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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

    //note to the user
    public void showOnSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Big Note:");
        alertDialog.setMessage("You must permit location and network connection for this app");
        alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
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

    //start loading view fot the callback
    public void loadingPage() {
        loadingBack.setVisibility(View.VISIBLE);
        isLoading=true;
    }

    //finish loading view fot the callback
    public void doneLoadingPage() {
        loadingBack.setVisibility(View.GONE);
        isLoading=false;
    }
}
