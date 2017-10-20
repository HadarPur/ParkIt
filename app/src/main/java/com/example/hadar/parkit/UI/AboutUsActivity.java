package com.example.hadar.parkit.UI;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.hadar.parkit.R;

public class AboutUsActivity extends AppCompatActivity {
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }


    protected void onStart() {
        super.onStart();
        findViews();
    }

    //find view by id and set text
    public void findViews() {
        tv=(TextView) findViewById(R.id.aboutus);
        tv.setText("need to fill");
        tv.setMovementMethod(new ScrollingMovementMethod());
    }
}
