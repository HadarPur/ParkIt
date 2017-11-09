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
        tv.setText("Park it: parking light\n" +
                "\n" +
                "Our application will help you to find a parking lot very quickly by the following features:\n" +
                "Find parking space: no more spending time on finding parking space!\n" +
                "While pressing the button \"find parking space\" in the main page you can enter limited distance of walking, and the street name of your destination. Click the button \"search\" and the app will show you a table of 5 most recommended streets which are available for parking and nearby parking stations. Clicking on a table row which contains a street information will show you the street location on the map \n" +
                "\n" +
                "You can also view daily on streets parking statistics:\n" +
                "On the main page click on the button \"statistics\" it will leads you to statistics screen. You can enter your destination street and arrival time hour, the map will show you the occupancy parking rate of the streets near your destination and their location. The streets will be shown on the map by markers with different colors.\n" +
                "Blue marker: empty, streets with occupancy rate between 0%-25%\n" +
                "Green marker: available, streets with occupancy rate between 25% to 50%\n" +
                "Orange marker : occupied, occupancy rate between 50%-75%\n" +
                "Purple marker: full, occupancy rate between 75%-100%\n" +
                "\n" +
                "Forgot when you parked your car?\n" +
                "With \"find my car screen\"\n" +
                "You can save your last parking location with the button \"save\" and search for your last parking location with the button \"search\" ,\n" +
                "The button reset will show you your current location.\n" +
                "\n" +
                "Park it, parking fast and parking light.");
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setTextColor(Color.BLACK);
    }
}
