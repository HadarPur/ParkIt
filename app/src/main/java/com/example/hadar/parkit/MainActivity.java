package com.example.hadar.parkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button statistics, findMyCar, findParkingSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViews ();
        addListenersToButtons();
    }

    public void findViews () {
        //Typeface type = Typeface.createFromAsset(getAssets(), "font/georgiabolditalic.ttf");
        findParkingSpace=(Button) findViewById(R.id.button1);
        //findParkingSpace.setTypeface(type);
        findMyCar=(Button) findViewById(R.id.button2);
        //findMyCar.setTypeface(type);
        statistics=(Button) findViewById(R.id.button3);
        //statistics.setTypeface(type);
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
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

}
