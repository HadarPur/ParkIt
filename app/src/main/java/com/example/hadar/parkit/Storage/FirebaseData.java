package com.example.hadar.parkit.Storage;

import android.util.Log;
import com.example.hadar.parkit.Logic.Street;
import com.example.hadar.parkit.Logic.StreetsData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class FirebaseData implements Serializable{
    private static final String TAG = "data";
    private static final int NUM_OF_HOURS =3;
    private Street readed;
    private ArrayList<Street> cloudData [];
    private DatabaseReference myRef;
    private DatabaseReference[] myRefChildren ;
    private String [] hours = {"morning","night","noon"};
    private String dayOfWeek;

    //c'tor
    public FirebaseData(){
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        myRefChildren = new DatabaseReference[NUM_OF_HOURS];
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SATURDAY:
                dayOfWeek = "Suterday";
                break;

            case Calendar.FRIDAY:
                dayOfWeek = "weekends";
                break;

            default:
                dayOfWeek = "working days";
                break;
        }

        Log.d(TAG,"today is : "+dayOfWeek);
        myRef = data.getReference(dayOfWeek);
        for( int i=0; i < myRefChildren.length; i++ ){
            myRefChildren[i] = myRef.child(hours[i]);
        }
    }

    //init array
    public void initData(){
        cloudData = new ArrayList[NUM_OF_HOURS];
        for(int i=0; i< cloudData.length; i++){
            cloudData[i] = new ArrayList<>();
        }
    }

    //read data from firebase
    public void ReadData( StreetsData.Callback calllback){
        initData();
        for(int i=0; i< myRefChildren.length; i++){
            callingEvent(myRefChildren[i],i,calllback);
        }
    }

    //callback event
    private void callingEvent(DatabaseReference ref,final int index,final StreetsData.Callback calllback){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> itr = dataSnapshot.getChildren().iterator();
                while(itr.hasNext()) {
                    readed = itr.next().getValue(Street.class);
                    Log.d(TAG," name: "+readed.getStreet());
                    cloudData[index].add(readed);
                }

                if (calllback != null) {
                    calllback.onCallback(cloudData);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}