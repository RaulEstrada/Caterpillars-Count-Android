package jforce.com.caterpillarcount;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by justinforsyth on 9/3/14.
 */
public class CircleSpinnerController implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        HomeActivity activity = (HomeActivity) view.getContext();
        String string = (String) parent.getItemAtPosition(pos);
        activity.setCircle(Integer.parseInt(string));




    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
