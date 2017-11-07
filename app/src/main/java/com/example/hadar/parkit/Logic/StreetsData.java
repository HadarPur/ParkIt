package com.example.hadar.parkit.Logic;

import com.example.hadar.parkit.Storage.FirebaseData;
import java.util.ArrayList;

public class StreetsData {//implements CallData {
    private static StreetsData instance;

    public static StreetsData getInstance() {
        if (instance == null) {
            instance = new StreetsData();
        }
        return instance;
    }
    private static final int NUM_OF_HOURS =3;
    private static final String TAG = "CALL";
    ArrayList<Street> data []; /** each index in the list refers to a single part of the day.
                             0-morning , 1- night , 2- noon. **/
    //c'tor
    private StreetsData(){}

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

    //gets all data
    public ArrayList<Street>[] getData() {
        return data;
    }

    public int arrSize(){
        return data.length;
    }

    public interface Callback {
        void onCallback(ArrayList<Street>[] cloudData);
    }
    public void readData(Callback callback) {
        FirebaseData data = new FirebaseData();
        data.ReadData(callback);
    }

    public void setData(ArrayList<Street>[] arr) {
        initAll();
        for(int i=0;i<arr.length;i++){
            data[i].addAll(arr[i]);
        }
    }
}
