package com.jforce.caterpillarscount;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by justinforsyth on 9/8/14.
 */
public class HerbivorySpinnerController implements AdapterView.OnItemSelectedListener{

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        HomeActivity activity = (HomeActivity) view.getContext();
        String string = (String) parent.getItemAtPosition(pos);
        activity.setHerbivory(Integer.parseInt(string));




    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
