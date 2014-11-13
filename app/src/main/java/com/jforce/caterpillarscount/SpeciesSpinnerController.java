package com.jforce.caterpillarscount;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by justinforsyth on 11/12/14.
 */
public class SpeciesSpinnerController implements AdapterView.OnItemSelectedListener{

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {


        HomeActivity activity = (HomeActivity) view.getContext();


        String string = (String) parent.getItemAtPosition(pos);

        if(string.equals("Other") && !activity.speciesEditTextIsVisible()){
            activity.showSpeciesEditText(true);

        }

        if(!string.equals("Other") && activity.speciesEditTextIsVisible()){
            activity.hideSpeciesEditText(true);
        }





    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }



}
