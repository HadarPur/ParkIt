package com.example.hadar.parkit.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.example.hadar.parkit.Logic.CallableArr;
import com.example.hadar.parkit.Logic.Comparator;
import com.example.hadar.parkit.Logic.DistanceClass;
import com.example.hadar.parkit.Logic.Map;
import com.example.hadar.parkit.Logic.Street;
import com.example.hadar.parkit.Logic.StreetsData;
import com.example.hadar.parkit.Logic.TableFrame;
import com.example.hadar.parkit.R;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Top5ParkingSpaceActivity extends AppCompatActivity implements CallableArr{
    private static final int PROXIMITY_RADIUS = 1000;
    private static final String TAG = "Top";
    private static final int ACTIVITY = 1;
    private static final int NUM_OF_THREADS =14;
    private DistanceClass[] tasks;
    private boolean isFirst=true;
    private int radius, count=0, hour;
    private double longitude, latitude;
    private double[] radiusLength={500, 1000, 1500, 2000};
    private String streetName;
    private StreetsData streetsInfo;
    private Street st;
    private ArrayList<Street>[] stData;
    private ArrayList<Street> onRadarStreets;
    private ArrayList<Street> allStreets;
    private ArrayList<Street> top5Streets;
    private SupportMapFragment mapFragment;
    private Map map;
    private RelativeLayout loadingBack;
    private TableFrame tableFragment;
    private ListView list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top5_parking_space);
        findViews();
        hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        tasks = new DistanceClass[NUM_OF_THREADS];
        streetsInfo = StreetsData.getInstance();
        stData = streetsInfo.getData();
        onRadarStreets = new ArrayList<>();
        top5Streets=new ArrayList<>();
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
            }
        }

        if (!isNetworkAvailable(this))
            showConnectionInternetFailed();
        else {
            convertStreet();
            showOnMap();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //find view from xml
    public void findViews() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        tableFragment= (TableFrame) getSupportFragmentManager().findFragmentById(R.id.table);
        loadingBack=(RelativeLayout) findViewById(R.id.load);
        loadingBack.setBackgroundColor(Color.argb(200, 165,205,253));
    }

    //set marker on the map
    public void showOnMap() {
        int type;
        String city = "Bat Yam";
        Street st = new Street();
        st.findStreetLocation(this,streetName+" "+city);
        map = new Map(mapFragment, st.getStreetLocation().getLatitude(), st.getStreetLocation().getLongitude(),
                this, (int)radiusLength[radius], ACTIVITY);
    }

    //convert street destination
    public void convertStreet() {
        int type=timeType(hour);
        allStreets=streetsInfo.getStreets(type);
        loadingPage();
        findNearbyStreets(streetName);
    }

    //time type on the day
    public int timeType(int time){
        int timeInTheDay = 0;
        if(time >= 0 && time<=7)
            timeInTheDay = 0;
        if(time >= 8 && time<= 15)
            timeInTheDay = 2;
        if(time >= 16 && time<= 23)
            timeInTheDay = 1;

        return timeInTheDay;
    }

    //massage that network isn't open
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

    @Override //calculate the radius
    public void filterDistance(ArrayList<Street> thArr) {
        ArrayList<Street> threadArr;
        threadArr = new ArrayList<>();
        onRadarStreets.addAll(thArr);
        count++;
        //Log.d(TAG,"thread size: "+thArr.size());
        if(count == NUM_OF_THREADS){
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<onRadarStreets.size();i++) {
                        Log.d(TAG,"Streets:" +onRadarStreets.get(i).getStreet());
                        onRadarStreets.get(i).utilityFunction(radiusLength[radius]);
                        Log.d(TAG,"Utility:" +onRadarStreets.get(i).getUtilityValue());
                    }
                    Collections.sort(onRadarStreets, new Comparator());
                    for(int i=0; i<5 ;i++) {
                        top5Streets.add(onRadarStreets.get(i));
                        Log.d(TAG,(i+1)+"top5:" +top5Streets.get(i).getUtilityValue());
                    }
                    tableFragment.setList(top5Streets);
                    list=tableFragment.getList();
                    doneLoadingPage();
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                Log.d(TAG,"lat:" +tableFragment.getArray().get(position).getStreetLocation().getLatitude());
                                Log.d(TAG,"long:" +tableFragment.getArray().get(position).getStreetLocation().getLongitude());
                                map.setMarkersOnMap(tableFragment.getArray().get(position).getStreetLocation().getLongitude(),
                                        tableFragment.getArray().get(position).getStreetLocation().getLatitude());
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    }

    //find the near by places for the automate complete
    private void findNearbyStreets(String stName){
        Street street;
        String city = "Bat Yam";
        ArrayList<Street> arr;
        arr = new ArrayList<Street>();
        stName+=" "+city;
        ExecutorService ex = Executors.newFixedThreadPool(NUM_OF_THREADS);
        for(int i=0; i<NUM_OF_THREADS; i++) {
            tasks[i] = new DistanceClass(allStreets,NUM_OF_THREADS,stName,this,this, radiusLength[radius]);
            ex.execute(tasks[i]);
        }
        if(ex.isTerminated()) {
            ex.shutdown();
        }
    }

    //start loading view fot the callback
    public void loadingPage() {
        loadingBack.setVisibility(View.VISIBLE);
    }

    //finish loading view fot the callback
    public void doneLoadingPage() {
        loadingBack.setVisibility(View.GONE);
    }

}
