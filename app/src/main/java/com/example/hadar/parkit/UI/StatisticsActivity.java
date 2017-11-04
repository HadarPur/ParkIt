package com.example.hadar.parkit.UI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.hadar.parkit.Logic.Map;
import com.example.hadar.parkit.Logic.Street;
import com.example.hadar.parkit.Logic.UserLocation;
import com.example.hadar.parkit.R;
import com.example.hadar.parkit.Storage.FirebaseData;
import com.example.hadar.parkit.Storage.StreetsData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {
    private static final int NUM_OF_HOURS =6;
    private static final String TAG ="static";
    private static final int PROXIMITY_RADIUS = 1000;
    private static final int ACTIVITY = 3;
    private Spinner spinner1;
    private UserLocation currLocation;
    private SupportMapFragment mapFragment;
    private Map map;
    private int position;
    private StreetsData streetsInfo;
    private Button bSearch;
    private Bundle ex;
    private int count_wait=0;
    private double longitude, latitude;
    private String streetName;
    private Street st;
    private AutoCompleteTextView txt;
    private boolean isFirst=true;
    private ArrayList<String> names;
    private RelativeLayout loadingBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        names = new ArrayList<>();
        streetsInfo = new StreetsData();
    }

    protected void onStart() {
        super.onStart();
        findViews();
        Intent intent = getIntent();
        if (intent != null) {
            ex = intent.getExtras();
            if (ex != null) {
                latitude = ex.getDouble("startLocationLat");
                longitude = ex.getDouble("startLocationLong");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isNetworkAvailable(this))
            showConnectionInternetFailed();
        else {
            streetsInfo.initAll();
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.drop_down, names);
            txt.setThreshold(2);
            txt.setAdapter(adapter);
            FirebaseData data = new FirebaseData();
            data.ReadData(streetsInfo, names, adapter);
            showOnMap(0, isFirst);
        }
    }

    public int timeType(int position){
        int time = 0;
        if(position >= 0 && position<= 3)
            time = 3;
        if(position >= 4 && position<= 8)
            time = 5;
        if(position >= 9 && position<= 11)
            time = 0;
        if(position >= 12 && position<= 14)
            time = 2;
        if(position >= 15 && position<= 20)
            time = 4;
        if(position >= 21 && position<= 23)
            time = 1;
        return time;
    }

    public void findViews () {
        loadingBack=(RelativeLayout)findViewById(R.id.load);
        txt = (AutoCompleteTextView)findViewById(R.id.autoTxt);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        bSearch = (Button) findViewById(R.id.search);
        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                streetName = txt.getText().toString();
                hideKeyboard(StatisticsActivity.this);
                String txt = spinner1.getSelectedItem().toString();
                position = spinner1.getSelectedItemPosition();
                Log.d(TAG," "+ txt + " position : "+position);
                isFirst=false;
                showOnMap(position,isFirst);
            }
        });
    }

    public void showOnMap(int position,boolean isFirst){
        int type;
        String city = "Rishon Lezion";
        if(isFirst == false) {
            streetName +=" st "+ city;
            st = new Street("10", "60", "20", streetName, this);
            st.findStreetLocation(this, streetName);
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            type = timeType(position);
            Log.d(TAG, "( " + st.getStreetLocation().getLatitude() + " , " + st.getStreetLocation().getLongitude() + " ) ");
            map = new Map(mapFragment, st.getStreetLocation().getLatitude(), st.getStreetLocation().getLongitude(), this, PROXIMITY_RADIUS, ACTIVITY);
        }
        else{
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            type = timeType(position);
            map = new Map(mapFragment, latitude, longitude, this, PROXIMITY_RADIUS, ACTIVITY);
        }
    }

    /** this function check is google service available **/
    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    //massage that network isn't open
    public void showConnectionInternetFailed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Network Connection Failed");
        alertDialog.setMessage("Network is not enabled." +
                "\n"+
                "If you want to see record table you need a connection to the network");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(StatisticsActivity.this, MainActivity.class);
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
