package com.example.hadar.parkit.Storage.quaries;

import android.widget.ArrayAdapter;
import com.example.hadar.parkit.Logic.Street;
import java.util.ArrayList;

/** this interface implemented inorder to synchronize the retrieved data from the Firebase **/

public interface CallData {
    void performQuery(ArrayList<Street> list, int index);
}
