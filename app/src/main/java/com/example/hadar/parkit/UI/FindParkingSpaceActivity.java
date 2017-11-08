package com.example.hadar.parkit.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import com.example.hadar.parkit.Logic.GPSTracker;
import com.example.hadar.parkit.Logic.Street;
import com.example.hadar.parkit.Logic.StreetsData;
import com.example.hadar.parkit.R;
import java.util.ArrayList;

public class FindParkingSpaceActivity extends AppCompatActivity {
    private static final int ACTIVITY = 1;
    private int radius;
    private boolean isFirst=true;
    private Button search;
    private Spinner spinner;
    private AutoCompleteTextView txt;
    private String streetName;
    private ArrayList<String> names;
    private StreetsData streetsInfo;
    private Street st;
    private ArrayList<Street>[] stData;
    private ArrayAdapter adapter;
    private GPSTracker gpsTracker;
    private boolean firstAsk=false;
    private double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_parking_space);

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
        names = new ArrayList<>();
        streetsInfo = StreetsData.getInstance();
        stData = streetsInfo.getData();
        adapter = new ArrayAdapter(FindParkingSpaceActivity.this, R.layout.drop_down, names);
        txt.setThreshold(2);
        txt.setAdapter(adapter);
        for(int i=0;i<stData[0].size();i++){
            names.add(stData[0].get(i).getStreet());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        addListenersToButtons();
    }

    //find views from xml
    void findViews() {
        txt = (AutoCompleteTextView)findViewById(R.id.autoTxt);
        search = (Button) findViewById(R.id.search);
        spinner = (Spinner)  findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.walk_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    //add listeners button
    public void addListenersToButtons() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streetName = txt.getText().toString();
                if(names.contains(streetName)) {
                    //String txt = spinner.getSelectedItem().toString();
                    radius = spinner.getSelectedItemPosition();
                    //Log.d(TAG," "+ txt + " position : "+position);
                    isFirst = false;
                    //showOnMap(position,isFirst);
                    Intent intent = new Intent(FindParkingSpaceActivity.this, Top5ParkingSpaceActivity.class);
                    intent.putExtra("Radius", radius);
                    intent.putExtra("Street", streetName);
                    intent.putExtra("startLocationLat", latitude);
                    intent.putExtra("startLocationLong", longitude);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

                else{
                    checkInput();
                }
            }
        });
    }

    //input exception
    public void checkInput(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("invalid input");
        alertDialog.setMessage("invalid input" +
                "\n"+
                "couldn't found the street In Bat Yam");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });
        alertDialog.show();
    }

    //not to the user
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
}


