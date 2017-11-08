package com.example.hadar.parkit.Logic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class TableFrame extends Fragment {
    private ListView list;
    private ArrayList<String> streetsName;
    private ArrayAdapter<String> adapter;
    private ArrayList<Street> streets;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        streetsName=new ArrayList<>();
        list=new ListView(getContext());
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, streetsName);
        list.setAdapter(adapter);
        return list;
    }


    //set specific list
    public void setList(ArrayList<Street> street){
        streets=new ArrayList<>();
        streets.addAll(street);
        for (int i=0; i<street.size(); i++) {
            DecimalFormat df = new DecimalFormat("#.##");
            streetsName.add("Street Name: "+street.get(i).getStreet()+"occupation rate: "+df.format(street.get(i).getOccupacy())+"%");

        }
        adapter.notifyDataSetChanged();
    }

    //get ready list
    public ListView getList() {
        return list;
    }

    //get ready array
    public ArrayList<Street> getArray() {
        return streets;
    }
}
