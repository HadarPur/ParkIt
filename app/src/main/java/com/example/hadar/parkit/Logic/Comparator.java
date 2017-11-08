package com.example.hadar.parkit.Logic;

public class Comparator implements java.util.Comparator<Street>{

    //comparator for the parking places list
    @Override
    public int compare(Street o1, Street o2) {
        if (o1.getUtilityValue()>o2.getUtilityValue())
            return 1;
        else if (o1.getUtilityValue()==o2.getUtilityValue())
            return 0;
        else
            return -1;
    }
}
