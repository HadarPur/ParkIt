package com.example.hadar.parkit.Storage;

import android.util.Log;
import android.widget.ArrayAdapter;
import com.example.hadar.parkit.Logic.Street;
import com.example.hadar.parkit.Storage.quaries.CallData;
import java.io.Serializable;
import java.util.ArrayList;

public class StreetsData implements CallData,Serializable {

    private static final int NUM_OF_HOURS =6;
    private static final String TAG = "CALL";
    ArrayList<Street> data []; /** each index in the list refers to a single part of the day.
                             1- early in the morning, 2-morning , 3- noon, 4- afternoon,
                             5- evening , 6- night. **/
    //c'tor
    public StreetsData(){}

    //init array
    public void initAll(){
        data = new ArrayList[NUM_OF_HOURS];
        for(int i=0; i< data.length; i++){
            data[i] = new ArrayList<>();
        }
    }

    //return streets on specific time
    public ArrayList<Street> getStreets(int index) {
        return data[index];
    }

    public int arrSize(){
        return data.length;
    }

    @Override //callback to the data base
    public void performQuery(ArrayList<Street> list, int index,ArrayList<String> names,ArrayAdapter adapter) {
        data[index].addAll(list);
        if(index==data.length-1) {
            for (int i = 0; i < data[index].size(); i++) {
                names.add(data[index].get(i).getStreet());
                Log.d(TAG," "+names.get(i));
            }
        }
       adapter.notifyDataSetChanged();
    }
}
