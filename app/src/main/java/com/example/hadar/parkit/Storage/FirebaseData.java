package com.example.hadar.parkit.Storage;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.hadar.parkit.Logic.Street;
import com.example.hadar.parkit.Storage.quaries.CallData;
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
    private static final int NUM_OF_HOURS =6;
    private Street readed;
    private ArrayList<Street> cloudData [];
    private DatabaseReference myRef;
    private DatabaseReference[] myRefChildren ;
    private String [] hours = {"afternoon","early in the morning","evening","morning","night","noon"};
    private String dayOfWeek;

    //c'tor
    public FirebaseData(){

        FirebaseDatabase data = FirebaseDatabase.getInstance();
        myRefChildren = new DatabaseReference[6];
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
    public void ReadData(final CallData queryCallback,ArrayList<String> names,ArrayAdapter adapter){
        initData();
        for(int i=0; i< myRefChildren.length; i++){
            callingEvent(myRefChildren[i], queryCallback,i,names,adapter);
        }
    }

    //callback event
    private void callingEvent(DatabaseReference ref, final CallData queryCallback,final int index,final ArrayList<String> names,final ArrayAdapter adapter){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> itr = dataSnapshot.getChildren().iterator();
                while(itr.hasNext()) {
                    readed = itr.next().getValue(Street.class);
                    Log.d(TAG," name: "+readed.getStreet());
                    cloudData[index].add(readed);
                }
                queryCallback.performQuery(cloudData[index],index,names, adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}