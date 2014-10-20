package com.jforce.caterpillarscount;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by justinforsyth on 9/14/14.
 */
public class TemperatureSpinnerController implements AdapterView.OnItemSelectedListener {


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
//        Toast toast = Toast.makeText(view.getContext(), (String) parent.getItemAtPosition(pos), Toast.LENGTH_SHORT);
//        toast.show();

        HomeActivity activity = (HomeActivity) view.getContext();
        activity.setTemperature(pos);

        //0 = below 40
        //1 = 40-50
        //2 = 50-60
        //3 = 60-70
        //4 = 70-80
        //5 = 80-90
        //6 = 90-100
        //7 = 100+




    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


}


