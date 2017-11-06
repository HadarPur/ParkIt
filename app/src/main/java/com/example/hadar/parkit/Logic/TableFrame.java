package com.example.hadar.parkit.Logic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TableFrame extends Fragment {
    private ListView list;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        list=new ListView(getContext());
        //adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,users);
        list.setAdapter(adapter);
        return list;
    }
}
