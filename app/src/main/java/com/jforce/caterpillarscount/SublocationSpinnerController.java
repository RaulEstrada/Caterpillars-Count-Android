package com.jforce.caterpillarscount;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by justinforsyth on 9/3/14.
 */
public class SublocationSpinnerController implements AdapterView.OnItemSelectedListener{

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
//        Toast toast = Toast.makeText(view.getContext(), (String) parent.getItemAtPosition(pos), Toast.LENGTH_SHORT);
//        toast.show();

        HomeActivity activity = (HomeActivity) view.getContext();
        activity.setSurvey((String) parent.getItemAtPosition(pos));




    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
