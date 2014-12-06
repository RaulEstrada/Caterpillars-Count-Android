package com.jforce.caterpillarscount;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by justinforsyth on 9/3/14.
 */
public class SiteSpinnerController implements AdapterView.OnItemSelectedListener {

    Activity activity;

    public SiteSpinnerController(Activity activity){
        this.activity = activity;

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {


        HomeActivity homeActivity = (HomeActivity) activity;

        Site site = (Site) parent.getItemAtPosition(pos);
        String name = site.getName();

        homeActivity.setSite(name);

        homeActivity.populateSpeciesSpinnerByState(site.getStateCode());



    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


}
