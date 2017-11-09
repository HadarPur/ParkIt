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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_parking_space);

        findViews();
        names = new ArrayList<>();
        streetsInfo = StreetsData.getInstance();
        stData = streetsInfo.getData();
        adapter = new ArrayAdapter(FindParkingSpaceActivity.this, R.layout.drop_down, names);
        txt.setThreshold(2);
        txt.setAdapter(adapter);
        for(int i=0;i<stData[0].size();i++){
            if (!names.contains(stData[0].get(i).getStreet()))
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
                    radius = spinner.getSelectedItemPosition();
                    isFirst = false;
                    Intent intent = new Intent(FindParkingSpaceActivity.this, Top5ParkingSpaceActivity.class);
                    intent.putExtra("Radius", radius);
                    intent.putExtra("Street", streetName);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                else {
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
}


